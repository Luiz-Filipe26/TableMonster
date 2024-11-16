import kotlin.math.abs

class GameManager {
    private val players: MutableList<Player> = mutableListOf()
    private var currentTurn = 0
    private val drawPile = Deck()
    private val NUMBER_OF_PLAYERS = 2
    private val DEFAULT_PLAYER_LIFE = 1000
    companion object {
        const val MAXIMUM_NUMBER_OF_CARDS = 10
        const val MAXIMUM_NUMBER_OF_MONSTERS = 5
    }

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

        while (true) {
            val currentPlayer: Player = players[currentTurn]
            val opponnent: Player = players[1 - currentTurn]

            if(currentPlayer.getCurrentLife() <= 0) {
                println("${opponnent.name} GANHOU o duelo !")
                return
            }

            if(opponnent.getCurrentLife() <=0) {
                println("${currentPlayer.name} GANHOU o duelo !")
                return
            }

            printTableState(players)

            println("Vez do jogador ${currentPlayer.name}.")
            getNewCardFromDrawPile(currentPlayer)

            showCurrentHand(currentPlayer)

            printOptions()
            var option = readln()
            while (option.length != 1 || !option.matches(Regex("[1-6]"))) {
                println("Digite uma opção existente: ")
                option = readln()
            }

            when (option) {
                "1" -> positionNewMonster(currentPlayer)
                "2" -> equipMonster(currentPlayer)
                "3" -> discardCard(currentPlayer)
                "4" -> performAttack(currentPlayer, opponnent)
                "5" -> changeMonsterState(currentPlayer)
                "6" -> skipTurn(currentPlayer)
                else -> println("Opção inválida") // Essa linha não deve ser atingida
            }

            currentTurn++
            currentTurn %= NUMBER_OF_PLAYERS
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

    private fun printTableState(playerList: List<Player>) {
        val headers = listOf("Nome Carta", "ATK Base", "DEF Base", "Total ATK", "Total DEF", "Equipamento(s)", "Estado")

        println("\n\t\t\t\t\t\t\t\tMESA ATUAL")

        for (player in playerList) {
            println("${player.name} | Vida total ${player.getCurrentLife()}:")

            val monsters = player.getMonsterList()

            //Calcular espacamento
            val colWidths = headers.indices.map { i ->
                maxOf(
                    headers[i].length,
                    monsters.maxOfOrNull {
                        when (i) {
                            0 -> it.name.length
                            1 -> it.attack.toString().length
                            2 -> it.defense.toString().length
                            3 -> it.totalAttack().toString().length
                            4 -> it.totalDefense().toString().length
                            5 -> it.getEquipamentName().joinToString(", ").length
                            6 -> (if(!it.isInDefenseState) "Ataque" else "Defesa").length
                            else -> 0
                        }
                    } ?: 0
                )
            }

            printSeparator(colWidths)
            printRow(headers, colWidths)
            printSeparator(colWidths)

            //Printa os detalhes do monstro
            for (monster in monsters) {
                val row = listOf(
                    monster.name,
                    monster.attack.toString(),
                    monster.defense.toString(),
                    monster.totalAttack().toString(),
                    monster.totalDefense().toString(),
                    monster.getEquipamentName().joinToString(", "),
                    if(!monster.isInDefenseState) "Ataque" else "Defesa"
                )
                printRow(row, colWidths)
            }

            printSeparator(colWidths)
            println()
        }
    }

    private fun printSeparator(colWidths: List<Int>) {
        println("+" + colWidths.joinToString("+") { "-".repeat(it + 2) } + "+")
    }

    private fun printRow(row: List<String>, colWidths: List<Int>) {
        println("| " + row.mapIndexed { i, cell -> cell.padEnd(colWidths[i]) }.joinToString(" | ") + " |")
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
        if (!player.canPositionNewMonster()) {
            println("Numero de monstros maximo atingido!")
            return
        }

        val monstersCardsNames = player.getMonstersCardsNames()

        if(monstersCardsNames.isEmpty()) {
            println("Não existe uma carta com monstro para posicionar!")
            return
        }

        println("Cartas de monstros disponíveis para posicionar:")
        monstersCardsNames.forEachIndexed { cardIndex, cardName ->
            println("$cardIndex- $cardName")
        }
        println("Digite o número do monstro que quer posicionar: ")
        var monsterIndex = readln().toIntOrNull()
        while(monsterIndex == null) {
            print("Digite um valor válido")
            monsterIndex = readln().toIntOrNull()
        }
        val monsterName = monstersCardsNames[monsterIndex]
        val monsterCard = player.getCardByName(monsterName)

        if(monsterCard == null) {
            println("Nenhuma carta encontrada com esse nome")
            return
        }

        println("Escolha o modo de posicionamento: 1 - Ataque | 2 - Defesa")
        val monsterState = readln().toIntOrNull()

        val monster = Monster(
            name = monsterName,
            isInDefenseState = monsterState == 2,
            attack = monsterCard.attack,
            defense = monsterCard.defense
        )

        player.positionMonster(monster)
        val cardIndex = player.getCardIndex(monsterCard)
        player.removeCard(cardIndex)
    }

    private fun equipMonster(player: Player) {
        println("${player.name} está equipando um monstro...")

        val monstersName = player.getMonsterNames()

        if (monstersName.isEmpty()) {
            print("Nao ha nenhum monstro posicionado")
            return
        }

        val equipamentCardNames = player.getEquipamentCardNames()

        if(equipamentCardNames.isEmpty()) {
            println("Nao ha nenhum equipamento disponivel.")
            return
        }

        println("Cartas de equipamento disponíveis para equipar:")
        equipamentCardNames.forEachIndexed { cardIndex, cardName ->
            println("$cardIndex- $cardName")
        }

        println("Digite o número do equipamento que deseja equipar: ")
        var equipamentIndex = readln().toIntOrNull()
        while(equipamentIndex == null) {
            println("Digite um valor válido")
            equipamentIndex = readln().toIntOrNull()
        }

        val equipamentName = equipamentCardNames[equipamentIndex]
        val equipamentCard = player.getCardByName(equipamentName)

        if(equipamentCard == null) {
            println("Equipamento nao encontrado")
            return
        }

        println("Monstros disponiveis para equipar:")
        monstersName.forEachIndexed { cardIndex, cardName ->
            println("$cardIndex- $cardName")
        }

        print("Digite o número do monstro que deseja equipar: ")
        var monsterIndex = readln().toIntOrNull()
        while(monsterIndex == null) {
            println("Digite um valor válido")
            monsterIndex = readln().toIntOrNull()
        }

        val monsterName = monstersName[monsterIndex]
        val monster = player.getMonsterByName(monsterName)

        if(monster == null) {
           println("Monstro nao encontrado")
           return
        }

        monster.appliedCards.add(equipamentCard)
    }

    private fun discardCard(player: Player) {
        println("${player.name} está descartando uma carta...")

        println("Cartas na sua mao: ")
        val cardsName: List<String> = player.getCardNames()
        cardsName.forEachIndexed { cardIndex, cardName ->
            println("$cardIndex- $cardName")
        }

        println("Digite o numero da carta que deseja descartar: ")
        var cardIndex = readln().toIntOrNull()
        while(cardIndex == null) {
            print("Digite um valor válido")
            cardIndex = readln().toIntOrNull()
        }

        player.removeCard(cardIndex)
    }

    private fun showCurrentHand(player: Player) {
        println("Mao atual de ${player.name}:\n$")
        val headers = listOf("Nome Carta", "ATK Base", "DEF Base", "Tipo", "Descricao")
        val cards = player.getCardList()
        val colWidths = headers.indices.map { i ->
            maxOf(
                headers[i].length,
                cards.maxOfOrNull {
                    when (i) {
                        0 -> it.name.length
                        1 -> it.attack.toString().length
                        2 -> it.defense.toString().length
                        3 -> it.type.toString().length
                        4 -> it.description.length
                        else -> 0
                    }
                } ?: 0
            )
        }

        printSeparator(colWidths)
        printRow(headers, colWidths)
        printSeparator(colWidths)

        for (card in cards) {
            val row = listOf(
                card.name,
                card.attack.toString(),
                card.defense.toString(),
                card.type.toString(),
                card.description
            )
            printRow(row, colWidths)
        }

        printSeparator(colWidths)
        println()
    }

    private fun performAttack(player: Player, opponnet: Player) {
        println("${player.name} está realizando um ataque...")

        val monstersName = player.getMonsterNames()
        val opponnetMonstersName = opponnet.getMonsterNames()

        if (monstersName.isEmpty() || opponnetMonstersName.isEmpty()) {
            println("Nao ha nenhum monstro posicionado")
            return
        }

        println("Monstros posicionados:")
        monstersName.forEachIndexed { cardIndex, cardName ->
            println("$cardIndex- $cardName")
        }

        println("Digite o número do monstro que deseja utilizar para atacar: ")
        var monsterIndex = readln().toIntOrNull()
        while(monsterIndex == null) {
            println("Digite um valor válido")
            monsterIndex = readln().toIntOrNull()
        }

        val monster: Monster = player.getMonsterByIndex(monsterIndex)

        println("Monstros posicionados do oponente:")
        opponnetMonstersName.forEachIndexed { cardIndex, cardName ->
            println("$cardIndex- $cardName")
        }

        println("Digite o número do monstro que deseja atacar: ")
        var opponnentMonsterIndex: Int? = readln().toIntOrNull()
        while(opponnentMonsterIndex == null) {
            println("Digite um valor válido")
            opponnentMonsterIndex = readln().toIntOrNull()
        }

        val opponnentMonster: Monster = opponnet.getMonsterByIndex(monsterIndex)

        val monsterAttkDefence: Int = if (!monster.isInDefenseState) monster.totalAttack() else monster.totalDefense()
        val opponnentMonsterAttkDefence: Int = if (!opponnentMonster.isInDefenseState) opponnentMonster.totalAttack() else opponnentMonster.totalDefense()

        val result = monsterAttkDefence - opponnentMonsterAttkDefence

        val damage: Int = abs(result)

        if(result < 0) {
            player.receiveDamage(damage)
            player.removeMonster(monsterIndex)
            println("O monstro ${monster.name} foi destruido!\nVoce recebeu $damage de dano!")
            return
        }
        if(result == 0) {
            println("Ambos os monstros foram destruidos!")
            return
        }

        opponnet.removeMonster(opponnentMonsterIndex)
        opponnet.receiveDamage(result)
        println("O monstro ${monster.name} do oponente foi destruido!\nVoce causou ao oponente $damage de dano!")
    }

    private fun changeMonsterState(player: Player) {
        println("${player.name} está alterando o estado de um monstro...")

        val monstersName = player.getMonsterNames()

        if (monstersName.isEmpty()) {
            print("Nao ha nenhum monstro posicionado")
            return
        }

        println("Monstros posicionados:")
        monstersName.forEachIndexed { cardIndex, cardName ->
            println("$cardIndex- $cardName")
        }

        println("Digite o número do monstro que deseja alterar o estado: ")
        var monsterIndex = readln().toIntOrNull()
        while(monsterIndex == null) {
            println("Digite um valor válido")
            monsterIndex = readln().toIntOrNull()
        }

        val monster: Monster = player.getMonsterByIndex(monsterIndex)

        monster.isInDefenseState = !monster.isInDefenseState
    }

    private fun skipTurn(player: Player) {
        println("${player.name} está pulando a vez...")
    }
}