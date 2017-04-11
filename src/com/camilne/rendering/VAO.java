package com.camilne.rendering;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.camilne.util.Util;

public class VAO {
    
    protected int vaoid;
    protected int vertexCount;
    
    private int attributes;
    private ArrayList<Integer> bufferObjects;
    
    /**
     * Creates a VertexArrayObject with the specified data
     * @param data The array of all the object data
     * @param attributes The different attributes of the data. x is size, y is stride
     */
    public VAO(float[] data, int... dimensions) {
	this(Util.toBuffer(data), dimensions);
    }
    
    /**
     * Creates a VertexArrayObject with the specified data
     * @param data The FloatBuffer of all the object data
     * @param attributes The different attributes of the data. x is size, y is stride
     */
    public VAO(FloatBuffer data, int... dimensions) {
	bufferObjects = new ArrayList<Integer>();
	
	// Create the VAO
	vaoid = GL30.glGenVertexArrays();
	
	// Set the number of attributes to enable
	attributes = dimensions.length;
	
	// Put the data in the VAO
	store(data, dimensions);
    }
    
    /**
     * Stores the specified data into the VAO with a VBO
     * @param data
     * @param dimensions
     */
    private void store(FloatBuffer data, int[] dimensions) {
	bind();
	
	// Generate a VBO to hold the data
	int vboid = GL15.glGenBuffers();
	// Bind the VBO
	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboid);
	// Store the data in the VBO
	GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
	
	// Get the stride of the data in bytes
	int stride = 0;
	if(dimensions.length > 1)
	    for(int i = 0; i < dimensions.length; i++) {
		stride += dimensions[i] * 4;
	    }
	
	// Determine the number of vertices assuming attribute 0 is position
	vertexCount = data.capacity() / dimensions[0];
	
	// Setup data in VBO
	int offset = 0;
	for(int i = 0; i < dimensions.length; i++) {
	    GL20.glVertexAttribPointer(i, dimensions[i], GL11.GL_FLOAT, false, stride, offset);
	    offset += dimensions[i] * 4;
	}
	
	// Add the vbo to the list of buffer objects for memory management
	addBufferObject(vboid);
	
	unbind();
    }
    
    /**
     * Renders this VAO as triangles
     */
    public void render() {
	bind();
	
	// Enable all the attributes
	enableAttributes();
	
	// Render the entire VAO as triangles
	GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexCount);

	// Disable all the attributes
	disableAttributes();
	
	unbind();
    }
    
    /**
     * Binds this VAO
     */
    public void bind() {
	GL30.glBindVertexArray(vaoid);
    }
    
    /**
     * Unbinds this VAO
     */
    public void unbind() {
	GL30.glBindVertexArray(0);
    }
    
    /**
     * Returns the id of this VAO
     * @return
     */
    public int getID() {
	return vaoid;
    }
    
    /**
     * Returns the number of vertices in this VAO
     * @return
     */
    public int getVertexCount() {
	return vertexCount;
    }
    
    /**
     * Enables all the stored attributes
     */
    protected void enableAttributes() {
	for (int i = 0; i < attributes; i++)
	    GL20.glEnableVertexAttribArray(i);
    }
    
    /**
     * Disables all the stored attributes
     */
    protected void disableAttributes() {
	for(int i = 0; i < attributes; i++)
	    GL20.glDisableVertexAttribArray(i);
    }
    
    /**
     * Returns the specified buffer object
     * @param index
     * @return
     */
    protected int getBufferObject(int index) {
	return bufferObjects.get(index);
    }
    
    /**
     * Adds the specified buffer object to the buffer objects for memory management
     * @param id
     */
    protected void addBufferObject(int id) {
	bufferObjects.add(id);
    }
    
    /**
     * Cleans up the VRAM of this VAO
     */
    @Override
    public void finalize() {
	GL30.glDeleteVertexArrays(vaoid);
	for(int vbo : bufferObjects)
	    GL15.glDeleteBuffers(vbo);
    }

}
