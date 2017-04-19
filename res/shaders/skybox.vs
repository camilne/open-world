#version 330

layout(location = 0) in vec3 v_position;
layout(location = 1) in vec2 v_texcoords;
layout(location = 2) in vec3 v_normal;
layout(location = 3) in vec3 v_color;

out vec2 f_texcoords;

uniform mat4 m_proj;
uniform mat4 m_view;

void main()
{
	gl_Position = m_proj * mat4(mat3(m_view)) * vec4(v_position, 1.0);
	f_texcoords = v_texcoords;
}