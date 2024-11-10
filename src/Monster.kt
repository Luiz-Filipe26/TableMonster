class Monster(
    private val appliedCards: MutableList<Card> = mutableListOf(),
    var isInDefenseState: Boolean = false,
    var name: String,
    var life: Int,
) {
    override fun toString(): String {
        val cards = appliedCards.joinToString { it.name }
        return "Monster(name='$name', life=$life, isInDefenseState=$isInDefenseState, appliedCards=[$cards])"
    }
}