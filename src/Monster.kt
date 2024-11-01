class Monster(
    val appliedCards: MutableList<Card> = mutableListOf(),
    val isInDefenseState: Boolean = false,
    var life: Int,
) {

}