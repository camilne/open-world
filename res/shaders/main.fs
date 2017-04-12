#version 330

struct DirectionalLight {
	vec3 direction;
	
	vec3 ambient;
	vec3 diffuse;
};

in vec3 f_position;
in vec2 f_texcoords;
in vec3 f_normal;

out vec4 out_color;

uniform sampler2D s_texture;
uniform DirectionalLight dir_light;

vec3 calcDirLight(DirectionalLight light, vec3 normal, vec4 color) {
	// Get light direction pointing toward light source
	vec3 light_dir = normalize(-light.direction);
	
	// Diffuse shading
	float diff = max(dot(normal, light_dir), 0.0);
	
	// Combine the parts
	vec3 ambient = light.ambient * vec3(color);
	vec3 diffuse = light.diffuse * diff * vec3(color);
	
	return (ambient + diffuse);
}

void main()
{
	vec4 textureColor = texture2D(s_texture, f_texcoords);
	vec3 normal = normalize(f_normal);
	
	out_color = vec4(calcDirLight(dir_light, normal, textureColor), 1.0);
}