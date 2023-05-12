data class Student(val name: String = "", val money: Int = 0) {
    override fun equals(other: Any?): Boolean {
        return other is Student
            && this.name == other.name
            && this.money < 1500 && other.money < 1500
    }
}
