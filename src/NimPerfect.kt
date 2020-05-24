import kotlin.math.*


class NimPerfect(
        private val fields: List<Int>,
        private val turn: Int = +1,
        private val history: List<NimPerfect> = listOf(),
        private val maxShift: Int = ceil(log2((fields.max()?:0).toFloat())).toInt()): NimGame {

    override fun makeMove(move: Move) = NimPerfect(
        fields = fields.take(move.row) + (if(fields[move.row] - move.number == 0) listOf<Int>() else listOf(fields[move.row] - move.number)) + fields.takeLast(fields.size - move.row - 1),
        turn = -turn,
        history = history.plus(this),
        maxShift = maxShift
    )

    override fun undoMove() = history.last()

    override fun bestMove() = nextMoves().reduce { acc, nimPerfect -> if(nimPerfect.fields.fold(0) {ac, i -> ac xor i} % 2 == 0) nimPerfect else acc}

    private fun nextMoves(): List<NimPerfect> {
        val result = mutableListOf<NimPerfect>()

        for(i in fields.indices)
            for(j in 1..fields[i])
                result.add(makeMove(Move(i, j)))

        return result
    }

    override fun isLegalMove(move: Move) = move.row >= 0 && move.row < fields.size && move.number >= 1 && move.number <= fields[move.row]

    override fun isGameOver() = fields.sum() == 0

    override fun isPlayer1Turn() = turn == 1

    override fun isWinPlayer1()  = isGameOver() && result() == 1

    override fun isInitialState() = history.isEmpty()

    override fun initialState() = if(history.isEmpty()) this else history.elementAt(0)

    override fun result() = if(isGameOver()) -turn else 0


    override fun toString() = fields.joinToString(separator = "\n", transform = {("I ".repeat(it)).trimEnd()})
}