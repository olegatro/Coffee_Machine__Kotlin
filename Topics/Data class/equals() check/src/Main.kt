data class Client(val name: String, val age: Int, val balance: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        other as Client

        if (this.name != other.name) return false
        if (this.age != other.age) return false

        return true
    }
}

fun main() {
    val name1 = readln()
    val age1 = readln().toInt()
    val balance1 = readln().toInt()
    val name2 = readln()
    val age2 = readln().toInt()
    val balance2 = readln().toInt()

    val client1 = Client(name1, age1, balance1)
    val client2 = Client(name2, age2, balance2)

    println(client1 == client2)
}
