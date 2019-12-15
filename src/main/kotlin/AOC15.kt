import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import java.lang.IllegalArgumentException
import kotlin.system.measureTimeMillis

fun main() {
    val input = getInput("input15.txt").readText().split(",").map { it.toLong() }
    val maze = AOC15.runRepairRobot(input).also { AOC15.printMap(it) }

    measureTimeMillis {
        println(AOC15.part1(maze))
    }.also { println("${it}ms") }

    measureTimeMillis {
        println(AOC15.part2(maze))
    }.also { println("${it}ms") }

}

class AOC15 {
    companion object {
        fun part1(maze : HashMap<Point, Long>): Int {
            val map = maze.toMutableMap()
            val oxygenStation = map.entries.find { it.value == 2L }?.key

            return findPath(
                Point.ORIGIN,
                oxygenStation!!,
                emptyList(),
                map
            ).size
        }

        fun part2(maze : HashMap<Point, Long>): Int {
            val map = maze.toMutableMap()
            val directions = listOf(Point.UP, Point.DOWN, Point.LEFT, Point.RIGHT)

            var time = 0
            while (map.values.filter { it == 1L }.sum() > 0) {
                map.entries.filter { it.value == 2L }.forEach { oxygen ->
                    directions.forEach { direction ->
                        if (map[oxygen.key + direction] == 1L) {
                            map[oxygen.key + direction] = 2L
                        }
                    }
                }
                time++
            }
            return time
        }

        fun runRepairRobot(program: List<Long>): HashMap<Point, Long> {
            val inputChannel = Channel<Long>()
            val outputChannel = Channel<Long>()
            val c = IntComputer(inputChannel = inputChannel, outputChannel = outputChannel)

            val map = HashMap<Point, Long>()

            runBlocking {
                val computer = launch {
                    runAsync(c, program)
                }
                launch {
                    map[Point.ORIGIN] = 1L
                    explore(map, inputChannel, outputChannel, Point.ORIGIN)
                    computer.cancelAndJoin()
                }
            }

            return map
        }

        private fun getReverseDirection(input: Long): Long {
            return when (input) {
                1L -> 2L
                2L -> 1L
                3L -> 4L
                4L -> 3L
                else -> 0L
            }
        }

        private fun findPath(from: Point, to: Point, currentPath: List<Point>, map: Map<Point, Long>): List<Point> {
            val directions = listOf(Point.UP, Point.DOWN, Point.LEFT, Point.RIGHT)

            return directions.map {
                if (!currentPath.contains(from + it)) {
                    when {
                        map[from + it] == 1L -> findPath(from + it, to, currentPath + from, map)
                        map[from + it] == 2L -> currentPath + to
                        else -> emptyList()
                    }
                } else {
                    emptyList()
                }
            }.firstOrNull { it.isNotEmpty() } ?: emptyList()
        }

        private suspend fun explore(
            map: HashMap<Point, Long>,
            inputChannel: Channel<Long>,
            outputChannel: Channel<Long>,
            location: Point
        ) {
            val directions = ArrayList<Long>()
            (1L..4L).forEach {
                if (!map.containsKey(getNextLocation(location, it))) {
                    inputChannel.send(it)
                    val response = outputChannel.receive()
                    map[getNextLocation(location, it)] = response

                    if (response > 0L) {
                        directions.add(it)
                        inputChannel.send(getReverseDirection(it))
                        outputChannel.receive()
                    }
                }
            }

            directions.forEach {
                inputChannel.send(it)
                outputChannel.receive()
                explore(map, inputChannel, outputChannel, getNextLocation(location, it))
                inputChannel.send(getReverseDirection(it))
                outputChannel.receive()
            }
        }

        private fun getNextLocation(current: Point, direction: Long): Point {
            return current + when (direction) {
                1L -> Point.UP
                2L -> Point.DOWN
                3L -> Point.LEFT
                4L -> Point.RIGHT
                else -> throw IllegalArgumentException()
            }
        }

        fun printMap(map: Map<Point, Long>) {
            val minX = map.keys.minBy { it.x }?.x
            val maxX = map.keys.maxBy { it.x }?.x

            val minY = map.keys.minBy { it.y }?.y
            val maxY = map.keys.maxBy { it.y }?.y

            for (y in maxY!! downTo minY!!) {
                for (x in minX!!..maxX!!) {
                    if (Point(x, y) == Point.ORIGIN) {
                        print("●")
                    } else {
                        print(
                            when (map[Point(x, y)]) {
                                0L -> "▓"
                                1L -> "▒"
                                2L -> "○"
                                else -> " "
                            }
                        )
                    }
                }
                print("\n")
            }
        }
    }
}