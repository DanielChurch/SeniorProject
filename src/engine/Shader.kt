package engine

import math.Vector2
import math.Vector3

abstract class Shader(vertSource: String, fragSource: String) {
    var uniformLocations: Map<String, Int> = mapOf()

    fun load() = false
    fun bind() {}
    fun uniformf(location: String, value: Float) {}
    fun uniform2f(location: String, value: Vector2) {}
    fun uniform3f(location: String, value: Vector3) {}
    fun uniformMat4(location: String, value: Vector3) {}
    fun uniformTex(location: String, value: Vector3) {}
}