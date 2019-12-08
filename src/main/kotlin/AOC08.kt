import java.lang.IllegalArgumentException
import kotlin.system.measureTimeMillis

fun main() {
    val input = getInput("input08.txt").readText()

    measureTimeMillis {
        println(part1(input))
    }.also { println("${it}ms") }

    measureTimeMillis {
        println(part2(input))
    }.also { println("${it}ms") }

}

private fun part1(input: String): Int {
    val checksumLayer = input
        .chunked(25 * 6)
        .minBy { layer -> layer.count { it == '0' } }

    return checksumLayer!!.count { it == '1' } * checksumLayer.count { it == '2' }
}

private fun part2(input: String): String {
    val image = input
        .chunked(25 * 6)
        .reduce { upperLayer, lowerLayer ->
            upperLayer.zip(lowerLayer).map {
                when (it) {
                    Pair('0', '0') -> '0'
                    Pair('0', '1') -> '0'
                    Pair('0', '2') -> '0'
                    Pair('1', '0') -> '1'
                    Pair('1', '1') -> '1'
                    Pair('1', '2') -> '1'
                    Pair('2', '0') -> '0'
                    Pair('2', '1') -> '1'
                    Pair('2', '2') -> '2'
                    else -> throw IllegalArgumentException()
                }
            }.joinToString(separator = "")
        }
        .chunked(25)

    return image.toString().replace(",", "\n").replace("0", " ").replace("1", "█")

}