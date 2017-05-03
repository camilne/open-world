#version 330

in vec4 clip_space;
in vec2 f_texcoords;

out vec4 final_color;

uniform float move_factor;

uniform sampler2D reflection_texture;
uniform sampler2D dudv_texture;

const float wave_strength = 0.03;

void main()
{
	vec2 ndc = (clip_space.xy/clip_space.w) / 2.0 + 0.5;

	vec2 distortedTexCoords = texture(dudv_texture, vec2(f_texcoords.x + move_factor, f_texcoords.y)).rg * 0.1;
	distortedTexCoords = f_texcoords + vec2(distortedTexCoords.x, distortedTexCoords.y + move_factor);
	vec2 totalDistortion = (texture(dudv_texture, distortedTexCoords).rg * 2.0 - 1.0) * wave_strength;
	
	vec2 reflectTexCoords = vec2(ndc.x, -ndc.y);
	reflectTexCoords += totalDistortion;
	reflectTexCoords.x = clamp(reflectTexCoords.x, 0.001, 0.999);
	reflectTexCoords.y = clamp(reflectTexCoords.y, -0.999, -0.001);

	vec4 reflect_color = texture(reflection_texture, reflectTexCoords);

	final_color = reflect_color * vec4(0.8, 0.8, 0.8, 0.2);
}