#version 330

layout (location = 0) in vec3 v_position;
layout (location = 1) in vec2 v_texcoords;
layout (location = 2) in vec3 v_normal;

out vec3 f_position;
out vec2 f_texcoords;
out vec3 f_normal;

uniform mat4 m_proj;
uniform mat4 m_view;
uniform mat4 m_model;

void main(void) {
	gl_Position =  m_proj * m_view * m_model * vec4(v_position, 1.0);
	
	f_position = v_position;
	f_texcoords = v_texcoords;
	f_normal = mat3(transpose(inverse(m_model))) * v_normal;
}