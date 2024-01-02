import java.io.File
import java.io.PrintWriter

var numLinhas = -1
var numColunas = -1
var tabuleiroHumano: Array<Array<Char?>> = emptyArray()
var tabuleiroComputador: Array<Array<Char?>> = emptyArray()
var tabuleiroPalpitesDoHumano: Array<Array<Char?>> = emptyArray()
var tabuleiroPalpitesDoComputador: Array<Array<Char?>> = emptyArray()
const val EXEMPLO = "Coordenadas? (ex: 6,G)"

// Funcoes parte 1

/**
 * Valida se o tamanho do tabuleiro é válido (5x5, 7x7, 8x8 e 10x10).
 * Validada pelo Drop Project, nao alterar.
 * @param linhas Numero de linhas do tabuleiro.
 * @param colunas Numero de Colunas do tabuleiro.
 * @return True se o tamanho do tabuleiro for valido, false se nao for.
 */
fun tamanhoTabuleiroValido(linhas: Int?, colunas: Int?): Boolean {
    return when {
        linhas == null || colunas == null -> false
        linhas != colunas -> false
        linhas == 4 -> true
        linhas == 5 -> true
        linhas == 7 -> true
        linhas == 8 -> true
        linhas == 10 -> true
        else -> false
    }
}

/**
 * Processa se as coordenadas sao validas e aplicaveis ao número de linhas e colunas.
 * Validada pelo Drop Project, nao alterar.
 * @param coordenadas Coordenadas do barco a colocar, no formato (coordenada1,coordenada2).
 * @param linhas Numero de linhas do tabuleiro.
 * @param colunas Numero de colunas do tabuleiro.
 * @return Pair das coordenadas inseridas, ou, se as coordenadas não forem válidas, null.
 */
fun processaCoordenadas(coordenadas: String?, linhas: Int, colunas: Int): Pair<Int, Int>? {
    if (coordenadas == null) {
        return null
    }
    val coordenada1: Int
    val coordenada2: Char
    val separador: Char
    when {
        (coordenadas.length == 4) -> {
            coordenada1 = "${coordenadas[0]}${coordenadas[1]}".toIntOrNull() ?: 0
            coordenada2 = coordenadas[3]
            separador = coordenadas[2]
        }

        (coordenadas.length == 3) -> {
            coordenada1 = "${coordenadas[0]}".toIntOrNull() ?: 0
            coordenada2 = coordenadas[2]
            separador = coordenadas[1]
        }

        else -> return null
    }
    return when {
        separador != ',' -> null
        coordenada1 !in 1..linhas -> null
        coordenada2 !in 'A'..'A' + colunas - 1 -> null
        else -> Pair(coordenada1, coordenada2.code - ('A' - 1).code)
    }
}

/**
 * Cria a legenda horizontal consoante o número de colunas.
 * Validada pelo Drop Project, nao alterar.
 * @param colunas Numero de colunas do tabuleiro.
 * @return String com a legenda horizontal.
 */
fun criaLegendaHorizontal(colunas: Int): String {
    var contador = 0
    var legenda = ""
    while (contador < colunas) {
        when (contador) {
            0 -> legenda += 'A'
            else -> legenda += " | " + ('A' + contador)
        }
        contador++
    }
    return (legenda)
}

/**
 * Imprime o menu do jogo. Nao tem parametros nem return.
 */
fun printMenuJogo() {
    println()
    println("> > Batalha Naval < <")
    println()
    println("1 - Definir Tabuleiro e Navios")
    println("2 - Jogar")
    println("3 - Gravar")
    println("4 - Ler")
    println("0 - Sair")
    println()
}

/**
 * Pergunta e retorna qual a opcao do menu escolhida pelo utilizador, so retornando
 * quando for inserida uma opcao valida.
 * @return Opcao do menu escolhida pelo utilizador.
 */
fun escolhaOpcao(): Int {
    do {
        val opcao = readLine()?.toIntOrNull()
        if (opcao == null || opcao !in 0..4) {
            println("!!! Opcao invalida, tente novamente")
        } else {
            return opcao
        }
    } while (opcao == null || opcao !in 0..4)
    return 0
}

/**
 * Gera um tabuleiro com os números de linhas e colunas que recebe como argumentos
 * Validada pelo Drop Project, nao alterar.
 * @param linhas Numero de linhas do tabuleiro.
 * @param colunas Numero de colunas do tabuleiro.
 * @return ‘String’ com o tabuleiro de jogo.
 */
fun criaTerreno(linhas: Int, colunas: Int): Array<String> {
    val tabuleiro: Array<String> = Array(linhas + 1) { "" }
    tabuleiro[0] = "| " + criaLegendaHorizontal(colunas) + " |"
    for (linha in 1..linhas) {
        for (coluna in 0..colunas - 1) {
            tabuleiro[linha] += "|   "
            if (coluna == colunas - 1) {
                tabuleiro[linha] += "|"
            }
        }
        tabuleiro[linha] += " $linha"
    }
    return tabuleiro
}

/**
 * Pede as coordenadas e orientacao do barco ao utilizador e valida-as.
 * Nao insere o barco (por implementar)
 * @param linhas Numero de linhas do tabuleiro.
 * @param colunas Numero de colunas do tabuleiro.
 */
fun inserirBarco(linhas: Int, colunas: Int) {
    println("Insira as coordenadas do navio:")
    do {
        println(EXEMPLO)
        val coordenadas = readLine()
        if (coordenadas != null && coordenadas == "-1") {
            return
        }
        if (processaCoordenadas(coordenadas, linhas, colunas) == null) {
            println("!!! Coordenadas invalidas, tente novamente")
        }
    } while (processaCoordenadas(coordenadas, linhas, colunas) == null)
    println("Insira a orientacao do navio:")
    do {
        println("Orientacao? (N, S, E, O)")
        val orientacao = readLine()
        if (orientacao != null && orientacao == "-1") {
            return
        }
        if (orientacao != "N" && orientacao != "S" && orientacao != "E" && orientacao != "O") {
            println("!!! Orientacao invalida, tente novamente")
        }
    } while (orientacao != "N" && orientacao != "S" && orientacao != "E" && orientacao != "O")
}

