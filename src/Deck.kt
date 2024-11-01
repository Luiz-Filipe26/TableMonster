class Deck {
    private val cardList: MutableList<Card> = mutableListOf()
    private val filePath  = ".\\cards.csv"
    private var currentCard = 0

    init {
        readCardsFromCSV()
        cardList.shuffle()
    }

    private fun readCardsFromCSV() {

    }

    fun getCard() : Card {
        currentCard++
        return cardList.get(currentCard-1)
    }
}