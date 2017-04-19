package com.camilne.world;

import java.util.HashMap;

import org.lwjgl.util.vector.Vector2f;

public class SkyboxConfiguration {
    
    private HashMap<SkyboxFace, Vector2f> faceTexturePositions;
    
    public int size;
    public int faceTextureSize;
    public String shaderName;
    public String textureName;
    
    public SkyboxConfiguration() {
	faceTexturePositions = new HashMap<SkyboxFace, Vector2f>();
	SkyboxFace[] faces = SkyboxFace.values();
	for(SkyboxFace face : faces) {
	    faceTexturePositions.put(face, new Vector2f());
	}
    }
    
    public Vector2f get(final SkyboxFace face) {
	return faceTexturePositions.get(face);
    }
    
    public void set(final SkyboxFace face, final int x, final int y) {
	if(x < 0 || y < 0) {
	    return;
	}
	
	faceTexturePositions.replace(face, new Vector2f(x, y));
    }

}