/**
 * Imprime o tabuleiro passado como parâmetro.
 */
fun imprimeTabuleiro(tabuleiro: Array<Array<Char?>>, tabuleiroReal: Boolean) {
    val tabuleiroImpresso = obtemMapa(tabuleiro, tabuleiroReal)
    for (linha in 0..tabuleiro.size) {
        println(tabuleiroImpresso[linha])
    }
}

/**
 * Pede os dados de um determinado navio repetidamente até que estes sejam válidos e coloca o barco no tabuleiro que
 * recebeu como parâmetro, retornando esse tabuleiro.
 * @param tabuleiro Tabuleiro real do humano onde o navio sera inserido
 * @param dimensao Dimensao do navio a colocar
 */
fun colocarBarco(tabuleiro: Array<Array<Char?>>, dimensao: Int): Array<Array<Char?>> {
    var parCoordenadas: Pair<Int, Int>?
    val tipoNavio = when (dimensao) {
        1 -> "submarino"
        2 -> "contra-torpedeiro"
        3 -> "navio-tanque"
        else -> "porta-avioes"
    }
    do {
        do {
            println("Insira as coordenadas de um $tipoNavio:")
            println(EXEMPLO)
            val coordenadas = readLine()
            if (coordenadas != null && coordenadas == "-1") {
                return emptyArray()
            }
            parCoordenadas = processaCoordenadas(coordenadas, tabuleiro.size, tabuleiro[0].size)
            if (parCoordenadas == null) {
                println("!!! Posicionamento invalido, tente novamente")
            }
        } while (parCoordenadas == null)
        var orientacao: String? = "E"
        if (dimensao != 1) {
            println("Insira a orientacao do navio:")
            do {
                println("Orientacao? (N, S, E, O)")
                orientacao = readLine()
                if (orientacao != null && orientacao == "-1") {
                    return emptyArray()
                }
                if (orientacao != "N" && orientacao != "S" && orientacao != "E" && orientacao != "O") {
                    println("!!! Orientacao invalida, tente novamente")
                }
            } while (orientacao != "N" && orientacao != "S" && orientacao != "E" && orientacao != "O")
        }
        val invalido =
            !insereNavio(tabuleiro, parCoordenadas.first, parCoordenadas.second, orientacao!!, dimensao)
        if (invalido) {
            println("!!! Posicionamento invalido, tente novamente")
        }
    } while (invalido)
    return tabuleiro
}

/**
 * Calcula o número de navios que um determinado tabuleiro deve ter, e chama o colocarBarco() para cada um dos tipos de
 * navios até o total desse tipo de navios estar colocado no tabuleiro.
 * @param tabuleiro Tabuleiro real do humano, onde os navios serão inseridos.
 * @return Tabuleiro real do humano preenchido com os navios que aquele tamanho de tabuleiro deve incluir.
 */
fun preencherTabuleiroHumano(tabuleiro: Array<Array<Char?>>): Array<Array<Char?>> {
    val numNavios = calculaNumNavios(tabuleiro.size, tabuleiro[0].size)
    val numSubmarinos = numNavios[0]
    val numContraTorpedeiros = numNavios[1]
    val numNaviosTanque = numNavios[2]
    val numPortaAvioes = numNavios[3]
    for (submarino in 1..numSubmarinos) {
        if (colocarBarco(tabuleiro, 1).contentEquals(emptyArray())) {
            return emptyArray()
        }
        imprimeTabuleiro(tabuleiro, true)
    }
    for (contraTorpedeiro in 1..numContraTorpedeiros) {
        if (colocarBarco(tabuleiro, 2).contentEquals(emptyArray())) {
            return emptyArray()
        }
        imprimeTabuleiro(tabuleiro, true)
    }
    for (navioTanque in 1..numNaviosTanque) {
        if (colocarBarco(tabuleiro, 3).contentEquals(emptyArray())) {
            return emptyArray()
        }
        imprimeTabuleiro(tabuleiro, true)
    }
    for (portaAvioes in 1..numPortaAvioes) {
        if (colocarBarco(tabuleiro, 4).contentEquals(emptyArray())) {
            return emptyArray()
        }
        imprimeTabuleiro(tabuleiro, true)
    }
    return tabuleiro
}
// Funcoes parte 2

/**
 * Pede o número de linhas e o número de colunas do tabuleiro ao utilizador. Chama o criaTabuleiroVazio() para criar um
 * tabuleiro com esse numero de linhas e de colunas, e envia esse tabuleiro para o preencherTabuleiroHumano para que
 * este possa ser preenchido com os navios que deve ter. Chama o preencherTabuleiroComputador() para criar um tabuleiro
 * do computador com as mesmas dimensões, e cria também os dois tabuleiros de palpites para que o jogo possa ser jogado.
 * @return Array<Array<Char?>> com o tabuleiro real do humano.
 */
