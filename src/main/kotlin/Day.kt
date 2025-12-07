import java.io.File

abstract class Day(
    fileName: String,
    isResource: Boolean = false
) {
    val numberPattern = """\d+""".toRegex()

    val input: List<String>
    init {
        input = if (isResource) {
            this::class.java.classLoader.getResource(fileName)?.readText()?.lines()?.filter { it.isNotEmpty() } ?: run {
                emptyList()
            }
        } else {
            File(fileName).readText().lines().filter { it.isNotEmpty() }
        }
    }
    abstract fun processInput(lines: Int? = null)

//    val input = File(fileName).readText().split("\n")
//    val input = File(fileName).readText().lines()

    fun log(debug: String) {
        println(debug)
    }
}