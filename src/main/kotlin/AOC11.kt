import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis


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
        fun part1(program: List<Long>): Int? {
            return runPaintMachine(program).keys.size
        }

        fun part2(program: List<Long>) {
            val panels = runPaintMachine(program, mapOf(Pair(Point.ORIGIN, 1L)))
            printPointMap(panels)
        }

        private fun runPaintMachine(program: List<Long>, initialPanels: Map<Point, Long> = HashMap()): Map<Point, Long> {
            val inputChannel = Channel<Long>()
            val outputChannel = Channel<Long>(Channel.UNLIMITED)
            val c = IntComputer(inputFunction = inputChannel::receive, outputFunction = outputChannel::send)

            val panels = initialPanels.toMutableMap()
            var location = Point.ORIGIN
            var direction = Point.UP

            runBlocking {
                val io = launch {
                    while (true) {
                        inputChannel.send(panels[location] ?: 0L)

                        val paint = outputChannel.receive()
                        panels[location] = paint

                        val moveInput = outputChannel.receive()
                        direction = when (moveInput) {
                            0L -> direction.rotateAnticlockwise90()
                            1L -> direction.rotateClockwise90()
                            else -> throw IllegalArgumentException("Invalid input $moveInput")
                        }
                        location += direction
                    }
                }
                launch {
                    runAsync(c, program)
                    io.cancelAndJoin()
                }

            }
            return panels
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
}