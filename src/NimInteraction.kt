import kotlin.io.readLine
import kotlin.random.Random
import kotlin.NumberFormatException

class NimInteraction {
    init {
        menu()
    }

    private fun gameSetup(mode: String) {
        println("\n".repeat(5)) //setup console display

        while(true) {
            if (mode == "n")
                println("Nim with the minimax algorithm!")
            else if(mode == "np")
                println("Nim with XOR - no chance for you")

            println(
                    "\n" +
                    "\n" +
                    "[ENTER] = Start a predefined game with the initial state 3-4-5\n" +
                    "[number-number-number] = Start a game with your own settings\n" +
                    "[r] = Start a random game with 2-5 rows and 1-7 sticks per row\n" +
                    "[q/e] = Go back to the main menu"
            )

            val input = readLine()?:"".toLowerCase()

            if(input.isEmpty())
                playGame(
                        if(mode == "n")
                            Nim(listOf(3,4,5))
                        else
                            NimPerfect(listOf(3,4,5))
                )
            else if(input.startsWith("r")) {
                val rows = mutableListOf<Int>()
                val rowCount = Random.nextInt(2, 6)
                (0 until rowCount).forEach { rows.add(Random.nextInt(1, 8)) }
                playGame(Nim(rows.toList()))
            }
            else if(input.startsWith("q") || input.startsWith("e")) {
                break
            }
            else {
                val possibleRows = input.split("-")

                try {
                    val rows = mutableListOf<Int>()
                    possibleRows.forEach { rows.add(it.toInt()) }
                    playGame(Nim(rows.toList()))
                } catch(nfe: NumberFormatException) {
                    println("Not a valid input")
                }
            }

            println("\n".repeat(15))
        }
    }

    private fun menu() {
        println("\n".repeat(5)) //setup console display

        while(true) {
            println(
                "++++++++++++++++++++" +
                "Welcome to Nim-World" +
                "++++++++++++++++++++" +
                "\n" +
                "\n" +
                "Please choose a game " +
                "[n] = Nim\t" + "[np] = NimPerfect\t" + "[q/e] = Exit game"
            )

            val input = readLine()?:"".toLowerCase()

            if(input.startsWith("np"))
                gameSetup("np")
            else if(input.startsWith("n"))
                gameSetup("n")
            else if(input.startsWith("q") || input.startsWith("e"))
                break
            else
                println("Please try a valid keystroke!")

            println("\n".repeat(15))
        }
    }

    private fun playGame(nim: NimGame) {

        var n = nim // copy instance
        println("\n".repeat(5)) //setup console display

        while(true) {
            println(
                    if(n.isGameOver())
                        if(n.isPlayer1Turn())
                            "Player 2 won the game"
                        else
                            "Player 1 won the game"
                    else
                        if(n.isPlayer1Turn())
                            "Player 1 turn"
                        else
                            "Player 2 turn"
            )
            println(n)
            println("[ENTER] = AI makes a best move\t" +
                    " [row.number]  = make your own move\t" +
                    "  [b]   = undo move\t" +
                    "  [r]   = reset game\t" +
                    " [q/e]  = quit game\t")

            val input = readLine()?:"".toLowerCase()

            if(input.isEmpty() && !n.isGameOver())
                n = n.bestMove()
            else if(input.startsWith("q") || input.startsWith("e"))
                return
            else if(input.startsWith("b"))
                if(n.isInitialState())
                    println("Can't undo the origin state")
                else
                    n = n.undoMove()
            else if(input.startsWith("r"))
                n = n.initialState()
            else if(!n.isGameOver()){
                try {
                    val inputTry = input.split(".")
                    val row = inputTry[0].toInt() - 1
                    val number = inputTry[1].toInt()

                    if(n.isLegalMove(Move(row, number)))
                        n = n.makeMove(Move(row, number))
                    else
                        println("Try a valid move!")
                } catch(e: Exception) {
                    println("Try again and enter a valid keystroke")
                }
            }
            if(n.isGameOver())
                println(if(n.isWinPlayer1()) "Player 1 won the game" else "Player 2 won the game")

            println("\n".repeat(15))
        }
    }
}