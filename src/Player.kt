class Player(
    val name: String,
    private var life: Int,
    private val cardList: MutableList<Card> = mutableListOf(),
    private val monstersList: MutableList<Monster> = mutableListOf(),
    private var maximumNumberOfCards: Int = GameManager.MAXIMUM_NUMBER_OF_CARDS,
    private var maximumNumberOfMonsters: Int = GameManager.MAXIMUM_NUMBER_OF_MONSTERS
) {
    fun receiveDamage(damage: Int) {
        life -= damage
    }

    fun receiveCard(card: Card) {
        cardList.add(card)
    }

    fun positionMonster(monster : Monster) {
        monstersList.add(monster)
    }

    fun canPositionNewMonster(): Boolean {
        return monstersList.size > maximumNumberOfMonsters
    }

    fun hasExceededMaximumNumberOfCards(): Boolean {
        return cardList.size > maximumNumberOfCards
    }

    fun getMonstersCardsNames(): List<String> {
        return cardList.filter{ it.type == CardType.MONSTER }.map{ it.name }
    }

    fun getCardNames(): List<String> {
        return cardList.map { it.name }
    }

    fun getMonsterNames(): List<String> {
        return monstersList.map { it.name }
    }

    fun removeCard(cardPosition: Int) {
        cardList.removeAt(cardPosition)
    }

    fun getCardByName(cardName: String): Card? {
        return cardList.find { it.name == cardName }
    }

    fun getCardIndex(card: Card): Int {
        return cardList.indexOf(card)
    }
}