fun tabuleiros(): Array<Array<Char?>> {
    var totalLinhas: Int?
    var totalColunas: Int?
    println()
    println("> > Batalha Naval < <")
    println()
    println("Defina o tamanho do tabuleiro:")
    do {
        do {
            println("Quantas linhas?")
            totalLinhas = readLine()?.toIntOrNull()
            if (totalLinhas == null) {
                println("!!! Número de linhas invalidas, tente novamente")
            }
            if (totalLinhas == -1) {
                return emptyArray()
            }
        } while (totalLinhas == null)
        do {
            println("Quantas colunas?")
            totalColunas = readLine()?.toIntOrNull()
            if (totalColunas == null) {
                println("!!! Número de colunas invalidas, tente novamente")
            }
            if (totalColunas == -1) {
                return emptyArray()
            }
        } while (totalColunas == null)
        if (!tamanhoTabuleiroValido(totalLinhas, totalColunas)) {
            println("\n!!! Tamanho de tabuleiro invalido, tente novamente\n")
        }
    } while (!tamanhoTabuleiroValido(totalLinhas, totalColunas))
    if (totalLinhas == null || totalColunas == null) {
        return emptyArray()
    }
    numLinhas = totalLinhas
    numColunas = totalColunas
    tabuleiroHumano = criaTabuleiroVazio(numLinhas, numColunas)
    imprimeTabuleiro(tabuleiroHumano, true)
    tabuleiroHumano = preencherTabuleiroHumano(tabuleiroHumano)
    if (tabuleiroHumano.contentEquals(emptyArray())){
        return emptyArray()
    }
    tabuleiroComputador = criaTabuleiroVazio(numLinhas, numColunas)
    preencheTabuleiroComputador(tabuleiroComputador, calculaNumNavios(numLinhas, numColunas))
    tabuleiroPalpitesDoComputador = criaTabuleiroVazio(numLinhas, numColunas)
    tabuleiroPalpitesDoHumano = criaTabuleiroVazio(numLinhas, numColunas)
    var verMapa: String?
    do {
        println("Pretende ver o mapa gerado para o Computador? (S/N)")
        verMapa = readLine()
        if (verMapa == "-1") {
            return tabuleiroHumano
        } else if ((verMapa == null || (verMapa != "S" && verMapa != "N"))) {
            println("!!! Resposta invalida.")
        }

    } while (verMapa == null || (verMapa != "S" && verMapa != "N"))
    if (verMapa == "S") {
        imprimeTabuleiro(tabuleiroComputador, true)
    }
    return tabuleiroHumano
}

/**
 * Retorna o numero de navios de cada tipo de navio que um tabuleiro com um determinado tamanho deverá ter.
 * @param linhas Numero inteiro com o numero de linhas do tabuleiro
 * @param colunas Numero inteiro com o numero de colunas do tabuleiro
 * @return Array de inteiros com o número de cada tipo de navios, pela ordem: submarinos (1), contra-torpedeiros (2),
 * navios-tanque (3), porta-avioes (4).
 */
fun calculaNumNavios(linhas: Int, colunas: Int): Array<Int> {
    return when {
        linhas == 4 && colunas == 4 -> arrayOf(2, 0, 0, 0)
        linhas == 5 && colunas == 5 -> arrayOf(1, 1, 1, 0)
        linhas == 7 && colunas == 7 -> arrayOf(2, 1, 1, 1)
        linhas == 8 && colunas == 8 -> arrayOf(2, 2, 1, 1)
        linhas == 10 && colunas == 10 -> arrayOf(3, 2, 1, 1)
        else -> emptyArray()
    }
}

/**
 * Cria um tabuleiro vazio com o tamanho definido.
 * @param linhas Numero inteiro com o numero de linhas do tabuleiro
 * @param colunas Numero inteiro com o numero de colunas do tabuleiro
 * @return Array de arrays de Char? com todos os char colocados a null
 */
fun criaTabuleiroVazio(linhas: Int, colunas: Int): Array<Array<Char?>> {
    return Array(linhas) { Array(colunas) { null } }
}

/**
 * Recebe coordenadas de um ponto, e verifica se este esta contido no tabuleiro.
 * @param tabuleiro Tabuleiro onde verifica se a coordenada esta contida.
 * @param linha Coordenada 1 a verificar, correspondente à linha.
 * @param coluna Coordenada 2 a verificar, correspondente à coluna.
 * @return True, se as coordenadas estiverem contidas no tabuleiro, ou false, se nao estiverem.
 */
fun coordenadaContida(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int): Boolean {
    return (linha in 1..tabuleiro.size) && (coluna in 1..tabuleiro[linha - 1].size)
}

/**
 * Recebe um array de pairs de inteiros, e retorna esse array removendo os elementos que são Pair(0,0)
 * @param coordenadas Lista de coordenadas, na forma Array<Pair<Int, Int>>
 * @return Array recebido sem os elementos Pair(0,0)
 */
fun limparCoordenadasVazias(coordenadas: Array<Pair<Int, Int>>): Array<Pair<Int, Int>> {
    var count = 0
    for (contador in 0 until coordenadas.size) {
        if (coordenadas[contador] != Pair(0, 0)) {
            count++
        }
    }
    val validos = Array(count) { Pair(0, 0) }
    var elemento = 0
    for (posicao in 0 until validos.size) {
        while (coordenadas[elemento] == Pair(0, 0)) {
            elemento++
        }
        validos[posicao] = coordenadas[elemento]
        elemento++
    }
    return validos
}

/**
 * Junta dois arrays de coordenadas.
 * @param listaCoordenadas1 Array de Pairs de coordenadas
 * @param listaCoordenadas2 Array de Pairs de coordenadas
 * @return Junçao dos dois parametros recebidos.
 */
fun juntarCoordenadas(
    listaCoordenadas1: Array<Pair<Int, Int>>, listaCoordenadas2: Array<Pair<Int, Int>>
): Array<Pair<Int, Int>> {
    return listaCoordenadas1 + listaCoordenadas2
}

/**
 * Gera as coordenadas do navio a colocar. Caso o navio fique fora do tabuleiro, retorna um array vazio.
 * @param tabuleiro Tabuleiro onde colocar o navio.
 * @param linha Linha onde a primeira casa do navio é colocada.
 * @param coluna Coluna onde a primeira casa do navio é colocada.
 * @param orientacao Orientacao do navio "N","S","O","E"
 * @param dimensao Numero de casas do navio
 * @return Retorna um array das coordenadas do navio a colocar, ou, se o navio nao couver no tabuleiro, um emptyArray().
 */
