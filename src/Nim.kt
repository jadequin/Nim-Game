import kotlin.math.*



class Nim(private val fields: List<Int> = listOf(1,2,3), private val turn: Int = +1, private val history: List<Nim> = listOf()) {

    companion object {
        val hashTable = hashMapOf<Nim, Int>()
        init { Nim(listOf(7,7,7,7,7)).negaMax(); println(hashTable.size)}
    }

    fun makeMove(move: Move): Nim = Nim(
            fields = fields.take(move.row) + (if(fields[move.row] - move.number == 0) listOf<Int>() else listOf(fields[move.row] - move.number)) + fields.takeLast(fields.size - move.row - 1),
            turn = -turn,
            history = history.plus(this)
    )

    fun undoMove(): Nim = history.last()

    fun bestMove() = nextMoves()
    .reduce {
        acc, move ->
        if(hashTable[move]!! > hashTable[acc]!!)
            move
        else
            acc
    }//TODO: Filter alle besten Züge heraus und wähle einen zufälligen Zug

    fun isLegalMove(move: Move): Boolean {
        return move.row >= 0 && move.row < fields.size && move.number >= 1 && move.number <= fields[move.row]
    }

    fun nextMoves(): List<Nim> {
        val result = mutableListOf<Nim>()

        for(i in fields.indices)
            for(j in 1..fields[i])
                result.add(makeMove(Move(i, j)))

        return result
    }

    fun isPlayer1Turn() = turn == 1

    fun isInitialState() = history.isEmpty()

    fun initialState() = if(history.isEmpty()) this else history.elementAt(0)

    fun isGameOver() = fields.sum() == 0

    fun result(): Int = if(isGameOver()) -turn else 0

    fun negaMax(): Int {
        if(hashTable[this] != null)
            return hashTable[this]!!

        if(isGameOver()) {
            hashTable[this] = result() * -turn
            return result() * -turn
        }

        val worst = nextMoves().fold(Int.MAX_VALUE) {worst, move -> min(worst, -move.negaMax())}
        hashTable[this] = worst
        return worst
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
        return turn * fields.sorted().fold(0) {acc: Int, i: Int ->  acc + (i shl 3)}
    }
}