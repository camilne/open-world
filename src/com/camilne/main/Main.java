package com.camilne.main;

import java.io.IOException;

import com.camilne.app.Application;
import com.camilne.app.ApplicationConfiguration;
import com.camilne.app.ApplicationListener;
import com.camilne.rendering.Shader;

public class Main implements ApplicationListener{
    
    private Shader mainShader;
    
    private Main() {
	mainShader = null;
	
	ApplicationConfiguration config = new ApplicationConfiguration();
	config.width = 1280;
	config.height = 720;
	config.title = "OpenWorld - Demo";
	config.vSyncEnabled = true;
	
	Application app = new Application(this, config);
	try {
	    app.run();
	} catch(Exception e) {
	    e.printStackTrace();
	    System.exit(1);
	}
    }

    public static void main(String[] args) {
	new Main();
    }

    @Override
    public void create() {
	Shader.setPath("res/shaders/");
	Shader.setVertexExtension("vs");
	Shader.setFragmentExtension("fs");
	try {
	    mainShader = new Shader("main");
	} catch(IOException e) {
	    e.printStackTrace();
	    System.exit(1);
	}
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void render() {
    }

    @Override
    public void dispose() {
    }

    @Override
    public void mouseMoved(double xpos, double ypos, double dx, double dy) {
    }

    @Override
    public void keyPressed(int keycode) {
    }

    @Override
    public void keyReleased(int keycode) {
    }

}
