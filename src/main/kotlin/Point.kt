import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.sqrt
import kotlin.math.pow

data class Point(
    val x: Int, val y: Int
) {
    fun getTaxicabMagnitude(): Int {
        return abs(this.x) + abs(this.y)
    }

    fun getCartesianDistance(b: Point): Double {
        return sqrt((b.x.toDouble() - this.x.toDouble()).pow(2) +
            (b.y.toDouble() - this.y.toDouble()).pow(2))
    }

    fun getDirection(b: Point): Double {
        // this is flipped upside down possibly (but it didn't matter for part 1)
        val dx = (b.x - this.x)
        val dy = (b.y - this.y)

        if (dx == 0 && dy == 0) {
            return 0.0
        } else if (dx == 0 && dy > 0) {
            return PI / 2
        } else if (dx == 0 && dy < 0) {
            return 1.5 * PI
        } else if (dy == 0 && dx > 0) {
            return 0.0
        } else if (dy == 0 && dx < 0) {
            return PI
        }

        val tan = abs(dy.toDouble() / dx.toDouble())

        return if (b == this) {
            0.0
        } else if (dx > 0 && dy > 0) {
            return atan(tan)
        } else if (dx < 0 && dy > 0) {
            return PI - atan(tan)
        } else if (dx < 0 && dy < 0) {
            return PI + atan(tan)
        } else {
            return 2 * PI - atan(tan)
        }
    }

    fun getBearing(b: Point): Double {
        return (1.25 + getDirection(b) / (2 * PI)) * 360 % 360
    }
}