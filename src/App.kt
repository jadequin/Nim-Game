

fun solve(fields: List<Int>): Int {
    var G = 0
    for(i in fields) {
        if(i%2!=0)
            G = G xor (i + 1)
        else
            G = G xor (i - 1)
    }
    return G
}

fun printSolve(fields: List<Int>) {
    if(solve(fields) == 0)
        println("Player 2 wins")
    else
        println("Player 1 wins")
}




fun main() {
//    printSolve(listOf(1,2,3))
//    NimInteraction()

    val game = listOf(1,3,5,7)
//
//
    var n = NimPerfect(game)
    while(!n.isGameOver()) {
        n = n.bestMove()
    }

    println(if(n.isWinPlayer1()) "Player 1 wins" else "Player 2 wins")
    printSolve(game)
}