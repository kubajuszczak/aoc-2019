import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() {
    val input = getInput("input05.txt").readText().split(",").map { it.toLong() }

    measureTimeMillis {
        AOC05.part1(input)
    }.also { println("${it}ms") }

    measureTimeMillis {
        AOC05.part2(input)
    }.also { println("${it}ms") }
}

class AOC05 {
    companion object {
        fun part1(program: List<Long>) {
            runBlocking {
                val inputChannel = Channel<Long>(Channel.UNLIMITED)
                val outputChannel = Channel<Long>(Channel.UNLIMITED)
                inputChannel.send(1)
                inputChannel.close()

                val computer = IntComputer(inputChannel = inputChannel, outputChannel = outputChannel)
                runAsync(computer, program)
                outputChannel.close()

                for (output in outputChannel) {
                    println("OUTPUT: $output")
                }
            }
        }

        fun part2(program: List<Long>) {
            runBlocking {
                val inputChannel = Channel<Long>(Channel.UNLIMITED)
                val outputChannel = Channel<Long>(Channel.UNLIMITED)
                inputChannel.send(5)

                val computer = IntComputer(inputChannel = inputChannel, outputChannel = outputChannel)
                runAsync(computer, program)
                outputChannel.close()

                for (output in outputChannel) {
                    println("OUTPUT: $output")
                }
            }
        }
    }
}
