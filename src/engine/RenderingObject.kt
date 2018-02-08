package engine

import math.Mat4
import math.Vec3
import org.khronos.webgl.Float32Array
import org.khronos.webgl.WebGLRenderingContext
import org.khronos.webgl.get

abstract class RenderingObject(val vertices: Array<Float>, val indices: List<Int>, val tint: Vec3 = Vec3()): Entity() {
    val vert: Float32Array = Float32Array(vertices.size)

    val attribBuffer = Engine.gl.createBuffer() ?: throw IllegalStateException("Unable to create webgl buffer!")

    init {
        vert.set(vertices, 0)
    }

    fun render(gl: WebGLRenderingContext, shaderProgram: ShaderProgram<Engine.ShaderData>) {
        val vMat = Mat4()

        vMat.translate(position)
        vMat.rotateY(rotation.array[0])
        vMat.rotateX(rotation.array[1])
        vMat.rotateZ(rotation.array[2])
        vMat.scale(scale)

        shaderProgram.setUniformMatrix4fv("vMat", vMat.array)

        gl.uniform3f(shaderProgram.getUniformLocation("tint"), tint.array[0], tint.array[1], tint.array[2])

        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, attribBuffer)
        gl.bufferData(WebGLRenderingContext.ARRAY_BUFFER, vert, WebGLRenderingContext.STATIC_DRAW)
        gl.drawArrays(shaderProgram.drawType, 0, vertices.size / 6)
    }
}