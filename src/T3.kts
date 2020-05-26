
/*

Till Nitsche
5197769
Informatik B. Sc.

 */





import kotlin.math.*
import kotlin.io.readLine
import kotlin.random.Random
import kotlin.NumberFormatException






interface NimGame {
    fun makeMove(nimMove: NimMove): NimGame
    fun undoMove(): NimGame
    fun bestMove(): NimGame
    fun isLegalMove(nimMove: NimMove): Boolean
    fun isGameOver(): Boolean
    fun isPlayer1Turn(): Boolean
    fun isWinPlayer1(): Boolean
    fun isInitialState(): Boolean
    fun initialState(): NimGame
    fun result(): Int
    fun nextMoves(): List<NimGame>
    fun of(fields: List<Int>): NimGame
    fun getField(): List<Int>
}








data class NimMove(val row: Int, val number: Int)









class NimPerfect(private val fields: List<Int>, private val turn: Int = +1, private val history: List<NimPerfect> = listOf()): NimGame {

    override fun makeMove(nimMove: NimMove) = NimPerfect(
            fields = fields.take(nimMove.row) + (if(fields[nimMove.row] - nimMove.number == 0) listOf() else listOf(fields[nimMove.row] - nimMove.number)) + fields.takeLast(fields.size - nimMove.row - 1),
            turn = -turn,
            history = history.plus(this)
    )

    override fun undoMove() = history.last()

    override fun bestMove() = nextMoves().reduce { acc, nimPerfect -> if(nimPerfect.fields.fold(0) {ac, i -> ac xor i} == 0) nimPerfect else acc}

    override fun nextMoves(): List<NimPerfect> {
        val result = mutableListOf<NimPerfect>()

        for(i in fields.indices)
            for(j in 1..fields[i])
                result.add(makeMove(NimMove(i, j)))

        return result.toList()
    }

    override fun of(fields: List<Int>) = NimPerfect(fields)

    override fun getField() = fields

    fun isEstimatedWinnerPlayer1() = initialState().fields.fold(0) { acc, i -> acc xor i } != 0

    override fun isLegalMove(nimMove: NimMove) = nimMove.row >= 0 && nimMove.row < fields.size && nimMove.number >= 1 && nimMove.number <= fields[nimMove.row]

    override fun isGameOver() = fields.sum() == 0

    override fun isPlayer1Turn() = turn == 1

    override fun isWinPlayer1()  = result() == 1

    override fun isInitialState() = history.isEmpty()

    override fun initialState() = history.firstOrNull()?: this

    override fun result() = if(isGameOver()) -turn else 0


    override fun toString() = fields.joinToString(separator = "\n", transform = {("I ".repeat(it)).trimEnd()})
}
















class Nim(
        private val fields: List<Int>,
        private val turn: Int = +1,
        private val history: List<Nim> = listOf(),
        private val results: HashMap<Nim, Int> = hashMapOf(),
        private val maxShift: Int = ceil(log2((fields.max()?:0).toFloat())).toInt()): NimGame {


    companion object {
        fun isValidBoardSize(fields: List<Int>) = ceil(log2((fields.max()?:0).toFloat())).toInt() * fields.size < Int.SIZE_BITS
    }

    init {
        this.minimax()
    }

    override fun makeMove(nimMove: NimMove) = Nim(
            fields = fields.take(nimMove.row) + (if(fields[nimMove.row] - nimMove.number == 0) listOf() else listOf(fields[nimMove.row] - nimMove.number)) + fields.takeLast(fields.size - nimMove.row - 1),
            turn = -turn,
            history = history.plus(this),
            results = results,
            maxShift = maxShift
    )

    override fun undoMove() = history.last()

    override fun bestMove() = nextMoves().firstOrNull { results[it] == turn } ?:nextMoves().random()

    override fun isLegalMove(nimMove: NimMove) = nimMove.row >= 0 && nimMove.row < fields.size && nimMove.number >= 1 && nimMove.number <= fields[nimMove.row]

    override fun nextMoves(): List<Nim> {
        val result = mutableListOf<Nim>()

        for(i in fields.indices)
            for(j in 1..fields[i])
                result.add(makeMove(NimMove(i, j)))

        return result.toList()
    }

    override fun getField() = fields

    override fun of(fields: List<Int>) = Nim(fields)

    override fun isPlayer1Turn() = turn == 1

    override fun isWinPlayer1() = result() == 1

    override fun isInitialState() = history.isEmpty()

    override fun initialState() = if(history.isEmpty()) this else history.elementAt(0)

    override fun isGameOver() = fields.sum() == 0

    override fun result() = if(isGameOver()) -turn else 0

    private fun minimax(): Int {
        if(results[this] != null)
            return results[this]!!

        if(isGameOver()) {
            results[this] = result()
            return result()
        }

        val bestScore = if(nextMoves().firstOrNull { it.minimax() == turn } != null) turn else -turn
        results[this] = bestScore
        return bestScore
    }

    override fun toString() = fields.joinToString(separator = "\n", transform = {("I ".repeat(it)).trimEnd()})

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return other.hashCode() == this.hashCode()
    }

    override fun hashCode(): Int {
        if(isGameOver())
            return turn * Int.MAX_VALUE
        return turn * fields.sorted().fold(0) {acc, i -> (acc shl maxShift) + i }
    }
}
















