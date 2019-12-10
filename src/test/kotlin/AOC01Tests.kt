import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AOC01Tests {

    @Test
    fun `Test module of mass 12 requires 2 units of fuel`() {
        assertEquals(2, AOC01.getFuel(12))
    }

    @Test
    fun `Test module of mass 14 still requires 2 units of fuel`() {
        assertEquals(2, AOC01.getFuel(14))
    }

    @Test
    fun `Test module of mass 1969 requires 654 units of fuel`() {
        assertEquals(654, AOC01.getFuel(1969))
    }

    @Test
    fun `Test module of mass 100756 requires 33583 units of fuel`() {
        assertEquals(33583, AOC01.getFuel(100756))
    }

    @Test
    fun `Test module of mass 14 and its fuel requires 2 units of fuel`() {
        assertEquals(2, AOC01.getRecursiveFuel(14))
    }

    @Test
    fun `Test module of mass 1969 and its fuel requires 966 units of fuel`() {
        assertEquals(966, AOC01.getRecursiveFuel(1969))
    }

    @Test
    fun `Test module of mass 100756 and its fuel requires 50346 units of fuel`() {
        assertEquals(50346, AOC01.getRecursiveFuel(100756))
    }

}