fun gerarCoordenadasNavio(
    tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, orientacao: String, dimensao: Int
): Array<Pair<Int, Int>> {
    val coordenadasNavio = Array(dimensao) { Pair(0, 0) }
    val andarLinhas = when (orientacao) {
        "N" -> -1
        "S" -> 1
        else -> 0
    }
    val andarColunas = when (orientacao) {
        "O" -> -1
        "E" -> 1
        else -> 0
    }
    for (i in 0 until dimensao) {
        if (coordenadaContida(tabuleiro, linha + i * andarLinhas, coluna + i * andarColunas)) {
            coordenadasNavio[i] = Pair(linha + i * andarLinhas, coluna + i * andarColunas)
        } else {
            return emptyArray()
        }
    }
    return coordenadasNavio
}

/**
 * Recebe dois arrays, e junta-os, removendo as coordenadas que se encontravam repetidas entre os dois.
 * @param coordenadas1 Array de pairs de coordenadas
 * @param coordenadas2 Array de pairs de coordenadas
 * @return Junção dos arrays recebidos como parâmetros, após removidas as coordenadas repetidas entre os dois.
 */
fun eliminarCoordenadasRepetidas(
    coordenadas1: Array<Pair<Int, Int>>, coordenadas2: Array<Pair<Int, Int>>
): Array<Pair<Int, Int>> {
    val array2 = coordenadas2
    for (coordenada in coordenadas1) {
        for (posicao in 0 until coordenadas2.size) {
            if (array2[posicao] == coordenada) {
                array2[posicao] = Pair(0, 0)
            }
        }
    }
    return juntarCoordenadas(coordenadas1, limparCoordenadasVazias(array2))
}

/**
 * Gera as coordenadas das fronteiras de um navio.
 * @param tabuleiro Tabuleiro onde o navio está inserido
 * @param linha Linha de base onde começa o navio
 * @param coluna Coluna de base onde começa o navio
 * @param orientacao Lado para onde o navio esta orientado (N,S,O,E)
 * @param dimensao Casas ocupadas pelo navio
 * @return Retorna um array de Pair<Int,Int> com todos os pares de coordenadas das fronteiras do navio.
 */
fun gerarCoordenadasFronteira(
    tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, orientacao: String, dimensao: Int
): Array<Pair<Int, Int>> {
    val coordenadasNavio = gerarCoordenadasNavio(tabuleiro, linha, coluna, orientacao, dimensao)
    var coordenadasFronteiraTotal = Array(0) { Pair(0, 0) }
    for (navio in coordenadasNavio) {
        var coordenadasFronteiraCasa = arrayOf(
            Pair(navio.first - 1, navio.second - 1),
            Pair(navio.first - 1, navio.second),
            Pair(navio.first - 1, navio.second + 1),
            Pair(navio.first, navio.second - 1),
            Pair(navio.first, navio.second + 1),
            Pair(navio.first + 1, navio.second - 1),
            Pair(navio.first + 1, navio.second),
            Pair(navio.first + 1, navio.second + 1)
        )
        for (posicao in 0 until coordenadasFronteiraCasa.size) {
            if (!coordenadaContida(
                    tabuleiro, coordenadasFronteiraCasa[posicao].first, coordenadasFronteiraCasa[posicao].second
                ) || Pair(
                    coordenadasFronteiraCasa[posicao].first, coordenadasFronteiraCasa[posicao].second
                ) in coordenadasNavio
            ) {
                coordenadasFronteiraCasa[posicao] = Pair(0, 0)
            }
        }
        coordenadasFronteiraCasa = limparCoordenadasVazias((coordenadasFronteiraCasa))
        coordenadasFronteiraTotal = eliminarCoordenadasRepetidas(coordenadasFronteiraTotal, coordenadasFronteiraCasa)
    }
    return coordenadasFronteiraTotal
}

/**
 * Verifica se todos os pares do array coordenadas são coordenadas válidas do tabuleiro enviado.
 * @param tabuleiro Array de arrays de Char? com o tabuleiro a verificar
 * @param coordenadas Array de pares de inteiros com as coordenadas a verificar
 * @return True, se todos os pares de coordenadas forem válidos, ou false, se algum não for
 */
fun estaLivre(tabuleiro: Array<Array<Char?>>, coordenadas: Array<Pair<Int, Int>>): Boolean {
    for (coordenada in coordenadas) {
        if (!coordenadaContida(
                tabuleiro, coordenada.first, coordenada.second
            ) || tabuleiro[coordenada.first - 1][coordenada.second - 1] != null
        ) {
            return false
        }
    }
    return true
}

/**
 * Insere um navio na orientação Este no tabuleiro que recebe como parametro. Se o navio não tiver água suficiente à
 * volta ou se sair do tabuleiro, o inserir navio falha, e a funcao retorna false e nao altera o tabuleiro. Se resultar,
 * a funcao retorna true.
 */
fun insereNavioSimples(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, dimensao: Int): Boolean {
    val coordenadasNavio = gerarCoordenadasNavio(tabuleiro, linha, coluna, "E", dimensao)
    val coordenadasFronteira = gerarCoordenadasFronteira(tabuleiro, linha, coluna, "E", dimensao)
    if (!estaLivre(tabuleiro, coordenadasFronteira) || !estaLivre(
            tabuleiro, coordenadasNavio
        ) || coordenadasNavio.contentEquals(emptyArray())
    ) {
        return false
    } else {
        for (coordenadas in coordenadasNavio) {
            tabuleiro[coordenadas.first - 1][coordenadas.second - 1] = dimensao.digitToChar()
        }
    }
    return true
}

/**
 * Insere um navio no tabuleiro. O navio é colocado nas coordenadas (linha, coluna), na orientação definida e com a
 * dimensão definida. Se o navio sair do tabuleiro ou se não tiver as fronteiras todas livres, a função retorna falso
 * e não altera o tabuleiro.
 */
