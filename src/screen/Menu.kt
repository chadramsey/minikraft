package screen

import Game
import InputHandler
import gfx.Color
import gfx.Font
import gfx.Screen

open class Menu(val game: Game, val inputHandler: InputHandler) {

    open fun tick() { }

    open fun render(screen: Screen) { }

    fun renderItemList(screen: Screen, xo: Int, yo: Int, x1: Int, y1: Int, listItems: List<ListItem>, argsSelected: Int) {
        var selected = argsSelected
        var renderCursor = true
        if (selected < 0) {
            selected = -selected - 1
            renderCursor = false
        }
        val w = x1 - xo
        val h = y1 - yo - 1
        val i0 = 0
        var i1 = listItems.size
        if (i1 > h) i1 = h
        var io = selected - h / 2
        if (io > listItems.size - h) io = listItems.size - h
        if (io < 0) io = 0

        for (i in i0 until i1) {
            listItems[i + io].renderInventory(screen, (1 + xo) * 8, (i + 1 + yo) * 8)
        }

        if (renderCursor) {
            val yy = selected + 1 - io + yo
            Font.draw(">", screen, (xo + 0) * 8, yy * 8, Color.get(5, 555, 555, 555))
            Font.draw("<", screen, (xo + w) * 8, yy * 8, Color.get(5, 555, 555, 555))
        }
    }
}