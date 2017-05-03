#version 330

in vec2 f_texcoords;

out vec4 final_color;

uniform sampler2D reflection_texture;

void main()
{
	vec2 screen_space = vec2(gl_FragCoord.x / 1280.0, 1 - gl_FragCoord.y / 720.0);
	vec4 reflect_color = texture(reflection_texture, screen_space);

	final_color = reflect_color * vec4(0.8, 0.8, 0.8, 0.5);
}