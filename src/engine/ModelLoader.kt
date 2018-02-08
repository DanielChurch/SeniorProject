package engine

import org.w3c.xhr.XMLHttpRequest

class ModelLoader {
    companion object {
        fun loadFile(file: String) {
            val rawFile = XMLHttpRequest()
            rawFile.open("GET", file, false)

            rawFile.onreadystatechange = {
                if (rawFile.readyState.toInt() == 4) {
                    if (rawFile.status.toInt() == 200 || rawFile.status.toInt() == 0) {
                        var allText = rawFile.responseText
                        println(allText)
                    }
                }
            }
            rawFile.send(null)
        }

        fun loadFBX(path: String): RenderingObject = Cube()
        fun loadOBJ(path: String): RenderingObject = Cube()
    }
}