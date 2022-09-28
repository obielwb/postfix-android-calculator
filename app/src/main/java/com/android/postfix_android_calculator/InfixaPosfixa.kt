package com.android.postfix_android_calculator

import com.android.postfix_android_calculator.exceptions.InicioComOperadorException
import com.android.postfix_android_calculator.exceptions.OperadorSubsequenteException
import kotlin.math.exp
import kotlin.math.pow

class InfixaPosfixa {
    // método para validar a entrada do text view
    fun validarEntrada(entrada: String, caractereDigitado: Char) {
        // se a entrada não está vazia
        if (entrada.isNotEmpty()) {
            val penultimoCaractere = entrada[entrada.length - 1]

            // verificação da presença de dois operadores seguidos
            if ("+-*/^".contains(penultimoCaractere) && "+-*/^".contains(caractereDigitado)) {
                throw OperadorSubsequenteException("Dois operadores não podem ser subsequentes!")
            }

            // somente '-' pode ser subsequente a um parênteses
            if (penultimoCaractere == '(' && "+*/^".contains(caractereDigitado)) {
                throw OperadorSubsequenteException("Apenas '-' pode ser subsquente a um parênteses por ser o indicador unário!")
            }
        }

        // verificação da presença de um operador
        // anterior à presença de um operando,
        // inválido mesmo para unários
        else if ("+-*/^".contains(caractereDigitado)) {
            throw InicioComOperadorException("Um operador não pode iniciar uma expressão!")
        }
    }

    // método para retornar a precedência númerica de um operador
    fun precedencia(operador: Char): Int {
        // + e - têm precedência númerica 1
        if (operador == '+' || operador == '-')
            return 1
        // * e / têm precedência númerica 2
        else if (operador == '*' || operador == '/')
            return 2
        // ^ tem precedência númerica 3
        else if (operador == '^')
            return 3
        // outros operadores têm precedência númerica 0
        else
            return 0
    }

    // método para determinar se uma expressão tem parênteses balanceados
    fun temParentesesBalanceados(expressao: String): Boolean {
        // pilha de parênteses
        val parenteses = PilhaVetor<Char>()

        // percorrimento da expressão para o balanceamento dos parênteses
        expressao.forEach { caractere ->
            // se o caractere "da vez" da expressão for parênteses
            if (caractere == '(' || caractere == ')') {
                // caso a pilha esteja vazia, é
                // empilhado o primeiro caractere
                if (parenteses.EstaVazia)
                    parenteses.empilhar(caractere)

                // se há um balanceamento entre o parênteses do topo
                // da pilha e o parênteses digitado, a pilha é desempilhada
                else if (parenteses.topo() == '(' && caractere == ')')
                    parenteses.desempilhar()

                // caso contrário, o parênteses é empilhado
                else
                    parenteses.empilhar(caractere)
            }
        }

        // a presença ou não de parênteses na pilha
        // determina o balanceamento da expressão
        // EstaVazia: true -> balanceada
        // EstaVazia: false -> desbalanceada
        return parenteses.EstaVazia
    }

    // método para conversão de uma expressão
    // regular para expressão infixa, retornando um
    // vetor com os valores extraídos da expressão regular
    fun converterParaInfixa(expressao: String): Pair<String, DoubleArray> {
        // atribuição dos operadores unários
        // "-" -> "@"
        // "+" -> ""
        var infixa = expressao.replace("(-", "(@").replace("(+", "(")

        // vetor de valores
        val valores = DoubleArray(expressao.length)

        // contador v para o vetor de valores
        var v = 0

        // percorrimento da expressão para extração
        // dos valores númericos e atribuição de letras
        var i = 0
        while (i < expressao.length) {
            val caractere = expressao[i]

            // se o caractere é númerico
            if (".0123456789".contains(caractere)) {
                // inicia-se o processo para a obtenção máxima do valor
                var valor = caractere.toString()
                var j = i + 1
                var fim = false

                // j se prolonga até o valor deixar de ser númerico,
                // seja tal o fim da expressão ou um operador, concatenando
                // os valores númericos no valor
                if (j < expressao.length) {
                    while (".0123456789".contains(expressao[j]) && !fim) {
                        valor += expressao[j]

                        if (j + 1 == expressao.length)
                            fim = true
                        else
                            j++
                    }
                }

                // utilização de regex para a substituição do valor para
                // sua respectiva letra, relativa à posição do valor
                var regex = Regex(Regex.escape(valor))
                infixa = regex.replace(infixa, ((65 + v).toChar()).toString())

                valores[v++] = valor.toDouble()
                i = j
            } else
                i++
        }

        // retorno da expressão regular convertida para infixa e do vetor de valores
        return Pair(infixa, valores)
    }

