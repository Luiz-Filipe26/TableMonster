class Monster(
    val appliedCards: MutableList<Card> = mutableListOf(),
    var isInDefenseState: Boolean = false,
    var name: String,
    var life: Int,
) {

}