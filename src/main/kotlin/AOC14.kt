import kotlin.math.ceil
import kotlin.math.min
import kotlin.system.measureTimeMillis

fun main() {
    val input = getInput("input14.txt").readLines()

    measureTimeMillis {
        println(AOC14.part1(input))
    }.also { println("${it}ms") }

//    measureTimeMillis {
//        AOC14.part2(input)
//    }.also { println("${it}ms") }

}

class AOC14 {
    companion object {
        fun part1(input: List<String>): Int {
            val reactions = input
                .map { it.split(" => ") }
                .map { reaction ->
                    val reagents = Regex("(\\d+) ([A-Z]+)").findAll(reaction[0])
                        .map { val (amount, chem) = it.destructured; Reagent(chem, amount.toInt()) }.toList()
                    val output = Regex("(\\d+) ([A-Z]+)").find(reaction[1])
                        .let { val (amount, chem) = it!!.destructured; Reagent(chem, amount.toInt()) }
                    Reaction(reagents, output)
                }

            val requirements =
                getOreRequirements(listOf(Reagent("FUEL", 1)), emptyList(), reactions).also { println(it) }

            return requirements.filter { it.chem == "ORE" }.sumBy { it.amount }
        }

        fun part2(reactions: List<String>) {

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

                    if (amountRequiredFromReaction == 0) {
                        return@flatMap emptyList<Reagent>()
                    }

                    val multiplier =
                        ceil(amountRequiredFromReaction.toDouble() / reaction.output.amount.toDouble()).toInt()

                    val outputRemainder = reaction.output.amount * multiplier - amountRequiredFromReaction
                    currentRemainders[requirement.chem] =
                        currentRemainders[requirement.chem]?.plus(outputRemainder) ?: outputRemainder


                    reaction.reagents.map { it * multiplier }
                        .map { reagent ->
                            if (currentRemainders.containsKey(reagent.chem)) {
                                val useFromRemainders = currentRemainders[reagent.chem]?.let { min(reagent.amount, it) }
                                    .also { currentRemainders[reagent.chem] = currentRemainders[reagent.chem]!! - it!! } ?: 0
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
                    currentRemainders.map { Reagent(it.key, it.value) }.filter { it.amount>0 },
                    reactions
                )
            }
        }
    }
}

data class Reaction(val reagents: List<Reagent>, val output: Reagent)
data class Reagent(val chem: String, val amount: Int) {
    operator fun times(n: Int): Reagent {
        return copy(amount = amount * n)
    }
}