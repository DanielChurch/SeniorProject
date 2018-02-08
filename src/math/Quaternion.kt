package math


import kotlin.math.*

open class Quaternion(var w:Float, var x: Float, var y: Float, var z: Float) {
    private val TOLERANCE = 0.00001f
    companion object {
        val zero = Quaternion(0f,0f,0f,0f)
    }
    //Normalizes Quaternion unless it is within set tolerance
    fun normalization(quaternion: Quaternion) {
        var q = Quaternion(0f,0f,0f,0f)
        var mag2 = quaternion.w * quaternion.w + quaternion.x * quaternion.x + quaternion.y * quaternion.y + quaternion.z * quaternion.z
        if (abs(((mag2 - 1.0f).toDouble())) > TOLERANCE) {
            var mag = sqrt(mag2.toDouble()).toFloat()
            w /= mag
            x /= mag
            y /= mag
            z /= mag
        }
    }

    fun getConjugate(quaternion: Quaternion): Quaternion {
        return Quaternion(-x, -y, -z, w)

    }



}