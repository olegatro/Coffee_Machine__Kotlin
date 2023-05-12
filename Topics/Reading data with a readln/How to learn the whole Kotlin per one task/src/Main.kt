fun main() {
    val list: MutableList<String> = mutableListOf()

    repeat(5) {
        list.add(readln())
    }

    println(list.joinToString(separator = " "))
}