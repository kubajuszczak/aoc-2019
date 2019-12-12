import kotlin.math.abs
import kotlin.system.measureTimeMillis

fun main() {
    val input = getInput("input12.txt").readLines()
        .map { moon -> moon.replace(Regex("[^0-9,-]"), "").split(",").map { it.toInt() } }
        .map { Point3(it[0], it[1], it[2]) }

    measureTimeMillis {
        println(AOC12.part1(input))
    }.also { println("${it}ms") }

    measureTimeMillis {
        println(AOC12.part2(input))
    }.also { println("${it}ms") }

}

class AOC12 {
    companion object {
        fun part1(moons: List<Point3>): Int {

            var system = moons.map { Vector3(it, Point3.ORIGIN) }

            for (iteration in 1..1000) {
                system = stepSimulation(system)
            }

            return system.map { getEnergy(it) }.sum()
        }


        fun part2(moons: List<Point3>): Int {
            val start = moons.map { Vector3(it, Point3.ORIGIN) }

            var system = stepSimulation(start)
            var iteration = 1

            while(system != start) {
                system = stepSimulation(system)
                iteration ++
            }

            return iteration
        }

        fun stepSimulation(moons: List<Vector3>): List<Vector3> {
            val vectors = ArrayList<Vector3>()

            for (moon in moons) {
                for (moon2 in moons) {
                    if (moon != moon2) {
                        val x = when {
                            moon.position.x < moon2.position.x -> 1
                            moon.position.x > moon2.position.x -> -1
                            else -> 0
                        }
                        val y = when {
                            moon.position.y < moon2.position.y -> 1
                            moon.position.y > moon2.position.y -> -1
                            else -> 0
                        }
                        val z = when {
                            moon.position.z < moon2.position.z -> 1
                            moon.position.z > moon2.position.z -> -1
                            else -> 0
                        }

                        vectors.add(Vector3(moon.position, Point3(x, y, z)))
                    }
                }
            }

            return vectors.groupBy { it.position }
                .map { Vector3(it.key, moons.find { moon->moon.position == it.key }!!.velocity + it.value.map { moon -> moon.velocity }.reduce { acc, point3 -> acc + point3 }) }
                .map { Vector3(it.position + it.velocity,  it.velocity) }
        }

        private fun getEnergy(moon: Vector3): Int {
            return (abs(moon.position.x) + abs(moon.position.y) + abs(moon.position.z)) *
                    (abs(moon.velocity.x) + abs(moon.velocity.y) + abs(moon.velocity.z))
        }
    }
}


data class Point3(val x: Int, val y: Int, val z: Int) {
    operator fun plus(b: Point3): Point3 {
        return Point3(this.x + b.x, this.y + b.y, this.z + b.z)
    }

    companion object {
        val ORIGIN = Point3(0, 0, 0)
    }
}

data class Vector3(val position: Point3, val velocity: Point3)