package com.camilne.world;

import java.io.IOException;

import org.lwjgl.util.vector.Vector3f;

import com.camilne.rendering.Mesh;
import com.camilne.rendering.PerspectiveCamera;
import com.camilne.rendering.Shader;
import com.camilne.rendering.Texture;
import com.camilne.rendering.TextureRegion;
import com.camilne.rendering.Vertex;

public class Skybox {
        
    private TextureRegion[] skyboxTextureRegions;
    private Shader skyboxShader;
    private Mesh skyboxMesh;
    
    /**
     * Creates a skybox centered at the player based on the configuration.
     * @param config The configuration the skybox should be created in.
     * @throws IOException If the texture or shader is not loaded correctly.
     */
    public Skybox(final SkyboxConfiguration config) throws IOException {
	// The pixel size of the texture on each face.
	//final int TEXTURE_REGION_SIZE = 512;
	skyboxTextureRegions = new TextureRegion[6];
	// Loads the texture for each face of the skybox and assigns it to the TextureRegion array based on the configuration.
	Texture skyboxTexture = new Texture(config.textureName);
	for(int i = 0; i < skyboxTextureRegions.length; i++) {
	    skyboxTextureRegions[i] = createRegion(skyboxTexture, SkyboxFace.values()[i], config);
	}

	skyboxShader = new Shader(config.shaderName);
	
	Vertex[] skyboxMeshVertices = new Vertex[24];
	int[] skyboxMeshIndices = new int[36];
	
	final int size = config.size;
	int i = 0;
	int j = 0;
	// Front face
	skyboxMeshVertices[i++] = new Vertex(new Vector3f(-size, -size, -size), skyboxTextureRegions[j  ].getST());
	skyboxMeshVertices[i++] = new Vertex(new Vector3f( size, -size, -size), skyboxTextureRegions[j  ].getUT());
	skyboxMeshVertices[i++] = new Vertex(new Vector3f( size,  size, -size), skyboxTextureRegions[j  ].getUV());
	skyboxMeshVertices[i++] = new Vertex(new Vector3f(-size,  size, -size), skyboxTextureRegions[j++].getSV());

	// Right face
	skyboxMeshVertices[i++] = new Vertex(new Vector3f( size, -size, -size), skyboxTextureRegions[j  ].getST());
	skyboxMeshVertices[i++] = new Vertex(new Vector3f( size, -size,  size), skyboxTextureRegions[j  ].getUT());
	skyboxMeshVertices[i++] = new Vertex(new Vector3f( size,  size,  size), skyboxTextureRegions[j  ].getUV());
	skyboxMeshVertices[i++] = new Vertex(new Vector3f( size,  size, -size), skyboxTextureRegions[j++].getSV());
	
	// Back face
	skyboxMeshVertices[i++] = new Vertex(new Vector3f( size, -size,  size), skyboxTextureRegions[j  ].getST());
	skyboxMeshVertices[i++] = new Vertex(new Vector3f(-size, -size,  size), skyboxTextureRegions[j  ].getUT());
	skyboxMeshVertices[i++] = new Vertex(new Vector3f(-size,  size,  size), skyboxTextureRegions[j  ].getUV());
	skyboxMeshVertices[i++] = new Vertex(new Vector3f( size,  size,  size), skyboxTextureRegions[j++].getSV());
	
	// Left face
	skyboxMeshVertices[i++] = new Vertex(new Vector3f(-size, -size,  size), skyboxTextureRegions[j  ].getST());
	skyboxMeshVertices[i++] = new Vertex(new Vector3f(-size, -size, -size), skyboxTextureRegions[j  ].getUT());
	skyboxMeshVertices[i++] = new Vertex(new Vector3f(-size,  size, -size), skyboxTextureRegions[j  ].getUV());
	skyboxMeshVertices[i++] = new Vertex(new Vector3f(-size,  size,  size), skyboxTextureRegions[j++].getSV());
	
	// Top face
	skyboxMeshVertices[i++] = new Vertex(new Vector3f( size,  size, -size), skyboxTextureRegions[j  ].getST());
	skyboxMeshVertices[i++] = new Vertex(new Vector3f( size,  size,  size), skyboxTextureRegions[j  ].getUT());
	skyboxMeshVertices[i++] = new Vertex(new Vector3f(-size,  size,  size), skyboxTextureRegions[j  ].getUV());
	skyboxMeshVertices[i++] = new Vertex(new Vector3f(-size,  size, -size), skyboxTextureRegions[j++].getSV());
	
	// Bottom face
	skyboxMeshVertices[i++] = new Vertex(new Vector3f(-size, -size, -size), skyboxTextureRegions[j  ].getST()); // culling weird? changed pos order
	skyboxMeshVertices[i++] = new Vertex(new Vector3f(-size, -size,  size), skyboxTextureRegions[j  ].getUT());
	skyboxMeshVertices[i++] = new Vertex(new Vector3f( size, -size,  size), skyboxTextureRegions[j  ].getUV());
	skyboxMeshVertices[i++] = new Vertex(new Vector3f( size, -size, -size), skyboxTextureRegions[j++].getSV());
	
	int l = 0;
	for(int k = 0; k < skyboxMeshIndices.length; k++) {
	    skyboxMeshIndices[k++] = l;
	    skyboxMeshIndices[k++] = l + 1;
	    skyboxMeshIndices[k++] = l + 2;
	    skyboxMeshIndices[k++] = l + 2;
	    skyboxMeshIndices[k++] = l + 3;
	    skyboxMeshIndices[k  ] = l;
	    l += 4;
	}
	
	skyboxMesh = new Mesh(skyboxMeshVertices, skyboxMeshIndices);
    }
    
    /**
     * Renders the skybox centered at the camera.
     * @param camera
     */
    public void render(PerspectiveCamera camera) {
	if(skyboxShader == null) {
	    throw new NullPointerException("Skybox Shader is null");
	}
	
	skyboxShader.bind();
	if(camera != null) {
	    skyboxShader.setUniform("m_proj", camera.getProjection());
	    skyboxShader.setUniform("m_view", camera.getView());
	}
	
	if(skyboxTextureRegions != null && skyboxTextureRegions[0] != null) {
	    skyboxTextureRegions[0].bind();
	}
	if(skyboxMesh == null) {
	    throw new NullPointerException("Skybox mesh is null");
	}
	skyboxMesh.render();
    }
    
    /**
     * Creates a texture region of the skybox face.
     * @param texture The texture from which to create the skybox region.
     * @param face The face to locate on the texture.
     * @param config The configuration to use to locate the feature.
     * @return The new TextureRegion representing the texture of the face.
     */
    public TextureRegion createRegion(final Texture texture, final SkyboxFace face, final SkyboxConfiguration config) {
	final int x = (int)config.get(face).x;
	final int y = (int)config.get(face).y;
	final int texSize = config.faceTextureSize;
	
	return new TextureRegion(texture, x * texSize, y * texSize, texSize, texSize);
    }
    
}
