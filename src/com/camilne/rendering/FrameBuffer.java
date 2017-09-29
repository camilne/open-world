package com.camilne.rendering;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.camilne.app.Application;

public class FrameBuffer {
    
    private int id;
    private ArrayList<Integer> textureids;
    private int dboid;
    private int width, height;
    
    public FrameBuffer(int width, int height) {
	this.width = width;
	this.height = height;
	
	textureids = new ArrayList<Integer>();
	
	id = GL30.glGenFramebuffers();
	bind();
    }
    
    public void bind() {
	GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, id);
	GL11.glViewport(0, 0, width, height);
    }
    
    public static void unbind() {
	GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	GL11.glViewport(0, 0, Application.WINDOW.getWidth(), Application.WINDOW.getHeight());
    }
    
    public void attachTexture() {
	attachTexture(width, height);
    }
    
    public void attachTexture(int width, int height) {
	attachTexture(GL11.GL_RGB, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, GL11.GL_LINEAR, GL30.GL_COLOR_ATTACHMENT0 + textureids.size(), width, height);
    }
    
    public void attachTexture(int internalFormat, int format, int type, int filtering, int attachment) {
	attachTexture(internalFormat, format, type, filtering, attachment, width, height);
    }
    
    public void attachTexture(int internalFormat, int format, int type, int filtering, int attachment, int width, int height) {
	bind();
	
	int tid = GL11.glGenTextures();
	GL11.glBindTexture(GL11.GL_TEXTURE_2D, tid);
	GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, type, (ByteBuffer) null);
	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, filtering);
	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, filtering);
	GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	
	GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, attachment, GL11.GL_TEXTURE_2D, tid, 0);
	textureids.add(tid);
	unbind();
    }
    
    public void drawBuffers() {
	bind();
	
	IntBuffer buffer = BufferUtils.createIntBuffer(textureids.size());
	for(int i : textureids)
	    buffer.put(GL30.GL_COLOR_ATTACHMENT0 + i);
	buffer.flip();
	
	GL20.glDrawBuffers(buffer);
	
	unbind();
    }
    
    public void attachDepthBuffer() {
	bind();
	
	dboid = GL30.glGenRenderbuffers();
	GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, dboid);
	GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width, height);
	
	GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, dboid);
	unbind();
    }
    
    public void attachDepthStencilBuffer() {
	bind();
	
	dboid = GL30.glGenRenderbuffers();
	GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, dboid);
	GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL30.GL_DEPTH24_STENCIL8, width, height);
	GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);
	
	GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_STENCIL_ATTACHMENT, GL30.GL_RENDERBUFFER, dboid);
	unbind();
    }
    
    public void verifyIntegrity() {
	bind();
	if(GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) != GL30.GL_FRAMEBUFFER_COMPLETE) {
	    System.err.println("Error in FrameBuffer.verifyIntegrity(): Framebuffer is not complete");
	    new Exception().printStackTrace();
	}
	unbind();
    }
    
    public void bindTexture() {
	for(int i = 0; i < textureids.size(); i++) {
	    GL13.glActiveTexture(GL13.GL_TEXTURE0 + i);
	    GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureids.get(i));
	}
    }
    
    public void dispose() {
	GL30.glDeleteFramebuffers(id);
	
	for(int i : textureids) {
	    GL11.glDeleteTextures(i);
	}
	    
	if(dboid != 0)
	    GL30.glDeleteRenderbuffers(dboid);
    }

}
