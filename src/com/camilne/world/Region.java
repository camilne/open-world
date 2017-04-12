package com.camilne.world;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.camilne.rendering.Mesh;
import com.camilne.rendering.Shader;
import com.camilne.rendering.Vertex;

public class Region {
    
    private static final int SIZE = 32;
    
    private Mesh terrainMesh;
    private Matrix4f transformationMatrix;
    
    public Region(float x, float y) {
	createTerrain();
	transformationMatrix = new Matrix4f();
	transformationMatrix.translate(new Vector2f(x, y));
    }
    
    public void render(Shader shader) {
	shader.setUniform("m_model", transformationMatrix);
	terrainMesh.render();
    }
    
    private void createTerrain() {
	Vertex[] vertices = new Vertex[SIZE * SIZE * 4];
	int idx = 0;
	for(int j = 0; j < SIZE; j++) {
	    for(int i = 0; i < SIZE; i++) {
		vertices[idx++] = new Vertex(new Vector3f(i, 0, -j),		new Vector2f(0, 0), new Vector3f(0, 1, 0));
		vertices[idx++] = new Vertex(new Vector3f(i + 1, 0, -j),	new Vector2f(1, 0), new Vector3f(0, 1, 0));
		vertices[idx++] = new Vertex(new Vector3f(i + 1, 0, -(j + 1)),	new Vector2f(1, 1), new Vector3f(0, 1, 0));
		vertices[idx++] = new Vertex(new Vector3f(i, 0, -(j + 1)),	new Vector2f(0, 1), new Vector3f(0, 1, 0));
	    }
	}
	
	for(int i = 0; i < vertices.length; i++) {
	    switch(i % 4) {
	    case 0:
		if(i + 3 < vertices.length) {
		    calculateNormal(vertices[i], vertices[i + 1], vertices[i + 3]);
		}
		break;
	    case 1:
	    case 2:
		if(i + 1 < vertices.length && i - 1 >= 0) {
		    calculateNormal(vertices[i], vertices[i + 1], vertices[i - 1]);
		}
		break;
	    case 3:
		if(i - 3 >= 0) {
		    calculateNormal(vertices[i], vertices[i - 3], vertices[i - 1]);
		}
		break;
	    }
	}
	
	int[] indices = new int[SIZE * SIZE * 6];
	int index = 0;
	int indicesIndex = 0;
	for(int j = 0; j < SIZE; j++) {
	    for(int i = 0; i < SIZE; i++) {
		indices[indicesIndex++] = index + 0;
		indices[indicesIndex++] = index + 1;
		indices[indicesIndex++] = index + 2;
		indices[indicesIndex++] = index + 2;
		indices[indicesIndex++] = index + 3;
		indices[indicesIndex++] = index + 0;
		index += 4;
	    }
	}
	
	terrainMesh = new Mesh(vertices, indices, false);
    }

    private void calculateNormal(Vertex src, Vertex v1, Vertex v2) {
	Vector3f a = Vector3f.sub(v1.getPos(), src.getPos(), null);
	Vector3f b = Vector3f.sub(v2.getPos(), src.getPos(), null);
	
	Vector3f.cross(a, b, src.getNormal()).normalise();
    }
    
}
