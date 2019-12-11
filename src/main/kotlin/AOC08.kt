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
                when (it.first) {
                    '2' -> it.second
                    else -> it.first
                }
            }.joinToString(separator = "")
        }
        .chunked(25)

    return image.toString().replace(",", "\n").replace("0", " ").replace("1", "▓")

}