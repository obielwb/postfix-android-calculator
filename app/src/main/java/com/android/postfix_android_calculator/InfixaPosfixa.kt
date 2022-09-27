package com.android.postfix_android_calculator

import com.android.postfix_android_calculator.exceptions.InicioComOperadorException
import com.android.postfix_android_calculator.exceptions.OperadorSubsequenteException
import kotlin.math.exp

class InfixaPosfixa {
    fun validarEntrada(entrada: String, caractereDigitado: Char) {
        if (entrada.length > 1) {
            var penultimoCaractere = entrada[entrada.length - 2]

            if ("+-*/^".contains(penultimoCaractere) && "+-*/^".contains(caractereDigitado)) {
                throw OperadorSubsequenteException("Dois operadores não podem ser subsequentes!")
            }
        }

        // verificação da presença de um operador
        // anterior à presença de um operando,
        // inválido mesmo para unários
        else if ("+-*/^".contains(caractereDigitado)) {
            throw InicioComOperadorException("Um operador não pode iniciar uma expressão!")
        }
    }

    fun precedencia(operador: Char): Int {
        if (operador == '-' || operador == '-')
            return 1
        else if (operador == '*' || operador == '/')
            return 2
        else if (operador == '^')
            return 3
        else
            return 0
    }

    fun temParentesesBalanceados(expressao: String): Boolean {
        val parenteses = PilhaVetor<Char>();

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

        return parenteses.EstaVazia
    }

    fun converterParaInfixa(expressao: String): Pair<String, DoubleArray> {
        var infixa: String = expressao.replace("(-", "(@").replace("(+", "(")

        // vetor de valores
        var valores = DoubleArray(expressao.length)

        // contador v para o vetor de valores
        var v = 0

        var i = 0
        while (i < expressao.length) {
            i++

            var caractere: Char = expressao[i]

            if (".0123456789".contains(caractere)) {
                // inicia-se o processo para a obtenção máxima do valor
                var valor = caractere.toString()
                var j = i + 1
                var fim = false

                if (j < expressao.length) {
                    while (".0123456789".contains(expressao[j]) && !fim) {
                        valor += expressao[j]

                        if (j + 1 == expressao.length)
                            fim = true

                        else
                            j++
                    }
                }

                var regex = Regex(Regex.escape(valor))
                infixa = regex.replace(infixa, ((65 + v).toChar()).toString())

                valores[v++] = valor.toDouble()
                i = j
            }
        }

        return Pair(infixa, valores)
    }

    fun converterParaPosfixa(infixa: String): String {
        var posfixa = ""

        val operadores = PilhaVetor<Char>()

        infixa.forEach { caractere ->
            if (!"+-*/^()".contains(caractere)) {
                posfixa += caractere
            }
            else {
                if (caractere == '(')
                    operadores.empilhar(caractere)
                else if (caractere == ')') {
                    while (!operadores.EstaVazia && operadores.topo() != '(')
                        posfixa += operadores.desempilhar()

                    operadores.desempilhar()
                }
                else {
                    while (
                        !operadores.EstaVazia &&
                        precedencia(caractere) <= precedencia(operadores.topo())
                    )
                        posfixa += operadores.desempilhar()

                    operadores.empilhar(caractere)
                }
            }
        }

        while (!operadores.EstaVazia)
            posfixa += operadores.desempilhar()

        return posfixa
    }

    fun calcularResultado(posfixa: String, valores: DoubleArray): Double {
        for (i in posfixa.indices) {
            if (posfixa[i] == '@') {
                val j = posfixa[i + 1] - 65;
                valores[j.code] = -1 * valores[j.code]
            }
        }

        var posfixa = posfixa.replace("@", "")

        val operacoes = PilhaVetor<Double>()

        posfixa.forEach { caractere ->
            // se o caractere for uma letra, representando um valor
            if (!"+-*/^".contains(caractere))
            {
                // obtém o operando relativo à letra
                var operando = valores[(caractere - 65).code];

                operacoes.empilhar(operando);
            }

            // se o caractere for um operador
            else {
                // desempilha os operandos em ordem
                var operandoDois = operacoes.desempilhar();
                var operandoUm = operacoes.desempilhar();

                var operacao = 0.0;

                // switch para realização da operação entre
                // operando um, operador e operando dois
                when {
                    caractere == '+' -> operacao = operandoUm + operandoDois
                    caractere == '-' -> operacao = operandoUm - operandoDois
                    caractere == '*' -> operacao = operandoUm * operandoDois
                    caractere == '/' -> operacao = operandoUm / operandoDois
                    caractere == '^' -> operacao = Math.pow(operandoUm, operandoDois)
                }
                operacoes.empilhar(operacao);
            }
        }
        return operacoes.desempilhar()
    }
}