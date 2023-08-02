#ifdef GL_ES
    #define PRECISION mediump
    precision PRECISION float;
    precision PRECISION int;
#else
    #define PRECISION
#endif


varying vec4 v_color;
varying vec2 v_texCoord0;

uniform sampler2D u_texture;

void main() {
    vec2 uv = v_texCoord0;
    // draw color based on uvs
    gl_FragColor = vec4(uv.x, uv.y, 0.0, 1.0);
//    gl_FragColor.a = texture2D(u_texture, v_texCoord0).a;
}