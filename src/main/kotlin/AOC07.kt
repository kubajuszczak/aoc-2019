import kotlin.system.measureTimeMillis

fun main() {
    val input = getInput("input07.txt").readText().split(",").map { it.toInt() }

    measureTimeMillis {
        println(part1(input))
    }.also { println("${it}ms") }

//    measureTimeMillis {
//        println(part2(input))
//    }.also { println("${it}ms") }
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
        val ampA = IntComputer(0, program, { a.next() }, { bInputs.add(it) })
        runProgram(ampA)

        val b = bInputs.iterator()
        val ampB = IntComputer(0, program, { b.next() }, { cInputs.add(it) })
        runProgram(ampB)

        val c = cInputs.iterator()
        val ampC = IntComputer(0, program, { c.next() }, { dInputs.add(it) })
        runProgram(ampC)

        val d = dInputs.iterator()
        val ampD = IntComputer(0, program, { d.next() }, { eInputs.add(it) })
        runProgram(ampD)

        val e = eInputs.iterator()
        val ampE = IntComputer(0, program, { e.next() }, { outputs.add(it) })
        runProgram(ampE)
    }

    return outputs.max()
}

private fun part2(program: List<Int>) {
    val computer = IntComputer(0, program, { 5 })
    runProgram(computer)
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