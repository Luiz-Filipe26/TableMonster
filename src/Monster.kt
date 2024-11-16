class Monster(
    val appliedCards: MutableList<Card> = mutableListOf(),
    var isInDefenseState: Boolean = false,
    var name: String,
    var attack: Int,
    var defense: Int
) {
    override fun toString(): String {
        val cards = appliedCards.joinToString { it.name }
        return "Monster(name='$name', attack='$attack', defense='$defense', isInDefenseState=$isInDefenseState, appliedCards=[$cards])"
    }

    public fun totalAttack(): Int {
        return attack + appliedCards.sumOf { it.attack }
    }

    public fun totalDefense(): Int {
        return defense + appliedCards.sumOf { it.defense }
    }

    public fun getEquipamentName(): List<String> {
        return appliedCards.map { it.name }
    }
}