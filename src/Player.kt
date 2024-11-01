class Player(
    val name: String,
    var life: Int,
    val cardList: MutableList<Card>
) {
    fun receiveDamage(damage: Int) {
        life -= damage
    }
}