class NimInteraction {

    private var onExit = false
    private var tailMessage = ""

    init {
        menu()
    }

    private fun randomField(): List<Int> {
        val rows = mutableListOf<Int>()
        val rowCount = Random.nextInt(2, 6)
        (0 until rowCount).forEach { rows.add(Random.nextInt(1, 8)) }
        return rows.toList()
    }

    private fun menu() {
        println("\n".repeat(5)) //setup console display

        while(!onExit) {
            println(
                    "++++++++++++++++++++\n" +
                            "   nim main menu\n" +
                            "++++++++++++++++++++" +
                            "\n" +
                            "\n" +
                            "please choose one of the following actions\n" +
                            "\n" +
                            "[n] = start game: nim\t\t" + "[np] = start game: nimPerfect\n" +
                            "[t] = launch test mode\t\t" + "[q/e] = quit"
            )
            println()
            println("-----------------------------")
            println(tailMessage)
            tailMessage = ""

            val input = readLine()?:"".toLowerCase()

            if(input.startsWith("np"))
                gameSetup("np")
            else if(input.startsWith("n"))
                gameSetup("n")
            else if(input.startsWith("t"))
                testMode()
            else if(input.startsWith("q") || input.startsWith("e"))
                break
            else
                tailMessage = "please try a valid keystroke!"

            println("\n".repeat(15))
        }
    }

    private fun gameSetup(mode: String) {
        println("\n".repeat(5)) //setup console display

        while(!onExit) {
            if (mode == "n")
                println("Nim with the minimax algorithm!")
            else if(mode == "np")
                println("Nim with XOR - no chance for you")

            println(
                    "\n" +
                            "\n" +
                            "       [ENTER]          Start a predefined game with the initial state 3-4-5\n" +
                            "[number-number-number]  Start a game with your own settings\n" +
                            "         [r]            Start a random game with 2-5 rows and 1-7 sticks per row\n" +
                            "         [b]            back to main menu\n" +
                            "        [q/e]           quit"
            )
            println()
            println("-----------------------------")
            println(tailMessage)
            tailMessage = ""

            val input = readLine()?:"".toLowerCase()

            if(input.isEmpty()) {
                playGame(
                        if (mode == "n")
                            Nim(listOf(3, 4, 5))
                        else
                            NimPerfect(listOf(3, 4, 5))
                )
            }
            else if(input.startsWith("r")) {
                playGame(if(mode == "n") Nim(randomField()) else NimPerfect(randomField()))
            }
            else if(input.startsWith("q") || input.startsWith("e")) {
                onExit = true
            }
            else if(input.startsWith("b")) {
                return
            }
            else {
                val possibleRows = input.split("-")
                val rows = mutableListOf<Int>()

                try {
                    possibleRows.forEach { rows.add(it.toInt()) }
                }
                catch(nfe: NumberFormatException) {
                    tailMessage = "not a valid input"
                }

                if(mode == "n" && !Nim.isValidBoardSize(rows))
                    tailMessage = "please try a valid board size -> ceil(ln(maxStickNumber)) * rows < 32"
                else
                    playGame(if(mode == "n") Nim(rows.toList()) else NimPerfect(rows.toList()))
            }

            println("\n".repeat(15))
        }
    }

