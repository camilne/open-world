package com.camilne.rendering;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import com.camilne.util.Util;

public class IndexedVAO extends VAO{
    
    /**
     * Creates an indexed VAO with the specified data, indices, and dimensions
     * @param data The vertex data in a float array
     * @param indices The index data
     * @param dimensions The number of dimensions of the vertex data
     */
    public IndexedVAO(float[] data, int[] indices, int... dimensions) {
	this(Util.toBuffer(data), indices, dimensions);
    }
    
    /**
     * Creates an indexed VAO with the specified data, indices, and dimensions
     * @param data The vertex data in a float buffer
     * @param indices The index data
     * @param dimensions The number of dimensions of the vertex data
     */
    public IndexedVAO(FloatBuffer data, int[] indices, int... dimensions) {
	super(data, dimensions);
	
	// Store the indices in an EBO
	store(indices);
		
	vertexCount = indices.length;
    }
    
    /**
     * Store the specified indices into an ElementBufferObject
     * @param indices
     */
    private void store(int[] indices) {
	bind();
	
	// Create an id for the ElementBufferObject
	int eboid = GL15.glGenBuffers();
	// Bind the EBO
	GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, eboid);
	// Store the data in the EBO
	GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, Util.toBuffer(indices), GL15.GL_STATIC_DRAW);
	
	// Add this to the buffer objects for memory management and rendering
	addBufferObject(eboid);
	
	unbind();
    }
    
    /**
     * Renders the VAO as triangles
     */
    @Override
    public void render() {
	bind();
	
	enableAttributes();
	
	// Bind the EBO
	GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, getBufferObject(1));
	// Render all the indexed vertices as triangles
	GL11.glDrawElements(GL11.GL_TRIANGLES, vertexCount, GL11.GL_UNSIGNED_INT, 0);
	
	disableAttributes();
	
	unbind();
    }

}
