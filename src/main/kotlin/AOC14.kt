import kotlin.math.ceil
import kotlin.math.min
import kotlin.system.measureTimeMillis

fun main() {
    val input = getInput("input14.txt").readLines()

    measureTimeMillis {
        println(AOC14.part1(input))
    }.also { println("${it}ms") }

    measureTimeMillis {
        AOC14.part2(input)
    }.also { println("${it}ms") }

}

class AOC14 {
    companion object {
        private fun getReactionList(input: List<String>): List<Reaction> {
            return input
                .map { it.split(" => ") }
                .map { reaction ->
                    val reagents = Regex("(\\d+) ([A-Z]+)").findAll(reaction[0])
                        .map { val (amount, chem) = it.destructured; Reagent(chem, amount.toLong()) }.toList()
                    val output = Regex("(\\d+) ([A-Z]+)").find(reaction[1])
                        .let { val (amount, chem) = it!!.destructured; Reagent(chem, amount.toLong()) }
                    Reaction(reagents, output)
                }
        }

        fun part1(input: List<String>): Long {
            val reactions = getReactionList(input)

            val requirements =
                getOreRequirements(listOf(Reagent("FUEL", 1)), emptyList(), reactions)

            return requirements.filter { it.chem == "ORE" }.map { it.amount }.reduce { acc, l -> acc + l }
        }

        fun part2(input: List<String>) {
            val reactions = getReactionList(input)

            var fuel = 3755000L
            while (true) {
                fuel++
                val requirements =
                    getOreRequirements(listOf(Reagent("FUEL", fuel)), emptyList(), reactions)

                val ore = requirements.filter { it.chem == "ORE" }.map { it.amount }.reduce { acc, l -> acc + l }
                if (ore >= 1_000_000_000_000) {
                    println("$ore ore->$fuel fuel")
                    break
                }
            }

            val requirements =
                getOreRequirements(listOf(Reagent("FUEL", fuel - 1)), emptyList(), reactions)

            val ore = requirements.filter { it.chem == "ORE" }.map { it.amount }.reduce { acc, l -> acc + l }
            println("$ore ore->${fuel - 1} fuel")
        }

        private fun getOreRequirements(
            reagents: List<Reagent>,
            remainders: List<Reagent>,
            reactions: List<Reaction>
        ): List<Reagent> {
            val currentRemainders = remainders.associate { Pair(it.chem, it.amount) }.toMutableMap()

            val requirements = reagents.filter { it.chem != "ORE" }
                .flatMap { requirement ->
                    val outputReagentRemainder = currentRemainders[requirement.chem] ?: 0
                    val reaction = reactions.find { it.output.chem == requirement.chem }!!

                    val amountRequiredFromReaction =
                        requirement.amount - min(requirement.amount, outputReagentRemainder).also {
                            currentRemainders[requirement.chem] = outputReagentRemainder - it
                        }

                    if (amountRequiredFromReaction == 0L) {
                        return@flatMap emptyList<Reagent>()
                    }

                    val multiplier =
                        ceil(amountRequiredFromReaction.toDouble() / reaction.output.amount.toDouble()).toLong()

                    val outputRemainder = reaction.output.amount * multiplier - amountRequiredFromReaction
                    currentRemainders[requirement.chem] =
                        currentRemainders[requirement.chem]?.plus(outputRemainder) ?: outputRemainder


                    reaction.reagents.map { it * multiplier }
                        .map { reagent ->
                            if (currentRemainders.containsKey(reagent.chem)) {
                                val useFromRemainders = currentRemainders[reagent.chem]?.let { min(reagent.amount, it) }
                                    .also { currentRemainders[reagent.chem] = currentRemainders[reagent.chem]!! - it!! }
                                    ?: 0
                                reagent.copy(amount = reagent.amount - useFromRemainders)
                            } else {
                                reagent
                            }
                        }
                }


            return if (requirements.isEmpty()) {
                reagents
            } else {
                reagents.filter { it.chem == "ORE" } + getOreRequirements(
                    requirements,
                    currentRemainders.map { Reagent(it.key, it.value) }.filter { it.amount > 0 },
                    reactions
                )
            }
        }
    }
}

data class Reaction(val reagents: List<Reagent>, val output: Reagent)
data class Reagent(val chem: String, val amount: Long) {
    operator fun times(n: Long): Reagent {
        return copy(amount = amount * n)
    }
}