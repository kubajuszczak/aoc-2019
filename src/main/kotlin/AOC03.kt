import kotlin.math.max
import kotlin.math.min
import kotlin.system.measureTimeMillis

fun main() {

    val input = getInput("input03.txt").readLines()

    val lines1 = AOC03.getWireLines(input[0])
    val lines2 = AOC03.getWireLines(input[1])

    measureTimeMillis {
        println(AOC03.part1(lines1, lines2))
    }.also { println("${it}ms") }

    measureTimeMillis {
        println(AOC03.part2(lines1, lines2))
    }.also { println("${it}ms") }

}

class AOC03 {
    companion object {
        fun part1(lines1: List<Line>, lines2: List<Line>): Int? {
            val intersections = getAllIntersections(lines1, lines2)

            return intersections.map { it.getManhattanDistance() }.min()
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

        fun getWireLines(wire: String): List<Line> {
            var currentPoint = Point.ORIGIN
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
                'R' -> point + (Point.RIGHT * magnitude)
                'L' -> point + (Point.LEFT * magnitude)
                'U' -> point + (Point.UP * magnitude)
                'D' -> point + (Point.DOWN * magnitude)
                else -> throw IllegalArgumentException("Unknown instruction ${instruction[0]}")
            }
        }

        private fun getAllIntersections(lines1: List<Line>, lines2: List<Line>): List<Point> {

            val intersections = HashSet<Point>()
            for (l1 in lines1) {
                for (l2 in lines2) {
                    intersections.addAll(getIntersectionPoints(l1, l2))
                }
            }
            intersections.remove(Point.ORIGIN)
            return intersections.toList()
        }

        private fun getIntersectionPoints(line1: Line, line2: Line): Set<Point> {
            if (line1.isHorizontal() == line2.isHorizontal()) {
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
    }
}