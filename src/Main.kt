import engine.ShaderProgram
import engine.VertextAttributeInfo
import org.khronos.webgl.Float32Array
import org.khronos.webgl.WebGLBuffer
import org.khronos.webgl.WebGLRenderingContext
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLDivElement
import web.Dom
import kotlin.browser.document
import kotlin.browser.window
import kotlin.js.Date

class Main {
    class ShaderData {
        var time: Float = 0f
    }

    val shaderProgram: ShaderProgram<ShaderData>
    val data = ShaderData()
    val attribBuffer: WebGLBuffer
    val vertices: Float32Array
    val gl_context: WebGLRenderingContext
    val start = Date().getTime()

    val vertexShader = """
        attribute vec2 a_position;

        void main(void) {
            gl_Position = vec4(a_position, 0.0, 1.0);
        }
    """

    val fragmentShader = """
        precision mediump float;

        uniform float time;
        uniform vec2 resolution;

        void main(void) {
            vec2 val = vec2(0.5, 0.5) - (gl_FragCoord.xy / resolution);

            val.x += sin(time);
            val.y += cos(time) + val.x;

            float amt = 1.0 * abs(sin(time)) / length(val);
            gl_FragColor = vec4(amt, amt, amt, 1.0);
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

    val array: Array<Float> = arrayOf(
            -1f, -1f, -5f,
            1f, -1f, -5f,
            1f, 1f, -5f,
            1f, 1f, -5f,
            -1f, 1f, -5f,
            -1f, -1f, -5f
    )

    fun initGL(width: Int, height: Int) : Pair<HTMLCanvasElement, WebGLRenderingContext> {
        val gl_canvas = document.createElement("canvas") as HTMLCanvasElement
        document.body!!.append(gl_canvas)
        var gl_context = gl_canvas.getContext("webgl") as WebGLRenderingContext

        gl_canvas.width = width
        gl_canvas.height = height

        gl_context.viewport(0, 0, gl_canvas.width, gl_canvas.height)

        return Pair(gl_canvas, gl_context)
    }

    init {
        val e = document.createElement("div") as HTMLDivElement
        e.innerText = "Hello World!"
        Dom.body(e)

        val c = document.createElement("canvas") as HTMLCanvasElement
        val g = c.getContext("2d") as CanvasRenderingContext2D
        Dom.body(c)

        val (gl_canvas, gl_context) = initGL(800, 800)
        this.gl_context = gl_context

        gl_context.clearColor(0f, 0f, 0f, 1f)

        vertices = Float32Array(array.size)
        vertices.set(array, 0)

        val setter = { program: ShaderProgram<ShaderData>, data: ShaderData ->
            program.setUniform1f("time", (start.toFloat() - Date().getTime().toFloat()) / 1000f)
            program.setUniform2f("resolution", gl_canvas.width.toFloat(), gl_canvas.height.toFloat())
        }

        val vainfo = arrayOf(
                VertextAttributeInfo("a_position", 3)
        )

        shaderProgram = ShaderProgram(gl_context, WebGLRenderingContext.TRIANGLES, vertexShader, fragmentShader, vainfo, setter)

        attribBuffer = gl_context.createBuffer() ?: throw IllegalStateException("Unable to create webgl buffer!")
        gl_context.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, attribBuffer)

        c.width = 200
        c.height = 200

        g.rect(50.0, 50.0, 100.0, 100.0)
        g.fill()

        g.rect(0.0, 0.0, 200.0, 200.0)
        g.stroke()

        render()
    }

    fun render() {
        gl_context.clear(WebGLRenderingContext.COLOR_BUFFER_BIT)

        shaderProgram.begin(attribBuffer, data)

        gl_context.bufferData(WebGLRenderingContext.ARRAY_BUFFER, vertices, WebGLRenderingContext.DYNAMIC_DRAW);
        gl_context.drawArrays(shaderProgram.drawType, 0, 6)

        shaderProgram.end()

        window.requestAnimationFrame {
            render()
        }
    }
}

fun main(args: Array<String>) {
    window.onload = {
        Main()
    }
}