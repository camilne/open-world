package com.camilne.rendering;

import org.lwjgl.util.vector.Vector2f;

public class TextureRegion {
    
    private Texture texture;
    
    private Vector2f st, ut, uv, sv;
    
    public TextureRegion(Texture texture, float x, float y, float width, float height) {
	this.texture = texture;
	
	float invTexWidth = 1f / texture.getWidth();
	float invTexHeight = 1f / texture.getHeight();
	
	float s = x * invTexWidth;
	float t = y * invTexHeight;
	float u = (x + width) * invTexWidth;
	float v = (y + height) * invTexHeight;
	
	st = new Vector2f(s, t);
	ut = new Vector2f(u, t);
	uv = new Vector2f(u, v);
	sv = new Vector2f(s, v);
    }
    
    public void bind() {
	texture.bind();
    }
    
    public Vector2f getST() {
	return st;
    }
    
    public Vector2f getUT() {
	return ut;
    }
    
    public Vector2f getUV() {
	return uv;
    }
    
    public Vector2f getSV() {
	return sv;
    }

}
