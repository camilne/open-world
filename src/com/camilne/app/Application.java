package com.camilne.app;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GLContext;

public class Application implements Runnable{
    
    private ApplicationListener applicationListener;
    private ApplicationConfiguration config;
    
    public static final Window WINDOW = new Window();
    
    /**
     * Creates a new Application
     * @param applicationListener The user-created ApplicationListener to receive callbacks
     */
    public Application(ApplicationListener applicationListener) {
	this(applicationListener, new ApplicationConfiguration());
    }
    
    /**
     * Creates a new Application
     * @param applicationListener The user-created ApplicationListener to receive callbacks
     * @param config The window and application configuration
     */
    public Application(ApplicationListener applicationListener, ApplicationConfiguration config) {
	this.applicationListener = applicationListener;
	this.config = config;
    }
    
    /**
     * Initializes everything in the application
     */
    private void init() {
	initGLFW();
	createWindow();
	initGLFWCallbacks();
	initGL();
	
	// Initialize the user application
	applicationListener.create();
	
	// Make the window visible when all initialization has finished
	if(config.show) {
	    WINDOW.show();
	} else {
	    WINDOW.hide();
	}
    }
    
    /**
     * Initialize GLFW. Stops application if error occurs
     */
    private void initGLFW() throws IllegalStateException {
	// If there is an error in initialization
	if(GLFW.glfwInit() == 0) {
	    throw new IllegalStateException("GLFW failed to initialize");
	}
    }
    
    /**
     * Creates a GLFWWindow and gives it OpenGL context
     */
    private void createWindow() {
	// Hide the window during application initialization
	Window.hint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);
	// Setup MSAA 4x
	Window.hint(GLFW.GLFW_SAMPLES, 4);
	
	WINDOW.create(config.width, config.height, config.title);
	
	// Make this thread and window the current context for OpenGL
	WINDOW.makeContextCurrent();
	GLContext.createFromCurrent();
	
	// Set other specified window configurations
	WINDOW.setVSyncEnabled(config.vSyncEnabled);
	WINDOW.setPosition(config.position);
    }
    
    /**
     * Initialize GLFW callbacks
     */
    private void initGLFWCallbacks() {
	// Set GLFW to output errors to System.err
	GLFW.glfwSetErrorCallback(Callbacks.errorCallbackPrint(System.err));
	
	// Setup input callbacks
	WINDOW.setCursorPosCallback(Input.cursorPosCallback);
	WINDOW.setKeyCallback(Input.keyCallback);
	Input.applicationListener = applicationListener;
    }
    
    /**
     * Initialize OpenGL to default settings
     */
    private void initGL() {
	// Set the clear color
	GL11.glClearColor(0, 0, 0, 1);
	
	// Setup viewport
	GL11.glViewport(0, 0, WINDOW.getWidth(), WINDOW.getHeight());
	
	// Setup depth testing
	GL11.glEnable(GL11.GL_DEPTH_TEST);
	
	// Setup face culling (ccw)
	GL11.glFrontFace(GL11.GL_CCW);
	GL11.glCullFace(GL11.GL_BACK);
	GL11.glEnable(GL11.GL_CULL_FACE);
	
	// Setup blending
	GL11.glEnable(GL11.GL_BLEND);
	GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
	
	// Enable MSAA
	GL11.glEnable(GL13.GL_MULTISAMPLE);
    }
    
    /**
     * Main application loop
     */
    @Override
    public void run() {
	init();
	
	// One second in nanoseconds
	final long ONE_SECOND_IN_NS = 1000000000;
	// The amount of time between frames in nanoseconds when running at 60 fps
	final long DELTA_PER_FRAME_60_FPS_NS = ONE_SECOND_IN_NS / 60;
	
	// The time of this update
	long nowTime = System.nanoTime();
	// The time of the last update
	long lastTime = nowTime;
	// The last time that the fps counter was updated
	long timeOfLastFPS = nowTime;
	// The current fps
	int fps = 0;
	
	// Whether or not the application should continue running;
	boolean shouldRun = true;
	
	while(shouldRun) {
	    
	    // If the user terminates the window, then close the application
	    if(WINDOW.isClosing())
		shouldRun = false;
	    
	    // Set the current time of this update
	    nowTime = System.nanoTime();
	    
	    // If it is not time for the next frame update, then wait
	    if(lastTime + DELTA_PER_FRAME_60_FPS_NS > nowTime)
		continue;
	    
	    // Update the fps counter every second
	    if(nowTime - timeOfLastFPS > ONE_SECOND_IN_NS) {
		// Update the window title
		WINDOW.setTitle(config.title + " --- FPS: " + fps);
		fps = 0;
		timeOfLastFPS = nowTime;
	    }
	    fps++;
	    
	    // Make sure that this thread is the current OpenGL context
	    GLContext.createFromCurrent();
	    
	    // Update input
	    Input.update();
	    
	    // TODO: maybe migrate with ~MouseClickCallback
	    // If the left mouse button is pressed, captures cursor
	    if(WINDOW.getMouseButton(0) == GLFW.GLFW_PRESS)
		WINDOW.setInputMode(GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
	    // If the right mouse button is pressed, releases cursor
	    else if(WINDOW.getMouseButton(1) == GLFW.GLFW_PRESS)
		WINDOW.setInputMode(GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
	    
	    // Updates the user application with delta time
	    applicationListener.update((float)(nowTime - lastTime) / ONE_SECOND_IN_NS);
	    
	    // Clear the screen
	    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	    
	    // Render the user application
	    applicationListener.render();
	    
	    // Update the window and input
	    WINDOW.swapBuffers();
	    GLFW.glfwPollEvents();
	    lastTime = nowTime;
	    
	    final int error = GL11.glGetError();
	    if(error != 0) {
		System.err.println("GL Error: " + error);
	    }
	}
	
	// Dispose the user application
	applicationListener.dispose();
	
	// Cleanup GLFW
	Input.dispose();
	WINDOW.destroy();
	GLFW.glfwTerminate();
    }
}
