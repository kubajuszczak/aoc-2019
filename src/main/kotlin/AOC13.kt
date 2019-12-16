import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() {
    val input = getInput("input13.txt").readText().split(",").map { it.toLong() }

    measureTimeMillis {
        println(AOC13.part1(input))
    }.also { println("${it}ms") }

    measureTimeMillis {
        println(AOC13.part2(input))
    }.also { println("${it}ms") }

}

class AOC13 {
    companion object {
        fun part1(program: List<Long>): Int {
            return runArcadeMachine(program).tiles
                .also { printArcadeMap(it) }
                .filter { it.value == Tile.BLOCK }.keys.size
        }

        fun part2(program: List<Long>): Long {
            return runArcadeMachine(listOf(2L) + program.drop(1)).score
        }

        data class GameState(
            val x: Long,
            val y: Long,
            val value: Long,
            val score: Long,
            val tiles: Map<Point, Tile>
        )

        private fun runArcadeMachine(program: List<Long>): GameState {
            var ballLocation = Point(0, 0)
            var paddleLocation = Point(0, 0)

            var state = GameState(0, 0, 0, 0, emptyMap())
            var outputMode = 0

            val outputFunction: suspend (Long) -> Unit = {
                when (outputMode % 3) {
                    0 -> state = state.copy(x = it)
                    1 -> state = state.copy(y = -it)
                    2 -> {
                        val location = Point(state.x.toInt(), state.y.toInt())
                        if (location == Point(-1, 0)) {
                            state = state.copy(score = it)
                        } else {
                            val tile = Tile.getTileFromValue(it)
                            val newTiles = state.tiles.toMutableMap().apply { this[location] = tile }

                            if (tile == Tile.BALL) ballLocation = location
                            else if (tile == Tile.PADDLE) paddleLocation = location

                            state = state.copy(tiles = newTiles)
                        }
                    }
                }
                outputMode++
            }

            val inputFunction: suspend () -> Long = {
                getInput(paddleLocation, ballLocation)
            }

            val c = IntComputer(inputFunction = inputFunction, outputFunction = outputFunction)

            runBlocking {
                runAsync(c, program)
            }

            return state
        }

        private fun getInput(paddle: Point, ball: Point): Long {
            return when {
                paddle.x < ball.x -> 1L
                paddle.x > ball.x -> -1L
                else -> 0L
            }
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

        enum class Tile {
            EMPTY,
            WALL,
            BLOCK,
            PADDLE,
            BALL;

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