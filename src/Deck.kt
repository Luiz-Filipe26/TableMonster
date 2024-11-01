import java.io.File

class Deck {
    private val cardList: MutableList<Card> = mutableListOf()
    private val filePath  = ".\\cards.csv"
    private var currentCard = 0

    init {
        readCardsFromCSV()
        cardList.shuffle()
    }

    private fun readCardsFromCSV() {
        val file = File(filePath)

        file.forEachLine { line ->
            val tokens = line.split(";")
            val card = Card(tokens[0], tokens[1], tokens[4], tokens[2].toIntOrNull() ?: 0, tokens[3].toIntOrNull() ?: 0)
            cardList.add(card)
        }
    }

    fun getCard(): Card? {
        if (currentCard >= cardList.size) return null
        return cardList[currentCard++]
    }
}