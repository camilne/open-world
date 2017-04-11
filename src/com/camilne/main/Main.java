package com.camilne.main;

import com.camilne.app.Application;
import com.camilne.app.ApplicationConfiguration;
import com.camilne.app.ApplicationListener;

public class Main implements ApplicationListener{
    
    private Main() {
	ApplicationConfiguration config = new ApplicationConfiguration();
	config.width = 1280;
	config.height = 720;
	config.title = "OpenWorld - Demo";
	config.vSyncEnabled = true;
	
	Application app = new Application(this, config);
	app.run();
    }

    public static void main(String[] args) {
	new Main();
    }

    @Override
    public void create() {
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
