import java.io.File
import java.io.PrintWriter

var numLinhas = -1
var numColunas = -1
var tabuleiroHumano: Array<Array<Char?>> = emptyArray()
var tabuleiroComputador: Array<Array<Char?>> = emptyArray()
var tabuleiroPalpitesDoHumano: Array<Array<Char?>> = emptyArray()
var tabuleiroPalpitesDoComputador: Array<Array<Char?>> = emptyArray()

// Funcoes parte 1

/**
 * Valida se o tamanho do tabuleiro é válido (5x5, 7x7, 8x8 e 10x10).
 * Validada pelo Drop Project, nao alterar.
 * @param numLinhas Numero de linhas do tabuleiro.
 * @param numColunas Numero de Colunas do tabuleiro.
 * @return True se o tamanho do tabuleiro for valido, false se nao for.
 */
fun tamanhoTabuleiroValido(numLinhas: Int?, numColunas: Int?): Boolean{
    return when{
        numLinhas == null || numColunas == null -> false
        numLinhas != numColunas -> false
        numLinhas == 4 -> true
        numLinhas == 5 -> true
        numLinhas == 7 -> true
        numLinhas == 8 -> true
        numLinhas == 10 -> true
        else -> false
    }
}

/**
 * Processa se as coordenadas sao validas e aplicaveis ao número de linhas e colunas.
 * Validada pelo Drop Project, nao alterar.
 * @param coordenadas Coordenadas do barco a colocar, no formato (coordenada1,coordenada2).
 * @param numLinhas Numero de linhas do tabuleiro.
 * @param numColunas Numero de colunas do tabuleiro.
 */
