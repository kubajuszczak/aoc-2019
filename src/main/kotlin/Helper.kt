import java.io.File

fun getInput(filename: String): File {
    return File(ClassLoader.getSystemResource(filename).toURI())
}