import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() {
    val input = getInput("input13.txt").readText().split(",").map { it.toLong() }

//    measureTimeMillis {
//        println(AOC13.part1(input))
//    }.also { println("${it}ms") }

    measureTimeMillis {
        println(AOC13.part2(input))
    }.also { println("${it}ms") }

}

class AOC13 {
    companion object {
        fun part1(program: List<Long>): Int? {
            val result = runArcadeMachine(program)
            printArcadeMap(result)
            return result.filter { it.value == Tile.BLOCK }.keys.size
        }

        fun part2(program: List<Long>) {
            runArcadeMachine(listOf(2L) + program.drop(1))
        }

        private fun runArcadeMachine(program: List<Long>): Map<Point, Tile> {
            val inputChannel = Channel<Long>()
            val outputChannel = Channel<Long>()
            val c = IntComputer(inputChannel = inputChannel, outputChannel = outputChannel)

            val tiles = HashMap<Point, Tile>()

            var score: Long
            var running = true
            runBlocking {
                launch {
                    runAsync(c, program)
                    running = false
                    inputChannel.close()
                    outputChannel.close()
                }

                launch {
                    while (running) {
                        val x = outputChannel.receive()
                        val y = -outputChannel.receive()
                        val location = Point(x.toInt(), y.toInt())
                        if (location == Point(-1, 0)) {
                            score = outputChannel.receive()
                            printArcadeMap(tiles)
                            println("Score: $score")
                        } else {
                            tiles[location] = Tile.getTileFromValue(outputChannel.receive())
                        }
                    }
                }

                launch {
                    inputChannel.send(0L)
                    while (running) {
                        delay(20)
                        val ball = tiles.entries.find { it.value == Tile.BALL }?.key
                        val paddle = tiles.entries.find { it.value == Tile.PADDLE }?.key

                        if (paddle != null && ball != null) {
                            val input = when {
                                paddle.x < ball.x -> 1L
                                paddle.x > ball.x -> -1L
                                else -> 0L
                            }
                            inputChannel.send(input)
                        } else {
                            delay(100)
                        }
                    }
                }

            }
            return tiles
        }

        private fun printArcadeMap(map: Map<Point, Tile>) {
            val minX = map.keys.minBy { it.x }?.x
            val maxX = map.keys.maxBy { it.x }?.x

            val minY = map.keys.minBy { it.y }?.y
            val maxY = map.keys.maxBy { it.y }?.y

            val s = StringBuilder()
            for (y in maxY!! downTo minY!!) {
                for (x in minX!!..maxX!!) {
                    s.append(
                        when (map[Point(x, y)]) {
                            Tile.EMPTY -> " "
                            Tile.BLOCK -> "□"
                            Tile.WALL -> "▓"
                            Tile.PADDLE -> "▾"
                            Tile.BALL -> "●"
                            else -> " "
                        }
                    )
                }
                s.append("\n")
            }
            print(s)
        }

        enum class Tile(val i: Int) {
            EMPTY(0),
            WALL(1),
            BLOCK(2),
            PADDLE(3),
            BALL(4);

            companion object {
                fun getTileFromValue(i: Long): Tile {
                    return when (i.toInt()) {
                        0 -> EMPTY
                        1 -> WALL
                        2 -> BLOCK
                        3 -> PADDLE
                        4 -> BALL
                        else -> throw IllegalArgumentException(i.toString())
                    }
                }
            }
        }
    }
}