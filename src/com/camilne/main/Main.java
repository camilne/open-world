package com.camilne.main;

import org.lwjgl.glfw.GLFW;

import com.camilne.app.Application;
import com.camilne.app.ApplicationConfiguration;
import com.camilne.app.ApplicationListener;
import com.camilne.app.Input;
import com.camilne.rendering.Camera;
import com.camilne.rendering.PerspectiveCamera;
import com.camilne.rendering.Shader;
import com.camilne.rendering.Texture;
import com.camilne.world.World;

public class Main implements ApplicationListener{
    
    private PerspectiveCamera camera;
    private float speed;
    private float sensitivity;
    private World world;
    
    private Main() {
	camera = null;
	speed = 5f;
	sensitivity = 0.2f;
	world = null;
	
	ApplicationConfiguration config = new ApplicationConfiguration();
	config.width = 1280;
	config.height = 720;
	config.title = "OpenWorld - Demo";
	config.vSyncEnabled = true;
	config.show = true;
	
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
	
	Texture.setPath("res/textures/");
	
	camera = new PerspectiveCamera(65.0f, 1280.0f/720.0f, 0.01f, 1000f);
	
	world = new World();
    }

    @Override
    public void update(float delta) {
	final float speedThisFrame = speed * delta;
	
	// Move forward
	if (Input.isKeyPressed(GLFW.GLFW_KEY_W))
	    camera.move(Camera.Direction.FORWARD, speedThisFrame);
	// Move backward
	if (Input.isKeyPressed(GLFW.GLFW_KEY_S))
	    camera.move(Camera.Direction.BACKWARD, speedThisFrame);
	// Move right
	if (Input.isKeyPressed(GLFW.GLFW_KEY_D))
	    camera.move(Camera.Direction.RIGHT, speedThisFrame);
	// Move left
	if (Input.isKeyPressed(GLFW.GLFW_KEY_A))
	    camera.move(Camera.Direction.LEFT, speedThisFrame);
	// Move up
	if (Input.isKeyPressed(GLFW.GLFW_KEY_SPACE))
	    camera.move(Camera.AXIS_Y, speedThisFrame);
	// Move down
	if (Input.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT))
	    camera.move(Camera.AXIS_Y, -speedThisFrame);
	
	camera.update();
	world.update(camera);
    }

    @Override
    public void render() {	
	world.render(camera);
    }

    @Override
    public void dispose() {
    }

    @Override
    public void mouseMoved(double xpos, double ypos, double dx, double dy) {
	camera.rotateYAxis((float) (-dx * sensitivity));
	camera.rotateX((float) (-dy * sensitivity));
    }

    @Override
    public void keyPressed(int keycode) {
	if(keycode == GLFW.GLFW_KEY_ESCAPE)
	    System.exit(0);
    }

    @Override
    public void keyReleased(int keycode) {
    }

}
