import kotlinx.coroutines.runBlocking
import kotlin.math.abs
import kotlin.system.measureTimeMillis

fun main() {
    val input = getInput("input17.txt").readText().split(",").map { it.toLong() }

    measureTimeMillis {
        println(AOC17.part1(input))
    }.also { println("${it}ms") }

    measureTimeMillis {
        AOC17.part2(input)
    }.also { println("${it}ms") }

}

class AOC17 {
    companion object {
        fun part1(input: List<Long>): Int {
            val pointMap = getMap(input)
            val directions = listOf(Point.ORIGIN, Point.UP, Point.DOWN, Point.LEFT, Point.RIGHT)
            return pointMap.filter { point -> directions.map { point.key + it }.all { pointMap[it] == '#' } }
                .map {
                    abs(it.key.x * it.key.y)
                }.sum()
        }

        fun part2(program: List<Long>) {

            val map = getMap(program).filter { it.value != '.' }
            val start: Point = map.entries.find { it.value in listOf('^', '>', '<', 'v') }?.key!!
            val movement = ArrayList<String>()
            var direction = when (map[start]) {
                '^' -> Point.UP
                '>' -> Point.RIGHT
                '<' -> Point.LEFT
                'v' -> Point.DOWN
                else -> throw Exception()
            }

            var location = start
            var run = 0

            while (true) {
                val forward = map.containsKey(location + direction)
                val left = map.containsKey(location + direction.turnLeft())
                val right = map.containsKey(location + direction.turnRight())

                if (!(forward || left || right)) {
                    // if no points ahead or either side: we're done.
                    movement.add(run.toString())
                    break;
                }
                // if there's points ahead, go.
                if (forward) {
                    run += 1
                    location += direction
                } else {
                    // else: change direction to where there's points
                    when {
                        left -> {
                            if (run > 0) {
                                movement.add(run.toString())
                                run = 0
                            }
                            movement.add("L")
                            direction = direction.turnLeft()
                        }
                        right -> {
                            if (run > 0) {
                                movement.add(run.toString())
                                run = 0
                            }
                            movement.add("R")
                            direction = direction.turnRight()
                        }
                    }
                }
            }

//            println(movement)
            // outputs..

//         A  R, 4, L, 12, L, 8, R, 4,
//         B  L, 8, R, 10, R, 10, R, 6,
//         A  R, 4, L, 12, L, 8, R, 4,
//         C  R, 4, R, 10, L, 12,
//         A  R, 4, L, 12, L, 8, R, 4,
//         B  L, 8, R, 10, R, 10, R, 6,
//         A  R, 4, L, 12, L, 8, R, 4,
//         C  R, 4, R, 10, L, 12,
//         B  L, 8, R, 10, R, 10, R, 6,
//         C  R, 4, R, 10, L, 12

            val main = "A,B,A,C,A,B,A,C,B,C"
            val a = "R,4,L,12,L,8,R,4"
            val b = "L,8,R,10,R,10,R,6"
            val c = "R,4,R,10,L,12"
            val input = "$main\n$a\n$b\n$c\nn\n".iterator()

            val computer = IntComputer(inputFunction = {input.next().toLong()}, outputFunction = { println("OUTPUT: $it") })
            runBlocking { runAsync(computer, listOf(2L) + program.drop(1)) }

        }

        private fun getMap(program: List<Long>): HashMap<Point, Char> {
            val list = ArrayList<Char>()
            val c = IntComputer(outputFunction = { list.add(it.toChar()) })
            runBlocking { runAsync(c, program) }

            val mapString = list.joinToString("").also { println(it) }
            val pointMap = HashMap<Point, Char>()
            for ((y, line) in mapString.lines().withIndex()) {
                for ((x, value) in line.withIndex()) {
                    pointMap[Point(x, -y)] = value
                }
            }

            return pointMap
        }
    }
}