fun insereNavio(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, orientacao: String, dimensao: Int): Boolean {
    if (orientacao == "E") {
        return insereNavioSimples(tabuleiro, linha, coluna, dimensao)
    }
    val coordenadasNavio = gerarCoordenadasNavio(tabuleiro, linha, coluna, orientacao, dimensao)
    val coordenadasFronteira = gerarCoordenadasFronteira(tabuleiro, linha, coluna, orientacao, dimensao)
    if (!estaLivre(tabuleiro, coordenadasFronteira) || !estaLivre(
            tabuleiro, coordenadasNavio
        ) || coordenadasNavio.contentEquals(emptyArray())
    ) {
        return false
    } else {
        for (coordenadas in coordenadasNavio) {
            tabuleiro[coordenadas.first - 1][coordenadas.second - 1] = dimensao.digitToChar()
        }
    }
    return true
}

/**
 * Verifica se as posições adjacentes a uma determinada posição são válidas. Estabelece as 8 posições à volta, e
 * verifica se cada uma delas é válida como coordenada da fronteira de um navio. Para ser válida, a coordenada tem de
 * não estar contida ou de estar livre, para que os navios não estejam encostados. Se isto não for verdade, a funcao
 * retorna logo falso. Se percorrer as coordenadas todas sem encontrar uma posição inválida, retorna true.
 */
fun posicoesAdjacentesValidas(tabuleiro: Array<Array<Char?>>, posicaoAtual: Pair<Int, Int>): Boolean {
    val arrayPosicoesAdjacentes = Array(8){Pair(0,0)}
    arrayPosicoesAdjacentes[0] = Pair(posicaoAtual.first - 1, posicaoAtual.second)
    arrayPosicoesAdjacentes[1] = Pair(posicaoAtual.first + 1, posicaoAtual.second)
    arrayPosicoesAdjacentes[2] = Pair(posicaoAtual.first, posicaoAtual.second - 1)
    arrayPosicoesAdjacentes[3] = Pair(posicaoAtual.first, posicaoAtual.second + 1)
    arrayPosicoesAdjacentes[4] = Pair(posicaoAtual.first - 1, posicaoAtual.second - 1)
    arrayPosicoesAdjacentes[5] = Pair(posicaoAtual.first - 1, posicaoAtual.second + 1)
    arrayPosicoesAdjacentes[6] = Pair(posicaoAtual.first + 1, posicaoAtual.second - 1)
    arrayPosicoesAdjacentes[7] = Pair(posicaoAtual.first + 1, posicaoAtual.second + 1)
    for (coordenadas in arrayPosicoesAdjacentes){
        if (!(!coordenadaContida(tabuleiro, coordenadas.first, coordenadas.second) || estaLivre(tabuleiro, arrayOf(coordenadas)))){
            return false
        }
    }
    return true
}

/**
 * Insere um navio com a dimensão recebida como parâmetro num local aleatório do navio, certificando-se de que o local
 * escolhido é um local válido para um navio. Tenta colocar o navio 5 vezes, e, se após essas 5 vezes não encontrar um
 * posicionamento válido para o navio, retorna falso. Se colocar o navio, retorna verdadeiro.
 */
fun insereNavioRandom(tabuleiro: Array<Array<Char?>>, dimensaoNavio: Int): Boolean {
    val linhas = tabuleiro.size
    val colunas = tabuleiro[0].size
    var coordenadasPossiveis: Array<Pair<Int, Int>> = emptyArray()
    for (linha in 0 until linhas) {
        for (coluna in 0 until colunas) {
            val posicaoAtual = Pair(linha + 1, coluna + 1)
            if (posicoesAdjacentesValidas(tabuleiro, posicaoAtual)) {
                coordenadasPossiveis = juntarCoordenadas(coordenadasPossiveis, arrayOf(posicaoAtual))
            }
        }
    }
    var contaTentativas = 0
    do {
        if (contaTentativas == 5) {
            return false
        }
        val orientacao = when ((1..4).random()) {
            1 -> "N"
            2 -> "S"
            3 -> "O"
            else -> "E"
        }
        val tiro = coordenadasPossiveis.random()
        contaTentativas++
    } while (!insereNavio(tabuleiro, tiro.first, tiro.second, orientacao, dimensaoNavio))
    return true
}

fun preencheTabuleiroComputador(tabuleiroComputador: Array<Array<Char?>>, navios: Array<Int>) {
    var tabuleiro: Array<Array<Char?>>
    val numSubmarinos = navios[0]
    val numContraTorpedeiros = navios[1]
    val numNaviosTanque = navios[2]
    val numPortaAvioes = navios[3]
    do {
        tabuleiro = criaTabuleiroVazio(tabuleiroComputador.size, tabuleiroComputador[0].size)
        var sucesso = true
        for (submarino in 1..numSubmarinos) {
            if (!insereNavioRandom(tabuleiro, 1)) {
                sucesso = false
            }
        }
        for (contraTorpedeiro in 1..numContraTorpedeiros) {
            if (!insereNavioRandom(tabuleiro, 2)) {
                sucesso = false
            }
        }
        for (navioTanque in 1..numNaviosTanque) {
            if (!insereNavioRandom(tabuleiro, 3)) {
                sucesso = false
            }
        }
        for (portaAvioes in 1..numPortaAvioes) {
            if (!insereNavioRandom(tabuleiro, 4)) {
                sucesso = false
            }
        }
    } while (!sucesso)
    for (linha in 0 until tabuleiroComputador.size) {
        for (coluna in 0 until tabuleiroComputador[0].size) {
            tabuleiroComputador[linha][coluna] = tabuleiro[linha][coluna]
        }
    }
}

/**
 * ele recebe um tabuleiro de palpites
 * ele quer saber se em cada tabuleiro, a linha e a coluna tem um navio completo ou só uma parte desse navio. Basicamente,
 * está função ajuda a perceber se o navio está parcialmente afundado ou completamente afundado ou inteiro. Vai usar
 * o lança tiro, contar navios dimensao.
 */
