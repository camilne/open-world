package com.camilne.app;

import java.nio.ByteBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.vector.Vector2f;

public class Window {
        
    private long handle;
    private long monitor;
    
    private int width;
    private int height;
    private String title;
    
    public Window() {}
    
    /**
     * Creates a window with the specified width, height, and title on the default monitor
     * @param width The width of the window
     * @param height The height of the window
     * @param title The title of the window
     */
    public void create(int width, int height, String title) {
	create(width, height, title, MemoryUtil.NULL);
    }
    
    /**
     * Creates a window width the specified width, height, and title on the specified monitor
     * @param width The width of the window
     * @param height The height of the window
     * @param title The title of the window
     * @param monitor The monitor on which to create the window
     */
    public void create(int width, int height, String title, long monitor) {
	this.width = width;
	this.height = height;
	this.title = title;
	this.monitor = monitor;
	
	// Obtain a handle for the window
	handle = GLFW.glfwCreateWindow(width, height, title, 0, MemoryUtil.NULL);
	// If the window did not create properly
	if(handle == 0) {
	    throw new IllegalStateException("Window did not initalize");
	}
    }
    
    /**
     * Refreshes the window by swapping buffers
     */
    public void swapBuffers() throws IllegalStateException {
	if(handle == 0) {
	    throw new IllegalStateException("handle is NULL");
	}
	
	GLFW.glfwSwapBuffers(handle);
    }
    
    /**
     * Sets the key callback for the window
     * @param callback The callback that will receive keyboard input
     */
    public void setKeyCallback(GLFWKeyCallback callback) throws IllegalStateException {
	if(handle == 0) {
	    throw new IllegalStateException("handle is NULL");
	}
	
	GLFW.glfwSetKeyCallback(handle, callback);
    }
    
    /**
     * Sets the cursor position callback for the window
     * @param callback The callback that will recieve mouse movement input
     */
    public void setCursorPosCallback(GLFWCursorPosCallback callback) throws IllegalStateException {
	if(handle == 0) {
	    throw new IllegalStateException("handle is NULL");
	}
	
	GLFW.glfwSetCursorPosCallback(handle, callback);
    }
    
    /**
     * Sets the size of this window
     * @param width
     * @param height
     */
    public void setSize(int width, int height) throws IllegalStateException {
	if(handle == 0) {
	    throw new IllegalStateException("handle is NULL");
	}
	
	GLFW.glfwSetWindowSize(handle, width, height);
    }
    
    /**
     * Set the position of the window anchored in the upper-left corner
     * @param position The position of the upper-left corner. If position is null, then window centers on set monitor.
     */
    public void setPosition(Vector2f position) {
	// If the position is null, center the window
	if(position == null) {
	    // Get the monitor video settings
	    ByteBuffer vidmode = null;
	    if(this.monitor != MemoryUtil.NULL)
		vidmode = GLFW.glfwGetVideoMode(monitor);
	    else
		vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
	    
	    // Get the monitor dimensions
	    int monitorWidth = GLFWvidmode.width(vidmode);
	    int monitorHeight = GLFWvidmode.height(vidmode);
	    
	    // Get the position of the window anchor
	    position = new Vector2f();
	    position.x = (monitorWidth - width) / 2;
	    position.y = (monitorHeight - height) / 2;
	}
	
	// Set the position of the window
	GLFW.glfwSetWindowPos(handle, (int)position.x, (int)position.y);
    }
    
    /**
     * Sets the title of this window
     * @param title
     */
    public void setTitle(String title) throws IllegalStateException {
	if(title == null) {
	    System.err.println("Null title");
	    return;
	}
	
	if(handle == 0) {
	    throw new IllegalStateException("handle is NULL");
	}
	
	GLFW.glfwSetWindowTitle(handle, title);
    }
    
    /**
     * Enables or disables vsync
     * @param flag 
     */
    public void setVSyncEnabled(boolean flag) {
	GLFW.glfwSwapInterval(flag ? 1 : 0);
    }
    
    /**
     * Makes this window visible
     */
    public void show() throws IllegalStateException {
	if(handle == 0) {
	    throw new IllegalStateException("handle is NULL");
	}
	
	GLFW.glfwShowWindow(handle);
    }
    
    /**
     * Makes this window invisible
     */
    public void hide() throws IllegalStateException {
	if(handle == 0) {
	    throw new IllegalStateException("handle is NULL");
	}
	
	GLFW.glfwHideWindow(handle);
    }
    
    /**
     * Causes this window to obtain the current GLContext
     */
    public void makeContextCurrent() throws IllegalStateException {
	if(handle == 0) {
	    throw new IllegalStateException("handle is NULL");
	}
	
	GLFW.glfwMakeContextCurrent(handle);
    }
    
    /**
     * Releases the cursor from the window and makes it visible
     */
    public void releaseCursor() throws IllegalStateException {
	if(handle == 0) {
	    throw new IllegalStateException("handle is NULL");
	}
	
	GLFW.glfwSetInputMode(handle, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
    }
    
    /**
     * Captures the cursor to the window and makes it invisible
     */
    public void captureCursor() throws IllegalStateException {
	if(handle == 0) {
	    throw new IllegalStateException("handle is NULL");
	}
	
	GLFW.glfwSetInputMode(handle, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
    }
    
    /**
     * Destroys the context of this window
     */
    public void destroy() throws IllegalStateException {
	if(handle == 0) {
	    throw new IllegalStateException("handle is NULL");
	}
	
	GLFW.glfwDestroyWindow(handle);
    }
    
    /**
     * Returns whether or not this window is trying to close
     * @return true if closing
     */
    public boolean isClosing() throws IllegalStateException {
	if(handle == 0) {
	    throw new IllegalStateException("handle is NULL");
	}
	
	return GLFW.glfwWindowShouldClose(handle) == 1;
    }
    
    /**
     * 
     * @return The width of the window
     */
    public int getWidth() {
	return width;
    }
    
    /**
     * 
     * @return The height of the window
     */
    public int getHeight() {
	return height;
    }
    
    /**
     * 
     * @return The title of the window
     */
    public String getTitle() {
	return title;
    }
    
    /**
     * Sets the specified input mode to the specified value
     * @param mode
     * @param value
     */
    public void setInputMode(int mode, int value) throws IllegalStateException {
	if(handle == 0) {
	    throw new IllegalStateException("handle is NULL");
	}
	
	GLFW.glfwSetInputMode(handle, mode, value);
    }
    
    /**
     * Returns the state of the specified mouse button
     * @param button The mouse button (0 = left, 1 = right, 2 = middle)
     * @return
     */
    public int getMouseButton(int button) throws IllegalStateException {
	if(handle == 0) {
	    throw new IllegalStateException("handle is NULL");
	}
	
	return GLFW.glfwGetMouseButton(handle, button);
    }
    
    /**
     * Sets the GLFW window hints to default
     */
    public static void defaultHints() {
	GLFW.glfwDefaultWindowHints();
    }
    
    /**
     * Hint to GLFW
     * @param target
     * @param hint
     */
    public static void hint(int target, int hint) {
	GLFW.glfwWindowHint(target, hint);
    }

}
