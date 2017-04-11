package com.camilne.rendering;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Vertex {
    
    // The size of the vertex in floats
    public static final int SIZE = 11;
    // The dimensions of the components
    public static final int[]	 DIMENSIONS = {3, 2, 3, 3};
    
    private Vector3f pos;
    private Vector2f texCoords;
    private Vector3f normal;
    private Vector3f color;
    
    /**
     * Creates a vertex with the specified position
     * @param pos
     */
    public Vertex(Vector3f pos) {
	this(pos, new Vector2f(0, 0));
    }
    
    /**
     * Creates a vertex with the specified position and texture coordinates
     * @param pos
     * @param texCoords
     */
    public Vertex(Vector3f pos, Vector2f texCoords) {
	this(pos, texCoords, new Vector3f(0, 0, 0));
    }
    
    /**
     * Creates a vertex with the specified position, texture coordinates, and normal
     * @param pos
     * @param texCoords
     * @param normal
     */
    public Vertex(Vector3f pos, Vector2f texCoords, Vector3f normal) {
	this(pos, texCoords, normal, new Vector3f(1, 1, 1));
    }
    
    /**
     * Creates a vertex with the specified position, texture coordinates, normal, and color
     * @param pos
     * @param texCoords
     * @param normal
     * @param color
     */
    public Vertex(Vector3f pos, Vector2f texCoords, Vector3f normal, Vector3f color) {
	this.pos = pos;
	this.texCoords = texCoords;
	this.normal = normal;
	this.color = color;
    }
    
    /**
     * Returns the position of this vertex
     * @return
     */
    public Vector3f getPos() {
        return pos;
    }

    /**
     * Sets the position of this vertex
     * @param pos
     * @return
     */
    public Vertex setPos(Vector3f pos) {
        this.pos = pos;
        
        return this;
    }
    
    /**
     * Returns the texture coordinates of this vertex
     * @return
     */
    public Vector2f getTexCoords() {
        return texCoords;
    }

    /**
     * Sets the texture coordinates of this vertex
     * @param texCoords
     * @return
     */
    public Vertex setTexCoords(Vector2f texCoords) {
        this.texCoords = texCoords;
        
        return this;
    }

    /**
     * Returns the normal of this vertex
     * @return
     */
    public Vector3f getNormal() {
        return normal;
    }

    /**
     * Sets the normal of this vertex
     * @param normal
     * @return
     */
    public Vertex setNormal(Vector3f normal) {
        this.normal = normal;
        
        return this;
    }

    /**
     * Returns the color of this vertex
     * @return
     */
    public Vector3f getColor() {
        return color;
    }

    /**
     * Sets the color of this vertex
     * @param color
     * @return
     */
    public Vertex setColor(Vector3f color) {
        this.color = color;
        
        return this;
    }
}
