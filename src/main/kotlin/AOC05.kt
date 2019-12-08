import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() {
    val input = getInput("input05.txt").readText().split(",").map { it.toInt() }

    measureTimeMillis {
        part1(input)
    }.also { println("${it}ms") }

    measureTimeMillis {
        part2(input)
    }.also { println("${it}ms") }
}

private fun part1(program: List<Int>) {
    runBlocking {
        val inputChannel = Channel<Int>(Channel.UNLIMITED)
        val outputChannel = Channel<Int>(Channel.UNLIMITED)
        inputChannel.send(1)
        inputChannel.close()

        val computer = IntComputer(program, inputChannel, outputChannel)
        runSync(computer)
        outputChannel.close()

        for (output in outputChannel){
            println("OUTPUT: $output")
        }
    }
}

private fun part2(program: List<Int>) {
    runBlocking {
        val inputChannel = Channel<Int>(Channel.UNLIMITED)
        val outputChannel = Channel<Int>(Channel.UNLIMITED)
        inputChannel.send(5)

        val computer = IntComputer(program, inputChannel, outputChannel)
        runSync(computer)
        outputChannel.close()

        for (output in outputChannel){
            println("OUTPUT: $output")
        }
    }
}