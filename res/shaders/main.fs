#version 330

in vec3 f_position;
in vec2 f_texCoords;

out vec4 out_color;

uniform sampler2D s_texture;

void main(void) {
	out_color = texture(s_texture, f_texCoords);
}