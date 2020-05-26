


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