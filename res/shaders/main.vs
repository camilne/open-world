#version 330

layout (location = 0) in vec3 v_position;
layout (location = 1) in vec2 v_texCoords;

out vec3 f_position;
out vec2 f_texCoords;

uniform mat4 m_proj;
uniform mat4 m_view;

void main(void) {
	gl_Position =  m_proj * m_view * vec4(v_position, 1.0);
	
	f_position = v_position;
	f_texCoords = v_texCoords;
}