fun main() {

    val part1 = (240298..784956)
        .filter { hasNonDecreasingDigits(it.toString()) }
        .filter { hasTwoAdjacentSameDigits(it.toString()) }
        .count()
    println(part1)

    val part2 = (240298..784956)
        .filter { hasNonDecreasingDigits(it.toString()) }
        .filter { hasAGroupOfExactlyTwoAdjacentSameDigits(it.toString()) }
        .count()
    println(part2)

}

fun hasNonDecreasingDigits(password: String): Boolean {
    for (index in 1 until password.length) {
        if (password[index].toInt() < password[index - 1].toInt()) {
            return false
        }
    }
    return true
}

fun hasTwoAdjacentSameDigits(password: String): Boolean {
    for (index in 1 until password.length) {
        if (password[index].toInt() == password[index - 1].toInt()) {
            return true
        }
    }
    return false
}

// run-length encoding derived
fun hasAGroupOfExactlyTwoAdjacentSameDigits(password: String): Boolean {
    var currentRun = 1
    var rle = ""

    for (index in 1 until password.length) {
        if (password[index] == password[index - 1]) {
            currentRun++
        } else {
            rle += currentRun
            currentRun = 1
        }
        if (index == password.length - 1) {
            rle += currentRun
        }
    }

    return rle.contains('2')
}