import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AOC03Tests {
    @Test
    fun `Test that in example 1 the closest intersection is 6 units from the origin`() {
        val lines1 = AOC03.getWireLines("R8,U5,L5,D3")
        val lines2 = AOC03.getWireLines("U7,R6,D4,L4")

        assertEquals(6, AOC03.part1(lines1, lines2))
    }

    @Test
    fun `Test that in example 2 the closest intersection is 159 units from the origin`() {
        val lines1 = AOC03.getWireLines("R75,D30,R83,U83,L12,D49,R71,U7,L72")
        val lines2 = AOC03.getWireLines("U62,R66,U55,R34,D71,R55,D58,R83")

        assertEquals(159, AOC03.part1(lines1, lines2))
    }

    @Test
    fun `Test that in example 3 the closest intersection is 135 units from the origin`() {
        val lines1 = AOC03.getWireLines("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51")
        val lines2 = AOC03.getWireLines("U98,R91,D20,R16,D67,R40,U7,R15,U6,R7")

        assertEquals(135, AOC03.part1(lines1, lines2))
    }

    @Test
    fun `Test that in example 1 the closest intersection is 30 steps from the origin`() {
        val lines1 = AOC03.getWireLines("R8,U5,L5,D3")
        val lines2 = AOC03.getWireLines("U7,R6,D4,L4")

        assertEquals(30, AOC03.part2(lines1, lines2))
    }

    @Test
    fun `Test that in example 2 the closest intersection is 610 units from the origin`() {
        val lines1 = AOC03.getWireLines("R75,D30,R83,U83,L12,D49,R71,U7,L72")
        val lines2 = AOC03.getWireLines("U62,R66,U55,R34,D71,R55,D58,R83")

        assertEquals(610, AOC03.part2(lines1, lines2))
    }

    @Test
    fun `Test that in example 3 the closest intersection is 410 units from the origin`() {
        val lines1 = AOC03.getWireLines("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51")
        val lines2 = AOC03.getWireLines("U98,R91,D20,R16,D67,R40,U7,R15,U6,R7")

        assertEquals(410, AOC03.part2(lines1, lines2))
    }
}