class GameManager {
    private val players: MutableList<Player> = mutableListOf()
    private var currentTurn = 0
    private val drawPile = Deck()
    private val NUMBER_OF_PLAYERS = 2
    private val DEFAULT_PLAYER_LIFE = 10000

    fun processGame() {
        if (!drawPile.hasCard()) {
            println("Não foi possível ler o arquivo de cartas. Jogo abortado")
            return
        }

        println("===== JOGO DE CARTAS TABLEMONSTER =====")
        println("Este é um jogo de cartas baseado em turnos, podendo equipar e atacar monstros")

        for (i in 1..NUMBER_OF_PLAYERS) {
            print("Digite o nome do jogador $i: ")
            var name = readln()
            while (name.isBlank()) {
                print("Digite um nome não vazio: ")
                name = readln()
            }
            val player = Player(name, DEFAULT_PLAYER_LIFE)
            players.add(player)
        }

        val isGameRunning = true
        while (isGameRunning) {
            val currentPlayer = players[currentTurn]

            println("Vez do jogador ${currentPlayer.name}.")
            getNewCardFromDrawPile(currentPlayer)

            printOptions()
            var option = readln()
            while (option.length != 1 || !option.matches(Regex("[1-6]"))) {
                print("Digite uma opção existente: ")
                option = readln()
            }

            when (option) {
                "1" -> positionNewMonster(currentPlayer)
                "2" -> equipMonster(currentPlayer)
                "3" -> discardCard(currentPlayer)
                "4" -> performAttack(currentPlayer)
                "5" -> changeMonsterState(currentPlayer)
                "6" -> skipTurn(currentPlayer)
                else -> println("Opção inválida") // Essa linha não deve ser atingida
            }

            currentTurn++
            currentTurn %= NUMBER_OF_PLAYERS

            printTableState()
        }
    }

    private fun getNewCardFromDrawPile(player: Player) {
        val card = drawPile.getCard() ?: return

        println("Nova carta para o jogador ${player.name}: ${card.name}")
        player.receiveCard(card)

        if (!player.hasExceededMaximumNumberOfCards()) return

        println("Cartas do jogador:")
        val cardNames = player.getCardNames()
        for ((cardIndex, cardName) in cardNames.withIndex()) {
            println("$cardIndex- $cardName")
        }

        var cardIndex: Int? = null
        while (cardIndex == null) {
            print("Digite o índice da carta que você deseja remover: ")
            val input = readln()
            cardIndex = input.toIntOrNull()

            if (cardIndex == null || cardIndex !in 0..cardNames.lastIndex) {
                println("Digite um valor válido entre 0 e ${cardNames.lastIndex}.")
                cardIndex = null
            }
        }

        player.removeCard(cardIndex)
    }

    private fun printTableState() {
    }

    private fun printOptions() {
        println("Ações possíveis para executar: ")
        println("1 - Posicionar um novo monstro no tabuleiro")
        println("2 - Equipar um monstro com uma carta de equipamento")
        println("3 - Descartar uma carta da mão")
        println("4 - Realizar um ataque contra o oponente")
        println("5 - Alterar o estado de um monstro (ataque/defesa)")
        println("6 - Pular a vez")
        print("Digite a ação que deseja realizar: ")
    }

    private fun positionNewMonster(player: Player) {
        println("${player.name} está posicionando um novo monstro...")
    }

    private fun equipMonster(player: Player) {
        println("${player.name} está equipando um monstro...")
    }

    private fun discardCard(player: Player) {
        println("${player.name} está descartando uma carta...")
    }

    private fun performAttack(player: Player) {
        println("${player.name} está realizando um ataque...")
    }

    private fun changeMonsterState(player: Player) {
        println("${player.name} está alterando o estado de um monstro...")
    }

    private fun skipTurn(player: Player) {
        println("${player.name} está pulando a vez...")
    }
}