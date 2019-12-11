import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AOC02Tests {

    @Test
    fun `Test computer state after running example program 1`() {
        val program = listOf(1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50).map { it.toLong() }

        val expected = listOf(3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50).map { it.toLong() }
        val actual = runBlocking { return@runBlocking runAsync(IntComputer(), program) }.memory.slice(expected.indices)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test computer state after running example program 2`() {
        val program = listOf(1, 0, 0, 0, 99).map { it.toLong() }

        val expected = listOf(2, 0, 0, 0, 99).map { it.toLong() }
        val actual = runBlocking { return@runBlocking runAsync(IntComputer(), program) }.memory.slice(expected.indices)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test computer state after running example program 3`() {
        val program = listOf(2, 3, 0, 3, 99).map { it.toLong() }

        val expected = listOf(2, 3, 0, 6, 99).map { it.toLong() }
        val actual = runBlocking { return@runBlocking runAsync(IntComputer(), program) }.memory.slice(expected.indices)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test computer state after running example program 4`() {
        val program = listOf(2, 4, 4, 5, 99, 0).map { it.toLong() }

        val expected = listOf(2, 4, 4, 5, 99, 9801).map { it.toLong() }
        val actual = runBlocking { return@runBlocking runAsync(IntComputer(), program) }.memory.slice(expected.indices)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test computer state after running example program 5`() {
        val program = listOf(1, 1, 1, 4, 99, 5, 6, 0, 99).map { it.toLong() }

        val expected = listOf(30, 1, 1, 4, 2, 5, 6, 0, 99).map { it.toLong() }
        val actual = runBlocking { return@runBlocking runAsync(IntComputer(), program) }.memory.slice(expected.indices)

        assertEquals(expected, actual)
    }

}