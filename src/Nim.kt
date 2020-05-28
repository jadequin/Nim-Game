import kotlin.math.*


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
            return results[this]!! * -turn

        if(isGameOver()) {
            results[this] = result() * -turn
            return result()
        }

        val bestScore = if(nextMoves().firstOrNull { it.minimax() == turn } != null) turn else -turn
        results[this] = bestScore * -turn
        return bestScore
    }

    override fun toString() = fields.joinToString(separator = "\n", transform = {("I ".repeat(it)).trimEnd()})

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return other.hashCode() == this.hashCode()
    }

    override fun hashCode(): Int = fields.sorted().fold(0) {acc, i -> (acc shl maxShift) + i }
}