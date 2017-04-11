#version 330

layout (location = 0) in vec3 v_position;

out vec3 f_position;

void main(void) {
	gl_Position = vec4(v_position, 1.0);
	
	f_position = v_position;
}