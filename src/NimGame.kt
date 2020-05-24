
interface NimGame {
    fun makeMove(move: Move): NimGame
    fun undoMove(): NimGame
    fun bestMove(): NimGame
    fun isLegalMove(move: Move): Boolean
    fun nextMoves(): List<NimGame>
    fun negaMax(): Int
}