package com.camilne.world;

import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.camilne.rendering.FrameBuffer;
import com.camilne.rendering.Mesh;
import com.camilne.rendering.PerspectiveCamera;
import com.camilne.rendering.Texture;
import com.camilne.rendering.Vertex;

public class WaterRegion {
    
    // Static mesh for the water. The mesh is transformed differently for each WaterRegion.
    private static final Mesh MESH = new Mesh(
	    new Vertex[] {
		    new Vertex(new Vector3f(0, 0, 0), new Vector2f(0, 0)),
		    new Vertex(new Vector3f(Region.SIZE, 0, 0), new Vector2f(1, 0)),
		    new Vertex(new Vector3f(Region.SIZE, 0, -Region.SIZE), new Vector2f(1, 1)),
		    new Vertex(new Vector3f(0, 0, -Region.SIZE), new Vector2f(0, 1)), },
	    new int[] { 0, 1, 2, 2, 3, 0 });
    
    
    // Translates the water to match the parent region.
    private Matrix4f transformation;
    // Animates the water along the displacement texture.
    private static float moveFactor;
    
    // Shader to render the water.
    private static WaterShader shader = null;
    
    private static final int REFLECTION_WIDTH = 1280;
    private static final int REFLECTION_HEIGHT = 720;

    // The FBO to render the water to.
    private FrameBuffer framebuffer;
    
    // The height of the water in the world.
    public static final float WATER_HEIGHT = 0;
    
    // The wave displacement texture of the water.
    private static Texture dudvTexture;
    
    /**
     * Creates a water region at the specified x and z coordinates. Scaled to the size of a region.
     * @param x
     * @param z
     */
    public WaterRegion(float x, float z) {
	transformation = new Matrix4f().translate(new Vector3f(x, WATER_HEIGHT, z));
	
	if(shader == null) {
	    try {
		shader = new WaterShader("water");
		shader.setUniform("reflection_texture", 0);
		shader.setUniform("dudv_texture", 1);
	    } catch (IOException e) {
		e.printStackTrace();
		System.exit(1);
	    }
	}
	
	if(dudvTexture == null) {
	    dudvTexture = new Texture("waterdudv.png");
	}
	
	framebuffer = new FrameBuffer(REFLECTION_WIDTH, REFLECTION_HEIGHT);
	framebuffer.attachTexture();
	framebuffer.attachDepthBuffer();
	framebuffer.verifyIntegrity();
    }
    
    /**
     * Renders the world to the water framebuffer.
     * @param worldCamera
     * @param world
     */
    public void preRender(final PerspectiveCamera camera, final World world) {
	GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
	framebuffer.bind();
	GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	
	// Render to reflection texture.
	float verticalDistance = camera.getPosition().y - WATER_HEIGHT;
	camera.getPosition().y -= verticalDistance * 2;
	camera.invertPitch();
	camera.update();
	world.renderWithoutWater(camera, true);
	camera.invertPitch();
	camera.getPosition().y += verticalDistance * 2;
	camera.update();
	
	FrameBuffer.unbind();
	GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
    }
    
    public static void updateMovement(final double delta) {
	moveFactor += 0.01f * delta;
	moveFactor %= 1;
    }
    
    /**
     * Renders the water.
     * @param camera
     */
    public void render(final PerspectiveCamera camera) {
	shader.bind();
	shader.setUniform("m_model", transformation);
	shader.setUniform("m_view", camera.getView());
	shader.setUniform("m_proj", camera.getProjection());
	shader.setUniform("move_factor", moveFactor);
	
	framebuffer.bindTexture();
	dudvTexture.bind(GL13.GL_TEXTURE1);
	
	MESH.render();
    }
    
    /**
     * Frees memory from the graphics card.
     */
    public void dispose() {
	framebuffer.dispose();
    }

}
