import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class AOC12Tests {

    private val example = listOf(
        "<x=-1, y=0, z=2>",
        "<x=2, y=-10, z=-7>",
        "<x=4, y=-8, z=8>",
        "<x=3, y=5, z=-1>"
    )
        .map { moon -> moon.replace(Regex("[^0-9,-]"), "").split(",").map { it.toInt() } }
        .map { Point3(it[0], it[1], it[2]) }

    private val example2 = listOf("<x=-8, y=-10, z=0>",
    "<x=5, y=5, z=10>",
    "<x=2, y=-7, z=3>",
    "<x=9, y=-8, z=-3>")
    .map { moon -> moon.replace(Regex("[^0-9,-]"), "").split(",").map { it.toInt() } }
    .map { Point3(it[0], it[1], it[2]) }

    @Test
    fun `Test the example simulation after 1 step`(){
        val system = example.map { Vector3(it, Point3.ORIGIN) }

        val expected = listOf(Vector3(Point3(x= 2, y=-1, z= 1), Point3(x= 3, y=-1, z=-1)),
        Vector3(Point3(x= 3, y=-7, z=-4), Point3(x= 1, y= 3, z= 3)),
        Vector3(Point3(x= 1, y=-7, z= 5), Point3(x=-3, y= 1, z=-3)),
        Vector3(Point3(x= 2, y= 2, z= 0), Point3(x=-1, y=-3, z= 1)))
        
        val actual = AOC12.stepSimulation(system)

        assertIterableEquals(expected, actual)
    }

    @Test
    fun `Test the repeat point for example 1 is 2772`(){
        val expected = 2772;
        val actual = AOC12.part2(example)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test the repeat point for example 2 is 4686774924`(){
        val expected = 4686774924;
        val actual = AOC12.part2(example2)

        assertEquals(expected, actual)
    }
}