    // método para conversão de uma expressão infixa para pósfixa
    fun converterParaPosfixa(infixa: String): String {
        var posfixa = ""

        // pilha de operadores
        val operadores = PilhaVetor<Char>()

        // percorrimento da expressão infixa para conversão
        // para pósifxa
        infixa.forEach { caractere ->
            // se o caractere é uma letra
            if (!"+-*/^()".contains(caractere)) {
                posfixa += caractere
            }
            // se o caractere é um operador
            else {
                if (caractere == '(')
                    operadores.empilhar(caractere)
                else if (caractere == ')') {
                    // enquanto não houver balanceamento,
                    // concatena-se o desempilhamento na expressão pósfixa
                    while (!operadores.EstaVazia && operadores.topo() != '(')
                        posfixa += operadores.desempilhar()

                    if (!operadores.EstaVazia)
                        operadores.desempilhar()
                }
                // se o caractere for +, -, *, / ou ^
                else {
                    // enquanto a pilha não estiver vazia e a precedência do operador
                    // for menor ou igual que a precedência do topo da pilha,
                    // concatena-se o desempilhamento na expressão pósfixa
                    while (
                        !operadores.EstaVazia &&
                        precedencia(caractere) <= precedencia(operadores.topo())
                    )
                        posfixa += operadores.desempilhar()

                    operadores.empilhar(caractere)
                }
            }
        }

        // desempilhamento dos operadores remanescentes
        while (!operadores.EstaVazia)
            posfixa += operadores.desempilhar()

        // retorno da expressão infixa convertida para pósfixa
        return posfixa
    }

    // método para calcular o resultado de uma dada expressão
    // pósfixa através de seu vetor de valores
    fun calcularResultado(posfixa: String, valores: DoubleArray): Double {
        // percorrimento da expressão pósfixa para o tratamento
        // de operadores unários negativos
        for (i in posfixa.indices) {
            // se há um operador unário negativo na posição i
            if (posfixa[i] == '@') {
                // a letra que representa o valor unário correspondente a i
                // está uma posição a frente, subtraída por 65, resulta na
                // posição j relativa ao valor no vetor de valores
                val j = posfixa[i + 1].code - 65
                // oposição do valor da posição j
                valores[j] = -1 * valores[j]
            }
        }

        // remoção dos operadores unários negativos da expressão pósfixa
        val posfixa = posfixa.replace("@", "")

        // pilha de operações
        val operacoes = PilhaVetor<Double>()

        // percorrimento da expressão pósfixa para realização do cálculo
        posfixa.forEach { caractere ->
            // se o caractere for uma letra, representando um valor
            if (!"+-*/^".contains(caractere))
            {
                // obtém o operando relativo à letra
                val operando = valores[(caractere).code.toByte().toInt() - 65]

                operacoes.empilhar(operando)
            }

            // se o caractere for um operador
            else {
                // desempilha os operandos em ordem
                val operandoDois = operacoes.desempilhar()
                val operandoUm = operacoes.desempilhar()

                var operacao = 0.0

                // when para realização da operação entre
                // operando um, operador e operando dois
                when {
                    caractere == '+' -> operacao = operandoUm + operandoDois
                    caractere == '-' -> operacao = operandoUm - operandoDois
                    caractere == '*' -> operacao = operandoUm * operandoDois
                    caractere == '/' -> operacao = operandoUm / operandoDois
                    caractere == '^' -> operacao = operandoUm.pow(operandoDois)
                }

                operacoes.empilhar(operacao)
            }
        }

        // o resultado do cálculo da expressão é a operação
        // remanescente na pilha de operações
        return operacoes.desempilhar()
    }
}