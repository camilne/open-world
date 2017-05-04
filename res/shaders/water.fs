#version 330

in vec4 clip_space;
in vec2 f_texcoords;
in vec3 to_camera;
in vec3 from_light;

out vec4 final_color;

uniform float move_factor;

uniform sampler2D reflection_texture;
uniform sampler2D dudv_texture;
uniform sampler2D normal_texture;

const float wave_strength = 0.03;
const float shineDamper = 20.0;
const float reflectivity = 1.8;

vec4 calculate_reflection(vec2 _ndc, vec2 _totalDistortion) {
	vec2 reflectTexCoords = vec2(_ndc.x, -_ndc.y);
	reflectTexCoords += _totalDistortion;
	reflectTexCoords.x = clamp(reflectTexCoords.x, 0.001, 0.999);
	reflectTexCoords.y = clamp(reflectTexCoords.y, -0.999, -0.001);
	
	vec4 reflect_color = texture(reflection_texture, reflectTexCoords);
	
	return reflect_color;
}

vec4 calculate_specular(vec2 _totalDistortion) {
	vec4 normalMapColor = texture(normal_texture, _totalDistortion);
	vec3 normal = vec3(normalMapColor.r * 2.0 - 1.0, normalMapColor.b, normalMapColor.g * 2.0 - 1.0);
	normal = normalize(normal);
	
	vec3 viewVector = normalize(to_camera);
	vec3 reflectedLight = reflect(normalize(from_light), normal);
	float specular = max(dot(reflectedLight, viewVector), 0.0);
	specular = pow(specular, shineDamper);
	vec3 light_color = vec3(1.0);
	vec3 specularHighlights = light_color * specular * reflectivity;	
	
	return vec4(specularHighlights, 0.0);
}

void main()
{
	vec2 ndc = (clip_space.xy/clip_space.w) / 2.0 + 0.5;

	vec2 distortedTexCoords = texture(dudv_texture, vec2(f_texcoords.x + move_factor, f_texcoords.y)).rg * 0.1;
	distortedTexCoords = f_texcoords + vec2(distortedTexCoords.x, distortedTexCoords.y + move_factor);
	vec2 totalDistortion = (texture(dudv_texture, distortedTexCoords).rg * 2.0 - 1.0) * wave_strength;
	
	vec4 reflect_color = calculate_reflection(ndc, totalDistortion);
	vec4 specular_color = calculate_specular(totalDistortion);

	final_color = reflect_color * vec4(0.8, 0.8, 0.8, 0.2);
	final_color = mix(final_color, vec4(0.0, 0.3, 0.5, 1.0), 0.2) + specular_color;
}