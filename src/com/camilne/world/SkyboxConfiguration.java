package com.camilne.world;

import java.util.HashMap;

import org.lwjgl.util.vector.Vector2f;

public class SkyboxConfiguration {
    
    private HashMap<SkyboxFace, Vector2f> faceTexturePositions;
    
    public int size;
    public int faceTextureSize;
    public String shaderName;
    public String textureName;
    
    /**
     * Creates a new default configuration for the skybox.
     */
    public SkyboxConfiguration() {
	// Creates an empty HashMap with each SkyboxFace.
	faceTexturePositions = new HashMap<SkyboxFace, Vector2f>();
	SkyboxFace[] faces = SkyboxFace.values();
	for(SkyboxFace face : faces) {
	    faceTexturePositions.put(face, new Vector2f());
	}
	
	size = 0;
	faceTextureSize = 0;
	shaderName = "";
	textureName = "";
    }
    
    /**
     * Gets the position of the face on the skybox texture in grid coordinates (y-up, y=0 at bottom, x=0 at left).
     * @param face The face to locate the position of.
     * @return The (x,y) position on the texture.
     */
    public Vector2f get(final SkyboxFace face) {
	return faceTexturePositions.get(face);
    }
    
    /**
     * Sets the position of the face on the skybox texture (y-up, y=0 at bottom, x=0 at left).
     * @param face The face to update the position of.
     * @param x The x grid position of the face.
     * @param y The y grid position of the face.
     */
    public void set(final SkyboxFace face, final int x, final int y) {
	if(x < 0 || y < 0) {
	    return;
	}
	
	faceTexturePositions.replace(face, new Vector2f(x, y));
    }

}
