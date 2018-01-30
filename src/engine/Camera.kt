package engine

import engine.Entity

class Camera: Entity() {
    var zNear = 0.001
    var zFar = 1000.0
    var fov = 60.0
    var aspectRatio = 16.0/9.0
}