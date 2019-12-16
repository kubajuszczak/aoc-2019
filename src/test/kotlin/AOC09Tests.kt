import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AOC09Tests {
    @Test
    fun `Test program outputs itself`() {
        val program = listOf(
            109, 1, 204, -1, 1001, 100, 1, 100, 1008, 100, 16, 101, 1006, 101, 0, 99
        ).map { it.toLong() }

        val output = ArrayList<Long>()

        runBlocking {
            return@runBlocking runAsync(IntComputer(outputFunction = { output.add(it) }), program)
        }

        assertEquals(program, output)
    }

    @Test
    fun `Test program 1 for large numbers`() {
        val program = listOf(
            1102, 34915192, 34915192, 7, 4, 7, 99, 0
        ).map { it.toLong() }

        runBlocking {
            return@runBlocking runAsync(
                IntComputer(outputFunction = { assertEquals(34915192L * 34915192L, it) }),
                program
            )
        }
        runBlocking {

        }
    }

    @Test
    fun `Test program 2 for large numbers`() {
        val program = listOf(104L, 1125899906842624L, 99L)

        runBlocking {
            return@runBlocking runAsync(IntComputer(outputFunction = { assertEquals(1125899906842624L, it) }), program)
        }

    }
}