package com.camilne.world;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.camilne.rendering.DirectionalLight;
import com.camilne.rendering.PerspectiveCamera;
import com.camilne.rendering.PhongForwardShader;

public class World {
    
    private HashMap<String, Region> regions;
    private Skybox skybox;
    private int viewDistance;
    private PhongForwardShader shader;
    private DirectionalLight directionalLight;
    
    public World() {
	regions = new HashMap<String, Region>();
	
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
	
	try {
	    shader = new PhongForwardShader("main");
	    shader.setUniform("m_model", new Matrix4f());
	} catch(IOException e) {
	    e.printStackTrace();
	    System.exit(1);
	}
	
	directionalLight = new DirectionalLight(new Vector3f(-0.5f, -0.9f, 0.65f));
	
	viewDistance = 4;
    }
    
    public void update(final PerspectiveCamera camera, final float delta) {	
	// Check regions that should unload
	removeRegionsOutOfRange(camera);
	
	// Check regions that should load
	addRegionsInRange(camera);	
	
	// Animate water.
	WaterRegion.updateMovement(delta);
    }
    
    public void render(final PerspectiveCamera camera) {
	shader.bind();
	shader.update(camera, directionalLight);
	
	for(Entry<String, Region> region : regions.entrySet()) {
	    region.getValue().preRenderWater(camera, this);
	}
	
	renderWithoutWater(camera, false);
	
	for(Entry<String, Region> region : regions.entrySet()) {
	    region.getValue().renderWater(camera);
	}
    }
    
    public void renderWithoutWater(final PerspectiveCamera camera, final boolean renderReflected) {
	shader.bind();
	shader.update(camera, directionalLight);
	
	if(renderReflected) {
	    shader.setUniform("clip_plane", new Vector4f(0, 1, 0, -WaterRegion.WATER_HEIGHT));
	} else {
	    shader.setUniform("clip_plane", new Vector4f(0, 0, 0, 0));
	}
	
	if(skybox != null) {
	    skybox.render(camera);
	}
	
	for(Entry<String, Region> region : regions.entrySet()) {
	    region.getValue().render(shader);
	}
    }
    
    private void removeRegionsOutOfRange(final PerspectiveCamera camera) {
	ArrayList<String> regionsToRemove = new ArrayList<String>();
	for(Entry<String, Region> region : regions.entrySet()) {
	    final int x = Integer.parseInt(region.getKey().split(" ")[0]);
	    final int z = Integer.parseInt(region.getKey().split(" ")[1]);
	    
	    if(squaredDistance(camera, x, z) > (viewDistance + 1) * (viewDistance + 1)) {
		regionsToRemove.add(region.getKey());
	    }
	}
	for(String key : regionsToRemove) {
	    regions.get(key).dispose();
	    regions.remove(key);
	}
	regionsToRemove.clear();
    }
    
    private void addRegionsInRange(final PerspectiveCamera camera) {
	for(int i = -viewDistance; i <= viewDistance; i++) {
	    for(int j = -viewDistance; j <= viewDistance; j++) {
		if(squaredDistance(camera, i, j) < viewDistance * viewDistance) {
		    final int x = i + (int) (camera.getPosition().x / Region.SIZE);
		    final int z = j + (int) (camera.getPosition().z / Region.SIZE);

		    if (!regions.containsKey(x + " " + z)) {
			regions.put(x + " " + z, new Region(x, z));
		    }
		}
	    }
	}
    }
    
    private float squaredDistance(final PerspectiveCamera camera, final int regionX, final int regionZ) {
	final float cx = camera.getPosition().x / Region.SIZE + 0.5f;
	final float cz = camera.getPosition().z / Region.SIZE - 0.5f;
	final float rx = regionX;
	final float rz = regionZ;
	
	return (rx - cx) * (rx - cx) + (rz - cz) * (rz - cz);
    }

}
