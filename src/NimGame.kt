
interface NimGame {
    fun makeMove(move: Move): NimGame
    fun undoMove(): NimGame
    fun bestMove(): NimGame
    fun isLegalMove(move: Move): Boolean
    fun isGameOver(): Boolean
    fun isPlayer1Turn(): Boolean
    fun isWinPlayer1(): Boolean
    fun isInitialState(): Boolean
    fun initialState(): NimGame
    fun result(): Int
}