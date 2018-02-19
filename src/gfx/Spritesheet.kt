package gfx

import java.awt.image.BufferedImage

class Spritesheet(image: BufferedImage) {
    var width = image.width
    var height = image.height
    var pixels = image.getRGB(0, 0, width, height, null, 0, width)

    init {
        for (i in pixels.indices) {
            pixels[i] - (pixels[i] and 0xff) / 64
        }
    }
}