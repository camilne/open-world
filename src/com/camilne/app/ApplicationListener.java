package com.camilne.app;

public interface ApplicationListener {
    
    public abstract void create();
    public abstract void update(float delta);
    public abstract void render();
    public abstract void dispose();
    
    public abstract void mouseMoved(double xpos, double ypos, double dx, double dy);
    public abstract void keyPressed(int keycode);
    public abstract void keyReleased(int keycode);

}
