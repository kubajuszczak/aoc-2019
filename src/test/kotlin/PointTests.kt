import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.math.PI

class PointTests {

    @Test
    fun `Test getBearing from origin to 0,1 returns bearing 0`() {
        Assertions.assertEquals(0.0, Point(0, 0).getBearing(Point(0, 1)))
    }

    @Test
    fun `Test getBearing from origin to 1,0 returns bearing 90`() {
        Assertions.assertEquals(90.0, Point(0, 0).getBearing(Point(1, 0)))
    }

    @Test
    fun `Test getBearing from origin to 0,-1 returns bearing 180`() {
        Assertions.assertEquals(180.0, Point(0, 0).getBearing(Point(0, -1)))
    }

    @Test
    fun `Test getBearing from origin to -1,0 returns bearing 270`() {
        Assertions.assertEquals(270.0, Point(0, 0).getBearing(Point(-1, 0)))
    }

    @Test
    fun `Test getPolarAngle from origin to 1,0 returns 0`() {
        Assertions.assertEquals(0.0, Point(0, 0).getPolarAngle(Point(1, 0)))
    }

    @Test
    fun `Test getPolarAngle from origin to 0,1 returns pi over 2`() {
        Assertions.assertEquals(PI / 2, Point(0, 0).getPolarAngle(Point(0, 1)))
    }

    @Test
    fun `Test getPolarAngle from origin to -1,0 returns pi`() {
        Assertions.assertEquals(PI, Point(0, 0).getPolarAngle(Point(-1, 0)))
    }

    @Test
    fun `Test getPolarAngle from origin to 0,-1 returns 3 pi over 2`() {
        Assertions.assertEquals(3 * PI / 2, Point(0, 0).getPolarAngle(Point(0, -1)))
    }

    @Test
    fun `Test getPolarAngle from origin to 1,1 returns pi over 4`() {
        Assertions.assertEquals(PI / 4, Point(0, 0).getPolarAngle(Point(1, 1)))
    }

}