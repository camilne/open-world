package com.camilne.world;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.camilne.noise.SimplexNoise;
import com.camilne.rendering.Mesh;
import com.camilne.rendering.PerspectiveCamera;
import com.camilne.rendering.Shader;
import com.camilne.rendering.Texture;
import com.camilne.rendering.Vertex;

public class Region {
    
    // Side of the length of the region in tiles.
    public static final int SIZE = 32;
    private static final int HEIGHTMAP_SIZE = SIZE + 2;
    
    // Holds terrain vertex data as well as region transformation.
    private Mesh terrainMesh;
    private Matrix4f transformationMatrix;
    
    private static Texture grassTexture;
    
    // The water for this region.
    private WaterRegion water;
    
    // The noise to create the heights of the region.
    private static final double FEATURE_HEIGHT = 1 << 6;
    private static final double PERSISTENCE = 0.75;
    private static final SimplexNoise NOISE = new SimplexNoise(FEATURE_HEIGHT, PERSISTENCE);
    
    /**
     * Creates a square region of length size SIZE, and generates tiled terrain.
     * @param x The x offset of the region
     * @param z The z offset of the region
     */
    public Region(float x, float z) {
	transformationMatrix = new Matrix4f();
	transformationMatrix.translate(new Vector3f(x * SIZE, 0, z * SIZE));
	
	if(grassTexture == null) {
	    grassTexture = new Texture("grass.png");
	}
	
	createTerrain();
	water = new WaterRegion(x * SIZE, z * SIZE);
    }
    
    public void preRenderWater(final PerspectiveCamera camera, final World world) {
	water.preRender(camera, world);
    }
    
    /**
     * Renders the transformed region.
     * @param shader The bound shader
     */
    public void render(Shader shader) {
	shader.bind();
	shader.setUniform("m_model", transformationMatrix);
	grassTexture.bind();
	terrainMesh.render();
    }
    
    public void renderWater(final PerspectiveCamera camera) {
    	water.render(camera);
    }
    
    /**
     * Generates tiled terrain. The structure of the terrain is 4 vertices and 6 indices to a tile.
     */
    private void createTerrain() {
	float[][] heightMap = new float[HEIGHTMAP_SIZE][HEIGHTMAP_SIZE];
	for(int j = 0; j < HEIGHTMAP_SIZE; j++) {
	    for(int i = 0; i < HEIGHTMAP_SIZE; i++) {
		final double xin = (i + transformationMatrix.m30) / (double)Region.SIZE;
		final double zin = (-j+ transformationMatrix.m32) / (double)Region.SIZE;
		heightMap[i][j] = (float)NOISE.getScaledNoise(xin, zin);
	    }
	}
	
	// Holds the vertex data for the region. (plus one more on zneg and xpos for normal calculation).
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
	calculateNormals(vertices, heightMap);
	//smoothMesh(vertices);
	
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
     * Calculates the normals per face.
     * @param vertices 
     */
    private void calculateNormals(Vertex[] vertices, float[][] heightMap) {
	// Calculates the normal to each face.
	int idx = 0;
	for(int j = 0; j < SIZE; j++) {
	    for(int i = 0; i < SIZE; i++) {
		Vector3f v = new Vector3f(i, heightMap[i][j], j);
		Vector3f v1 = new Vector3f(i + 1, heightMap[i + 1][j], j);
		Vector3f v2 = new Vector3f(i, heightMap[i][j + 1], j + 1);
		
		calculateNormal(vertices[idx++], v, v1, v2);
		
		v.y = heightMap[i + 1][j];
		v1.y = heightMap[i + 2][j];
		v2.y = heightMap[i + 1][j + 1];
		
		calculateNormal(vertices[idx++], v, v1, v2);
		
		v.y = heightMap[i + 1][j + 1];
		v1.y = heightMap[i + 2][j + 1];
		v2.y = heightMap[i + 1][j + 2];
		
		calculateNormal(vertices[idx++], v, v1, v2);
		
		v.y = heightMap[i][j + 1];
		v1.y = heightMap[i + 1][j + 1];
		v2.y = heightMap[i][j + 2];
		
		calculateNormal(vertices[idx++], v, v1, v2);
	    }
	}
    }

    /**
     * Calculates the normal of a vertex based on the cross product of adjacent vertices.
     * @param src The vertex of which to calculate the normal
     * @param v1 The first vertex (Right-hand coordinate system)
     * @param v2 The second vertex (Right-hand coordinate system)
     */
    private void calculateNormal(Vertex src, Vector3f v, Vector3f v1, Vector3f v2) {
	// Get the displacement vectors between the src vertex and the adjacent vertices.
	Vector3f a = Vector3f.sub(v1, v, null);
	Vector3f b = Vector3f.sub(v2, v, null);
	
	// Calculate the normal and set it.
	Vector3f.cross(b, a, src.getNormal()).normalise();
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
     * Smoothes the mesh normals by averaging overlapping vertices.
     * @param vertices The vertices to be smoothed
     */
    @SuppressWarnings("unused")
    private void smoothMesh(Vertex[] vertices) {
	for(int j = 0; j < SIZE; j++) {
	    for (int i = 0; i < SIZE; i++) {
		int idx = (i + SIZE * j) * 4;
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
    
    /**
     * Frees memory on the graphics card.
     */
    public void dispose() {
	water.dispose();
    }
}
