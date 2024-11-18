import java.io.File

class Deck {
    private val cardList: MutableList<Card> = mutableListOf()
    private val filePath  = "cartas.csv"
    private var currentCard = 0

    init {
        readCardsFromCSV()
        cardList.shuffle()
    }

    fun getCard(): Card? {
        if (currentCard >= cardList.size) return null
        return cardList[currentCard++]
    }

    fun hasCard(): Boolean {
        return cardList.isNotEmpty()
    }

    private fun readCardsFromCSV() {
        val resourceStream = javaClass.getResourceAsStream(filePath)

        if (resourceStream == null) {
            println("Arquivo de cartas em ($filePath) não encontrado no classpath.")
            return
        }

        resourceStream.bufferedReader().use { reader ->
            reader.forEachLine { line ->
                val tokens = line.split(";")

                val name = tokens[0].trim()
                if (name.isEmpty()) {
                    println("Nome inválido: $name")
                    return@forEachLine
                }

                val description = tokens[1].trim()
                if (description.isEmpty()) {
                    println("Descrição inválida: $description")
                    return@forEachLine
                }

                val attack = tokens[2].toIntOrNull()
                if (attack == null || attack < 0) {
                    println("Ataque inválido: ${tokens[2]}")
                    return@forEachLine
                }

                val defense = tokens[3].toIntOrNull()
                if (defense == null || defense < 0) {
                    println("Defesa inválida: ${tokens[3]}")
                    return@forEachLine
                }

                val cardType = CardType.fromString(tokens[4].trim())
                if (cardType == null) {
                    println("Tipo de carta desconhecido: ${tokens[4]}")
                    return@forEachLine
                }

                val card = Card(name, description, cardType, attack, defense)
                cardList.add(card)
            }
        }
    }

}