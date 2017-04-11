package com.camilne.rendering;

import org.lwjgl.util.vector.Matrix4f;

public class OrthographicCamera extends Camera{
    
    /**
     * Creates a new OrthographicCamera with the specified orthographic projection properties
     * @param left   Coordinates of the left vertical clipping plane.
     * @param right  Coordinates of the right vertical clipping plane.
     * @param bottom Coordinates of the bottom horizontal clipping plane.
     * @param top    Coordinates of the top horizontal clipping plane.
     * @param zNear  The distance to the near clipping plane.
     * @param zFar   The distance to the far clipping plane.
     */
    public OrthographicCamera(float left, float right, float bottom, float top, float zNear, float zFar) {
	super(createOrthographic(left, right, bottom, top, zNear, zFar));
    }

    /**
     * Creates an Orthographic Projection Matrix.
     * 
     * @param left   Coordinates of the left vertical clipping plane.
     * @param right  Coordinates of the right vertical clipping plane.
     * @param bottom Coordinates of the bottom horizontal clipping plane.
     * @param top    Coordinates of the top horizontal clipping plane.
     * @param zNear  The distance to the near clipping plane.
     * @param zFar   The distance to the far clipping plane.
     * 
     * @return An Orthographic projection matrix.
     */
    public static Matrix4f createOrthographic(float left,   float right, float bottom, float top, float zNear,  float zFar){
        // Create a new empty matrix
        Matrix4f mat = new Matrix4f();
        
        // Apply the projection to it
        mat.m00 = 2 / (right - left);
        mat.m11 = 2 / (top - bottom);
        mat.m22 = -2 / (zFar - zNear);
        mat.m30 = -(right + left) / (right - left);
        mat.m31 = -(top + bottom) / (top - bottom);
        mat.m32 = -(zFar + zNear) / (zFar - zNear);
        mat.m33 = 1;
        
        return mat;
    }

}
