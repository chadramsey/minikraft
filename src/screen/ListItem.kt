package screen

import gfx.Screen

interface ListItem {
    fun renderInventory(screen: Screen, i: Int, j: Int)
}