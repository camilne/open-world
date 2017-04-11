package com.camilne.rendering;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

import com.camilne.util.QuaternionUtil;

public class Camera {
    
    // World space axes for reference
    public static final Vector3f AXIS_X = new Vector3f(1, 0, 0);
    public static final Vector3f AXIS_Y = new Vector3f(0, 1, 0);
    public static final Vector3f AXIS_Z = new Vector3f(0, 0, 1);
    
    // Position of the Camera and its orientation
    private Vector3f position;
    private Quaternion orientation;
    
    // The projection and view matrices of the Camera
    private Matrix4f projection;
    private Matrix4f view;
    
    // View space axes of the camera
    private Vector3f up;
    private Vector3f forward;
    private Vector3f right;
    
    /**
     *	Enumeration of all possible directions
     */
    public static enum Direction {
	FORWARD, BACKWARD, LEFT, RIGHT, UP, DOWN
    }
    
    /**
     * Creates a camera with the specified projection matrix
     * @param projection
     */
    public Camera(Matrix4f projection) {
	this.projection = projection;
	
	// Set the view to a default identity matrix
	view = new Matrix4f();
	
	// Set the position to origin
	position = new Vector3f();
	// Set the orientation to standard
	orientation = new Quaternion();
	
	// Create the default local axes (right handed)
	up = new Vector3f(AXIS_Y);
	forward = new Vector3f(AXIS_Z).negate(null);
	right = new Vector3f(AXIS_X);
    }
    
    /**
     * Updates the view matrix
     */
    public void update() {
	// Rotate the scene based on orientation
	QuaternionUtil.toRotationMatrix(orientation, view);
	// Translate the scene based on position
	Matrix4f.translate(position.negate(null), view, view);
    }
    
    /**
     * Convenience method for move(Vector3f, float). Takes an enumeration for possible camera directions
     * @param direction The direction to move in view space
     * @param distance The distance to move in world space
     */
    public void move(Direction direction, float distance) {
	switch(direction) {
	case FORWARD:	move(forward, distance); break;
	case BACKWARD:	move(forward, -distance); break;
	case RIGHT:	move(right, distance); break;
	case LEFT:	move(right, -distance); break;
	case UP:	move(up, distance); break;
	case DOWN:	move(up, -distance); break;
	}
    }
    
    /**
     * Moves the camera in the specified direction by the specified distance
     * @param direction The direction to move in world space
     * @param distance The distance to move in world space
     */
    public void move(Vector3f direction, float distance) {
	// Create a copy of the direction to avoid modifying the original TODO: fix temp
	Vector3f deltaMovement = new Vector3f(direction);
	
	// Scale the direction to be the length of the specified distance
	deltaMovement.normalise();
	deltaMovement.scale(distance);
	
	// Add the movement to the camera's position
	Vector3f.add(position, deltaMovement, position);
    }
    
    /**
     * Rotates the camera around the world x-axis
     * @param angle The angle to rotate in degrees
     */
    public void rotateXAxis(float angle) {
	rotate(AXIS_X, up, forward, angle);
    }
    
    /**
     * Rotates the camera around the world y-axis
     * @param angle The angle to rotate in degrees
     */
    public void rotateYAxis(float angle) {
	rotate(AXIS_Y, forward, right, angle);
    }
    
    /**
     * Rotates the camera around the world z-axis
     * @param angle The angle to rotate in degrees
     */
    public void rotateZAxis(float angle) {
	rotate(AXIS_Z, up, right, angle);
    }
    
    /**
     * Rotates the camera around the camera's x-axis
     * @param angle The angle to rotate in degrees
     */
    public void rotateX(float angle) {
	rotate(right, up, forward, angle);
    }
    
    /**
     * Rotates the camera around the camera's y-axis
     * @param angle The angle to rotate in degrees
     */
    public void rotateY(float angle) {
	rotate(up, forward, right, angle);
    }
    
    /**
     * Rotates the camera around the camera's z-axis
     * @param angle The angle to rotate in degrees
     */
    public void rotateZ(float angle) {
	rotate(forward, up, right, angle);
    }
    
    /**
     * Rotates the camera around the specified axis in the counter-clockwise direction
     * @param axis The axis to rotate around
     * @param v1 The first local camera vector to be updated
     * @param v2 The second local camera vector to be updated
     * @param angle The amount to rotate around the axis in degrees
     */
    private void rotate(Vector3f axis, Vector3f v1, Vector3f v2, float angle) {
	Quaternion rotation = QuaternionUtil.createFromAxisAngle(axis, angle, null);
	Quaternion.mul(rotation, orientation, orientation);
	
	QuaternionUtil.rotate(v1, rotation, v1);
	QuaternionUtil.rotate(v2, rotation, v2);
	
	v1.normalise();
	v2.normalise();
    }

    /**
     * Returns the position of the camera in world space
     * @return
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     * Sets the position of the camera in world space
     * @param position
     */
    public void setPosition(Vector3f position) {
        this.position = position;
    }

    /**
     * Returns the projection matrix
     * @return
     */
    public Matrix4f getProjection() {
        return projection;
    }

    /**
     * Sets the projection matrix
     * @param projection
     */
    public void setProjection(Matrix4f projection) {
        this.projection = projection;
    }

    /**
     * Returns the view matrix
     * @return
     */
    public Matrix4f getView() {
        return view;
    }

    /**
     * Returns the camera's up vector
     * @return
     */
    public Vector3f getUp() {
        return up;
    }

    /**
     * Returns the camera's forward vector
     * @return
     */
    public Vector3f getForward() {
        return forward;
    }

    /**
     * Returns the camera's right vector
     * @return
     */
    public Vector3f getRight() {
        return right;
    }
}
