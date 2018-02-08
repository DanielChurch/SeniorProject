package engine

import jquery.jq
import math.Mat4
import math.Vec3
import org.khronos.webgl.Float32Array
import org.khronos.webgl.WebGLRenderingContext
import org.w3c.dom.*
import org.w3c.dom.events.Event
import web.Dom
import kotlin.browser.document
import kotlin.browser.window
import kotlin.js.Date
import kotlin.math.*

class Engine {
    companion object {
        lateinit var gl: WebGLRenderingContext
    }
    var scene: Scene = Scene()

    var onKeyPressed: (String) -> Unit = {}
    var onKeyReleased: (String) -> Unit = {}
    var onKeyDown: (Event) -> Unit = {}
    var onMouseMove: (Unit) -> Unit = {}
    var onMousePress: (Int) -> Unit = {}
    var onMouseRelease: (Int) -> Unit = {}
    var onMouseDown: (Int) -> Unit = {}


    class ShaderData {
        var time: Float = 0f
    }

    val shaderProgram: ShaderProgram<ShaderData>
    val data = ShaderData()
    val start = Date().getTime()

    var canvas: HTMLCanvasElement

    val objects = mutableListOf<RenderingObject>()

    val vertexShader = """
        precision mediump float;

        attribute vec3 a_position;
        attribute vec3 a_normal;

        uniform mat4 projectionMatrix;
        uniform mat4 vMat;
        uniform float time;

        varying vec3 color;
        varying vec4 pos;
        varying vec4 normal;

        void main(void) {
            normal = vMat * vec4(a_normal, 1.0);

            color = a_position;
            pos = vMat * vec4(a_position.xyz, 1.0);
            gl_Position = projectionMatrix * pos;
        }
    """

    val fragmentShader = """
        precision highp float;

        uniform vec3 lightPos;
        uniform vec3 tint;

        varying vec3 color;

        varying vec4 pos;
        varying vec4 normal;

        void main(void) {
            float lightPower = 1.0;
            float distance = length(pos.xyz - lightPos);
            vec3 ambient = vec3(0.1, 0.1, 0.1);
            gl_FragColor = vec4(tint, 0.0) + vec4(abs(color) * clamp(dot(normalize(normal.xyz), normalize(pos.xyz - lightPos)) * lightPower, 0.1, 1.0), 1.0);
        }
    """

    /*
    if ((gl_FragCoord.x >= 399.0 && gl_FragCoord.x <= 401.0) || (gl_FragCoord.y >= 399.0 && gl_FragCoord.y <= 401.0)) {
                gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);
            } else {
                if (gl_FragCoord.x < 400.0 && gl_FragCoord.y < 400.0) {
                    vec2 val = vec2(200.0, 200.0) - gl_FragCoord.xy;

                    vec2 val1 = val + vec2(cos(time), sin(time)) * 100.0;
                    vec2 val2 = val + vec2(cos(time * 2.0), sin(time * 2.0)) * 100.0;

                    vec2 val3 = val + vec2(cos(time * 3.0), sin(time * 3.0)) * 100.0;

                    vec2 val4 = val3 + vec2(cos(time * 8.0), sin(time * 8.0)) * 40.0;
                    vec2 val5 = val3 + vec2(cos(time * 8.0 + radians(90.0)), sin(time * 8.0 + radians(90.0))) * 40.0;
                    vec2 val6 = val3 + vec2(cos(time * 8.0 + radians(180.0)), sin(time * 8.0 + radians(180.0))) * 40.0;

                    float amt = 40.0 / length(val1);
                    float amt2 = 20.0 / length(val2);
                    float amt3 = 10.0 / length(val3);

                    float amt4 = 5.0 / length(val4) - 5.0 / length(val5) + 5.0 / length(val6);

                    gl_FragColor = vec4(amt + amt4, amt2 + amt4, amt3 + amt4, 1.0);
                } else if (gl_FragCoord.x > 400.0 && gl_FragCoord.y < 400.0) {
                    vec2 val = vec2(600.0, 200.0) - gl_FragCoord.xy;

                    float amt = 40.0 * abs(sin(time)) / length(val);
                    gl_FragColor = vec4(amt, amt, amt, 1.0);
                } else if (gl_FragCoord.x > 400.0 && gl_FragCoord.y > 400.0) {
                    vec2 val = vec2(600.0, 600.0) - gl_FragCoord.xy;

                    float amt = 40.0 * abs(sin(time)) / length(val);
                    gl_FragColor = vec4(amt, 0.0, 0.0, 1.0);
                } else {
                    vec2 val = vec2(200.0, 600.0) - gl_FragCoord.xy;

                    float amt = 40.0 * abs(sin(time)) / length(val);
                    gl_FragColor = vec4(0.0, 0.0, amt, 1.0);
                }
            }
     */

