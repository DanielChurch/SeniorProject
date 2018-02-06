package engine

import org.khronos.webgl.Float32Array
import org.khronos.webgl.WebGLRenderingContext

abstract class RenderingObject(val vertices: Array<Float>, val indices: List<Int>): Entity() {
    val vert: Float32Array = Float32Array(vertices.size)

    val attribBuffer = Engine.gl.createBuffer() ?: throw IllegalStateException("Unable to create webgl buffer!")

    init {
        vert.set(vertices, 0)
    }

    fun render(gl: WebGLRenderingContext, drawType: Int) {
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, attribBuffer)
        gl.bufferData(WebGLRenderingContext.ARRAY_BUFFER, vert, WebGLRenderingContext.STATIC_DRAW)
        gl.drawArrays(drawType, 0, vertices.size / 6)
    }
}