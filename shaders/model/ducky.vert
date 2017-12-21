#version 150
in vec3 inPosition; // input from the vertex buffer
in vec2 inTexCoord; // input from the vertex buffer
in vec3 inNormal; // input from the vertex buffer
out vec3 vertColor; // output from this shader to the next pipleline stage
uniform mat4 mProj;
uniform mat4 mMV;
uniform float time;


void main() {
	gl_Position = mProj * mMV * vec4(inPosition*0.01, 1.0);
	//vertColor = inNormal * 0.5 + 0.5;
	vertColor = vec3(inTexCoord , 0.5);
	//vertColor = inNormal;
}