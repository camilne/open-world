#version 330

in vec4 clip_space;
in vec2 f_texcoords;

out vec4 final_color;

uniform sampler2D reflection_texture;

void main()
{
	vec2 ndc = (clip_space.xy/clip_space.w) / 2.0 + 0.5;

	vec4 reflect_color = texture(reflection_texture, vec2(ndc.x, -ndc.y));

	final_color = reflect_color * vec4(0.8, 0.8, 0.8, 0.5);
}