package com.camilne.world;

import java.util.Random;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.camilne.rendering.Mesh;
import com.camilne.rendering.Shader;
import com.camilne.rendering.Vertex;

public class Region {
    
    // Side of the length of the region in tiles.
    private static final int SIZE = 32;
    
    // Holds terrain vertex data as well as region transformation.
    private Mesh terrainMesh;
    private Matrix4f transformationMatrix;
    
    /**
     * Creates a square region of length size SIZE, and generates tiled terrain.
     * @param x The x offset of the region
     * @param z The z offset of the region
     */
    public Region(float x, float z) {
	createTerrain();
	transformationMatrix = new Matrix4f();
	transformationMatrix.translate(new Vector2f(x, z));
    }
    
    /**
     * Renders the transformed region
     * @param shader The bound shader
     */
    public void render(Shader shader) {
	shader.setUniform("m_model", transformationMatrix);
	terrainMesh.render();
    }
    
    /**
     * Generates tiled terrain. The structure of the terrain is 4 vertices and 6 indices to a tile.
     */
    private void createTerrain() {
	float[][] heightMap = new float[SIZE + 1][SIZE + 1];
	Random random = new Random();
	for(int j = 0; j < SIZE + 1; j++) {
	    for(int i = 0; i < SIZE + 1; i++) {
		heightMap[i][j] = random.nextFloat();
	    }
	}
	
	// Holds the vertex data for the region.
	Vertex[] vertices = new Vertex[SIZE * SIZE * 4];
	int idx = 0; // Used to access a 1-D array through a double for-loop.
	for(int j = 0; j < SIZE; j++) {
	    for(int i = 0; i < SIZE; i++) {
		// Creates a tile with the specified (i,j) offset. Normals are default to pointing directly up.
		// Specification of the vertex data is as follows:
		// 3 *-* 2     y(+) *-> x(+)
		//   |/|            |
		// 0 *-* 1     z(+) v
		vertices[idx++] = new Vertex(new Vector3f(i, heightMap[i][j], -j),		new Vector2f(0, 0), new Vector3f(0, 1, 0));
		vertices[idx++] = new Vertex(new Vector3f(i + 1, heightMap[i + 1][j], -j),	new Vector2f(1, 0), new Vector3f(0, 1, 0));
		vertices[idx++] = new Vertex(new Vector3f(i + 1, heightMap[i + 1][j + 1], -(j + 1)),	new Vector2f(1, 1), new Vector3f(0, 1, 0));
		vertices[idx++] = new Vertex(new Vector3f(i, heightMap[i][j + 1], -(j + 1)),	new Vector2f(0, 1), new Vector3f(0, 1, 0));
	    }
	}
	
	// Automatically calculate the normals.
	calculateNormals(vertices);
	smoothMesh(vertices);
	
	// Holds the index data for the region.
	int[] indices = new int[SIZE * SIZE * 6];
	int index = 0; // Used to convert between the indices array and the vertices array.
	idx = 0; // Used to access a 1-D array through a double for-loop.
	for(int j = 0; j < SIZE; j++) {
	    for(int i = 0; i < SIZE; i++) {
		indices[idx++] = index + 0;
		indices[idx++] = index + 1;
		indices[idx++] = index + 2;
		indices[idx++] = index + 2;
		indices[idx++] = index + 3;
		indices[idx++] = index + 0;
		// Increment 4 because that is the number of vertices per tile.
		index += 4;
	    }
	}
	
	// Creates the terrainMesh and specifies to not re-calculate the normals.
	terrainMesh = new Mesh(vertices, indices, false);
    }
    
    /**
     * Calculates the normals per face. Creates artifact of squares in the mesh.
     * @param vertices 
     */
    private void calculateNormals(Vertex[] vertices) {
	// Calculates the normal to each face.
	for(int i = 0; i < vertices.length; i++) {
	    // Uses the index of the vertex to determine which vectors to cross. Refer the the vertex data specification above.
	    switch(i % 4) {
	    case 0:
		if(i + 3 < vertices.length) {
		    calculateNormal(vertices[i], vertices[i + 1], vertices[i + 3]);
		}
		break;
	    case 1:
	    case 2:
		if(i + 1 < vertices.length && i - 1 >= 0) {
		    calculateNormal(vertices[i], vertices[i + 1], vertices[i - 1]);
		}
		break;
	    case 3:
		if(i - 3 >= 0) {
		    calculateNormal(vertices[i], vertices[i - 3], vertices[i - 1]);
		}
		break;
	    }
	}
    }
    