fun navioCompleto(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int): Boolean {
    val guardar = tabuleiro[linha - 1][coluna - 1]

    if (guardar == null || guardar == 'X'){
        return false

    }
    var posicao = 0
    var count = 0
    while (coordenadaContida(tabuleiro, linha, coluna + posicao) && tabuleiro[linha - 1][coluna - 1 + posicao] == guardar) {
        posicao++
        count++
    }
    posicao = 1
    while (coordenadaContida(tabuleiro, linha, coluna - posicao) && tabuleiro[linha - 1][coluna - 1 - posicao] == guardar) {
        posicao++
        count++
    }
    if (count.digitToChar() == guardar) {
        return true
    }
    posicao = 0
    count = 0
    while (coordenadaContida(tabuleiro, linha + posicao, coluna) && tabuleiro[linha - 1 + posicao][coluna - 1] == guardar) {
        posicao++
        count++
    }
    posicao= 1
    while (coordenadaContida(tabuleiro, linha - posicao, coluna) && tabuleiro[linha - 1 - posicao][coluna - 1] == guardar) {
        posicao++
        count++
    }
    if (count.digitToChar() == guardar) {
        return true
    }
    return false
}

/**
 * Cria o desenho do mapa correspondente ao tabuleiro que recebe, para poder mostrar ao utilizador.
 * @param tabuleiro Array<Array<Char?>> do tabuleiro que queremos transformar (tabuleiroHumano OU tabuleiroComputador)
 * @param tabuleiroReal True, se o tabuleiro for o real, ou false, se for um tabuleiro de palpites
 * @return Array de strings contendo o mapa para mostrar no stdout (incluindo a legenda)
 */
fun obtemMapa(tabuleiro: Array<Array<Char?>>, tabuleiroReal: Boolean): Array<String> {
    val mapa = Array(tabuleiro.size + 1) { "" }
    mapa[0] = "| " + criaLegendaHorizontal(tabuleiro[0].size) + " |"
    for (linha in 0 until tabuleiro.size) {
        mapa[linha + 1] += "|"
        for (coluna in 0 until tabuleiro[linha].size) {
            if (tabuleiroReal) {
                when (tabuleiro[linha][coluna]) {
                    null -> mapa[linha + 1] += " ~ |"
                    '1' -> mapa[linha + 1] += " 1 |"
                    '2' -> mapa[linha + 1] += " 2 |"
                    '3' -> mapa[linha + 1] += " 3 |"
                    '4' -> mapa[linha + 1] += " 4 |"
                }
            } else {
                mapa[linha + 1] += when (tabuleiro[linha][coluna]) {
                    '1' -> if (navioCompleto(tabuleiro, linha + 1, coluna + 1)) " 1 |" else " \u2081 |"
                    '2' -> if (navioCompleto(tabuleiro, linha + 1, coluna + 1)) " 2 |" else " \u2082 |"
                    '3' -> if (navioCompleto(tabuleiro, linha + 1, coluna + 1)) " 3 |" else " \u2083 |"
                    '4' -> if (navioCompleto(tabuleiro, linha + 1, coluna + 1)) " 4 |" else " \u2084 |"
                    'X' -> " X |"
                    else -> " ? |"
                }
            }
        }
        mapa[linha + 1] += " ${linha + 1}"
    }
    return mapa
}

/**
 * Recebe um pair de coordenadas e verifica o que está nesse local do tabuleiroReal recebido. Consoante o que se
 * encontrar nesse posicionamento, altera o tabuleiro de palpites.
 * @return ‘String’ com o que esse tiro gerou ("coordenadas invalidas", "Agua.", "Tiro num submarino", etc.)
 */
fun lancarTiro(tabuleiroReal: Array<Array<Char?>>, tabuleiroPalpites: Array<Array<Char?>>, coordenadas: Pair<Int,Int>): String {

    val linha = coordenadas.first
    val coluna = coordenadas.second

    if (linha < 1 || linha > tabuleiroReal.size && coluna < 1 || coluna > tabuleiroReal.size) {
        return "Coordenadas Inválidas"
    }
    when(tabuleiroReal[linha - 1][coluna - 1]) {
        null -> {
            tabuleiroPalpites[linha - 1][coluna - 1] = 'X'
            return "Agua."
        }
        '1' -> {
            tabuleiroPalpites[linha - 1][coluna - 1] = '1'
            return "Tiro num submarino."
        }
        '2' -> {
            tabuleiroPalpites[linha - 1][coluna - 1] = '2'
            return "Tiro num contra-torpedeiro."
        }
        '3' -> {
            tabuleiroPalpites[linha - 1][coluna - 1] = '3'
            return "Tiro num navio-tanque."
        }
        else -> {
            return "Tiro num porta-avioes."
        }

    }

}

/**
 * Gera aleatoriamente um tiro do computador para o tabuleiro do Humano, retornando as coordenadas desse tiro. Gera
 * sempre um tiro numa casa vazia, isto é, em que ainda não tenha sido lançado qualquer tiro.
 */
fun geraTiroComputador(tabuleiroPalpitesComputador: Array<Array<Char?>>): Pair<Int, Int> {
    val coordenadas = Array(tabuleiroPalpitesComputador.size * tabuleiroPalpitesComputador.size) {Pair(0, 0)}
    var casa = 0
    for (linha in 0 until tabuleiroPalpitesComputador.size) {
        for (coluna in 0 until tabuleiroPalpitesComputador[0].size) {
            if (tabuleiroPalpitesComputador[linha][coluna] == null) {
                coordenadas[casa] = Pair(linha + 1, coluna + 1)
                casa++
            }
        }
    }
    val coordenadasNull = limparCoordenadasVazias(coordenadas)
    return coordenadasNull.random()
}

/**
 * Copia um Array<Array<Char?>>
 */
fun copiaMatriz(matriz: Array<Array<Char?>>): Array<Array<Char?>> {
    val copiaMatriz: Array<Array<Char?>> = Array(matriz.size) { arrayOf(null) }
    for (linha in 0 until matriz.size) {
        copiaMatriz[linha] = Array(matriz[linha].size) { null }
        for (coluna in 0 until matriz[linha].size) {
            copiaMatriz[linha][coluna] = matriz[linha][coluna]
        }
    }
    return copiaMatriz
}

