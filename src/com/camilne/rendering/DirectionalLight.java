package com.camilne.rendering;

import org.lwjgl.util.vector.Vector3f;

public class DirectionalLight extends Light{
    
    // The direction of the light
    private Vector3f direction;
    
    /**
     * Creates a new DirectionalLight with the specified direction
     * @param direction The direction of the light source
     */
    public DirectionalLight(Vector3f direction) {
	super();
	this.direction = direction;
    }

    /**
     * Returns the direction of the light
     * @return
     */
    public Vector3f getDirection() {
        return direction;
    }

    /**
     * Sets the direction of the light
     * @param direction
     */
    public void setDirection(Vector3f direction) {
        this.direction = direction;
    }

}
