package com.camilne.rendering;

import java.nio.ByteBuffer;

public class TextureData {
    
    private int id;
    private int width;
    private int height;
    private ByteBuffer data;
    
    /**
     * Creates a wrapper to hold texture-specific data
     * @param id The id of the texture
     * @param width The width of the texture
     * @param height The height of the texture
     * @param data The pixel data of the texture
     */
    public TextureData(int id, int width, int height, ByteBuffer data) {
	this.id = id;
	this.width = width;
	this.height = height;
	this.data = data;
    }
    
    /**
     * Returns the id of the texture
     * @return
     */
    public int getID() {
	return id;
    }

    /**
     * Returns the width of the texture in pixels
     * @return
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the texture in pixels
     * @return
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the pixel data of the texture
     * @return
     */
    public ByteBuffer getData() {
        return data;
    }
    
    

}
