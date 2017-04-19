package com.camilne.world;

import java.io.IOException;

import com.camilne.rendering.PerspectiveCamera;
import com.camilne.rendering.Shader;

public class World {
    
    private Region region;
    private Skybox skybox;
    
    public World() {
	region = new Region(0, 0);
	
	SkyboxConfiguration config = new SkyboxConfiguration();
	config.faceTextureSize = 1024;
	config.size = 400;
	config.shaderName = "skybox";
	config.textureName = "skybox.png";
	config.set(SkyboxFace.RIGHT, 1, 1);
	config.set(SkyboxFace.BACK, 2, 1);
	config.set(SkyboxFace.LEFT, 3, 1);
	config.set(SkyboxFace.FRONT, 0, 1);
	config.set(SkyboxFace.TOP, 1, 2);
	config.set(SkyboxFace.BOTTOM, 1, 0);
	try {
	    skybox = new Skybox(config);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    public void render(final Shader shader, final PerspectiveCamera camera) {
	region.render(shader);

	if(skybox != null) {
	    skybox.render(camera);
	}
    }

}
