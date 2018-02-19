package gfx

class Font {
    companion object {
        private const val chars = "" +
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ      " +
                "0123456789.,!?'\"-+=/\\%()<>:;     " +
                ""

        fun draw(argsMsg: String, screen: Screen, x: Int, y: Int, col: Int) {
            var msg = argsMsg
            msg = msg.toUpperCase()
            for (i in 0 until msg.length) {
                val ix = chars.indexOf(msg[i])
                if (ix >= 0) {
                    screen.render(x + i * 8, y, ix + 30 * 32, col, 0)
                }
            }
        }

        fun renderFrame(screen: Screen, title: String, x0: Int, y0: Int, x1: Int, y1: Int) {
            for (y in y0..y1) {
                for (x in x0..x1) {
                    if (x == x0 && y == y0)
                        screen.render(x * 8, y * 8, 0 + 13 * 32, Color.get(-1, 1, 5, 445), 0)
                    else if (x == x1 && y == y0)
                        screen.render(x * 8, y * 8, 0 + 13 * 32, Color.get(-1, 1, 5, 445), 1)
                    else if (x == x0 && y == y1)
                        screen.render(x * 8, y * 8, 0 + 13 * 32, Color.get(-1, 1, 5, 445), 2)
                    else if (x == x1 && y == y1)
                        screen.render(x * 8, y * 8, 0 + 13 * 32, Color.get(-1, 1, 5, 445), 3)
                    else if (y == y0)
                        screen.render(x * 8, y * 8, 1 + 13 * 32, Color.get(-1, 1, 5, 445), 0)
                    else if (y == y1)
                        screen.render(x * 8, y * 8, 1 + 13 * 32, Color.get(-1, 1, 5, 445), 2)
                    else if (x == x0)
                        screen.render(x * 8, y * 8, 2 + 13 * 32, Color.get(-1, 1, 5, 445), 0)
                    else if (x == x1)
                        screen.render(x * 8, y * 8, 2 + 13 * 32, Color.get(-1, 1, 5, 445), 1)
                    else
                        screen.render(x * 8, y * 8, 2 + 13 * 32, Color.get(5, 5, 5, 5), 1)
                }
            }
            draw(title, screen, x0 * 8 + 8, y0 * 8, Color.get(5, 5, 5, 550))
        }
    }
}