fun main() {
    val input = readln()

    println(
        when (input) {
            input.reversed() -> "yes"
            else -> "no"
        }
    )
}