    private fun playGame(nim: NimGame) {

        var n = nim // copy instance
        println("\n".repeat(5)) //setup console display

        while(!onExit) {
            if(!n.isGameOver())
                println((if (n.isPlayer1Turn()) "p1" else "p2") + " turn")

            println(n)
            println()
            println("   [ENTER]     ai makes a best move\t" +
                    " [row.number]  make your own move\n" +
                    "     [u]       undo move\t" +
                    "             [r]       reset game\n" +
                    "     [b]       back to main menu\t" +
                    "    [q/e]      quit")
            println()
            println("-----------------------------")
            println(tailMessage)
            tailMessage = ""

            val input = readLine()?:"".toLowerCase()

            if(input.isEmpty() && !n.isGameOver())
                n = n.bestMove()
            else if(input.startsWith("b"))
                return
            else if(input.startsWith("q") || input.startsWith("e"))
                onExit = true
            else if(input.startsWith("u"))
                if(n.isInitialState())
                    tailMessage = "can not undo the origin state"
                else
                    n = n.undoMove()
            else if(input.startsWith("r"))
                n = n.initialState()
            else if(!n.isGameOver()){
                try {
                    val inputTry = input.split(".")
                    val row = inputTry[0].toInt() - 1
                    val number = inputTry[1].toInt()

                    if(n.isLegalMove(NimMove(row, number)))
                        n = n.makeMove(NimMove(row, number))
                    else
                        tailMessage = "try a valid move"
                } catch(e: Exception) {
                    tailMessage = "try again and enter a valid keystroke"
                }
            }
            if(n.isGameOver())
                println(if(n.isWinPlayer1()) "p1 won the game" else "p2 won the game")

            println("\n".repeat(15))
        }
    }

    private fun testMode() {
        println("\n".repeat(5)) //setup console display

        while(!onExit) {
            println(
                    "test mode\n" +
                            "\n" +
                            "[ENTER]  nim vs nimPerfect: play 40 game simulations\n" +
                            "  [b]    back to main menu\n" +
                            " [q/e]   quit"
            )
            println()
            println("-----------------------------")
            println(tailMessage)
            tailMessage = ""


            val input = readLine()?:"".toLowerCase()
            if(input.startsWith("q") || input.startsWith("e"))
                onExit = true
            else if(input.isEmpty()) {



                var resultNim = 0

                var resultNimPerfect = 0

                for(i in 1..40) {

                    println("Round $i")

                    val rndField = randomField()
                    val nim = Nim(rndField)
                    val nimPerfect = NimPerfect(rndField)

                    val player1isNim: Boolean

                    var player1: NimGame
                    var player2: NimGame

                    val player1Wins: Boolean = nimPerfect.isEstimatedWinnerPlayer1()
                    println("estimated winner: ${if(player1Wins) "player1" else "player2"}")

                    if(Random.nextBoolean()) {
                        player1 = nim; player2 = nimPerfect
                        player1isNim = true
                    }
                    else {
                        player1 = nimPerfect; player2 = nim
                        player1isNim = false
                    }

                    var player1Turn = true
                    while(!player1.isGameOver()) {
                        if(player1Turn) {
                            player1 = player1.bestMove()
                            player2 = player2.of(player1.getField())
                        }
                        else {
                            player2 = player2.bestMove()
                            player1 = player1.of(player2.getField())
                        }

                        player1Turn = !player1Turn
                    }

                    if(player1.isWinPlayer1() && player1isNim || !player1.isWinPlayer1() && player1isNim)
                        resultNim++
                    else
                        resultNimPerfect++

                    println("actual winner: ${if(player1.isWinPlayer1()) "player1" else "player2"} (nim${if(player1isNim) "" else " perfect"})")
                    if(player1.isWinPlayer1() == player1Wins)
                        println("---correct---")
                    else {
                        tailMessage = "error - actual and estimated result are not the same"
                        break
                    }
                }

                println()
                println()
                tailMessage = "result: score nim -> ${resultNim}, score nimPerfect -> ${resultNimPerfect}"
            }
        }
        println("\n".repeat(15))
    }
}



NimInteraction()
