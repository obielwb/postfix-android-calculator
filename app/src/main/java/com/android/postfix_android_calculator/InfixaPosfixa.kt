package com.android.postfix_android_calculator

import com.android.postfix_android_calculator.exceptions.InicioComOperadorException
import com.android.postfix_android_calculator.exceptions.OperadorSubsequenteException

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

        expressao.forEach { caractere ->
            if (caractere == '(' || caractere == ')') {
                if (parenteses.EstaVazia)
                    parenteses.Empilhar(caractere)

                else if (parenteses.Topo() == '(' && caractere == ')')
                    parenteses.Desempilhar()

                else
                    parenteses.Empilhar(caractere)
            }
        }

        return parenteses.EstaVazia
    }

    fun converterParaInfixa(expressao: String,  ) {

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
                    operadores.Empilhar(caractere)
                else if (caractere == ')') {
                    while (!operadores.EstaVazia && operadores.Topo() != '(')
                        posfixa += operadores.Desempilhar()

                    operadores.Desempilhar()
                }
                else {
                    while (
                        !operadores.EstaVazia &&
                        precedencia(caractere) <= precedencia(operadores.Topo())
                    )
                        posfixa += operadores.Desempilhar()

                    operadores.Empilhar(caractere)
                }
            }
        }

        while (!operadores.EstaVazia)
            posfixa += operadores.Desempilhar()

        return posfixa
    }

    fun calcularResultado(posfixa: String, valores: DoubleArray): Double {
        for (i in posfixa.indices) {
            if (posfixa[i] == '@') {
                val j = posfixa[i + 1] - 65;
                valores[j.code] = -1 * valores[j.code]
            }
        }

        posfixa = posfixa.replace("@", "")

        val operacoes = PilhaVetor<Double>()

        posfixa.forEach { caractere ->
            // se o caractere for uma letra, representando um valor
            if (!"+-*/^".contains(caractere))
            {
                // obtém o operando relativo à letra
                var operando = valores[(caractere - 65).code];

                operacoes.Empilhar(operando);
            }

            // se o caractere for um operador
            else
            {
                // desempilha os operandos em ordem
                var operandoDois = operacoes.Desempilhar();
                var operandoUm = operacoes.Desempilhar();

                var operacao = 0;

                // switch para realização da operação entre
                // operando um, operador e operando dois
                when {
                    caractere == '+' -> operacao = operandoUm + operandoDois
                    caractere == '-' -> operacao = operandoUm - operandoDois
                    caractere == '*' -> operacao = operandoUm * operandoDois
                    caractere == '/' -> operacao = operandoUm / operandoDois
                    caractere == '^' -> operacao = Math.Pow(operandoUm, operandoDois)
                }

                operacoes.Empilhar(operacao);
        }

        return operacoes.Desempilhar()
    }
}