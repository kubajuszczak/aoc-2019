fun main() {
    val result = getInput("input01.txt").readLines()
        .map { getFuel(it.toInt()) }
        .reduce { a, b -> a + b }
    println(result)

    val recursive = getInput("input01.txt").readLines()
        .map { getRecursiveFuel(it.toInt()) }
        .reduce { a, b -> a + b }
    println(recursive)
}

fun getFuel(mass: Int): Int {
    return mass / 3 - 2
}

fun getRecursiveFuel(mass: Int): Int {
    val x = getFuel(mass)
    return if(x<=0){
        0
    } else {
        getRecursiveFuel(x) + x
    }
}
