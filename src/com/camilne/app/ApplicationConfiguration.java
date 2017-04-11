package com.camilne.app;

import org.lwjgl.util.vector.Vector2f;

public class ApplicationConfiguration {
    
    public int width;
    public int height;
    public Vector2f position;
    public String title;
    public boolean vSyncEnabled;
    
    /**
     * Create a new ApplicationConfiguration with default settings
     */
    public ApplicationConfiguration() {
	width = 640;
	height = 480;
	position = null;
	title = "";
	vSyncEnabled = false;
    }

}
