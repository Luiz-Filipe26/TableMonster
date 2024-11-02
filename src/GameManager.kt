class GameManager {
    private var currentTurn: Int = 0
    private val players: MutableList<Player> = mutableListOf()
    private val drawPile: Deck = Deck()

    fun processGame() {
        var card = drawPile.getCard()

        while (card != null) {
            println("Carta puxada: ${card.name}")
            card = drawPile.getCard()
        }

        println("Todas as cartas foram distribuídas.")
    }
}
