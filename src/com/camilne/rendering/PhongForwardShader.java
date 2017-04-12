package com.camilne.rendering;

import java.io.IOException;


public class PhongForwardShader extends Shader{
    
    // The maximun number of point lights allowed to render
    public static final int MAX_POINT_LIGHTS = 4;
    
    /**
     * Creates a new PhongShader
     */
    public PhongForwardShader(final String name) throws IOException {
	super(name);
    }
    
    /**
     * Updates the shader uniforms
     * @param camera The current camera
     * @param sun The sun DirectionalLight
     * @param lights The PointLights in the scene
     */
    public void update(Camera camera, DirectionalLight sun)	 {
	// Update the transformation matrices
	setUniform("m_proj", camera.getProjection());
	setUniform("m_view", camera.getView());
	
	// Update the sun
	if(sun != null) {
	    setUniform("dir_light", sun);
	}
    }
    
    private static final String DIR_LIGHT_UNIFORM = "DirectionalLight";
    
    /**
     * Method to add all components of custom uniforms. Called by super-class
     * @param type The type of uniform
     * @param name The name of the uniform
     */
    @Override
    public void addCustomUniform(String type, String name) {
	// Adds a DirectionalLight uniform
	if (type.equals(DIR_LIGHT_UNIFORM)) {
	    addUniform(name + ".direction");
	    addUniform(name + ".ambient");
	    addUniform(name + ".diffuse");
	}
    }
    
    /**
     * Sets the specified uniform with the specified value
     * @param uniform
     * @param value The DirectionalLight value
     */
    public void setUniform(String uniform, DirectionalLight value) {
	setUniform(uniform + ".direction", value.getDirection());
	setUniform(uniform + ".ambient", value.getAmbient());
	setUniform(uniform + ".diffuse", value.getDiffuse());
    }

}
