package engine

import engine.Entity
import math.Vector3

abstract class RenderingObject: Entity() {
    val vertices: List<Vector3> = listOf()
    val indices: List<Int> = listOf()

    abstract fun render()
}