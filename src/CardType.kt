enum class CardType {
    MONSTER, EQUIPAMENT;

    companion object {
        fun fromString(type: String): CardType? {
            return when (type.uppercase()) {
                "MONSTRO", "MONSTER" -> MONSTER
                "EQUIPAMENTO", "EQUIPMENT" -> EQUIPAMENT
                else -> null
            }
        }
    }
}