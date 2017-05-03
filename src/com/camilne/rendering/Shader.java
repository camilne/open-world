package com.camilne.rendering;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.camilne.util.Util;


public class Shader {
    
    public static String path = "res/shaders/";
    public static String vertexExtension = "vs";
    public static String fragmentExtension = "fs";
    
    private int pid;
    private HashMap<String, Integer> uniforms;
    
    /**
     * Creates a shader with the specified file name.
     * @param name The filename of the shader as found in the specified PATH with the specified extensions
     */
    public Shader(String name) throws IOException {
	// Create the program id
	pid = GL20.glCreateProgram();
	
	// Instantiate the uniform lookup
	uniforms = new HashMap<String, Integer>();
	
	// Add the vertex shader to the program
	String vertSource = addShader(GL20.GL_VERTEX_SHADER, path + name + "." + vertexExtension);
	// Add the fragment shader to the program
	String fragSource = addShader(GL20.GL_FRAGMENT_SHADER, path + name + "." + fragmentExtension);
	
	// Link the program
	GL20.glLinkProgram(pid);
	// Check if linking was successful
	closeIfProgramError(GL20.glGetProgrami(pid, GL20.GL_LINK_STATUS) == 0, pid);
	
	// Validate the program
	GL20.glValidateProgram(pid);
	// Check if validation was successful
	closeIfProgramError(GL20.glGetProgrami(pid, GL20.GL_VALIDATE_STATUS) == 0, pid);
	
	// Bind the shader
	bind();
	
	// Automatically add uniforms
	autoAddUniforms(vertSource);
	autoAddUniforms(fragSource);
    }
    
    /**
     * Adds the specified shader part to the shader program
     * @param type The type of shader to add
     * @param fileName The full path of the shader
     * @return The shader source
     */
    private String addShader(int type, String fileName) throws IOException {
	// The loaded shader source
	String shaderSource = null;
	
	// Load the file
	StringBuilder builder = new StringBuilder();
	BufferedReader reader = new BufferedReader(new FileReader(fileName));

	// Read the file one line at a time
	String line;
	while((line = reader.readLine()) != null) {
	    // Add the line to the source
	    builder.append(line).append("\n");
	}

	// Release memory resources
	reader.close();

	// Set the shader source to the loaded file
	shaderSource = builder.toString();
	
	// The id of the shader part
	int id = GL20.glCreateShader(type);
	
	// Set the source of the shader
	GL20.glShaderSource(id, shaderSource);
	// Compile the shader source
	GL20.glCompileShader(id);
	
	// Check to make sure compliation was successful
	closeIfShaderError(GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS) == 0, id);
	
	// Attach the shader to the program
	GL20.glAttachShader(pid, id);
	
