package gfx

class Color {
    companion object {
        fun get(a: Int, b: Int, c: Int, d: Int): Int {
            return ((get(d) shl 24) + (get(c) shl 16) + (get(b) shl 8) + get(a))
        }

        fun get(d: Int): Int {
            if (d < 0) return 255
            val r = d / 100 % 10
            val g = d / 10 % 10
            val b = d % 10
            return r * 36 + g * 6 + b
        }
    }
}