import gfx.Screen

import gfx.Spritesheet
import screen.Menu
import screen.TitleMenu
import java.awt.BorderLayout
import java.awt.Canvas
import java.awt.Dimension
import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt
import java.io.IOException
import javax.imageio.ImageIO
import javax.swing.JFrame
class Game : Canvas(), Runnable {

    private val colors = IntArray(256)
    private val levels = IntArray(5)

    private val image: BufferedImage = BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB)
    private val pixels = (image.raster.dataBuffer as DataBufferInt).data

    private lateinit var menu: Menu
    private lateinit var screen: Screen
    private lateinit var lightScreen: Screen

    private var input: InputHandler = InputHandler(this)
    private var running = false
    private var tickCount = 0

    /**
     * The main thread which runs the game (implemented via Runnable).
     * Manages system time variables and all rendering functions.
     */
    override fun run() {
        System.out.println("Game loop initialized")

        val nanoSecondsPerTick = 1000000000.0 / 60

        var lastTimer = System.currentTimeMillis()
        var lastTime = System.nanoTime()
        var unprocessed = 0.0
        var frames = 0
        var ticks = 0

        init()

        while (running) {
            val now = System.nanoTime()
            unprocessed += (now - lastTime) / nanoSecondsPerTick
            lastTime = now
            var shouldRender = true
            while(unprocessed >= 1) {
                ticks++
                tick()
                unprocessed -= 1
                shouldRender = true
            }

            try {
                Thread.sleep(2)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            if(shouldRender) {
                frames++
                render()
            }

            if(System.currentTimeMillis() - lastTimer > 1000) {
                lastTimer += 1000
                System.out.println("$ticks ticks, $frames fps")
                frames = 0
                ticks = 0
            }
        }
    }

    /**
     * Initializes the game's color palette, loads the sprite sheets and
     * sets the title menu
     */
    private fun init() {
        var pp = 0
        for (r in 0 until 6) {
            for (g in 0 until 6) {
                for (b in 0 until 6) {
                    val rr = (r * 255 / 5)
                    val gg = (g * 255 / 5)
                    val bb = (b * 255 / 5)
                    val mid = (rr * 30 + gg * 59 + bb * 11) / 100

                    val r1 = ((rr + mid * 1) / 2) * 230 / 255 + 10
                    val g1 = ((gg + mid * 1) / 2) * 230 / 255 + 10
                    val b1 = ((bb + mid * 1) / 2) * 230 / 255 + 10

                    /**
                     * TODO Say more about bit operations and bit shifting
                     */
                    colors[pp++] = (r1 shl 16) or (g1 shl 8) or b1
                }
            }
        }

        try {
            screen = gfx.Screen(WIDTH, HEIGHT, Spritesheet(ImageIO.read(Game::class.java.getResourceAsStream("/icons.png"))))
            lightScreen = gfx.Screen(WIDTH, HEIGHT, Spritesheet(ImageIO.read(Game::class.java.getResourceAsStream("/icons.png"))))
        } catch (e: IOException) {
            e.printStackTrace()
        }

        //TODO Implement resetGame()
        setMenu(TitleMenu(this, input))
    }

    fun render() {
        val bs = bufferStrategy
        if (bs == null) {
            createBufferStrategy(3)
            requestFocus()
            return
        }

        if (menu != null) {
            menu.render(screen)
        }

        for (y in 0 until screen.height) {
            for (x in 0 until screen.width) {
                val cc = screen.pixels[x + y * screen.width]
                if (cc < 255) pixels[x + y * WIDTH] = colors[cc]
            }
        }

        val g = bs.drawGraphics
        g.fillRect(0, 0, width, height)

        val ww = WIDTH * 3
        val hh = HEIGHT * 3
        val xo = (width - ww) / 2
        val yo = (height - hh) / 2
        g.drawImage(image, xo, yo, ww, hh, null)
        g.dispose()
        bs.show()
    }

    fun tick() {
        tickCount++
        if (!hasFocus()) {
            input.releaseAll()
        } else {
            // if (!player.removed && !hasWon) gameTime++
            input.tick()
            if (menu != null) {
                menu.tick()
            }
        }
    }

    fun setMenu(menu: Menu?) {
        if (null != menu) {
            this.menu = menu
        }
    }

    /**
     * Toggles the 'running' variable and starts the main application
     * thread which manages the game (see [Game.run])
     */
    fun start() {
        running = true
        Thread(this).start()
    }

    /**
     * Companion object for storing window properties (used when
     * initializing the [Game] component)
     */
    companion object WindowProperties {
        const val WINDOW_NAME = ""
        const val HEIGHT = 200
        const val WIDTH = 267
        const val SCALE = 3
    }
}

object Main {
    /**
     * The main method, called when the application is executed.
     * This method builds the [Game] component and its [JFrame] container,
     * then starts the game.
     */
    @JvmStatic fun main(args : Array<String>) {
        val game = Game()
        game.minimumSize = Dimension((Game.WindowProperties.WIDTH * Game.WindowProperties.SCALE),
                (Game.WindowProperties.HEIGHT * Game.WindowProperties.SCALE))
        game.maximumSize = Dimension((Game.WindowProperties.WIDTH * Game.WindowProperties.SCALE),
                (Game.WindowProperties.HEIGHT * Game.WindowProperties.SCALE))
        game.preferredSize = Dimension((Game.WindowProperties.WIDTH * Game.WindowProperties.SCALE),
                (Game.WindowProperties.HEIGHT * Game.WindowProperties.SCALE))

        val frame = JFrame(Game.WINDOW_NAME)
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.layout = BorderLayout()
        frame.isResizable = false
        frame.isVisible = true
        frame.add(game, BorderLayout.CENTER)
        frame.pack()

        game.start()
    }
}