import java.lang.IllegalArgumentException
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.system.measureTimeMillis

fun main() {

    val input = getInput("input03.txt").readLines()
//    val wire1 = "R8,U5,L5,D3"
//    val wire2 = "U7,R6,D4,L4"
//    val wire1 = "R75,D30,R83,U83,L12,D49,R71,U7,L72"
//    val wire2 = "U62,R66,U55,R34,D71,R55,D58,R83"
//    val wire1 = "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51"
//    val wire2 = "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7"

    val lines1 = getWireLines(input[0])
    val lines2 = getWireLines(input[1])

    measureTimeMillis {
        println(part1(lines1, lines2))
    }.also { println("${it}ms") }

    measureTimeMillis {
        println(part2(lines1, lines2))
    }.also { println("${it}ms") }

}

private fun part1(lines1: List<Line>, lines2: List<Line>): Int? {
    val intersections = getAllIntersections(lines1, lines2)

    val origin = Point(0,0)
    return intersections.map { origin.getManhattanDistance(it) }.min()
}

private fun part2(lines1: List<Line>, lines2: List<Line>): Int? {
    val intersections = getAllIntersections(lines1, lines2)

    return intersections.map {
        var sum = 0
        for (line in lines1) {
            if (line.containsPoint(it)) {
                sum += Line(line.start, it).getLength()
                break
            } else {
                sum += line.getLength()
            }
        }
        for (line in lines2) {
            if (line.containsPoint(it)) {
                sum += Line(line.start, it).getLength()
                break
            } else {
                sum += line.getLength()
            }
        }
        sum
    }.min()
}

private fun getWireLines(wire: String): List<Line> {
    var currentPoint = Point(0, 0)
    val lines = ArrayList<Line>()
    for (instruction in wire.split(",")) {
        val nextPoint = getNextPoint(instruction, currentPoint)
        lines.add(Line(currentPoint, nextPoint))
        currentPoint = nextPoint
    }
    return lines
}

private fun getNextPoint(instruction: String, point: Point): Point {
    val magnitude = instruction.substring(1).toInt()
    return when (instruction[0]) {
        'R' ->
            Point(point.x + magnitude, point.y)
        'L' ->
            Point(point.x - magnitude, point.y)
        'U' ->
            Point(point.x, point.y + magnitude)
        'D' ->
            Point(point.x, point.y - magnitude)
        else ->
            throw IllegalArgumentException("Unknown instruction ${instruction[0]}")
    }
}

private fun getAllIntersections(lines1: List<Line>, lines2: List<Line>): List<Point> {

    val intersections = HashSet<Point>()
    for (l1 in lines1) {
        for (l2 in lines2) {
            intersections.addAll(getIntersection(l1, l2))
        }
    }
    intersections.remove(Point(0, 0))
    return intersections.toList()
}

private fun getIntersection(line1: Line, line2: Line): Set<Point> {
    if ((line1.isHorizontal() && line2.isHorizontal()) || (!line1.isHorizontal() && !line2.isHorizontal())) {
        if (line1.getIntercept() != line2.getIntercept()) {
            return emptySet()
        }

        val e = max(line1.getLowerBound(), line2.getLowerBound())
        val f = min(line1.getUpperBound(), line2.getUpperBound())

        if (e <= f) {
            return if (line1.isHorizontal()) {
                (e..f).map { Point(it, line1.start.y) }.toSet()
            } else {
                (e..f).map { Point(line1.start.x, it) }.toSet()
            }
        }
    } else {
        val horizontalLine = when (line1.isHorizontal()) {
            true -> line1
            false -> line2
        }
        val verticalLine = when (line1.isHorizontal()) {
            true -> line2
            false -> line1
        }

        val crossover = Point(verticalLine.getIntercept(), horizontalLine.getIntercept())
        if (verticalLine.containsPoint(crossover) && horizontalLine.containsPoint(crossover)) {
            return setOf(crossover)
        }
    }
    return emptySet()
}

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