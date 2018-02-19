package gfx

class Screen(var width: Int, var height: Int, var sheet: Spritesheet) {

    var xOffset = 0
    var yOffset = 0
    var pixels = IntArray(width * height)

    fun clear(color: Int) {
        for (i in pixels.indices) {
            pixels[i] = color
        }
    }

    fun render(argsXp: Int, argsYp: Int, tile: Int, colors: Int, bits: Int) {
        val xp = argsXp - xOffset
        val yp = argsYp - yOffset
        val mirrorX = (bits and BIT_MIRROR_X) > 0
        val mirrorY = (bits and BIT_MITTOR_Y) > 0
        val xTile = tile % 32
        val yTile = tile / 32
        val tileOffset = xTile * 8 + yTile * 8 * sheet.width

        for (y in 0 until 8) {
            var ys = y
            if (mirrorY) ys = 7 - y
            if (y + yp < 0 || y + yp >= height) continue
            for (x in 0 until 8) {
                if (x + xp < 0 || x + xp >= width) continue
                var xs = x
                if (mirrorX) xs = 7 - x
                val col = (colors shr (sheet.pixels[xs + ys * sheet.width + tileOffset] * 8)) and 255
                if (col < 255) pixels[(x + xp) + (y + yp) * width] = col
            }
        }

    }

    fun setOffset(xOffset: Int, yOffset: Int) {
        this.xOffset = xOffset
        this.yOffset = yOffset
    }

    private val dither = intArrayOf(0, 8, 2, 10, 12, 4, 14, 6, 3, 11, 1, 9, 15, 7, 13, 5)

    fun overlay(screenOverlay: Screen, xa: Int, ya: Int) {
        val overlayPixels = screenOverlay.pixels
        var currentPixelIndex = 0
        for (y in 0 until height) {
            for (x in 0 until width) {
                if(overlayPixels[currentPixelIndex] / 10 <= dither[((x + xa) and 3) + ((y + ya) and 3) * 4]) {
                    pixels[currentPixelIndex] = 0
                }
                currentPixelIndex++
            }
        }
    }

    fun renderLight(argsX: Int, argsY: Int, r: Int) {
        val x = argsX - xOffset
        val y = argsY - yOffset

        var x0 = x - r
        var x1 = x + r
        var y0 = y - r
        var y1 = y + r

        if (x0 < 0) x0 = 0
        if (y0 < 0) y0 = 0
        if (x1 > width) x1 = width
        if (y1 > height) y1 = height
        for (yy in y0 until y1) {
            var yd = yy - y
            yd *= yd
            for (xx in x0 until x1) {
                val xd = xx - x
                val dist = xd * xd + yd
                if (dist <= r * r) {
                    val br = 255 - dist * 255 / (r * r)
                    if (pixels[xx + yy * width] < br) pixels[xx + yy * width] = br
                }
            }
        }
    }

    companion object {
        const val BIT_MIRROR_X = 0x01
        const val BIT_MITTOR_Y = 0x02

    }
}