    fun initGL(width: Int, height: Int) : Pair<HTMLCanvasElement, WebGLRenderingContext> {
        val gl_canvas = document.createElement("canvas") as HTMLCanvasElement
        document.body!!.append(gl_canvas)
        val gl_context = gl_canvas.getContext("webgl") as WebGLRenderingContext

        gl_canvas.width = width
        gl_canvas.height = height

        gl_context.viewport(0, 0, gl_canvas.width, gl_canvas.height)

        return Pair(gl_canvas, gl_context)
    }

    var pos = mutableListOf<HTMLInputElement>()

    init {
        fun test(text: String) {
            val vertices = mutableListOf<Float>()
            val normals = mutableListOf<Float>()

            val indices = mutableListOf<Int>()

            text.split("\n").forEach {
                val values = it.split(" ")

                if (it.startsWith("vn")) {
                    normals.addAll(listOf(values[1].toFloat(), values[2].toFloat(), values[3].toFloat()))
                } else if (it.startsWith("v")) {
                    vertices.addAll(listOf(values[1].toFloat(), values[2].toFloat(), values[3].toFloat()))
                } else if (it.startsWith("f")) {
                    fun getIndex(value: String) = value.split("//")[0].toInt()
                    indices.addAll(listOf(getIndex(values[1]), getIndex(values[2]), getIndex(values[3])))
                }
            }



            // ObjObject(, normals.toFloatArray(), indices)
        }

        val x = js("""
                (function() {
                    var x;
                    $.get("out/production/SeniorProjectKotlin/test.txt", function( data ) {
                        x = data; // can be a global variable too...
                        test(x);
                    });
                    return x;
                })();
        """)

        println("test ${x}")

        val (gl_canvas, gl_context) = initGL(1280, 720)
        Engine.gl = gl_context

        this.canvas = gl_canvas
        gl_canvas.onkeydown =  {event -> println(event)}

        gl_context.clearColor(0f, 0f, 0f, 1f)

        (0 until 3).forEach {
            val e = document.createElement("input") as HTMLInputElement
            pos.add(e)
            Dom.body(e)
            e.value = "0"
            e.type = "range"
            e.min = "-1000"
            e.max = "1000"
            e.className = "slider"
            e.id = "slider$it"
        }

        val setter = { program: ShaderProgram<ShaderData>, data: ShaderData ->
            program.setUniform1f("time", (start.toFloat() - Date().getTime().toFloat()) / 1000f)
            program.setUniform2f("resolution", gl_canvas.width.toFloat(), gl_canvas.height.toFloat())
            program.setUniform3f("lightPos", pos[0].value.toFloat() / 100, pos[1].value.toFloat() / 100, pos[2].value.toFloat() / 100)
        }

        val vainfo = arrayOf(
                VertextAttributeInfo("a_position", 3),
                VertextAttributeInfo("a_normal", 3)
        )

        shaderProgram = ShaderProgram(gl, WebGLRenderingContext.TRIANGLES, vertexShader, fragmentShader, vainfo, setter)

        val near = 0.01f
        val far = 45f
        val r = 1f
        val l = -1f
        val t = 1f
        val b = -1f

//        val projectionMatrix = arrayOf(
//                2*near/(r - l), 0f,      (r+l)/(r-l),    0f,
//                0f,      2f*near/(t-b), (t+b)/(t-b),    0f,
//                0f,      0f,      (near+far)/(near-far),    2*near*far/(near-far),
//                0f,      0f,      -1f,            0f
//        )

                val projectionMatrix = arrayOf(
                2*near/(r - l), 0f,      0f,    0f,
                0f,      2f*near/(t-b), 0f,    0f,
                (r+l)/(r-l),      (t+b)/(t-b),      (near+far)/(near-far),    -1f,
                0f,      0f,      2*near*far/(near-far),            0f
        )

        val pMatrix = Mat4()
        pMatrix.perspective(PI / 3, 16f / 9, 0.1, 60f)

//        val aspectRatio = 16f / 9
//        val FOV = PI / 3
//        val y_scale = ((1f / tan((FOV / 2f) * PI / 180))).toFloat()
//        val x_scale = y_scale / aspectRatio
//        val f = 60.0f
//        val n = 0.1f
//        val frustum_length = f - n
//
//        val projectionMatrix = arrayOf(
//            x_scale, 0f, 0f, 0f,
//            0f, y_scale, 0f, 0f,
//            0f, 0f, -((f + n) / frustum_length), -((2 * n * f) / frustum_length),
//            0f, 0f, -1f, 0f
//        )

        shaderProgram.setUniformMatrix4fv("projectionMatrix", pMatrix.array)
        shaderProgram.setUniform3f("lightPos", -5f, 0f, -5f)

        gl.viewport(0, 0, gl_canvas.width, gl_canvas.height)

        objects.add(Cube())
        objects.add(Cube())
        objects.add(Cube(tint = Vec3(1, 1, 1)))

        render(0.0)
    }

