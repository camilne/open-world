package com.camilne.rendering;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class Texture {
    
    private static String path = "res/textures/";
    
    private int id;
    private int width;
    private int height;
    
    /**
     * Creates a texture with the specified filename
     * @param name The name of the texture (extension inclusive)
     */
    public Texture(String name) {
	TextureData data = loadTexture(name);
	
	this.id = data.getID();
	this.width = data.getWidth();
	this.height = data.getHeight();
	
	bind();
	
	// TODO: Parameter configuration
	
	// Setup texture parameters
	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
	
	// Upload buffer data to texture
	GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height,
		0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getData());
    }
    
    /**
     * Binds this texture to TEXTURE0
     */
    public void bind() {
	bind(GL13.GL_TEXTURE0);
    }
    
    /**
     * Binds this texture to the specified active texture
     * @param activeTexture The active texture to bind to
     */
    public void bind(int activeTexture) {
	GL13.glActiveTexture(activeTexture);
	GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
    }
    
    /**
     * Releases this texture (in TEXTURE0) from VRAM
     */
    public void dispose() {
	dispose(GL13.GL_TEXTURE0);
    }
    
    /**
     * Releases this texture from VRAM
     * @param activeTexture The active texture to release
     */
    public void dispose(int activeTexture) {
	GL13.glActiveTexture(activeTexture);
	GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	GL11.glDeleteTextures(id);
    }
    
    /**
     * Returns the width of this texture in pixels
     * @return
     */
    public int getWidth() {
	return width;
    }
    
    /**
     * Returns the height of this texture in pixels
     * @return
     */
    public int getHeight() {
	return height;
    }
    
    /**
     * Loads a texture with the specified filename
     * @param name The name of the texture (extension inclusive)
     * @return The loaded TextureData
     */
    public static TextureData loadTexture(String name) {
	// The BufferedImage of the loaded texture
	BufferedImage image = null;
	
	// Try to load the buffered image into memory
	try {
	    image = ImageIO.read(new FileInputStream(new File(path + name)));
	} catch (IOException e) {
	    System.err.println("Error in Texture.loadTexture(): unable to load texture[" + name + "]");
	    e.printStackTrace();
	    System.exit(1);
	}
	
	// Get image dimensions
	int width = image.getWidth();
	int height = image.getHeight();
	
	// Get a new texture id
	int id = GL11.glGenTextures();
	
	// Store the image data into a pixel array
	int[] pixels = new int[width * height * 4];
	image.getRGB(0, 0, width, height, pixels, 0, width);
	
	// Create a ByteBuffer to hold the image data to upload to OpenGL
	ByteBuffer buffer = BufferUtils.createByteBuffer(pixels.length);
	
	// Store the pixel data into the buffer
	for(int y = height - 1; y >= 0; y--) {
	    for(int x = 0; x < width; x++) {
		// Get the current pixel;
		int pixel = pixels[y * width + x];
		
		// Store the red value
		buffer.put((byte) ((pixel >> 16) & 0xFF));
		// Store the green value
		buffer.put((byte) ((pixel >> 8) & 0xFF));
		// Store the blue value
		buffer.put((byte) ((pixel) & 0xFF));
		// Store the alpha value
		buffer.put((byte) ((pixel >> 24) & 0xFF));
	    }
	}
	
	// Prepare the buffer for get() operations
	buffer.flip();
	
	return new TextureData(id, width, height, buffer);
    }
    
    /**
     * Sets the relative root file path of textures
     * @param path
     */
    public static void setPath(final String path) {
	Texture.path = path;
    }
}
