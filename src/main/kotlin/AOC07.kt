import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() {
    val input = getInput("input07.txt").readText().split(",").map { it.toInt() }

    measureTimeMillis {
        println(part1(input))
    }.also { println("${it}ms") }

    measureTimeMillis {
        println(part2(input))
    }.also { println("${it}ms") }
}

private fun part1(program: List<Int>): Int? {

    val outputs = ArrayList<Int>()

    for (permutation in permute(listOf(0, 1, 2, 3, 4))) {
        val aInputs = listOf(permutation[0], 0)
        val bInputs = listOf(permutation[1]).toMutableList()
        val cInputs = listOf(permutation[2]).toMutableList()
        val dInputs = listOf(permutation[3]).toMutableList()
        val eInputs = listOf(permutation[4]).toMutableList()

        val a = aInputs.iterator()
        val ampA = IntComputer(program, { a.next() }, { bInputs.add(it) })
        runSync(ampA)

        val b = bInputs.iterator()
        val ampB = IntComputer(program, { b.next() }, { cInputs.add(it) })
        runSync(ampB)

        val c = cInputs.iterator()
        val ampC = IntComputer(program, { c.next() }, { dInputs.add(it) })
        runSync(ampC)

        val d = dInputs.iterator()
        val ampD = IntComputer(program, { d.next() }, { eInputs.add(it) })
        runSync(ampD)

        val e = eInputs.iterator()
        val ampE = IntComputer(program, { e.next() }, { outputs.add(it) })
        runSync(ampE)
    }

    return outputs.max()
}

private fun part2(program: List<Int>): Int? {
    val outputs = ArrayList<Int>()

    for (permutation in permute(listOf(5, 6, 7, 8, 9))) {
        val a = listOf(permutation[0], 0)
        val b = listOf(permutation[1])
        val c = listOf(permutation[2])
        val d = listOf(permutation[3])
        val e = listOf(permutation[4])

        val channelAB = Channel<Int>()
        val channelBC = Channel<Int>()
        val channelCD = Channel<Int>()
        val channelDE = Channel<Int>()
        val channelEA = Channel<Int>(1) // buffer of 1 to let the IntComputer finish, then we can read it at the end

        runBlocking {
            launch {
                val ampA = IntComputer(program, input(a.iterator(), channelEA), output(channelAB))
                runAsync(ampA)
            }
            launch {
                val ampB = IntComputer(program, input(b.iterator(), channelAB), output(channelBC))
                runAsync(ampB)
            }
            launch {
                val ampC = IntComputer(program, input(c.iterator(), channelBC), output(channelCD))
                runAsync(ampC)
            }
            launch {
                val ampD = IntComputer(program, input(d.iterator(), channelCD), output(channelDE))
                runAsync(ampD)
            }
            launch {
                val ampE = IntComputer(program, input(e.iterator(), channelDE), output(channelEA))
                runAsync(ampE)
            }
        }
        runBlocking {
            outputs.add(channelEA.receive())
        }

        channelAB.close()
        channelBC.close()
        channelCD.close()
        channelDE.close()
        channelEA.close()
    }

    return outputs.max()
}

fun input(iterator: Iterator<Int>, channel: Channel<Int>): suspend () -> Int {
    return suspend {
        if (iterator.hasNext()) {
            iterator.next()
        } else {
            channel.receive()
        }
    }
}

fun output(channel: Channel<Int>): suspend (Int) -> Unit {
    return { x -> channel.send(x) }
}

// https://rosettacode.org/wiki/Permutations
fun <T> permute(input: List<T>): List<List<T>> {
    if (input.size == 1) return listOf(input)
    val perms = mutableListOf<List<T>>()
    val toInsert = input[0]
    for (perm in permute(input.drop(1))) {
        for (i in 0..perm.size) {
            val newPerm = perm.toMutableList()
            newPerm.add(i, toInsert)
            perms.add(newPerm)
        }
    }
    return perms
}