import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.IllegalArgumentException
import kotlin.system.measureTimeMillis

@ExperimentalCoroutinesApi
fun main() {
    val input = getInput("input11.txt").readText().split(",").map { it.toLong() }

    measureTimeMillis {
        println(AOC11.part1(input))
    }.also { println("${it}ms") }

    measureTimeMillis {
        AOC11.part2(input)
    }.also { println("${it}ms") }

}

class AOC11 {
    companion object {
        @ExperimentalCoroutinesApi
        fun part1(program: List<Long>): Int? {
            return runPaintMachine(program).keys.size
        }

        @ExperimentalCoroutinesApi
        fun part2(program: List<Long>) {
            val panels = runPaintMachine(program, mapOf(Pair(Point(0, 0), 1L)))
            printPointMap(panels)
        }

        @ExperimentalCoroutinesApi
        fun runPaintMachine(program: List<Long>, initialPanels: Map<Point, Long> = HashMap()): Map<Point, Long> {
            val inputChannel = Channel<Long>()
            val outputChannel = Channel<Long>(Channel.UNLIMITED)
            val c = IntComputer(inputChannel = inputChannel, outputChannel = outputChannel)

            val panels = initialPanels.toMutableMap()
            var currentLocation = Point(0, 0)
            var facing = Facing.UP

            runBlocking {
                launch {
                    runAsync(c, program)
                    inputChannel.close()
                }

                launch {
                    while (!inputChannel.isClosedForSend) {
                        if (panels.containsKey(currentLocation)) {
                            inputChannel.send(panels[currentLocation]!!)
                        } else {
                            inputChannel.send(0L)
                        }

                        val paint = outputChannel.receive()
                        panels[currentLocation] = paint

                        val moveInput = outputChannel.receive()
                        facing = getNextFacing(facing, moveInput)
                        currentLocation = getNextLocation(currentLocation, facing)
                    }
                }

            }
            return panels
        }

        private fun getNextFacing(currentFacing: Facing, moveInput: Long): Facing {
            return when (currentFacing) {
                Facing.UP -> when (moveInput) {
                    0L -> Facing.LEFT
                    1L -> Facing.RIGHT
                    else -> throw IllegalArgumentException("Unexpected output")
                }
                Facing.RIGHT -> when (moveInput) {
                    0L -> Facing.UP
                    1L -> Facing.DOWN
                    else -> throw IllegalArgumentException("Unexpected output")
                }
                Facing.DOWN -> when (moveInput) {
                    0L -> Facing.RIGHT
                    1L -> Facing.LEFT
                    else -> throw IllegalArgumentException("Unexpected output")
                }
                Facing.LEFT -> when (moveInput) {
                    0L -> Facing.DOWN
                    1L -> Facing.UP
                    else -> throw IllegalArgumentException("Unexpected output")
                }
            }

        }

        private fun getNextLocation(location: Point, facing: Facing): Point {
            return when (facing) {
                Facing.UP -> Point(location.x, location.y + 1)
                Facing.RIGHT -> Point(location.x + 1, location.y)
                Facing.DOWN -> Point(location.x, location.y - 1)
                Facing.LEFT -> Point(location.x - 1, location.y)
            }
        }

        private fun printPointMap(map: Map<Point, Long>) {
            val minX = map.keys.minBy { it.x }?.x
            val maxX = map.keys.maxBy { it.x }?.x

            val minY = map.keys.minBy { it.y }?.y
            val maxY = map.keys.maxBy { it.y }?.y

            for (y in maxY!! downTo minY!!) {
                for (x in minX!!..maxX!!) {
                    print(
                        when (map[Point(x, y)]) {
                            0L -> "░"
                            1L -> "▓"
                            else -> " "
                        }
                    )
                }
                print("\n")
            }
        }
    }

    enum class Facing {
        UP, RIGHT, DOWN, LEFT
    }
}