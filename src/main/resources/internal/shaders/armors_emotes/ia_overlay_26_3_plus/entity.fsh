#version 330
#extension GL_ARB_separate_shader_objects : require

#ifdef GLINT
#include <minecraft:globals.glsl>
#endif
#include <minecraft:fog.glsl>
#if defined(NO_OVERLAY) && defined(ALPHA_CUTOUT) && !defined(EMISSIVE) && !defined(OIT_ALPHA_ONLY)
#include <minecraft:light.glsl>
#endif
#include <minecraft:dynamictransforms.glsl>
#include <minecraft:oit.glsl>

//%START_CUSTOM_ARMORS_VARS%
#define V1 %TEX_RES%
#define V2 50
#define TEX_WIDTH %TEX_WIDTH%
#define TEX_HEIGHT %TEX_HEIGHT%
//%END_CUSTOM_ARMORS_VARS%

uniform sampler2D Sampler0;

#ifdef DISSOLVE
uniform sampler2D DissolveMaskSampler;
#endif

#ifdef GLINT
uniform sampler2D GlintSampler;
#endif

layout(location = 0) in float sphericalVertexDistance;
layout(location = 1) in float cylindricalVertexDistance;
#ifdef PER_FACE_LIGHTING
layout(location = 2) in vec4 vertexPerFaceColorBack;
layout(location = 3) in vec4 vertexPerFaceColorFront;
#else
layout(location = 2) in vec4 vertexColor;
#endif

#ifndef EMISSIVE
layout(location = 4) in vec4 lightMapColor;
#endif

#ifndef NO_OVERLAY
layout(location = 5) in vec4 overlayColor;
#endif

layout(location = 6) in vec2 texCoord0;
#ifdef GLINT
layout(location = 7) in vec2 texCoordGlint;
#endif

layout(location = 8) flat in vec4 tint;
layout(location = 9) flat in vec3 vNormal;
layout(location = 10) in vec2 origTexCoord0;
layout(location = 11) flat in int isTopLayer;
layout(location = 12) flat in int isHead;
layout(location = 13) in float vGameTime;

#ifndef OIT_ALPHA_ONLY
layout(location = 0) out vec4 fragColor;
#endif

vec4 calculateFinalColor(vec4 color, bool applyVanillaLighting) {
#ifndef NO_OVERLAY
    if (applyVanillaLighting)
        color.rgb = mix(overlayColor.rgb, color.rgb, overlayColor.a);
#endif
#ifndef EMISSIVE
    if (applyVanillaLighting)
        color *= lightMapColor;
#endif
#ifdef GLINT
    vec4 glintColor = GlintAlpha * texture(GlintSampler, texCoordGlint);
    color.rgb += glintColor.rgb * glintColor.rgb;
#endif
#ifdef OIT_ACCUMULATE
    color = sampleColorForAccumulation(color);
    vec4 fogColor = vec4(FogColor.rgb * color.a, FogColor.a);
#else
    vec4 fogColor = FogColor;
#endif
    return apply_fog(color, sphericalVertexDistance, cylindricalVertexDistance,
            FogEnvironmentalStart, FogEnvironmentalEnd,
            FogRenderDistanceStart, FogRenderDistanceEnd, fogColor);
}

void writeFinalColor(vec4 color, bool applyVanillaLighting) {
#ifdef GLINT
    color.a = max(color.a, GlintAlpha);
#endif
#ifdef OIT_ALPHA_ONLY
    executeAlphaOnlyPhase(gl_FragCoord.z, color.a);
#else
    fragColor = calculateFinalColor(color, applyVanillaLighting);
#endif
}

#if defined(NO_OVERLAY) && defined(ALPHA_CUTOUT) && !defined(EMISSIVE)
vec4 customArmorLighting() {
#ifdef OIT_ALPHA_ONLY
    return vec4(1.0);
#else
    return minecraft_mix_light(Light0_Direction, Light1_Direction, vNormal, vec4(1.0)) * lightMapColor;
#endif
}
#endif

