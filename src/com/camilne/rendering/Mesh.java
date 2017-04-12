package com.camilne.rendering;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Mesh {
    
    private Matrix4f modelMatrix;
    private IndexedVAO vao;
    
    /**
     * Create a mesh with the specified vertices and indices
     * @param vertices
     * @param indices
     */
    public Mesh(Vertex[] vertices, int[] indices) {
	this(vertices, indices, true);
    }
    
    /**
     * Create a mesh with the specified vertices and indices and whether the normals should be calculated
     * @param vertices
     * @param indices
     * @param calculateNormals
     */
    public Mesh(Vertex[] vertices, int[] indices, boolean calculateNormals) {
	modelMatrix = new Matrix4f();
	
	// Automatically calculate normals for the mesh
	if(calculateNormals) {
	    calculateNormals(vertices, indices);
	}
	
	// Create a FloatBuffer to hold Vertex data
	FloatBuffer data = BufferUtils.createFloatBuffer(vertices.length * Vertex.SIZE);
	
	for(int i = 0; i < vertices.length; i++) {
	    // Store the position
	    data.put(vertices[i].getPos().x);
	    data.put(vertices[i].getPos().y);
	    data.put(vertices[i].getPos().z);
	    // Store the texture coordinates
	    data.put(vertices[i].getTexCoords().x);
	    data.put(vertices[i].getTexCoords().y);
	    // Store the normal
	    data.put(vertices[i].getNormal().x);
	    data.put(vertices[i].getNormal().y);
	    data.put(vertices[i].getNormal().z);
	    // Store the color
	    data.put(vertices[i].getColor().x);
	    data.put(vertices[i].getColor().y);
	    data.put(vertices[i].getColor().z);
	}
	
	// Prepare the buffer for get() operations
	data.flip();
	
	// Create the VAO using the vertices and indices
	vao = new IndexedVAO(data, indices, Vertex.DIMENSIONS);
    }
    
    /**
     * Calculates the normals of the mesh using vector cross products
     * @param vertices The vertices of the mesh
     * @param indices The indices of the mesh
     */
    public static void calculateNormals(Vertex[] vertices, int[] indices) {
	// Iterate through every face
	for(int i = 0; i < indices.length; i+=3) {
	    // Get the three indices for the triangle face
	    int i0 = indices[i];
	    int i1 = indices[i+1];
	    int i2 = indices[i+2];
	    
	    // Get the two displacement vectors for the face from the base vertex (arbitrary)
	    Vector3f v1 = Vector3f.sub(vertices[i1].getPos(), vertices[i0].getPos(), null);
	    Vector3f v2 = Vector3f.sub(vertices[i2].getPos(), vertices[i0].getPos(), null);
	    
	    // Calculate the normal using the cross product of the two displacements
	    Vector3f normal = Vector3f.cross(v1, v2, null);
	    normal.normalise();
	    
	    // Set the normal to all the vertices in the face
	    vertices[i0].setNormal(normal);
	    vertices[i1].setNormal(normal);
	    vertices[i2].setNormal(normal);
	}
    }
    
    /**
     * Renders the mesh
     */
    public void render() {
	vao.render();
    }
    
    /**
     * Translates the mesh by the specified position
     * @param position
     */
    public void translate(Vector3f position) {
	Matrix4f.translate(position, modelMatrix, modelMatrix);
    }
    
    /**
     * Rotates the mesh around the specified axis by the specified amount
     * @param axis
     * @param amount
     */
    public void rotate(Vector3f axis, float angle) {
	Matrix4f.rotate(angle, axis, modelMatrix, modelMatrix);
    }
    
    /**
     * Returns this mesh's model matrix transformation
     * @return
     */
    public Matrix4f getModelMatrix() {
	return modelMatrix;
    }
    
}
