import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.sqrt
import kotlin.math.pow

data class Point(
    val x: Int, val y: Int
) {
    fun getManhattanDistance(b: Point): Int {
        return abs(b.x - this.x) + abs(b.y - this.y)
    }

    fun getCartesianDistance(b: Point): Double {
        return sqrt((b.x.toDouble() - this.x.toDouble()).pow(2) +
            (b.y.toDouble() - this.y.toDouble()).pow(2))
    }

    // angle in radians from positive x-axis anticlockwise
    fun getPolarAngle(b: Point): Double {
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

    // bearing: 0 north, 90 east, 180 south, 270 west
    fun getBearing(b: Point): Double {
        return (1.25 - getPolarAngle(b) / (2 * PI)) * 360 % 360
    }
}