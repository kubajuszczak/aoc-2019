import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class Line(
    val start: Point, val end: Point
) {
    fun getLength(): Int {
        return abs(start.x - end.x) + abs(start.y - end.y)
    }

    fun isHorizontal(): Boolean {
        return start.y == end.y
    }

    fun getIntercept(): Int {
        return if (isHorizontal()) {
            start.y
        } else {
            start.x
        }
    }

    fun getLowerBound(): Int {
        return if (isHorizontal()) {
            min(start.x, end.x)
        } else {
            min(start.y, end.y)
        }
    }

    fun getUpperBound(): Int {
        return if (isHorizontal()) {
            max(start.x, end.x)
        } else {
            max(start.y, end.y)
        }
    }

    fun containsPoint(point: Point): Boolean {
        return when (this.isHorizontal()) {
            true -> point.y == this.getIntercept() && point.x >= this.getLowerBound() && point.x <= this.getUpperBound()
            false -> point.x == this.getIntercept() && point.y >= this.getLowerBound() && point.y <= this.getUpperBound()
        }
    }
}