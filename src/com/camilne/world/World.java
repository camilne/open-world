package com.camilne.world;

import com.camilne.rendering.Shader;

public class World {
    
    private Region region;
    
    public World() {
	region = new Region(0, 0);
    }
    
    public void render(Shader shader) {
	region.render(shader);
    }

}
