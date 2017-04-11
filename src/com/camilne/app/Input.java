package com.camilne.app;

import java.util.Arrays;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;

public class Input {
    
    // The input callbacks 
    public static GLFWCursorPosCallback cursorPosCallback;
    public static GLFWKeyCallback keyCallback;
    
    // Stores the states of the keys in the current and last frames
    public static boolean[] keys = new boolean[512];
    public static boolean[] lastKeys = new boolean[512];
    
    // The ApplicationListener to call when input updates
    public static ApplicationListener applicationListener;
    
    // The location of the mouse in the last frame
    private static double lastMouseX;
    private static double lastMouseY;
    
    static {
	// Setup the cursor position calllback
	cursorPosCallback = new GLFWCursorPosCallback() {

	    @Override
	    public void invoke(long window, double xpos, double ypos) {
		mouseMoved(xpos, ypos);
	    }
	    
	};
	
	// Setup the keyboard callback
	keyCallback = new GLFWKeyCallback() {

	    @Override
	    public void invoke(long window, int keycode, int scancode, int action, int mods) {
		// If the key was pressed
		if(action == GLFW.GLFW_PRESS) {
		    keyPressed(keycode);
		// If the key was released
		} else if (action == GLFW.GLFW_RELEASE){
		    keyReleased(keycode);
		}
	    }
	    
	};
	
	// Fill the arrays with default info
	Arrays.fill(keys, false);
	Arrays.fill(lastKeys, false);
	
	// Assume the mouse starts at 0, 0
	lastMouseX = 0;
	lastMouseY = 0;
    }
    
    /**
     * Updates the input key states
     */
    public static void update() {
	lastKeys = keys;
    }
    
    /**
     * Releases memory used by GLFW Callbacks
     */
    public static void dispose() {
	cursorPosCallback.release();
	keyCallback.release();
    }
    
    /**
     * Returns whether or not the specified key is currently down
     * @param keycode The key to check
     * @return true if the key is down, false if the key is up
     */
    public static boolean isKeyPressed(int keycode) {    
	return keys[keycode];
    }
    
    /**
     * Returns whether or not the specified key was pressed between the last and current frame
     * @param keycode The key to check
     * @return true if the key was just pressed, false otherwise
     */
    public static boolean isKeyJustPressed(int keycode) {
	return keys[keycode] && !lastKeys[keycode];
    }
    
    /**
     * Returns whether or not the specified key was released between the last and current frame
     * @param keycode The key to check
     * @return true if the key was just released, false otherwise
     */
    public static boolean isKeyJustReleased(int keycode) {
	return !keys[keycode] && lastKeys[keycode];
    }
    
    /**
     * Called whenever the mouse moves (from the CursorPosCallback)
     * @param xpos The x-position of the mouse
     * @param ypos The y-position of the mouse
     */
    private static void mouseMoved(double xpos, double ypos) {
	// Notify the user application
	if(applicationListener != null)
	    applicationListener.mouseMoved(xpos, ypos, xpos - lastMouseX, ypos - lastMouseY);
	
	// Save the positions to calculate deltas
	lastMouseX = xpos;
	lastMouseY = ypos;
    }
    
    /**
     * Called whenever the specified key is pressed (from the KeyCallback)
     * @param keycode The specified key
     */
    private static void keyPressed(int keycode) {
	// Update the key array
	keys[keycode] = true;
	
	// Notify the user application
	if(applicationListener != null)
	    applicationListener.keyPressed(keycode);
    }
    
    /**
     * Called whenever the specified key is released (from the KeyCallback)
     * @param keycode The specified key
     */
    private static void keyReleased(int keycode) {
	// Update the key array
	keys[keycode] = false;
	
	// Notify the user application
	if(applicationListener != null)
	    applicationListener.keyReleased(keycode);
    }
    
}