	return shaderSource;
    }
    
    // Contains all the standard GLSL variable types to compare to custom types
    private static final Set<String> TYPE_KEYWORDS = new HashSet<String>();
    private static final String[] KEYWORDS_ARRAY = new String[] {
	"bool", "int", "uint", "float", "double", "bvec2", "bvec3", "bvec4",
	"ivec2", "ivec3", "ivec4", "uvec2", "uvec3", "uvec4", "vec2", "vec3",
	"vec4", "dvec2", "dvec3", "dvec4", "mat2", "mat3", "mat4", "dmat2",
	"dmat3", "dmat4", "mat2x2", "mat2x3", "mat2x4", "dmat2x2", "dmat2x3",
	"dmat2x4", "mat3x2", "mat3x3", "mat3x4", "dmat3x2", "dmat3x3", "dmat3x4",
	"mat4x2", "mat4x3", "mat4x4", "dmat4x2", "dmat4x3", "dmat4x4",
	"sampler1D", "sampler1D", "sampler2D", "sampler3D", "samplerCube",
	"sampler1DShadow", "sampler2DShadow", "samplerCubeShadow",
	"sampler1DArray", "sampler2DArray",
	"sampler1DArrayShadow", "sampler2DArrayShadow",
	"isampler1D", "isampler2D", "isampler3D", "isamplerCube",
	"isampler1DArray", "isampler2DArray",
	"usampler1D", "usampler2D", "usampler3D", "usamplerCube",
	"usampler1DArray", "usampler2DArray",
	"sampler2DRect", "sampler2DRectShadow", "isampler2DRect", "usampler2DRect",
	"samplerBuffer", "isamplerBuffer", "usamplerBuffer",
	"sampler2DMS", "isampler2DMS", "usampler2DMS",
	"sampler2DMSArray", "isampler2DMSArray", "usampler2DMSArray",
	"samplerCubeArray", "samplerCubeArrayShadow", "isamplerCubeArray", "usamplerCubeArray"
    };
    
    /**
     * Fills the TYPE_KEYWORDS Set with all the GLSL type keywords for easy lookup
     */
    static {
	for(String keyword : KEYWORDS_ARRAY)
	    TYPE_KEYWORDS.add(keyword);
    }
    
    /**
     * Automatically detects and adds uniforms from the shader source
     * @param shaderSource
     */
    private void autoAddUniforms(String shaderSource) {
	// The uniform keyword to detect uniforms
	final String UNIFORM_KEYWORD = "uniform";
	
	// Holds the start location of the current uniform
	int uniformLocation = -1;
	
	// Iterates through the shader source looking for the uniform keyword
	while((uniformLocation = shaderSource.indexOf(UNIFORM_KEYWORD, ++uniformLocation)) != -1) {
	    // Holds the location of the end of the line
	    int newLineLocation = shaderSource.indexOf("\n", uniformLocation);
	    // The line that the uniform is declared on
	    String line = shaderSource.substring(uniformLocation, newLineLocation).trim();
	    // The different tokens (words) of the line
	    String[] tokens = line.split(" ");
	    
	    // Holds the name of the uniform
	    String name = tokens[2].substring(0, tokens[2].length() - 1).trim();
	    // Holds the type of the uniform
	    String type = tokens[1].trim();
	    
	    // Adds the uniform to the shader program and HashMap lookup
	    if(TYPE_KEYWORDS.contains(type)) {
		addUniform(name);
	    } else {
		addCustomUniform(type, name);
	    }
	}
    }
    
    /**
     * Method to be overridden by child class to add custom uniforms like structs
     * @param type The type of the uniform
     * @param name The name of the uniform
     */
    protected void addCustomUniform(String type, String name) {}
    
    /**
     * Closes the application and prints an info log if an error occurs
     * @param error Whether or not there is an error
     * @param id The id of the program
     */
    private void closeIfProgramError(boolean error, int id) {
	if(error) {
	    // Print the error log of the program with a max length of 1024 bytes
	    System.err.println(GL20.glGetProgramInfoLog(id, 1024));
	    System.exit(1);
	}
    }
    
    /**
     * Closes the application and prints an info log if an error occurs
     * @param error Whether or not there is an error
     * @param id The id of the shader
     */
    private void closeIfShaderError(boolean error, int id) {
	if(error) {
	    // Print the error log of the shader with a max length of 1024 bytes
	    System.err.println(GL20.glGetShaderInfoLog(id, 1024));
	    System.exit(1);
	}
    }
    
    /**
     * Binds this shader
     */
    public void bind() {
	GL20.glUseProgram(pid);
    }
    
    /**
     * Unbinds this shader
     */
    public void unbind() {
	GL20.glUseProgram(0);
    }
    
    /**
     * Binds an attribute to this shader
     * @param index The index of the attribute
     * @param name The name of the attribute
     */
    public void bindAttribute(int index, String name) {
	GL20.glBindAttribLocation(pid, index, name);
    }
    
    /**
     * Adds the specified uniform to the lookup table and shader
     * @param uniform
     */
    public void addUniform(String uniform) {
	// The location in memory of the uniform
	int location = GL20.glGetUniformLocation(pid, uniform);
	
	// If the uniform does not exist
	if(location == -1) {
	    System.err.println("Error in Shader.addUniform(): uniform [" + uniform + "] does not exist");
	    System.exit(1);
	}
	
	// Add the uniform to the lookup table
	uniforms.put(uniform, location);
    }
    
    /**
     * Sets the specified uniform with the specified value
     * @param uniform
     * @param value The integer value
     */
    public void setUniform(String uniform, int value) {
	GL20.glUniform1i(uniforms.get(uniform), value);
    }
    
    /**
     * Sets the specified uniform with the specified value
     * @param uniform
     * @param value The float value
     */
    public void setUniform(String uniform, float value) {
	GL20.glUniform1f(uniforms.get(uniform), value);
    }
    
    /**
     * Sets the specified uniform with the specified value
     * @param uniform
     * @param value The Vector3f value
     */
    public void setUniform(String uniform, Vector3f value) {
	GL20.glUniform3f(uniforms.get(uniform), value.x, value.y, value.z);
    }
    
    /**
     * Sets the specified uniform with the specified value
     * @param uniform
     * @param value The Vector4f value
     */
    public void setUniform(String uniform, Vector4f value) {
	GL20.glUniform4f(uniforms.get(uniform), value.x, value.y, value.z, value.w);
    }

    /**
     * Sets the specified uniform with the specified value
     * @param uniform
     * @param value The Matrix4f value
     */
    public void setUniform(String uniform, Matrix4f value) {
	GL20.glUniformMatrix4fv(uniforms.get(uniform), false, Util.toBuffer(value));
    }
    
    /**
     * Updates the path for all shaders
     * @param path The relative path
     */
    public static void setPath(String path) {
	if(path == null) {
	    System.err.println("New shader path is null");
	    return;
	}
	
	Shader.path = path;
    }
    
    /**
     * Updates the vertex shader file extension for all shaders
     * @param ext
     */
    public static void setVertexExtension(String ext) {
	if(ext == null) {
	    System.err.println("New vertex extension is null");
	    return;
	}
	
	Shader.vertexExtension = ext;
    }
    
    /**
     * Updates the fragment shader file extension for all shaders
     * @param ext
     */
    public static void setFragmentExtension(String ext) {
	if(ext == null) {
	    System.err.println("New fragment extension is null");
	    return;
	}
	
	Shader.fragmentExtension = ext;
    }
    
}
