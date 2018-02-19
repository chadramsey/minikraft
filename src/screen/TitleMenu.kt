package screen

import Game
import InputHandler
import gfx.Screen
import gfx.Color
import gfx.Font

class TitleMenu(game: Game, inputHandler: InputHandler) : Menu(game, inputHandler) {
    private var selected = 0

    override fun tick() {
        if (inputHandler.up.clicked) selected--
        if (inputHandler.down.clicked) selected++

        val len = options.size
        if (selected < 0) selected += len
        if (selected >= len) selected -= len

        if (inputHandler.attack.clicked || inputHandler.menu.clicked) {
            if (selected == 0) {
                game.setMenu(null)
            }
        }
    }

    override fun render(screen: Screen) {
        screen.clear(0)

        val h = 2
        val w = 13
        val titleColor = Color.get(0, 8, 131, 551)
        val xo = (screen.width - w * 8) / 2
        val yo = 24

        for (y in 0 until h) {
            for (x in 0 until w) {
                screen.render(xo + x * 8, yo + y * 8, x + (y + 6) * 32, titleColor, 0)
            }
        }

        for (i in options.indices) {
            var msg = options[i]
            var col = Color.get(0, 222, 222, 222)
            if (i == selected) {
                msg = "> $msg <"
                col = Color.get(0, 555, 555, 555)
            }
            Font.draw(msg, screen, (screen.width - msg.length * 8) / 2, (8 + i) * 8, col)
        }
        Font.draw("(Arrow keys,X and C)", screen, 0, screen.height - 8, Color.get(0, 111, 111, 111))
    }

    companion object {
        val options = arrayOf("Start Game", "How to Play", "About")
    }
}