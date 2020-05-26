
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