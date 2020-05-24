import kotlin.math.*



class Nim(
        private val fields: List<Int> = listOf(3,4,5),
        private val turn: Int = +1,
        private val history: List<Nim> = listOf(),
        private val results: HashMap<Nim, Int> = hashMapOf(),
        private val maxShift: Int = ceil(log2((fields.max()?:0).toFloat())).toInt()): NimGame {


    init {
        if(maxShift * fields.size > Int.SIZE_BITS)
            throw IllegalArgumentException("Please use smaller numbers or less rows (not more than ceil(ln(max. number in row)) * rows ")

        this.negamax()
    }

    override fun makeMove(move: Move): Nim = Nim(
            fields = fields.take(move.row) + (if(fields[move.row] - move.number == 0) listOf<Int>() else listOf(fields[move.row] - move.number)) + fields.takeLast(fields.size - move.row - 1),
            turn = -turn,
            history = history.plus(this),
            results = results,
            maxShift = maxShift
    )

    override fun undoMove() = history.last()

    override fun bestMove() = nextMoves().reduce { acc, move -> if(results[move]!! > results[acc]!!) move else acc }

    override fun isLegalMove(move: Move) = move.row >= 0 && move.row < fields.size && move.number >= 1 && move.number <= fields[move.row]

    private fun nextMoves(): List<Nim> {
        val result = mutableListOf<Nim>()

        for(i in fields.indices)
            for(j in 1..fields[i])
                result.add(makeMove(Move(i, j)))

        return result
    }

    override fun isPlayer1Turn() = turn == 1

    override fun isWinPlayer1() = isGameOver() && result() == 1

    override fun isInitialState() = history.isEmpty()

    override fun initialState() = if(history.isEmpty()) this else history.elementAt(0)

    override fun isGameOver() = fields.sum() == 0

    override fun result() = if(isGameOver()) -turn else 0

    private fun negamax(): Int {
        if(results[this] != null)
            return results[this]!!

        if(isGameOver()) {
            results[this] = result() * -turn
            return result() * -turn
        }

        val worst = nextMoves().fold(Int.MAX_VALUE) {worst, move -> min(worst, -move.negamax())}
        results[this] = worst
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
        return turn * fields.sorted().fold(0) {acc, i -> (acc shl maxShift) + i }
    }
}