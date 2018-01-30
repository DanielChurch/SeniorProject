package web

import org.w3c.dom.Element
import kotlin.browser.document

public class Dom {
    companion object {
        fun body(vararg children: Element) = appendChildren(document.body as Element, children)
        fun div(vararg children: Element) = appendChildren(document.createElement("div"), children)
        fun canvas(vararg children: Element) = appendChildren(document.createElement("canvas"), children)
        fun a(vararg children: Element) = appendChildren(document.createElement("a"), children)
        fun p(vararg children: Element) = appendChildren(document.createElement("p"), children)

        fun appendChildren(e: Element, children: Array<out Element>): Element {
            children.forEach {
                e.append(it)
            }
            return e
        }
    }
}