void main() {
#ifdef PER_FACE_LIGHTING
    vec4 faceVertexColor = gl_FrontFacing ? vertexPerFaceColorFront : vertexPerFaceColorBack;
#else
    vec4 faceVertexColor = vertexColor;
#endif

    //%START_CUSTOM_ARMORS%
#if defined(NO_OVERLAY) && defined(ALPHA_CUTOUT) && !defined(EMISSIVE)
    if(tint != vec4(0)) {
        ivec2 atlasSize = textureSize(Sampler0, 0);
        if(atlasSize == ivec2(TEX_WIDTH, TEX_HEIGHT) && atlasSize.x > 7 && atlasSize.y > 7) {
            int armorAmount = atlasSize.x / (V1 * 4);
            float armorColumns = float(armorAmount);
            float maxFrames = atlasSize.y / (V1 * 2.0);
            vec2 coords = texCoord0;
            coords.x /= armorColumns;
            coords.y /= maxFrames;
            if (texelFetch(Sampler0, ivec2(0, 1), 0) == vec4(1)
                    && texelFetch(Sampler0, ivec2(7, 7), 0) == vec4(0)) {
                vec4 textureProperties = vec4(0);
                vec4 customColor = vec4(0);
                float h_offset = 1.0 / armorColumns;
                vec2 nextFrame = coords;
                float interpolClock = 0;
                vec4 vtc = faceVertexColor;
                for (int i = 1; i < armorAmount; i++) {
                    customColor = texelFetch(Sampler0, ivec2(V1 * 4 * i + 0.5, 0), 0);
                    if (tint == customColor) {
                        coords.x += (h_offset * i);
                        vec4 animInfo = texelFetch(Sampler0, ivec2(V1 * 4 * i + 1.5, 0), 0);
                        animInfo.rgb *= animInfo.a * 255;
                        textureProperties = texelFetch(Sampler0, ivec2(V1 * 4 * i + 2.5, 0), 0);
                        textureProperties.rgb *= textureProperties.a * 255;
                        if (animInfo.r > 0.0) {
                            float timer = floor(mod(vGameTime * V2 * animInfo.g, animInfo.r));
                            if (animInfo.b > 0) interpolClock = fract(vGameTime * V2 * animInfo.g);
                            float v_offset = (V1 * 2.0) / atlasSize.y * timer;
                            nextFrame = coords;
                            coords.y += v_offset;
                            nextFrame.y += (V1 * 2.0) / atlasSize.y * mod(timer + 1, animInfo.r);
                        }
                        break;
                    }
                }
                if (textureProperties.g == 1) {
                    if (textureProperties.r > 1) {
                        vtc = tint;
                    } else if (textureProperties.r == 1) {
                        if (texture(Sampler0, vec2(coords.x + h_offset, coords.y)).a != 0) {
                            vtc = tint * texture(Sampler0, vec2(coords.x + h_offset, coords.y)).a;
                        }
                    }
                } else if (textureProperties.g == 0) {
                    if (textureProperties.r > 1) {
                        vtc = vec4(1);
                    } else if (textureProperties.r == 1) {
                        if (texture(Sampler0, vec2(coords.x + h_offset, coords.y)).a != 0) {
                            vtc = vec4(1) * texture(Sampler0, vec2(coords.x + h_offset, coords.y)).a;
                        } else {
                            vtc = customArmorLighting();
                        }
                    } else {
                        vtc = customArmorLighting();
                    }
                } else {
                    vtc = customArmorLighting();
                }
                vec4 armor = mix(texture(Sampler0, coords), texture(Sampler0, nextFrame), interpolClock);
                vec4 color;
                if (coords.x < (1.0 / armorColumns))
                    color = armor * faceVertexColor * ColorModulator;
                else
                    color = armor * vtc * ColorModulator;

#ifdef OIT_ADDITIVE
                color.a = min(0.99, color.a);
#endif
                if (color.a < 0.1) discard;
                writeFinalColor(color, false);
                return;
            }
        }
    }
#endif
    //%END_CUSTOM_ARMORS%

    if (isHead == 1 && gl_FrontFacing == false) discard;

    vec2 UV = texCoord0;
    if (isTopLayer == 1 || origTexCoord0.y >= 0.25)
        UV = origTexCoord0;

    vec4 color = texture(Sampler0, UV);
#ifdef OIT_ADDITIVE
    color.a = min(0.99, color.a);
#endif
#ifdef ALPHA_CUTOUT
    if (color.a < ALPHA_CUTOUT) {
        discard;
    }
#endif

#ifdef DISSOLVE
    if (faceVertexColor.a < texture(DissolveMaskSampler, UV).a) {
        discard;
    }
    // The dissolve effect entirely replaces translucency
    faceVertexColor.a = 1.0;
#endif

    color *= faceVertexColor * ColorModulator;
    writeFinalColor(color, true);
}
