#version 330
#extension GL_ARB_separate_shader_objects : require

#if !defined(IS_GUI) && !defined(IS_SEE_THROUGH)
#include <minecraft:fog.glsl>
#include <minecraft:sample_lightmap.glsl>
#endif

#include <minecraft:dynamictransforms.glsl>
#include <minecraft:projection.glsl>
#include <minecraft:globals.glsl>

#define ANIMATION_SPEED_MULTIPLIER 1000.
#define WAVE_SCALE_FACTOR 150.

// Function to calculate hue based on a value, returns a color vector. This creates a rainbow-like effect.
// Adds a cosine-modulated variation to RGB channels to generate color changes over time.
#define hue(v) ((.6 + .6 * cos(6. * (v) + vec4(0, 23, 21, 1))) + vec4(0., 0., 0., 1.))

#define finalize() { texCoord0 = UV0; }

layout(location = 0) in vec3 Position;
layout(location = 1) in vec4 Color;
layout(location = 2) in vec2 UV0;
#if !defined(IS_GUI) && !defined(IS_SEE_THROUGH)
layout(location = 3) in ivec2 UV2;
#endif

#if !defined(IS_GUI) && !defined(IS_SEE_THROUGH)
uniform sampler2D Sampler2;
layout(location = 0) out float sphericalVertexDistance;
layout(location = 1) out float cylindricalVertexDistance;
#endif

layout(location = 2) out vec4 vertexColor;
layout(location = 3) out vec2 texCoord0;

float safeGameTime() {
    float gameTime = GameTime;
    // Fallback for AMD GPUs bug.
    if (gameTime <= 0.) {
        gameTime = 0.5;
    }
    return gameTime;
}

float scaledTime() {
    return safeGameTime() * 12000.;
}

vec4 lightmapped_color(vec4 color) {
#if !defined(IS_GUI) && !defined(IS_SEE_THROUGH)
    return color * sample_lightmap(Sampler2, UV2);
#else
    return color;
#endif
}

void calculate_gl_pos(inout vec4 vertex) {
    gl_Position = ProjMat * ModelViewMat * vertex;
}

void default_color() {
    vertexColor = lightmapped_color(Color);
}

//<SCOREBOARD_HIDE_RED_NUMBERS>
void apply_scoreboard_hide_red_numbers(inout vec4 vertex) {
    calculate_gl_pos(vertex);
    if (Position.z == 0. && gl_Position.x > 0.95) {
        vertexColor = vec4(0);
    } else {
        default_color();
    }
    finalize();
}
//</SCOREBOARD_HIDE_RED_NUMBERS>

//<TEXT_EFFECTS>
void rainbow_effect() {
    vertexColor = lightmapped_color(hue(gl_Position.x + safeGameTime() * ANIMATION_SPEED_MULTIPLIER));
}

void wavy_effect_1() {
    gl_Position.y += sin(scaledTime() + (gl_Position.x * 6.)) / WAVE_SCALE_FACTOR;
}

void apply_rainbow_effect(inout vec4 vertex) {
    calculate_gl_pos(vertex);
    rainbow_effect();
    finalize();
}

void apply_wavy_effect(inout vec4 vertex) {
    calculate_gl_pos(vertex);
    default_color();
    wavy_effect_1();
    finalize();
}

void apply_rainbow_wavy_effect(inout vec4 vertex) {
    calculate_gl_pos(vertex);
    wavy_effect_1();
    rainbow_effect();
    finalize();
}

void apply_jump_animation(inout vec4 vertex) {
    default_color();
    float vertexId = mod(float(gl_VertexIndex), 4.);
    if (vertex.z <= 0.) {
        if (vertexId == 3. || vertexId == 0.) {
            vertex.y += cos(scaledTime() / 4.) * 0.1;
            vertex.y += max(cos(scaledTime() / 4.) * 0.1, 0.);
        }
    } else {
        if (vertexId == 3. || vertexId == 0.) {
            vertex.y -= cos(scaledTime() / 4.) * 3.;
            vertex.y -= max(cos(scaledTime() / 4.) * 4., 0.);
        }
    }
    calculate_gl_pos(vertex);
    finalize();
}

void apply_rainbow_jump_animation(inout vec4 vertex) {
    float vertexId = mod(float(gl_VertexIndex), 4.);
    if (vertex.z <= 0.) {
        if (vertexId == 3. || vertexId == 0.) {
            vertex.y += cos(scaledTime() / 4.) * 0.1;
            vertex.y += max(cos(scaledTime() / 4.) * 0.1, 0.);
        }
    } else {
        if (vertexId == 3. || vertexId == 0.) {
            vertex.y -= cos(scaledTime() / 4.) * 3.;
            vertex.y -= max(cos(scaledTime() / 4.) * 4., 0.);
        }
    }
    calculate_gl_pos(vertex);
    rainbow_effect();
    finalize();
}

