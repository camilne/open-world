package com.camilne.main;

import java.io.IOException;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.camilne.app.Application;
import com.camilne.app.ApplicationConfiguration;
import com.camilne.app.ApplicationListener;
import com.camilne.app.Input;
import com.camilne.rendering.Camera;
import com.camilne.rendering.Mesh;
import com.camilne.rendering.PerspectiveCamera;
import com.camilne.rendering.Shader;
import com.camilne.rendering.Texture;
import com.camilne.rendering.Vertex;

public class Main implements ApplicationListener{
    
    private Shader mainShader;
    private Mesh testMesh;
    private PerspectiveCamera camera;
    private float speed;
    private float sensitivity;
    private Texture testTexture;
    
    private Main() {
	mainShader = null;
	testMesh = null;
	camera = null;
	speed = 1f;
	sensitivity = 0.1f;
	testTexture = null;
	
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
	
	final Vertex testMeshVertices[] = {
		new Vertex(new Vector3f(0, 0, 0), new Vector2f(0, 0)),
		new Vertex(new Vector3f(1, 0, 0), new Vector2f(1, 0)),
		new Vertex(new Vector3f(1, 1, -1), new Vector2f(1, 1))
	};
	final int testMeshIndices[] = {
		0, 1, 2
	};
	testMesh = new Mesh(testMeshVertices, testMeshIndices);
	
	camera = new PerspectiveCamera(65.0f, 1280.0f/720.0f, 0.1f, 200f);
	
	Texture.setPath("res/textures/");
	testTexture = new Texture("test.png");
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
    }

    @Override
    public void render() {
	mainShader.bind();
	mainShader.setUniform("m_proj", camera.getProjection());
	mainShader.setUniform("m_view", camera.getView());
	
	testTexture.bind();
	testMesh.render();
    }

    @Override
    public void dispose() {
	testTexture.dispose();
    }

    @Override
    public void mouseMoved(double xpos, double ypos, double dx, double dy) {
	camera.rotateYAxis((float) (-dx * sensitivity));
	camera.rotateX((float) (-dy * sensitivity));
    }

    @Override
    public void keyPressed(int keycode) {
    }

    @Override
    public void keyReleased(int keycode) {
    }

}
