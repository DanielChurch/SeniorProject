package math

import org.khronos.webgl.Float32Array

typealias Vec3JS = Float32Array

class Vec3(val array: Vec3JS) {
    constructor(x: Number, y: Number, z: Number):
            this(vec3.fromValues(x.toFloat(), y.toFloat(), z.toFloat()))
    constructor():
            this(vec3.create())

    fun cross(other: Vec3): Vec3 {
        val ret = Vec3()
        vec3.cross(ret.array, this.array, other.array)
        return ret
    }

    fun normalize() {
        vec3.normalize(array, array)
    }

    operator fun plus(other: Vec3) = add(other)

    fun add(other: Vec3): Vec3 {
        val ret = Vec3()
        vec3.add(ret.array, this.array, other.array)
        return ret
    }
}

external open class vec3 {
    companion object {
        fun create(): Vec3JS
        fun clone(a: Vec3JS): Vec3JS
        fun fromValues(x: Number, y: Number, z: Number): Vec3JS
        fun copy(out: Vec3JS, a: Vec3JS): Vec3JS
        fun set(out: Vec3JS, x: Number, y: Number, z: Number): Vec3JS
        fun add(out: Vec3JS, a: Vec3JS, b: Vec3JS): Vec3JS
        fun subtract(out: Vec3JS, a: Vec3JS, b: Vec3JS): Vec3JS
        fun sub(): Nothing
        fun multiply(out: Vec3JS, a: Vec3JS, b: Vec3JS): Vec3JS
        fun mul(): Nothing
        fun divide(out: Vec3JS, a: Vec3JS, b: Vec3JS): Vec3JS
        fun div(): Nothing
        fun ceil(out: Vec3JS, a: Vec3JS): Vec3JS
        fun floor(out: Vec3JS, a: Vec3JS): Vec3JS
        fun min(out: Vec3JS, a: Vec3JS, b: Vec3JS): Vec3JS
        fun max(out: Vec3JS, a: Vec3JS, b: Vec3JS): Vec3JS
        fun round(out: Vec3JS, a: Vec3JS): Vec3JS
        fun scale(out: Vec3JS, a: Vec3JS, b: Number): Vec3JS
        fun scaleAndAdd(out: Vec3JS, a: Vec3JS, b: Vec3JS, scale: Number): Vec3JS
        fun distance(a: Vec3JS, b: Vec3JS): Number
        fun dist(): Nothing
        fun squaredDistance(a: Vec3JS, b: Vec3JS): Number
        fun sqrDist(): Nothing
        fun length(a: Vec3JS): Number
        fun len(): Nothing
        fun squaredLength(a: Vec3JS): Number
        fun sqrLen(): Nothing
        fun negate(out: Vec3JS, a: Vec3JS): Vec3JS
        fun inverse(out: Vec3JS, a: Vec3JS): Vec3JS
        fun normalize(out: Vec3JS, a: Vec3JS): Vec3JS
        fun dot(a: Vec3JS, b: Vec3JS): Number
        fun cross(out: Vec3JS, a: Vec3JS, b: Vec3JS): Vec3JS
        fun lerp(out: Vec3JS, a: Vec3JS, b: Vec3JS, t: Number): Vec3JS
        fun hermite(out: Vec3JS, a: Vec3JS, b: Vec3JS, c: Vec3JS, d: Vec3JS, t: Number): Vec3JS
        fun bezier(out: Vec3JS, a: Vec3JS, b: Vec3JS, c: Vec3JS, d: Vec3JS, t: Number): Vec3JS
        fun random(out: Vec3JS, scale: Number): Vec3JS
        fun transformMat4(out: Vec3JS, a: Vec3JS, m: Mat4JS): Vec3JS
        fun transformMat3(out: Vec3JS, a: Vec3JS, m: Mat4JS): Vec3JS
//        fun transformQuat(out: Vec3JS, a: Vec3JS, q: QuatJS): Vec3JS
        fun rotateX(out: Vec3JS, a: Vec3JS, b: Vec3JS, c: Number): Vec3JS
        fun rotateY(out: Vec3JS, a: Vec3JS, b: Vec3JS, c: Number): Vec3JS
        fun rotateZ(out: Vec3JS, a: Vec3JS, b: Vec3JS, c: Number): Vec3JS
        fun forEach(a: dynamic, stride: Number, offset: Number, count: Number, fn: dynamic, arg: dynamic): dynamic
        fun angle(a: Vec3JS, b: Vec3JS): Number
        fun str(a: Vec3JS): String
        fun exactEquals(a: Vec3JS, b: Vec3JS): Boolean
        fun equals(a: Vec3JS, b: Vec3JS): Boolean
    }
}