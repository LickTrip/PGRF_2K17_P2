#version 400
varying vec3 vertColor;
varying vec3 normal;
varying vec3 lightDirection;
varying vec3 viewDirection;
varying float dist;

varying vec2 texCoord;

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

uniform sampler2D colorKukriText;
uniform sampler2D normalKukriText;
uniform sampler2D heightKukriText;

uniform int showTexture;

out vec4 outColor; // output from the fragment shader

vec4 baseColor;
vec4 normalColor;
vec4 phongColor;
vec4 nDotLColor;
vec4 phongSpotColor;

const float spotCutOff = 0.99;

subroutine vec4 dispMode();
subroutine (dispMode ) vec4 modeBase() {
        return baseColor;
    }

subroutine (dispMode ) vec4 modeNormal() {
        return normalColor;
    }
subroutine (dispMode ) vec4 modePhong() {
        return phongColor;
    }
subroutine (dispMode ) vec4 modeNdotL() {
        return nDotLColor;
    }
subroutine (dispMode ) vec4 modeSpotPhong() {
        return phongSpotColor;
    }

subroutine uniform dispMode MyDisplayMode;

void main() {

    //normalizace
    vec3 lghtDrct = normalize(lightDirection);
    vec3 nrml = normalize(normal);
    //nrml = nrml * transponse();
    vec3 viewDrct = normalize(viewDirection);

    //nastaveni slozek
        vec3 bump;
        if(showTexture == 1){
             bump = texture(normalKukriText, texCoord).rgb  * 2.0 - 1.0;
             baseColor = texture(colorKukriText, texCoord);
        }else{
             baseColor = myColor;
        }

        vec4 totalAmbient = ambient * baseColor;
                vec4 totalDifuse = vec4(0.0);
                vec4 totalSpecular = vec4(0.0);

        float NDotL;
        if(showTexture == 0){
            NDotL = dot(lghtDrct, nrml);
        }
        else{
            NDotL = dot(lghtDrct, bump);
        }

        if(NDotL > 0.0){
            //skalar soucty
            vec3 reflection = normalize(((2.0 * nrml) * NDotL) - lghtDrct);
            float RDotV = max(0.0, dot(reflection, viewDrct));

            vec3 halfVector = normalize(lghtDrct + viewDrct);

            float NDotH;
            if(showTexture == 0){
                NDotH = max(0.0, dot(nrml, halfVector));
            }
            else{
                NDotH = max(0.0, dot(bump, halfVector));
            }

            //vypocet difuzni slozky
            totalDifuse = diffuse * NDotL * baseColor;
            //vypocet total difuzni slozky
            totalSpecular = specular * (pow(NDotH, specularPower*4.0));
        }
        //vypocet utlumu
        float att = 1.0/(constantAttenuation + linearAttenuation * dist + quadraticAttenuation * dist * dist);
        float spotEffect = dot(normalize(spotDirection), normalize(lghtDrct));


    normalColor = vec4(vec3((nrml +1.0) / 2.0), 1.0);
    phongColor = totalAmbient + att*(totalDifuse + totalSpecular)*5;
    nDotLColor = vec4(vec3(NDotL), 1.0);

    if(spotEffect > spotCutOff){
       phongSpotColor = totalAmbient + att*(totalDifuse + totalSpecular);
    }
    else
       phongSpotColor = vec4(vec3(totalAmbient),1);

	outColor = MyDisplayMode();
}

