import AOC04.Companion.hasAGroupOfExactlyTwoAdjacentSameDigits
import AOC04.Companion.hasNonDecreasingDigits
import AOC04.Companion.hasTwoAdjacentSameDigits
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AOC04Tests {

    @Test
    fun `Test 111111 fits part 1 criteria`(){
        assertEquals(true, hasNonDecreasingDigits("111111"))
        assertEquals(true, hasTwoAdjacentSameDigits("111111"))
    }

    @Test
    fun `Test 223450 does not fit part 1 criteria`(){
        assertEquals(false, hasNonDecreasingDigits("223450"))
        assertEquals(true, hasTwoAdjacentSameDigits("223450"))
    }

    @Test
    fun `Test 123789 does not fit part 1 criteria`(){
        assertEquals(true, hasNonDecreasingDigits("123789"))
        assertEquals(false, hasTwoAdjacentSameDigits("123789"))
    }

    @Test
    fun `Test 112233 fits part 2 criteria`(){
        assertEquals(true, hasNonDecreasingDigits("112233"))
        assertEquals(true, hasAGroupOfExactlyTwoAdjacentSameDigits("112233"))
    }

    @Test
    fun `Test 123444 does not fit part 2 criteria`(){
        assertEquals(true, hasNonDecreasingDigits("123444"))
        assertEquals(false, hasAGroupOfExactlyTwoAdjacentSameDigits("123444"))
    }

    @Test
    fun `Test 111122 fits part 2 criteria`(){
        assertEquals(true, hasNonDecreasingDigits("111122"))
        assertEquals(true, hasAGroupOfExactlyTwoAdjacentSameDigits("111122"))
    }
}