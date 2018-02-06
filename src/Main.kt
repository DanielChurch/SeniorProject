import engine.Engine
import kotlin.browser.window

class Main {
    init {
        val engine = Engine()
        engine.onKeyDown = {event -> println(event)}
    }
}

fun main(args: Array<String>) {
    window.onload = {
        Main()
    }
}