/**
 * Cria a legenda horizontal consuante o número de colunas.
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
 * Valida se o tamanho do tabuleiro é válido (5x5, 7x7, 8x8 e 10x10).
 * Validada pelo Drop Project, nao alterar.
 * @param numLinhas Numero de linhas do tabuleiro.
 * @param numColunas Numero de Colunas do tabuleiro.
 * @return True se o tamanho do tabuleiro for valido, false se nao for.
 */
fun tamanhoTabuleiroValido(numLinhas: Int?, numColunas: Int?): Boolean{
    return when{
        numLinhas == null || numColunas == null -> false
        numLinhas == 5 && numColunas == 5 -> true
        numLinhas == 7 && numColunas == 7 -> true
        numLinhas == 8 && numColunas == 8 -> true
        numLinhas == 10 && numColunas == 10 -> true
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
fun processaCoordenadas(coordenadas: String?, numLinhas: Int, numColunas: Int): Boolean{
    if (coordenadas == null){
        return false
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
        else -> return false
    }
    return when{
        separador != ',' -> false
        coordenada1 !in 1..numLinhas -> false
        coordenada2 !in 'A'..'A' + numColunas - 1 -> false
        else -> true
    }
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
fun criaTerreno(numLinhas: Int, numColunas: Int): String{
    var tabuleiro = ""
    var contador = 0
    tabuleiro += "\n| " + criaLegendaHorizontal(numColunas) + " |"
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
        if (!processaCoordenadas(coordenadas, numLinhas, numColunas)) {
            println("!!! Coordenadas invalidas, tente novamente")
        }
    } while (!processaCoordenadas(coordenadas, numLinhas, numColunas))
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

/**
 * Pede o número de linhas e o número de colunas do tabuleiro ao utilizador
 * e retorna uma ‘string’ com o tabuleiro e com os barcos.
 * @return String com o tabuleiro, se este for
 */
fun tabuleiro(): String?{
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
                println("Número de linhas invalidas, tente novamente")
            }
            if (numLinhas == -1){
                return null
            }
        } while (numLinhas == null)
        do {
            println("Quantas colunas?")
            numColunas = readLine()?.toIntOrNull()
            if (numColunas == null){
                println("Número de colunas invalidas, tente novamente")
            }
            if (numColunas == -1) {
                return null
            }
        } while (numColunas == null)
    } while (!tamanhoTabuleiroValido(numLinhas, numColunas))
    if (numLinhas == null || numColunas == null){
        return null
    }
    val tabuleiro = criaTerreno(numLinhas, numColunas)
    println(tabuleiro)
    inserirBarco(numLinhas, numColunas)
    return (tabuleiro)
}

fun main() {
    while (true) {
        printMenuJogo()
        val opcao = escolhaOpcao()
        when (opcao) {
            0 -> return
            2, 3, 4 -> println("!!! POR IMPLEMENTAR, tente novamente")
            1 -> tabuleiro()
        }
    }
}