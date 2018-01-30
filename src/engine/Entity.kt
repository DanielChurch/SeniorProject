package engine

import math.Quaternion
import math.Vector3

open class Entity {
    var position = Vector3.zero
    var scale = Vector3.zero
    var velocity = Vector3.zero
    var acceleration = Vector3.zero
    var rotation = Quaternion.zero
}