package com.camilne.util;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;

public class Util {

    /**
     * Stores the specified data into a FloatBuffer
     * @param data The array of float data
     * @return The generated FloatBuffer
     */
    public static FloatBuffer toBuffer(float[] data) {
	// Create an empty FloatBuffer with the correct size
	FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
	
	// Store all the data in the buffer
	for(int i = 0; i < data.length; i++) {
	    buffer.put(data[i]);
	}
	
	// Prepares the buffer for get() operations
	buffer.flip();
	
	return buffer;
    }
    
    /**
     * Stores the specified data into a IntBuffer
     * @param data The array of integer data
     * @return The generated IntBuffer
     */
    public static IntBuffer toBuffer(int[] data) {
	// Create an empty FloatBuffer with the correct size
	IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
	
	// Store all the data in the buffer
	for(int i = 0; i < data.length; i++) {
	    buffer.put(data[i]);
	}
	
	// Prepares the buffer for get() operations
	buffer.flip();
	
	return buffer;
    }
    
    /**
     * Stores the specified 4x4 matrix into a float buffer
     * @param data The 4x4 matrix
     * @return The generated FloatBuffer
     */
    public static FloatBuffer toBuffer(Matrix4f data) {
	// Create an empty FloatBuffer with the correct size (4x4)
	FloatBuffer buffer = BufferUtils.createFloatBuffer(4 * 4);
	
	// Store the matrix into the buffer
	data.store(buffer);
	
	// Prepare the buffer for get() operations
	buffer.rewind();
	
	return buffer;
    }
    
}
