import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AOC16Tests {

    @Test
    fun `Check that after 100 FFT rounds on 80871224585914546619083218645595 the first 8 digits are 24176176`() {
        val expected = "24176176"
        val actual = AOC16.part1("80871224585914546619083218645595")
        assertEquals(expected, actual)
    }

    @Test
    fun `Check that after 100 FFT rounds on 19617804207202209144916044189917 the first 8 digits are 73745418`() {
        val expected = "73745418"
        val actual = AOC16.part1("19617804207202209144916044189917")
        assertEquals(expected, actual)
    }

    @Test
    fun `Check that after 100 FFT rounds on 69317163492948606335995924319873 the first 8 digits are 52432133`() {
        val expected = "52432133"
        val actual = AOC16.part1("69317163492948606335995924319873")
        assertEquals(expected, actual)
    }

    @Test
    fun `Check that after 100 FFT rounds on 03036732577212944063491565474664 x10000 the result is 84462026`() {
        val expected = "84462026"
        val actual = AOC16.part2("03036732577212944063491565474664")
        assertEquals(expected, actual)
    }

    @Test
    fun `Check that after 100 FFT rounds on 02935109699940807407585447034323 x10000 the result is 78725270`() {
        val expected = "78725270"
        val actual = AOC16.part2("02935109699940807407585447034323")
        assertEquals(expected, actual)
    }

    @Test
    fun `Check that after 100 FFT rounds on 03081770884921959731165446850517 x10000 the result is 53553731`() {
        val expected = "53553731"
        val actual = AOC16.part2("03081770884921959731165446850517")
        assertEquals(expected, actual)
    }
}