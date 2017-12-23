#version 400
in vec3 inPosition;
in vec2 inTexCoord;
in vec3 inNormal;

varying vec3 vertColor;
varying vec3 normal;
varying vec2 texCoord;
varying vec3 lightDirection;
varying vec3 viewDirection;
varying float dist;

//region setConstants
varying vec4 myColor;
varying vec4 ambient;
varying vec4 diffuse;
varying vec4 specular;
varying float constantAttenuation;
varying float linearAttenuation;
varying float quadraticAttenuation;
varying float specularPower;
varying vec3 spotDirection;
//endregion

//uniforms
uniform mat4 mProj;
uniform mat4 mMV;
uniform mat4 mSwapXY;
uniform vec3 camera;
uniform mat4 mRotate;

const float PI = 3.1415926;

vec3 toSpotDirection;


//methods
void setConstants();

void main() {
    setConstants();
    normal = inverse(transpose(mat3(mMV* mRotate * mSwapXY))) * inNormal;
    vec4 position = mMV * mRotate * mSwapXY * vec4(inPosition*0.01, 1.0);
    viewDirection = - normalize(position.xyz);
    dist = length(inPosition - camera);

	gl_Position = mProj * position;
	vertColor = inNormal * 0.5 + 0.5;
	spotDirection = toSpotDirection;

}

void setConstants(){
    //barva
     myColor = vec4(0.0, 0.9, 0.25, 1.0);
     ambient = vec4(0.70);
     diffuse = vec4(0.30);
     specular = vec4(0.10);

     //lightDirection = vec3(-3.5, 3.0, 3.0);
     //lightDirection = vec3(-0.60, 0.30, 0.15);
     lightDirection = vec3(-camera.x, camera.y, camera.z);


     //utlum
     //konstatni osvetleni cim mensi tim vetsi osvetleni
     constantAttenuation = 0.05;
     linearAttenuation = 0.10; //0.05
     quadraticAttenuation = 0.01;

     specularPower = 6.0;

     texCoord = inTexCoord;

     toSpotDirection = vec3(-3.5, 3.0, 2.0);
}