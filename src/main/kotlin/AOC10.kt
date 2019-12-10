import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.system.measureTimeMillis

fun main() {
    val input = getInput("input10.txt").readLines()

    measureTimeMillis {
        println(AOC10.part1(input))
    }.also { println("${it}ms") }

    measureTimeMillis {
        println(AOC10.part2(input))
    }.also { println("${it}ms") }

}

class AOC10 {
    companion object {
        fun part1(input: List<String>): Int? {
            val asteroids = ArrayList<Point>()

            input.forEachIndexed { y, s ->
                s.forEachIndexed { x, r ->
                    if (r == '#') {
                        asteroids.add(Point(x, -y))
                    }
                }
            }

            return asteroids.map { asteroid ->
                asteroids.filter { it != asteroid }.map { target ->
                    asteroid.getPolarAngle(target)
                }.toSet().size
            }.max()
        }

        fun part2(input: List<String>): Int? {
            val asteroids = ArrayList<Point>()

            input.forEachIndexed { y, s ->
                s.forEachIndexed { x, r ->
                    if (r == '#') {
                        asteroids.add(Point(x, -y))
                    }
                }
            }

            val station = asteroids.maxBy { asteroid ->
                asteroids.filter { it != asteroid }.map { target ->
                    asteroid.getPolarAngle(target)
                }.toSet().size
            }

            asteroids.remove(station)

            if (station != null) {
                val vaporized = ArrayList<Point>()

                var currentBearing = 0.0
                var index = 1
                while (asteroids.isNotEmpty()) {
                    val asteroid = asteroids
                        .filter { station.getBearing(it) == currentBearing }
                        .minBy { station.getCartesianDistance(it) }

                    asteroids.remove(asteroid)
                    if (asteroid != null) {
                        vaporized.add(asteroid)
                    }

                    currentBearing = asteroids.map { station.getBearing(it) }
                        .filter { it > currentBearing }
                        .min() ?: 0.0

                    index++
                }

                return vaporized[199].x * 100 + abs(vaporized[199].y)
            }

            return 0
        }
    }
}