/**
 * Conta os navios de uma determinada dimensão que existem completos no tabuleiro. Para tal, cria uma copia do
 * tabuleiro, para podermos ir fazendo as verificações, e vai percorrendo essa copia linha a linha, de cima para baixo.
 * Quando encontra uma casa que não esteja vazia, procura o resto do navio na horizontal e na vertical. Se encontrar
 * um navio completo, torna as casas desse navio em null (para que não sejam contadas repetidamente) e adiciona 1 ao
 * total. Depois de percorrer a cópia do tabuleiro toda, retorna esse total.
 */
fun contarNaviosDeDimensao(tabuleiro: Array<Array<Char?>>, dimensao: Int): Int {
    val mapa = copiaMatriz(tabuleiro)
    var total = 0
    for (linha in 0 until tabuleiro.size) {
        for (coluna in 0 until tabuleiro[0].size) {
            if (mapa[linha][coluna] != null) {
                var contagem = 0
                for (i in 0 until dimensao) {
                    if (coluna + i < mapa[0].size && mapa[linha][coluna + i] == dimensao.digitToChar()) {
                        contagem++
                    }
                }
                if (contagem == dimensao) {
                    total++
                    for (i in 0 until dimensao) {
                        mapa[linha][coluna + i] = null
                    }
                } else {
                    contagem = 0
                    for (i in 0 until dimensao) {
                        if (linha + i < mapa.size && mapa[linha + i][coluna] == dimensao.digitToChar()) {
                            contagem++
                        }
                    }
                    if (contagem == dimensao) {
                        total++
                        for (i in 0 until dimensao) {
                            mapa[linha + i][coluna] = null
                        }
                    }
                }
            }
        }
    }
    return total
}

/* Eu fiz navios esperados[dimensao -1] porque os naviosEsperados começam na posição 0, e a dimensao começa no 1 entao
tive que diminuir para chegar até a posiçao zero. Com isso os navios de dimensao 1 sao os submarinos, de dimensao 2 sao
os contra topedeiros e assim por diante.
*/

fun venceu(tabuleiro: Array<Array<Char?>>): Boolean {
    val naviosEsperados: Array<Int> = calculaNumNavios(tabuleiro.size, tabuleiro[0].size)

    for (dimensao in 1 .. naviosEsperados.size) {
        val naviosDerrubados = contarNaviosDeDimensao(tabuleiro, dimensao)
        if (naviosEsperados[dimensao - 1] != naviosDerrubados) {
            return false
        }
    }
    return true
}

/**
 * Le um ficheiro de jogo. (Assume que esse ficheiro existe e que tem o formato correto, nao sabendo lidar com casos em
 * que isto nao aconteça.
 */
fun lerJogo(ficheiro: String, tipoTabuleiro: Int): Array<Array<Char?>> {
    val ficheiroGravado = File(ficheiro).readLines()
    numLinhas = ficheiroGravado[0][0].digitToInt()
    numColunas = ficheiroGravado[0][2].digitToInt()
    val linhasNoFicheiro = 3 * tipoTabuleiro + 1 + numLinhas * (tipoTabuleiro - 1) until 3 * tipoTabuleiro + 1 + numLinhas * tipoTabuleiro
    val tabuleiro = criaTabuleiroVazio(numLinhas,numColunas)
    var linha = linhasNoFicheiro.min()
    while(linha in linhasNoFicheiro){
        var coluna = 0
        var contaVirgulas = 0
        while (contaVirgulas < numColunas && coluna < ficheiroGravado[linha].length){
            when (ficheiroGravado[linha][coluna]){
                ',' -> contaVirgulas++
                '1','2','3','4','X' -> tabuleiro[linha - linhasNoFicheiro.min()][contaVirgulas] = ficheiroGravado[linha][coluna]
            }
            coluna++
        }
        linha++
    }
    return tabuleiro
}

/**
 * Recebe um PrintWriter aberto do ficheiro onde gravamos os tabuleiros de jogo e grava um determinado
 * tabuleiro lá para dentro. Coloca um newLine, depois coloca o tabuleiro e termina no fim do tabuleiro, sem newLine no
 * final.
 * @param jogoGravado PrintWriter para o ficheiro onde queremos gravar o tabuleiro (já no local onde o queremos gravar)
 * @param tabuleiro Tabuleiro que pretendemos colocar no ficheiro
 */
fun gravarTabuleiro(jogoGravado: PrintWriter, tabuleiro: Array<Array<Char?>>) {
    for (linha in 0 until tabuleiro.size) {
        jogoGravado.println()
        for (coluna in 0 until tabuleiro[0].size) {
            when (tabuleiro[linha][coluna]) {
                'X' -> jogoGravado.print("X")
                '1', '2', '3', '4' -> jogoGravado.print(tabuleiro[linha][coluna])
            }
            if (coluna < tabuleiro[0].size - 1) {
                jogoGravado.print(",")
            }
        }
    }
}

/**
 * Grava o jogo no formato:
 * 5,5
 *
 * Jogador
 * Real
 * ,,,,3
 * 2,,,2,
 * 3,,3,,
 * ,4,4,,4
 * 3,,3,,
 *
 * Jogador
 * Palpites
 * ,,,,3
 * 2,,,2,
 * 3,,3,,
 * ,4,4,,4
 * 3,,3,,
 *
 * Computador
 * Real
 * ,,,,3
 * 2,,,2,
 * 3,,3,,
 * ,4,4,,4
 * 3,,3,,
 *
 * Computador
 * Palpites
 * ,,,,3
 * 2,,,2,
 * 3,,3,,
 * ,4,4,,4
 * 3,,3,,
 *
 * @param ficheiro String com o nome do ficheiro onde gravar
 * @param tabuleiroHumano Array<Array<Char?>> com o tabuleiro real do jogador
 * @param tabuleiroPalpitesDoHumano Array<Array<Char?>> com o tabuleiro dos palpites jogador
 * @param tabuleiroComputador Array<Array<Char?>> com o tabuleiro real do computador
 * @param tabuleiroPalpitesDoComputador Array<Array<Char?>> com o tabuleiro dos palpites do computador
 *
 */
