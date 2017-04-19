package com.camilne.world;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import com.camilne.rendering.PerspectiveCamera;
import com.camilne.rendering.Shader;

public class World {
    
    private HashMap<String, Region> regions;
    private Skybox skybox;
    
    public World() {
	regions = new HashMap<String, Region>();
	for(int i = -5; i < 5; i++) {
	    for(int j = -5; j < 5; j++) {
		regions.put(i + " " + j, new Region(i, j));
	    }
	}
	
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
	for(Entry<String, Region> region : regions.entrySet()) {
	    region.getValue().render(shader);
	}

	if(skybox != null) {
	    skybox.render(camera);
	}
    }

}
