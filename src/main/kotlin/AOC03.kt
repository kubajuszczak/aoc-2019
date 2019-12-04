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

fun part1(lines1: List<Line>, lines2: List<Line>): Int? {
    val intersections = getAllIntersections(lines1, lines2)

    return intersections.map { it.getTaxicabMagnitude() }.min()
}

fun part2(lines1: List<Line>, lines2: List<Line>): Int? {
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

fun getAllIntersections(lines1: List<Line>, lines2: List<Line>): ArrayList<Point> {

    val intersections = ArrayList<Point>()
    for (l1 in lines1) {
        for (l2 in lines2) {
            intersections.addAll(getIntersection(l1, l2))
        }
    }
    return intersections
}

fun getIntersection(line1: Line, line2: Line): List<Point> {
    val set1 = getLinePoints(line1)
    val set2 = getLinePoints(line2)

    set1.retainAll(set2)
    set1.remove(Point(0, 0))

    return set1.toList()
}

private fun getLinePoints(line: Line): HashSet<Point> {
    val set = HashSet<Point>()
    for (x in min(line.start.x, line.end.x)..max(line.start.x, line.end.x)) {
        for (y in min(line.start.y, line.end.y)..max(line.start.y, line.end.y)) {
            set.add(Point(x, y))
        }
    }
    return set
}

fun getWireLines(wire: String): List<Line> {
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

data class Line(
    val start: Point, val end: Point
) {
    fun getLength(): Int {
        return abs(start.x - end.x) + abs(start.y - end.y)
    }

    fun containsPoint(point: Point): Boolean {
        return ((start.x <= point.x && point.x <= end.x) || (end.x <= point.x && point.x <= start.x)) &&
                ((start.y <= point.y && point.y <= end.y) || (end.y <= point.y && point.y <= start.y))
    }
}

data class Point(
    val x: Int, val y: Int
) {
    fun getTaxicabMagnitude(): Int {
        return abs(this.x) + abs(this.y)
    }
}