    fun run() {
        handleInput()
    }
    fun handleInput() {

    }
    fun update() {}
    fun render(delta: Double) {
        pos[0].value = (3 * sin((start.toFloat() - Date().getTime().toFloat()) / 1000f) * 100).toString()
        pos[2].value = (3 * cos((start.toFloat() - Date().getTime().toFloat()) / 1000f) * 100 - 500).toString()

        gl.clear(WebGLRenderingContext.COLOR_BUFFER_BIT or WebGLRenderingContext.DEPTH_BUFFER_BIT)
        gl.clearDepth(1f)
        gl.enable(WebGLRenderingContext.DEPTH_TEST)

        shaderProgram.begin(objects[0].attribBuffer, data)

        objects[0].position = Vec3(0, 0, -5)
        objects[0].rotation = Vec3(sin((start.toFloat() - Date().getTime().toFloat()) / 1000f), sin((start.toFloat() - Date().getTime().toFloat()) / 100f), 0f)
        val scaleAmt = 2.0 * abs(sin((start.toFloat() - Date().getTime().toFloat()) / 1000f))
        objects[0].scale = Vec3(1, 1, 1)

        // objects[1].position = Vec3(0, 0, -5)
        // objects[1].scale = Vec3(1, 1, 1)
        // objects[1].rotation = Vec3(0, sin((start.toFloat() - Date().getTime().toFloat()) / 1000f), 0)

        objects[2].position = Vec3(pos[0].value.toFloat() / 100, pos[1].value.toFloat() / 100, pos[2].value.toFloat() / 100)
        objects[2].scale = Vec3(0.1, 0.1, 0.1)
//        vMat.transpose()
//        vMat.invert()

//        println(vMat.array)

        objects.forEach { it.render(gl, shaderProgram) }

        shaderProgram.end()
        gl.disable(WebGLRenderingContext.DEPTH_TEST)

        window.requestAnimationFrame {
            render(0.0)
        }
    }
}