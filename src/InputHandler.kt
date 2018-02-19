import java.awt.event.KeyEvent
import java.awt.event.KeyListener

private var keys: ArrayList<InputHandler.Key> = ArrayList()

class InputHandler(game: Game) : KeyListener {

    val up = InputHandler.Key()
    val down = InputHandler.Key()
    val left = InputHandler.Key()
    val right = InputHandler.Key()
    val attack = InputHandler.Key()
    val menu = InputHandler.Key()

    class Key {
        var presses = 0
        var absorbs = 0
        var down = false
        var clicked  = false

        init {
            keys.add(this)
        }

        fun toggle(pressed: Boolean) {
            if(pressed != down) {
                down = pressed
            }
            if(pressed) {
                presses++
            }
        }

        fun tick() {
            if (absorbs < presses) {
                absorbs++
                clicked = true
            } else {
                clicked = false
            }
        }
    }

    fun releaseAll() {
        for (i in keys.indices) {
            keys.get(i).down = false
        }
    }

    fun tick() {
        for (i in keys.indices) {
            keys.get(i).tick()
        }
    }

    init {
        game.addKeyListener(this)
    }

    override fun keyTyped(e: KeyEvent?) { }

    override fun keyPressed(e: KeyEvent?) { toggle(e!!, true) }

    override fun keyReleased(e: KeyEvent?) { toggle(e!!, false) }

    fun toggle(keyEvent: KeyEvent, pressed: Boolean) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_UP) up.toggle(pressed)
        if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) down.toggle(pressed)
        if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) left.toggle(pressed)
        if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) right.toggle(pressed)
        if (keyEvent.keyCode == KeyEvent.VK_X) menu.toggle(pressed)
        if (keyEvent.keyCode == KeyEvent.VK_C) attack.toggle(pressed)
    }

}