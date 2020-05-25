

fun player1Wins(fields: List<Int>): Boolean {
    var G = 0
    for(i in fields)
        G = G xor i

    return G != 0
}




fun main() {
    NimInteraction()

//    val game = listOf(1,3,5,7,9)
////
////
//    var n = Nim(game)
//    var np = NimPerfect(game)
//
//    while(!n.isGameOver())
//        n = n.bestMove()
//
//    while(!np.isGameOver()) {
//        np = np.bestMove()
//    }
//
//    println("Winner Nim:\t\t\t" + if(n.isWinPlayer1()) "Player 1 wins" else "Player 2 wins")
//    println("Winner NimPerfect:\t" + if(np.isWinPlayer1()) "Player 1 wins" else "Player 2 wins")
//    println(if(player1Wins(game)) "Player 1 Winsss" else "Player 2 Winsss")
}