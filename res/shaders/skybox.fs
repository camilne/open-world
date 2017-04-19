#version 330

in vec2 f_texcoords;

out vec4 out_color;

uniform sampler2D sampler;

void main()
{
	vec4 texture_color = texture(sampler, f_texcoords);
	
	out_color = texture_color;
}