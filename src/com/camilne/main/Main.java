package com.camilne.main;

import java.io.IOException;

import org.lwjgl.util.vector.Vector3f;

import com.camilne.app.Application;
import com.camilne.app.ApplicationConfiguration;
import com.camilne.app.ApplicationListener;
import com.camilne.rendering.Mesh;
import com.camilne.rendering.Shader;
import com.camilne.rendering.Vertex;

public class Main implements ApplicationListener{
    
    private Shader mainShader;
    private Mesh testMesh;
    
    private Main() {
	mainShader = null;
	testMesh = null;
	
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
		new Vertex(new Vector3f(0, 0, 0)),
		new Vertex(new Vector3f(1, 0, 0)),
		new Vertex(new Vector3f(1, 1, 0))
	};
	final int testMeshIndices[] = {
		0, 1, 2
	};
	testMesh = new Mesh(testMeshVertices, testMeshIndices);
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void render() {
	mainShader.bind();
	testMesh.render();
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