void apply_blinking_effect(inout vec4 vertex, float speed) {
    calculate_gl_pos(vertex);
    float blink = abs(sin(scaledTime() * speed));
    vertexColor = lightmapped_color(Color * blink);
    finalize();
}
//</TEXT_EFFECTS>

//<NOSHADOW>
void apply_remove_shadow(inout vec4 vertex) {
    calculate_gl_pos(vertex);
    default_color();
    vertexColor = vec4(1., 1., 1., vertexColor.a);
    finalize();
}
//</NOSHADOW>

void main() {
#if !defined(IS_GUI) && !defined(IS_SEE_THROUGH)
    sphericalVertexDistance = fog_spherical_distance(Position);
    cylindricalVertexDistance = fog_cylindrical_distance(Position);
#endif
    default_color();

    vec4 vertex = vec4(Position, 1.);
    ivec3 iColor = ivec3(Color.xyz * 255. + vec3(0.5));

    // Hide scoreboard background.
    //<SCOREBOARD_HIDE_RED_NUMBERS>
    if (iColor == ivec3(255, 85, 85)) {
        apply_scoreboard_hide_red_numbers(vertex);
        return;
    }
    //</SCOREBOARD_HIDE_RED_NUMBERS>

    // Apply custom effects also to shadows.
    if (fract(Position.z) < 0.01) {
        //<NOSHADOW>
        // Shadow of #4e5c24
        if (iColor == ivec3(19, 23, 9)) {
            gl_Position = vec4(2., 2., 2., 1.);
            default_color();
            finalize();
            return;
        }
        //</NOSHADOW>

        //<TEXT_EFFECTS>
        // Shadow color: #393f3f, original text color: #e6fffe - rainbow.
        if (iColor == ivec3(57, 63, 63)) {
            calculate_gl_pos(vertex);
            default_color();
            finalize();
            return;
        }

        // Shadow color: #393f3e, original text color: #e6fffa - wave.
        if (iColor == ivec3(57, 63, 62)) {
            apply_wavy_effect(vertex);
            return;
        }

        // Shadow color: #393e3f, original text color: #e6fbfe - rainbow + wave.
        if (iColor == ivec3(57, 62, 63)) {
            apply_wavy_effect(vertex);
            return;
        }

        // Shadow color: #393e3e, original text color: #e6fbfa - jump.
        if (iColor == ivec3(57, 62, 62)) {
            apply_jump_animation(vertex);
            return;
        }

        // Shadow color: #393d3f, original text color: #e6f7fe - rainbow + jump.
        if (iColor == ivec3(57, 61, 63)) {
            apply_jump_animation(vertex);
            return;
        }

        // Shadow color: #393d3e, original text color: #e6f7fa - blink.
        if (iColor == ivec3(57, 61, 62)) {
            apply_blinking_effect(vertex, .5);
            return;
        }

        //%USER_TEXT_EFFECTS_SHADOWS% // TODO
        //</TEXT_EFFECTS>
    }

    //<NOSHADOW>
    // #4e5c24 - Remove shadow from text.
    if (iColor == ivec3(78, 92, 36)) {
        apply_remove_shadow(vertex);
        return;
    }
    //</NOSHADOW>

    //<TEXT_EFFECTS>
    // #e6fffe - Rainbow.
    if (iColor == ivec3(230, 255, 254)) {
        apply_rainbow_effect(vertex);
        return;
    }

    // #e6fffa - Wave.
    if (iColor == ivec3(230, 255, 250)) {
        apply_wavy_effect(vertex);
        return;
    }

    // #e6fbfe - Rainbow + wave.
    if (iColor == ivec3(230, 251, 254)) {
        apply_rainbow_wavy_effect(vertex);
        return;
    }

    // #e6fbfa - Jump.
    if (iColor == ivec3(230, 251, 250)) {
        apply_jump_animation(vertex);
        return;
    }

    // #e6f7fe - Rainbow + jump.
    if (iColor == ivec3(230, 247, 254)) {
        apply_rainbow_jump_animation(vertex);
        return;
    }

    // #e6f7fa - Blink.
    if (iColor == ivec3(230, 247, 250)) {
        apply_blinking_effect(vertex, .5);
        return;
    }

    //%USER_TEXT_EFFECTS% // TODO
    //</TEXT_EFFECTS>

    //<LEGACY_TEXT_EFFECTS>
    // Legacy ItemsAdder text effects colors support.
    if (iColor == ivec3(255, 255, 254)) {
        apply_rainbow_effect(vertex);
        return;
    }

    if (iColor == ivec3(255, 255, 253)) {
        apply_wavy_effect(vertex);
        return;
    }

    if (iColor == ivec3(255, 255, 252)) {
        apply_rainbow_wavy_effect(vertex);
        return;
    }

    if (iColor == ivec3(255, 255, 251)) {
        apply_jump_animation(vertex);
        return;
    }

    if (iColor == ivec3(255, 254, 254)) {
        apply_rainbow_jump_animation(vertex);
        return;
    }
    //</LEGACY_TEXT_EFFECTS>

    calculate_gl_pos(vertex);
    default_color();
    finalize();
}