fun processaCoordenadas(coordenadas: String?, numLinhas: Int, numColunas: Int): Pair<Int,Int>?{
    if (coordenadas == null){
        return null
    }
    val coordenada1: Int
    val coordenada2: Char
    val separador: Char
    when{
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
    return when{
        separador != ',' -> null
        coordenada1 !in 1..numLinhas -> null
        coordenada2 !in 'A'..'A' + numColunas - 1 -> null
        else -> Pair(coordenada1, coordenada2.code - ('A' - 1).code)
    }
}

/**
 * Cria a legenda horizontal consoante o número de colunas.
 * Validada pelo Drop Project, nao alterar.
 * @param numColunas Numero de colunas do tabuleiro.
 * @return String com a legenda horizontal.
 */
fun criaLegendaHorizontal(numColunas: Int): String{
    var contador = 0
    var legenda = ""
    while (contador < numColunas){
        when (contador){
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
fun printMenuJogo(){
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
fun escolhaOpcao(): Int{
    do {
        val opcao = readLine()?.toIntOrNull()
        if (opcao == null || opcao !in 0..4){
            println("!!! Opcao invalida, tente novamente")
        }
        else {
            return opcao
        }
    } while (opcao == null || opcao !in 0..4)
    return 0
}

/**
 * Gera um tabuleiro com os números de linhas e colunas que recebe como argumentos
 * Validada pelo Drop Project, nao alterar.
 * @param numLinhas Numero de linhas do tabuleiro.
 * @param numColunas Numero de colunas do tabuleiro.
 * @return ‘String’ com o tabuleiro de jogo.
 */
fun criaTerreno(numLinhas: Int, numColunas: Int): Array<String>{
    /*    var contador = 0
        var tabuleiro = "\n| " + criaLegendaHorizontal(numColunas) + " |"
        while (contador < numLinhas){
            tabuleiro += "\n|"
            var contaColunas = 0
            while (contaColunas < numColunas){
                tabuleiro += "   |"
                contaColunas++
            }
            tabuleiro += " " + (contador + 1)
            contador++
        }
        tabuleiro += "\n"
        return tabuleiro*/
    val tabuleiro: Array<String> = Array(numLinhas + 1){""}
    tabuleiro[0] = "| " + criaLegendaHorizontal(numColunas) + " |"
    for (linha in 1..numLinhas){
        for (coluna in 0..numColunas - 1){
            tabuleiro[linha] += "|   "
            if (coluna == numColunas - 1){
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
 * @param numLinhas Numero de linhas do tabuleiro.
 * @param numColunas Numero de colunas do tabuleiro.
 */
fun inserirBarco(numLinhas: Int, numColunas: Int) {
    println("Insira as coordenadas do navio:")
    do {
        println("Coordenadas? (ex: 6,G)")
        val coordenadas = readLine()
        if (coordenadas != null && coordenadas == "-1"){
            return
        }
        if (processaCoordenadas(coordenadas, numLinhas, numColunas) == null) {
            println("!!! Coordenadas invalidas, tente novamente")
        }
    } while (processaCoordenadas(coordenadas, numLinhas, numColunas) == null)
    println("Insira a orientacao do navio:")
    do {
        println("Orientacao? (N, S, E, O)")
        val orientacao = readLine()
        if (orientacao != null && orientacao == "-1"){
            return
        }
        if (orientacao != "N" && orientacao != "S" && orientacao != "E" && orientacao != "O") {
            println("!!! Orientacao invalida, tente novamente")
        }
    } while (orientacao != "N" && orientacao != "S" && orientacao != "E" && orientacao != "O")
}

// Funcoes parte 2

/**
 * Pede o número de linhas e o número de colunas do tabuleiro ao utilizador
 * e retorna uma ‘string’ com o tabuleiro e com os barcos.
 * @return ‘String’ com o tabuleiro, se este for valido
 */
fun tabuleiro(): Array<String>?{
    var numLinhas: Int?
    var numColunas: Int?
    println()
    println("> > Batalha Naval < <")
    println()
    println("Defina o tamanho do tabuleiro:")
    do {
        do {
            println("Quantas linhas?")
            numLinhas = readLine()?.toIntOrNull()
            if (numLinhas == null){
                println("!!! Número de linhas invalidas, tente novamente")
            }
            if (numLinhas == -1){
                return null
            }
        } while (numLinhas == null)
        do {
            println("Quantas colunas?")
            numColunas = readLine()?.toIntOrNull()
            if (numColunas == null){
                println("!!! Número de colunas invalidas, tente novamente")
            }
            if (numColunas == -1) {
                return null
            }
        } while (numColunas == null)
        if (!tamanhoTabuleiroValido(numLinhas, numColunas))
        {
            println("\n!!! Tamanho de tabuleiro invalido, tente novamente\n")
        }
    } while (!tamanhoTabuleiroValido(numLinhas, numColunas))
    if (numLinhas == null || numColunas == null){
        return null
    }
    val tabuleiro = criaTerreno(numLinhas, numColunas)
    for(linha in 0.. numLinhas){
        println(tabuleiro[linha])
    }
    inserirBarco(numLinhas, numColunas)
    return (tabuleiro)
}

fun calculaNumNavios(numLinhas: Int, numColunas: Int): Array<Int>{
    return emptyArray()
}

/**
 * Cria um tabuleiro vazio com o tamanho definido.
 * @param numLinhas Numero inteiro com o numero de linhas do tabuleiro
 * @param numColunas Numero inteiro com o numero de colunas do tabuleiro
 * @return Array de arrays de Char? com todos os char colocados a null
 */
fun criaTabuleiroVazio(numLinhas: Int, numColunas: Int): Array<Array<Char?>>{
    return Array(numLinhas){Array(numColunas){null}}
}

fun coordenadaContida(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int): Boolean{
    return if (linha !in 1..tabuleiro.size || coluna !in 1..tabuleiro[0].size) false else true
}

fun limparCoordenadasVazias(coordenadas: Array<Pair<Int,Int>>): Array<Pair<Int,Int>> {
    var count = 0
    for (contador in 0 until coordenadas.size) {
        if (coordenadas[contador] != Pair(0, 0)) {
            count++
        }
    }
    val validos = Array(count){Pair(0,0)}
    var posicaoCoordenadas = 0
    for (posicaoValidos in 0 until validos.size) {
        while (coordenadas[posicaoCoordenadas] == Pair(0,0)) {
            posicaoCoordenadas++
        }
        validos[posicaoValidos] = coordenadas[posicaoCoordenadas]
        posicaoCoordenadas++;
    }
    return validos
}

fun juntarCoordenadas(listaCoordenadas: Array<Pair<Int,Int>>, coordenada: Array<Pair<Int,Int>>): Array<Pair<Int,Int>>{
    val coordenadas = Array(listaCoordenadas.size + coordenada.size){Pair(0,0)}
    var posicao = 0
    for (elemento in listaCoordenadas){
        coordenadas[posicao] = elemento
        posicao++
    }
    for (elemento in coordenada){
        coordenadas[posicao] = elemento
        posicao++
    }
    return coordenadas
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
fun gerarCoordenadasNavio(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, orientacao: String, dimensao: Int): Array<Pair<Int,Int>> {
    val coordenadasNavio = Array(dimensao) { Pair(0, 0) }
    val andarLinhas = when (orientacao){
        "N" -> -1
        "S" -> 1
        else -> 0
    }
    val andarColunas = when (orientacao){
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

fun eliminarCoordenadasRepetidas(coordenadas1:Array<Pair<Int,Int>>, coordenadas2: Array<Pair<Int, Int>>): Array<Pair<Int,Int>>{
    val array2 = coordenadas2
    for (coordenada in coordenadas1)
    {
        for (posicao in 0 until coordenadas2.size){
            if (array2[posicao] == coordenada){
                array2[posicao] = Pair(0,0)
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
fun gerarCoordenadasFronteira(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, orientacao: String, dimensao: Int): Array<Pair<Int,Int>>{
    val coordenadasNavio = gerarCoordenadasNavio(tabuleiro, linha, coluna, orientacao, dimensao)
    var coordenadasFronteiraTotal = Array(0){Pair(0,0)}
    for (navio in coordenadasNavio){
        var coordenadasFronteiraCasa = arrayOf(Pair(navio.first - 1, navio.second - 1),
            Pair(navio.first - 1, navio.second), Pair(navio.first - 1, navio.second + 1),
            Pair(navio.first, navio.second - 1), Pair(navio.first, navio.second + 1),
            Pair(navio.first + 1, navio.second - 1), Pair(navio.first + 1, navio.second),
            Pair(navio.first + 1, navio.second + 1))
        for (posicao in 0 until coordenadasFronteiraCasa.size){
            if (!coordenadaContida(tabuleiro, coordenadasFronteiraCasa[posicao].first,
                    coordenadasFronteiraCasa[posicao].second) || Pair(coordenadasFronteiraCasa[posicao].first,
                    coordenadasFronteiraCasa[posicao].second) in coordenadasNavio){
                coordenadasFronteiraCasa[posicao] = Pair(0,0)
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
    for (coordenada in coordenadas){
        if (!coordenadaContida(tabuleiro, coordenada.first, coordenada.second) || tabuleiro[coordenada.first - 1][coordenada.second - 1] != null){
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
fun insereNavioSimples(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, dimensao: Int): Boolean{
    val coordenadasFronteira = gerarCoordenadasFronteira(tabuleiro, linha, coluna, "E", dimensao)
    for (coordenadas in coordenadasFronteira){
        if (tabuleiro[coordenadas.first - 1][coordenadas.second - 1] != null){
            return false
        }
    }
    for (casa in 0 until dimensao){
        if (!coordenadaContida(tabuleiro, linha, coluna + casa)){
            return false
        }
    }
    for (casa in 0 until dimensao){
        tabuleiro[linha - 1][coluna + casa - 1] = dimensao.digitToChar()
    }
    return true
}

fun insereNavio(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, orientacao: String, dimensao: Int): Boolean{
    // Recomendado usar gerarCoordenadasNavio() e gerarCoordenadasFronteira(), depois o
    // juntarCoordenadas(), depois o estaLivre().
    return false
}

fun preencheTabuleiroComputador(tabuleiro: Array<Array<Char?>>, navios: Array<Int>){
    return
}

fun navioCompleto(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int): Boolean{
    return false
}

/**
 * Cria o desenho do mapa correspondente ao tabuleiro que recebe, para poder mostrar ao utilizador.
 * @param tabuleiro Array<Array<Char?>> do tabuleiro que queremos transformar (tabuleiroHumano OU tabuleiroComputador)
 * @param tabuleiroReal True, se o tabuleiro for o real, ou false, se for um tabuleiro de palpites
 * @return Array de strings contendo o mapa para mostrar no stdout (incluindo a legenda)
 */
fun obtemMapa(tabuleiro: Array<Array<Char?>>, tabuleiroReal: Boolean): Array<String>{
    val mapa = Array(tabuleiro.size + 1){""}
    mapa[0] = "| " + criaLegendaHorizontal(tabuleiro[0].size) + " |"
        for (linha in 0 until tabuleiro.size){
            mapa[linha + 1] += "|"
            for (coluna in 0 until tabuleiro[linha].size){
                if (tabuleiroReal){
                    when (tabuleiro[linha][coluna]){
                        null -> mapa[linha + 1] += " ~ |"
                        '1' -> mapa[linha + 1] += " 1 |"
                        '2' -> mapa[linha + 1] += " 2 |"
                        '3' -> mapa[linha + 1] += " 3 |"
                        '4' -> mapa[linha + 1] += " 4 |"
                    }
                } else {
                    mapa[linha + 1] += when (tabuleiro[linha][coluna]){
                       '1' -> if (navioCompleto(tabuleiro, linha, coluna)) " 1 |" else " \u2081 |"
                       '2' -> if (navioCompleto(tabuleiro, linha, coluna)) " 2 |" else " \u2082 |"
                       '3' -> if (navioCompleto(tabuleiro, linha, coluna)) " 3 |" else " \u2083 |"
                       '4' -> if (navioCompleto(tabuleiro, linha, coluna)) " 4 |" else " \u2084 |"
                       'X' -> " X |"
                       else -> " ? |"
                   }
                }
            }
            mapa[linha + 1] += " ${linha + 1}"
        }
    return mapa
}

fun lancarTiro(tabuleiroReal: Array<Array<Char?>>, tabuleiroPalpites: Array<Array<Char?>>, coordenadas: Pair<Int,Int>): String{
    // Se for um humano a lancar
    return ""
}

fun geraTiroComputador(tabuleiroPalpitesComputador: Array<Array<Char?>>): Pair<Int, Int>{
    return Pair(0,0)
}

fun contarNaviosDeDimensao(tabuleiro: Array<Array<Char?>>, dimensao: Int): Int{
    return 0
}

fun venceu(tabuleiro: Array<Array<Char?>>): Boolean{
    // Recomendado usar calculaNumNavios() e contarNaviosDeDimensao()
    return false
}

fun lerJogo(ficheiro: String, tipoTabuleiro: Int): Array<Array<Int>>{
    return emptyArray()
}

/**
 * Recebe um PrintWriter aberto do ficheiro onde gravamos os tabuleiros de jogo e grava um determinado
 * tabuleiro lá para dentro. Coloca um newLine, depois coloca o tabuleiro e termina no fim do tabuleiro, sem newLine no
 * final.
 * @param jogoGravado PrintWriter para o ficheiro onde queremos gravar o tabuleiro (já no local onde o queremos gravar)
 * @param tabuleiro Tabuleiro que pretendemos colocar no ficheiro
 */
fun gravarTabuleiro(jogoGravado: PrintWriter, tabuleiro: Array<Array<Char?>>){
    for (linha in 0 until tabuleiro.size){
        jogoGravado.println()
        for (coluna in 0 until tabuleiro[0].size){
            when(tabuleiro[linha][coluna]){
                'X' -> jogoGravado.print("X")
                '1','2','3','4' -> jogoGravado.print(tabuleiro[linha][coluna])
            }
            if (coluna < tabuleiro[0].size - 1){
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
fun gravarJogo(ficheiro: String, tabuleiroRealHumano: Array<Array<Char?>>, tabuleiroPalpitesDoHumano: Array<Array<Char?>>,
               tabuleiroRealComputador: Array<Array<Char?>>, tabuleiroPalpitesDoComputador: Array<Array<Char?>>){
    val jogoGravado = File(ficheiro).printWriter()
    jogoGravado.print("${tabuleiroHumano.size},${tabuleiroHumano[0].size}")
    jogoGravado.print("\n\nJogador\nReal")
    gravarTabuleiro(jogoGravado, tabuleiroHumano)
    jogoGravado.print("\n\nJogador\nPalpites")
    gravarTabuleiro(jogoGravado, tabuleiroPalpitesDoHumano)
    jogoGravado.print("\n\nComputador\nReal")
    gravarTabuleiro(jogoGravado, tabuleiroComputador)
    jogoGravado.print("\n\nComputador\nPalpites")
    gravarTabuleiro(jogoGravado, tabuleiroPalpitesDoComputador)
    jogoGravado.close()
    return
}

fun main() {
/*    while (true) {
        printMenuJogo()
        val opcao = escolhaOpcao()
        when (opcao) {
            0 -> return
            2, 3, 4 -> println("!!! POR IMPLEMENTAR, tente novamente")
            1 -> tabuleiro()
        }
    }*/

/*    val mapa = arrayOf(arrayOf(null,null,null,null,'3'), arrayOf(null, null, null,'1','3'), arrayOf(null,null,null,null,'3'), arrayOf('4','4','4','4',null), arrayOf('3','3','3',null,'1'))
    var mapaDesenhado = obtemMapa(mapa, true)
    println(mapaDesenhado[0])
    println(mapaDesenhado[1])
    println(mapaDesenhado[2])
    println(mapaDesenhado[3])
    println(mapaDesenhado[4])
    println(mapaDesenhado[5])
    println()
    println(insereNavioSimples(mapa, 1,1, 2))
    println()
    mapaDesenhado = obtemMapa(mapa, true)
    println(mapaDesenhado[0])
    println(mapaDesenhado[1])
    println(mapaDesenhado[2])
    println(mapaDesenhado[3])
    println(mapaDesenhado[4])
    println(mapaDesenhado[5])*/

    /*
    val mapa = arrayOf(arrayOf(null,null,'1',null,'3'), arrayOf('2','2',null,'1','3'), arrayOf('1',null,'1',null,'3'), arrayOf('4','4','4','4',null), arrayOf('3','3','3',null,'1'))
    val mapaDesenhado = obtemMapa(mapa, true)
    println(mapaDesenhado[0])
    println(mapaDesenhado[1])
    println(mapaDesenhado[2])
    println(mapaDesenhado[3])
    println(mapaDesenhado[4])
    println(mapaDesenhado[5])
    gravarJogo("ficheiro.txt", mapa, mapa, mapa, mapa)
    println(gerarCoordenadasNavio(mapa, 3,2, "E",3).contentToString())
    println(limparCoordenadasVazias(arrayOf(Pair(0,0),Pair(1,2),Pair(0,0),Pair(3,4), Pair(5,6))).contentToString())
    println(eliminarCoordenadasRepetidas(arrayOf(Pair(3,2),Pair(3,5),Pair(1,2)),arrayOf(Pair(4,5),Pair(0,9),Pair(3,5),Pair(9,10),Pair(3,2))).contentToString())
    println(gerarCoordenadasFronteira(mapa, 1, 5, "S", 3).contentToString())


     */
}

