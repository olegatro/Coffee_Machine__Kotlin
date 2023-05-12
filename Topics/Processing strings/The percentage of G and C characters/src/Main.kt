fun main() {
    val input: String = readln()
    val regex: Regex = "[cg]".toRegex(RegexOption.IGNORE_CASE)
    val count: Int = regex.findAll(input).count()
    val result: Double = (count.toDouble() / input.length) * 100

    println(result)
}
