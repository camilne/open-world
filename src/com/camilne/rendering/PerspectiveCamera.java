package com.camilne.rendering;

import org.lwjgl.util.vector.Matrix4f;

public class PerspectiveCamera extends Camera{
    
    private float fov;
    private float aspect;
    private float zNear;
    private float zFar;

    /**
     * Creates a new PerspectiveCamera with the specified projection properties
     * @param fov The field of view of the camera in degrees
     * @param aspect The aspect ration of the display (width/height)
     * @param zNear The distance to the near plane
     * @param zFar The distance to the far plane
     */
    public PerspectiveCamera(float fov, float aspect, float zNear, float zFar) {
	super(createPerspective(fov, aspect, zNear, zFar));
	
	this.fov = fov;
	this.aspect = aspect;
	this.zNear = zNear;
	this.zFar = zFar;
    }
    
    /**
     * Creates a Perspective Projection matrix.
     * 
     * @param fov    The Field Of View Angle.
     * @param aspect The Aspect ratio of the Display.
     * @param zNear  The near depth plane.
     * @param zFar   The far depth plane.
     * 
     * @return A Perspective Projection Matrix. 
     */
    private static Matrix4f createPerspective(float fov, float aspect, float zNear, float zFar)
    {
        // Create a new empty matrix
        Matrix4f mat = new Matrix4f();
        
        // Calculate the frustum properties from aspect ratio
        float yScale = 1f / (float) Math.tan(Math.toRadians(fov / 2f));
        float xScale = yScale / aspect;
        float frustumLength = zFar - zNear;
        
        // Apply them to the matrix
        mat.m00 = xScale;
        mat.m11 = yScale;
        mat.m22 = -((zFar + zNear) / frustumLength);
        mat.m23 = -1;
        mat.m32 = -((2 * zFar * zNear) / frustumLength);
        mat.m33 = 0;
        
        return mat;
    }

    /**
     * Returns the field of view of the perspective projection
     * @return
     */
    public float getFov() {
        return fov;
    }

    /**
     * Returns the aspect ratio of the perspective projection (width / height)
     * @return
     */
    public float getAspect() {
        return aspect;
    }

    /**
     * Returns the distance to the near plane
     * @return
     */
    public float getzNear() {
        return zNear;
    }

    /**
     * Returns the distance to the far plane
     * @return
     */
    public float getzFar() {
        return zFar;
    }

}
