import kotlin.math.*

data class Point(
    val x: Int, val y: Int
) {

    operator fun plus(b: Point): Point {
        return Point(this.x + b.x, this.y + b.y)
    }

    operator fun minus(b: Point): Point {
        return Point(this.x - b.x, this.y - b.y)
    }

    operator fun times(multiple: Int): Point {
        return Point(multiple * x, multiple * y)
    }

    fun getManhattanDistance(): Int {
        return this.getManhattanDistance(ORIGIN)
    }

    private fun getManhattanDistance(b: Point): Int {
        return abs(b.x - this.x) + abs(b.y - this.y)
    }

    fun getCartesianDistance(b: Point): Double {
        val (dx, dy) = b - this
        return hypot(dx.toDouble(), dy.toDouble())
    }

    // angle in radians from positive x-axis
    fun getPolarAngle(b: Point): Double {
        val (dx, dy) = b - this

        return atan2(dy.toDouble(), dx.toDouble())
    }

    // bearing: 0 north, 90 east, 180 south, 270 west
    fun getBearing(b: Point): Double {
        return (1.25 - getPolarAngle(b) / (2 * PI)) * 360 % 360
    }

    // Matrix rotation for 2D vector
    fun rotateAnticlockwise90(): Point {
        return Point(-this.y, this.x)
    }

    fun rotateClockwise90(): Point {
        return Point(this.y, -this.x)
    }

    fun turnLeft(): Point {
        return this.rotateAnticlockwise90()
    }

    fun turnRight(): Point {
        return this.rotateClockwise90()
    }

    companion object {
        val UP = Point(0, 1)
        val DOWN = Point(0, -1)
        val LEFT = Point(-1, 0)
        val RIGHT = Point(1, 0)
        val ORIGIN = Point(0, 0)
    }
}