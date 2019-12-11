import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.math.PI
import kotlin.math.sqrt

class PointTests {

    @Test
    fun `Test getBearing from origin to 0,1 returns bearing 0`() {
        assertEquals(0.0, Point(0, 0).getBearing(Point(0, 1)))
    }

    @Test
    fun `Test getBearing from origin to 1,0 returns bearing 90`() {
        assertEquals(90.0, Point(0, 0).getBearing(Point(1, 0)))
    }

    @Test
    fun `Test getBearing from origin to 0,-1 returns bearing 180`() {
        assertEquals(180.0, Point(0, 0).getBearing(Point(0, -1)))
    }

    @Test
    fun `Test getBearing from origin to -1,0 returns bearing 270`() {
        assertEquals(270.0, Point(0, 0).getBearing(Point(-1, 0)))
    }

    @Test
    fun `Test getPolarAngle from origin to 1,0 returns 0`() {
        assertEquals(0.0, Point(0, 0).getPolarAngle(Point(1, 0)))
    }

    @Test
    fun `Test getPolarAngle from origin to 0,1 returns pi over 2`() {
        assertEquals(PI / 2, Point(0, 0).getPolarAngle(Point(0, 1)))
    }

    @Test
    fun `Test getPolarAngle from origin to -1,0 returns pi`() {
        assertEquals(PI, Point(0, 0).getPolarAngle(Point(-1, 0)))
    }

    @Test
    fun `Test getPolarAngle from origin to 0,-1 returns minus pi over 2`() {
        assertEquals(-PI / 2, Point(0, 0).getPolarAngle(Point(0, -1)))
    }

    @Test
    fun `Test getPolarAngle from origin to 1,1 returns pi over 4`() {
        assertEquals(PI / 4, Point(0, 0).getPolarAngle(Point(1, 1)))
    }

    @Test
    fun `Test getCartesianDistance returns sqrt 2 distance between origin and 1,1`(){
        assertEquals(sqrt(2.0), Point(0,0).getCartesianDistance(Point(1,1)))
    }

    @Test
    fun `Test getCartesianDistance returns 12 distance between origin and 0,12`(){
        assertEquals(12.0, Point(0,0).getCartesianDistance(Point(0,12)))
    }

}