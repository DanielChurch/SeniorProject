package engine

class Engine {
    var scene: Scene = Scene()

    var onKeyPressed: (String) -> Unit = {}
    var onKeyReleased: (String) -> Unit = {}
    var onKeyDown: (String) -> Unit = {}
    var onMouseMove: (Unit) -> Unit = {}
    var onMousePress: (Int) -> Unit = {}
    var onMouseRelease: (Int) -> Unit = {}
    var onMouseDown: (Int) -> Unit = {}

    fun run() {}
    fun handleInput() {}
    fun update() {}
    fun render(delta: Double) {}
}