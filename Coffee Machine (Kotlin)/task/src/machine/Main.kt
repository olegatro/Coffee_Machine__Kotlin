package machine

data class Recipe(val name: String, val water: Int = 0, val milk: Int = 0, val beans: Int = 0, val price: Int = 0)

class CoffeMachine {
    private var water: Int = 400
    private var milk: Int = 540
    private var beans: Int = 120
    private var money: Int = 550
    private var cups: Int = 9
    private val recipeList: MutableList<Recipe> = mutableListOf()

    private val emptyAction: String = ""
    private val emptyInput: String = ""
    private val defaultProgram: String = "start"

    private var program: String = this.defaultProgram
    private var lastCompletedAction: String = this.emptyAction

    init {
        this.recipeList.add(Recipe("espresso", water = 250, beans = 16, price = 4))
        this.recipeList.add(Recipe("latte", water = 350, milk = 75, beans = 20, price = 7))
        this.recipeList.add(Recipe("cappuccino", water = 200, milk = 100, beans = 12, price = 6))

        this.reset()
    }

    private fun printMenu(): Boolean {
        println("Write action (buy, fill, take, remaining, exit):")

        return true
    }

    private fun handleMenu(input: String): Boolean {
        this.program = when (input) {
            "buy" -> "buy"
            "fill" -> "fill"
            "take" -> "take"
            "remaining" -> "remaining"
            else -> "start"
        }

        return true
    }

    private fun handleTake(): Boolean {
        println("I gave you \$${this.money}")
        this.money = 0
        return true
    }

    private fun printRemaining(): Boolean {
        println("The coffee machine has:")
        println("${this.water} ml of water")
        println("${this.milk} ml of milk")
        println("${this.beans} g of coffee beans")
        println("${this.cups} disposable cups")
        println("\$${this.money} of money")

        return true
    }

    private fun printFill(type: String): Boolean {
        when (type) {
            "water" -> println("Write how many ml of water you want to add:")
            "milk" -> println("Write how many ml of milk you want to add:")
            "beans" -> println("Write how many grams of coffee beans you want to add:")
            "cups" -> println("Write how many disposable cups you want to add:")
            else -> return false
        }

        return true
    }

    private fun handleFillWater(input: String): Boolean {
        this.water += input.toInt()

        return true
    }

    private fun handleFillMilk(input: String): Boolean {
        this.milk += input.toInt()

        return true
    }

    private fun handleFillBeans(input: String): Boolean {
        this.beans += input.toInt()

        return true
    }

    private fun handleFillCups(input: String): Boolean {
        this.cups += input.toInt()

        return true
    }

    private fun printBuy(): Boolean {
        print("What do you want to buy? ")
        print(this.recipeList.joinToString { "${this.recipeList.indexOf(it) + 1} - ${it.name}" })
        print(", back - to main menu")
        println(":")

        return true
    }

    private fun handleBuy(input: String): Boolean {
        if (input == "back") return true

        val recipe = this.recipeList[input.toInt() - 1]

        if (this.water < recipe.water) {
            println("Sorry, not enough water!")
            return true
        }
        if (this.milk < recipe.milk) {
            println("Sorry, not enough milk!")
            return true
        }
        if (this.beans < recipe.beans) {
            println("Sorry, not enough beans!")
            return true
        }
        if (this.cups == 0) {
            println("Sorry, not enough cups!")
            return true
        }

        println("I have enough resources, making you a coffee!")

        this.water -= recipe.water
        this.milk -= recipe.milk
        this.beans -= recipe.beans
        this.cups -= 1
        this.money += recipe.price

        return true
    }

    // handle action
    private fun handleAction(action: String, input: String): Boolean {
        return when (action) {
            "print:menu" -> printMenu()
            "handle:menu" -> handleMenu(input)
            "print:buy" -> printBuy()
            "handle:buy" -> handleBuy(input)
            "handle:take" -> handleTake()
            "print:remaining" -> printRemaining()
            "print:fill:water" -> printFill("water")
            "handle:fill:water" -> handleFillWater(input)
            "print:fill:milk" -> printFill("milk")
            "handle:fill:milk" -> handleFillMilk(input)
            "print:fill:beans" -> printFill("beans")
            "handle:fill:beans" -> handleFillBeans(input)
            "print:fill:cups" -> printFill("cups")
            "handle:fill:cups" -> handleFillCups(input)
            else -> false
        }
    }

    // program list
    // the technique allow to create many scenarios
    private fun getProgramActionList(program: String): List<String> {
        return when (program) {
            "start" -> listOf<String>("print:menu", "handle:menu")
            "buy" -> listOf<String>("print:buy", "handle:buy")
            "fill" -> listOf<String>(
                "print:fill:water",
                "handle:fill:water",
                "print:fill:milk",
                "handle:fill:milk",
                "print:fill:beans",
                "handle:fill:beans",
                "print:fill:cups",
                "handle:fill:cups"
            )

            "take" -> listOf<String>("handle:take")
            "remaining" -> listOf<String>("print:remaining")
            else -> throw Exception("Not found program")
        }
    }

    // get next program action
    private fun getNextProgramAction(program: String, lastCompletedAction: String): String {
        val actionList: List<String> = this.getProgramActionList(program)
        val firstAction = actionList.first()

        if (lastCompletedAction == this.emptyInput) return firstAction
        if (actionList.indexOf(lastCompletedAction) == -1) return firstAction
        if (lastCompletedAction == actionList.last()) return firstAction

        return actionList[actionList.indexOf(lastCompletedAction) + 1]
    }

    // Handle the program action
    private fun handleProgramAction(program: String, action: String, input: String) {
        val actionList: List<String> = this.getProgramActionList(program)

        if (actionList.indexOf(action) == -1) return
        if (!this.handleAction(action, input)) return

        if (program != this.program) {
            println()
            return this.handleProgram(this.program, this.emptyAction, this.emptyInput)
        }

        this.lastCompletedAction = action

        if (this.lastCompletedAction == actionList.last()) {
            println()
            return this.reset()
        }

        if (input == this.emptyInput) return

        this.handleProgramAction(program, this.getNextProgramAction(program, action), this.emptyInput)
    }

    // Handle program step by step
    private fun handleProgram(program: String, lastCompletedAction: String, input: String) {
        val action = this.getNextProgramAction(program, lastCompletedAction)
        this.handleProgramAction(program, action, input)
    }

    // Reset program to the start step
    private fun reset() {
        this.program = this.defaultProgram
        this.lastCompletedAction = this.emptyAction
        this.handleProgram(this.program, this.lastCompletedAction, this.emptyInput)
    }

    // Handle input from user
    fun handleInput(input: String) {
        this.handleProgram(this.program, this.lastCompletedAction, input)
    }
}

fun main() {
    val coffeMachine: CoffeMachine = CoffeMachine()

    while (true) {
        val input = readln()
        if (input == "exit") return

        coffeMachine.handleInput(input)
    }
}