    /**
     * Calculates the normals per face.
     * @param vertices
     */
    @SuppressWarnings("unused")
    private void calculateNormals2(Vertex[] vertices) {
	// Calculate the normal of each lower row of a tile.
	for(int i = 0; i < vertices.length; i++) {
	    switch(i % 4) {
	    case 0:
		if(i + 3 < vertices.length) {
		    calculateNormal(vertices[i], vertices[i + 1], vertices[i + 3]);
		}
		break;
	    case 1:
		if(i + 1 < vertices.length && i - 1 >= 0) {
		    calculateNormal(vertices[i], vertices[i + 1], vertices[i - 1]);
		}
		break;
	    case 2:
		if(i + SIZE * 4 < vertices.length) {
		    calculateNormal(vertices[i], vertices[i + SIZE * 4], vertices[i + SIZE * 4 - 2]);
		}
		break;
	    case 3:
		if(i + SIZE * 4 < vertices.length) {
		    calculateNormal(vertices[i], vertices[i + SIZE * 4 - 2], vertices[i + SIZE * 4]);
		}
		break;
	    }
	}
    }

    /**
     * Calculates the normal of a vertex based on the cross product of adjacent vertices.
     * @param src The vertex of which to calculate the normal
     * @param v1 The first vertex (Right-hand coordinate system)
     * @param v2 The second vertex (Right-hand coordinate system)
     */
    private void calculateNormal(Vertex src, Vertex v1, Vertex v2) {
	// Get the displacement vectors between the src vertex and the adjacent vertices.
	Vector3f a = Vector3f.sub(v1.getPos(), src.getPos(), null);
	Vector3f b = Vector3f.sub(v2.getPos(), src.getPos(), null);
	
	// Calculate the normal and set it.
	Vector3f.cross(a, b, src.getNormal()).normalise();
    }
        
    /**
     * Computes the average of the normals supplied.
     * @param normals The normals, located at the same position, that should be averaged
     */
    private Vector3f averageNormals(Vector3f[] normals) {
	Vector3f sum = new Vector3f();
	for(int i = 0; i < normals.length; i++) {
	    if(normals[i] != null) {
		Vector3f.add(sum, normals[i], sum);
	    }
	}
	sum.normalise();
	return sum;
    }
    
    /**
     * Smooths the mesh normals by averaging overlapping vertices.
     * @param vertices The vertices to be smoothed
     */
    private void smoothMesh(Vertex[] vertices) {
	for(int j = 0; j < SIZE; j++) {
	    for (int i = 0; i < SIZE; i++) {
		int idx = i + SIZE * j * 4;
		Vector3f[] normals = new Vector3f[4];
		
		normals[0] = vertices[idx].getNormal();
		if(idx - 3 >= 0) {
		    normals[1] = vertices[idx - 3].getNormal();
		}
		if(idx - SIZE * 4 + 3 >= 0) {
		    normals[2] = vertices[idx - SIZE * 4 + 3].getNormal();
		}
		if(idx - SIZE * 4 - 2 >= 0) {
		    normals[3] = vertices[idx - SIZE * 4 - 2].getNormal();
		}
		
		// Calculate the average of the potentially 4 vertices.
		Vector3f average = averageNormals(normals);
		vertices[idx].setNormal(average);
		if(idx - 3 >= 0) {
		    vertices[idx - 3].setNormal(average);
		}
		if(idx - SIZE * 4 + 3 >= 0) {
		    vertices[idx - SIZE * 4 + 3].setNormal(average);
		}
		if(idx - SIZE * 4 - 2 >= 0) {
		    vertices[idx - SIZE * 4 - 2].setNormal(average);
		}
	    }
	}
    }
}
