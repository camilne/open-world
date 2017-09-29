#version 330

layout(location = 0) in vec3 v_position;
layout(location = 1) in vec2 v_texcoords;
layout(location = 2) in vec3 v_normal;

out vec4 clip_space;
out vec2 f_texcoords;
out vec3 to_camera;
out vec3 from_light;

uniform mat4 m_proj;
uniform mat4 m_view;
uniform mat4 m_model;

uniform vec3 camera_pos;
uniform vec3 light_pos;

void main()
{
	vec4 world_pos = m_model * vec4(v_position, 1.0);
	clip_space = m_proj * m_view * world_pos;
	gl_Position = clip_space;
	
	to_camera = camera_pos - world_pos.xyz;
	from_light = world_pos.xyz - light_pos;
	
	f_texcoords = v_texcoords;
}