package com.camilne.rendering;

import org.lwjgl.util.vector.Vector3f;

public class Light {
    
    // The components of a base light
    private Vector3f ambient;
    private Vector3f diffuse;
    private Vector3f specular;
    
    /**
     * Creates a new light with default color values
     */
    public Light() {
	ambient = new Vector3f(0.1f, 0.1f, 0.1f);
	diffuse = new Vector3f(0.5f, 0.5f, 0.5f);
	specular = new Vector3f(1.0f, 1.0f, 1.0f);
    }

    /**
     * Returns the ambient color of the light
     * @return
     */
    public Vector3f getAmbient() {
        return ambient;
    }

    /**
     * Set the ambeint value of the light
     * @param ambient
     */
    public void setAmbient(Vector3f ambient) {
        this.ambient = ambient;
    }

    /**
     * Returns the diffuse value of the light
     * @return
     */
    public Vector3f getDiffuse() {
        return diffuse;
    }

    /**
     * Sets the diffuse value of the light
     * @param diffuse
     */
    public void setDiffuse(Vector3f diffuse) {
        this.diffuse = diffuse;
    }

    /**
     * Returns the specular value of the light
     * @return
     */
    public Vector3f getSpecular() {
        return specular;
    }

    /**
     * Sets the specular value of the light
     * @param specular
     */
    public void setSpecular(Vector3f specular) {
        this.specular = specular;
    }

}
