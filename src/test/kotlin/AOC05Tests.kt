import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AOC05Tests {

    @Test
    fun `Test computer state after running example program 1`() {
        val program = listOf(1002, 4, 3, 4, 33).map { it.toLong() }

        val expected = listOf(1002, 4, 3, 4, 99).map { it.toLong() }
        val actual = runBlocking { return@runBlocking runAsync(IntComputer(), program) }.memory.slice(expected.indices)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test computer state after running example program 2`() {
        val program = listOf(1101, 100, -1, 4, 0).map { it.toLong() }

        val expected = listOf(1101, 100, -1, 4, 99).map { it.toLong() }
        val actual = runBlocking { return@runBlocking runAsync(IntComputer(), program) }.memory.slice(expected.indices)

        assertEquals(expected, actual)
    }

    @Test
    fun `Test program for equality in position mode - when input is 8, output is 1`() {
        val program = listOf(3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8).map { it.toLong() }

        runBlocking {
            runAsync(IntComputer(inputFunction = { 8 }, outputFunction = { assertEquals(1L, it) }), program)
        }
    }

    @Test
    fun `Test program for equality in position mode - when input is not 8, output is 0`() {
        val program = listOf(3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8).map { it.toLong() }

        runBlocking {
            return@runBlocking runAsync(
                IntComputer(inputFunction = { 7 }, outputFunction = { assertEquals(0L, it) }),
                program
            )
        }
    }

    @Test
    fun `Test program for less than in position mode - when input is less than 8, output is 1`() {
        val program = listOf(3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8).map { it.toLong() }

        runBlocking {
            return@runBlocking runAsync(
                IntComputer(inputFunction = { 7 }, outputFunction = { assertEquals(1L, it) }),
                program
            )
        }
    }

    @Test
    fun `Test program for less than in position mode - when input is not less than 8, output is 0`() {
        val program = listOf(3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8).map { it.toLong() }

        runBlocking {
            return@runBlocking runAsync(
                IntComputer(inputFunction = { 8 }, outputFunction = { assertEquals(0L, it) }),
                program
            )
        }
    }

    @Test
    fun `Test program for equality in immediate mode - when input is 8, output is 1`() {
        val program = listOf(3, 3, 1108, -1, 8, 3, 4, 3, 99).map { it.toLong() }

        runBlocking {
            return@runBlocking runAsync(
                IntComputer(inputFunction = { 8 }, outputFunction = { assertEquals(1L, it) }),
                program
            )
        }
    }

    @Test
    fun `Test program for equality in immediate mode - when input is not 8, output is 0`() {
        val program = listOf(3, 3, 1108, -1, 8, 3, 4, 3, 99).map { it.toLong() }

        runBlocking {
            return@runBlocking runAsync(
                IntComputer(inputFunction = { 7 }, outputFunction = { assertEquals(0L, it) }),
                program
            )
        }
    }

    @Test
    fun `Test program for less than in immediate mode - when input is less than 8, output is 1`() {
        val program = listOf(3, 3, 1107, -1, 8, 3, 4, 3, 99).map { it.toLong() }

        runBlocking {
            return@runBlocking runAsync(
                IntComputer(inputFunction = { 7 }, outputFunction = { assertEquals(1L, it) }),
                program
            )
        }
    }

    @Test
    fun `Test program for less than in immediate mode - when input is not less than 8, output is 0`() {
        val program = listOf(3, 3, 1107, -1, 8, 3, 4, 3, 99).map { it.toLong() }

        runBlocking {
            return@runBlocking runAsync(
                IntComputer(inputFunction = { 8 }, outputFunction = { assertEquals(0L, it) }),
                program
            )
        }
    }

    @Test
    fun `Test program for jump in position mode outputs 0 if input was 0`() {
        val program = listOf(3, 12, 6, 12, 15, 1, 13, 14, 13, 4, 13, 99, -1, 0, 1, 9).map { it.toLong() }

        runBlocking {
            return@runBlocking runAsync(
                IntComputer(inputFunction = { 0 }, outputFunction = { assertEquals(0L, it) }),
                program
            )
        }

    }

    @Test
    fun `Test program for jump in position mode outputs 1 if input was non-zero`() {
        val program = listOf(3, 12, 6, 12, 15, 1, 13, 14, 13, 4, 13, 99, -1, 0, 1, 9).map { it.toLong() }

        runBlocking {
            return@runBlocking runAsync(
                IntComputer(inputFunction = { 7 }, outputFunction = { assertEquals(1L, it) }),
                program
            )
        }

    }

    @Test
    fun `Test program for jump in immediate mode outputs 0 if input was 0`() {
        val program = listOf(3, 3, 1105, -1, 9, 1101, 0, 0, 12, 4, 12, 99, 1).map { it.toLong() }

        runBlocking {
            return@runBlocking runAsync(
                IntComputer(inputFunction = { 0 }, outputFunction = { assertEquals(0L, it) }),
                program
            )
        }
    }

    @Test
    fun `Test program for jump in immediate mode outputs 1 if input was non-zero`() {
        val program = listOf(3, 3, 1105, -1, 9, 1101, 0, 0, 12, 4, 12, 99, 1).map { it.toLong() }

        runBlocking {
            return@runBlocking runAsync(
                IntComputer(inputFunction = { 7 }, outputFunction = { assertEquals(1L, it) }),
                program
            )
        }
    }

    @Test
    fun `Test program outputs 999 when input is less than 8`() {
        val program = listOf(
            3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107, 8, 21, 20, 1006, 20, 31,
            1106, 0, 36, 98, 0, 0, 1002, 21, 125, 20, 4, 20, 1105, 1, 46, 104,
            999, 1105, 1, 46, 1101, 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99
        ).map { it.toLong() }

        runBlocking {
            return@runBlocking runAsync(
                IntComputer(inputFunction = { 7 }, outputFunction = { assertEquals(999L, it) }),
                program
            )
        }
    }

    @Test
    fun `Test program outputs 1000 when input is equal to 8`() {
        val program = listOf(
            3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107, 8, 21, 20, 1006, 20, 31,
            1106, 0, 36, 98, 0, 0, 1002, 21, 125, 20, 4, 20, 1105, 1, 46, 104,
            999, 1105, 1, 46, 1101, 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99
        ).map { it.toLong() }

        runBlocking {
            return@runBlocking runAsync(
                IntComputer(
                    inputFunction = { 8 },
                    outputFunction = { assertEquals(1000L, it) }), program
            )
        }
    }

    @Test
    fun `Test program outputs 1001 when input is greater than 8`() {
        val program = listOf(
            3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107, 8, 21, 20, 1006, 20, 31,
            1106, 0, 36, 98, 0, 0, 1002, 21, 125, 20, 4, 20, 1105, 1, 46, 104,
            999, 1105, 1, 46, 1101, 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99
        ).map { it.toLong() }

        runBlocking {
            return@runBlocking runAsync(
                IntComputer(
                    inputFunction = { 9 },
                    outputFunction = { assertEquals(1001L, it) }), program
            )
        }
    }

}