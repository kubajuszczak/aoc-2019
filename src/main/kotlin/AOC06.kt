import kotlin.system.measureTimeMillis

fun main() {
    val input = getInput("input06.txt").readLines()

    val map = input.groupBy({ it.split(")")[0] }, { it.split(")")[1] })

    measureTimeMillis {
        println(AOC06.part1(map))
    }.also { println("${it}ms") }

    measureTimeMillis {
        println(AOC06.part2(map))
    }.also { println("${it}ms") }
}

class AOC06 {
    companion object {
        fun part1(map: Map<String, List<String>>): Int {
            return addOrbits(map, "COM", 1)
        }

        fun part2(map: Map<String, List<String>>): Int {
            // find path from COM to the object YOU orbit
            val you = find(map, "COM", "YOU")

            // find path from COM to the object SAN orbits
            val san = find(map, "COM", "SAN")

            // find path from COM to common node
            val common = ArrayList<String>()
            for (i in you.indices) {
                if (you[i] != san[i]) {
                    break
                }
                common.add(you[i])
            }

            // add orbital jumps to both YOU and SAN from COM
            // subtract the jumps from COM to common node which are double counted
            return (you.size - 1) + (san.size - 1) - 2 * (common.size - 1)
        }

        private fun addOrbits(map: Map<String, List<String>>, objectName: String, depth: Int): Int {
            val currentOrbitingBodyCount = map[objectName]?.size ?: 0
            val deeperOrbits = map[objectName]?.map { addOrbits(map, it, depth + 1) }?.sum() ?: 0

            return currentOrbitingBodyCount * depth + deeperOrbits
        }

        // depth-first search?
        private fun find(
            map: Map<String, List<String>>,
            current: String,
            target: String,
            path: List<String> = ArrayList()
        ): List<String> {
            val myPath = path.toMutableList()
            myPath.add(current)

            return if (map.containsKey(current)) {
                if (map[current]?.contains(target)!!) {
                    myPath
                } else {
                    val deeper = map[current]?.map { find(map, it, target, myPath) } ?: listOf(emptyList())
                    if (deeper.any { it.isNotEmpty() }) {
                        deeper.first { it.isNotEmpty() }
                    } else {
                        emptyList()
                    }
                }
            } else {
                emptyList()
            }
        }
    }
}