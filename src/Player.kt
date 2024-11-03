class Player(
    val name: String,
    private var life: Int,
    private val cardList: MutableList<Card> = mutableListOf(),
    private val MAXIMUM_NUMBER_OF_CARDS : Int = 5
) {
    fun receiveDamage(damage: Int) {
        life -= damage
    }

    fun receiveCard(card: Card) {
        cardList.add(card)
    }

    fun hasExceededMaximumNumberOfCards(): Boolean {
        return cardList.size > MAXIMUM_NUMBER_OF_CARDS
    }

    fun getCardNames(): List<String> {
        return cardList.map { it.name }
    }

    fun removeCard(cardPosition: Int) {
        cardList.removeAt(cardPosition)
    }
}