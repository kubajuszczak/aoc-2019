import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() {
    val input = getInput("input09.txt").readText().split(",").map { it.toLong() }

    measureTimeMillis {
        part1(input)
    }.also { println("${it}ms") }

    measureTimeMillis {
        part2(input)
    }.also { println("${it}ms") }
}

private fun part1(program: List<Long>) {
    val inputChannel = Channel<Long>(Channel.UNLIMITED)
    val outputChannel = Channel<Long>(Channel.UNLIMITED)
    val c = IntComputer(inputChannel = inputChannel, outputChannel = outputChannel)

    runBlocking {
        inputChannel.send(1L)
        runAsync(c, program)
        outputChannel.close()

        for (output in outputChannel) {
            println("OUTPUT: $output")
        }
    }
}

private fun part2(program: List<Long>) {
    val inputChannel = Channel<Long>(Channel.UNLIMITED)
    val outputChannel = Channel<Long>(Channel.UNLIMITED)
    val c = IntComputer(inputChannel = inputChannel, outputChannel = outputChannel)

    runBlocking {
        inputChannel.send(2L)
        runAsync(c, program)
        outputChannel.close()

        for (output in outputChannel) {
            println("OUTPUT: $output")
        }
    }
}