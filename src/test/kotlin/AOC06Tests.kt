import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AOC06Tests {

    @Test
    fun `Test part 1 example has 42 total orbits`() {
        val input = listOf(
            "COM)B",
            "B)C",
            "C)D",
            "D)E",
            "E)F",
            "B)G",
            "G)H",
            "D)I",
            "E)J",
            "J)K",
            "K)L"
        ).groupBy({ it.split(")")[0] }, { it.split(")")[1] })

        assertEquals(42, AOC06.part1(input))
    }

    @Test
    fun `Test part 2 example has 4 orbital jumps from YOU to SAN`() {
        val input = listOf(
            "COM)B",
            "B)C",
            "C)D",
            "D)E",
            "E)F",
            "B)G",
            "G)H",
            "D)I",
            "E)J",
            "J)K",
            "K)L",
            "K)YOU",
            "I)SAN"
        ).groupBy({ it.split(")")[0] }, { it.split(")")[1] })
        assertEquals(4, AOC06.part2(input))
    }
}