fun gravarJogo(
    ficheiro: String,
    tabuleiroRealHumano: Array<Array<Char?>>,
    tabuleiroPalpitesHumano: Array<Array<Char?>>,
    tabuleiroRealComputador: Array<Array<Char?>>,
    tabuleiroPalpitesComputador: Array<Array<Char?>>
) {
    val jogoGravado = File(ficheiro).printWriter()
    jogoGravado.print("${tabuleiroRealHumano.size},${tabuleiroRealHumano[0].size}")
    jogoGravado.print("\n\nJogador\nReal")
    gravarTabuleiro(jogoGravado, tabuleiroRealHumano)
    jogoGravado.print("\n\nJogador\nPalpites")
    gravarTabuleiro(jogoGravado, tabuleiroPalpitesHumano)
    jogoGravado.print("\n\nComputador\nReal")
    gravarTabuleiro(jogoGravado, tabuleiroRealComputador)
    jogoGravado.print("\n\nComputador\nPalpites")
    gravarTabuleiro(jogoGravado, tabuleiroPalpitesComputador)
    jogoGravado.close()
    return
}

/**
 * Permite ao utilizador jogar o jogo, pedindo coordenadas e mostrando o seu tabuleiro de palpites.
 */
fun jogar() {
    while (true) {
        imprimeTabuleiro(tabuleiroPalpitesDoHumano, false)
        var coordenadas: Pair<Int, Int>?
        do {
            println("Indique a posição que pretende atingir")
            println(EXEMPLO)
            val leitura = readLine()
            if (leitura != null && leitura == "-1"){
                return
            }
            coordenadas = processaCoordenadas(leitura, numLinhas, numColunas)
            if (coordenadas == null || !estaLivre(tabuleiroPalpitesDoHumano, arrayOf(coordenadas))) {
                println(
                    "!!! Insira coordenadas que estejam contidas no tabuleiro e numa posição ainda não atingida, no " + "formato correto.\n"
                )
            }
        } while (coordenadas == null || !estaLivre(tabuleiroPalpitesDoHumano, arrayOf(coordenadas)))
        print(">>> HUMANO >>>${lancarTiro(tabuleiroComputador, tabuleiroPalpitesDoHumano, coordenadas)}")
        if (navioCompleto(tabuleiroPalpitesDoHumano, coordenadas.first, coordenadas.second)) {
            print(" Navio ao fundo!")
        }
        var venceu = false
        if (venceu(tabuleiroPalpitesDoHumano)) {
            println("\nPARABENS! Venceu o jogo!")
            venceu = true

        } else {
            println()
            val tiroComputador = geraTiroComputador(tabuleiroPalpitesDoComputador)
            println("Computador lançou tiro para a posição (${tiroComputador.first},${'A' + tiroComputador.second - 1})")
            print(">>> COMPUTADOR >>>${lancarTiro(tabuleiroHumano, tabuleiroPalpitesDoComputador, tiroComputador)}")
            if (navioCompleto(tabuleiroPalpitesDoComputador, tiroComputador.first, tiroComputador.second)) {
                print(" Navio ao fundo!")
            }
            println()
        }

        if (venceu(tabuleiroPalpitesDoComputador)) {
            println("OPS! O computador venceu o jogo!")
            venceu = true
        }
        if (venceu) {
            tabuleiroHumano = emptyArray()
            tabuleiroComputador = emptyArray()
            tabuleiroPalpitesDoHumano = emptyArray()
            tabuleiroPalpitesDoComputador = emptyArray()
                println("Prima enter para voltar ao menu principal")
                val enter = readLine()
            return
        }
        var escolha: String?
            println("Prima enter para continuar")
            escolha = readLine()
            if (escolha == "-1") {
                return
            }
    }
}

fun main() {
    while (true) {
        printMenuJogo()
        val opcao = escolhaOpcao()
        when (opcao) {
            0 -> return
            1 -> tabuleiros()
            2 -> {
                if (tabuleiroHumano.contentEquals(emptyArray())) {
                    println("!!! Tem que primeiro definir o tabuleiro do jogo, tente novamente")
                } else jogar()
            }

            3 -> if (tabuleiroHumano.contentEquals(emptyArray())) {
                println("!!! Tem que primeiro definir o tabuleiro do jogo, tente novamente")
            } else {
                var ficheiro: String?
                do {
                    println("Introduza o nome do ficheiro (ex: jogo.txt)")
                    ficheiro = readLine()
                } while (ficheiro == null)
                gravarJogo(
                    ficheiro,
                    tabuleiroHumano,
                    tabuleiroPalpitesDoHumano,
                    tabuleiroComputador,
                    tabuleiroPalpitesDoComputador
                )
                if (ficheiro != "-1"){
                    println("Tabuleiro ${tabuleiroHumano.size}x${tabuleiroHumano[0].size} gravado com sucesso")
                }
            }

            4 -> {
                var ficheiro: String?
                do {
                    println("Introduza o nome do ficheiro (ex: jogo.txt)")
                    ficheiro = readLine()
                } while (ficheiro == null)
                if (ficheiro != "-1"){
                    tabuleiroHumano = lerJogo(ficheiro, 1)
                    tabuleiroPalpitesDoHumano = lerJogo(ficheiro, 2)
                    tabuleiroComputador = lerJogo(ficheiro, 3)
                    tabuleiroPalpitesDoComputador = lerJogo(ficheiro, 4)
                    println("Tabuleiro ${numLinhas}x${numColunas} lido com sucesso")
                    imprimeTabuleiro(tabuleiroHumano, true)
                }
            }
        }
    }
}

