package com.android.postfix_android_calculator

import com.android.postfix_android_calculator.exceptions.InicioComOperadorException
import com.android.postfix_android_calculator.exceptions.OperadorSubsequenteException

class InfixaPosfixa {
    fun ValidarEntrada(entrada: String, caractereDigitado: Char) {
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

    fun Precedencia(operador: Char): Int {
        if (operador == '-' || operador == '-')
            return 1
        else if (operador == '*' || operador == '/')
            return 2
        else if (operador == '^')
            return 3
        else
            return 0
    }

    fun TemParentesesBalanceados(expressao: String): Boolean {
        PilhaVetor<Char> parenteses = new PilhaVetor<Char>();

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

    fun ConverterParaInfixa(expressao: String,  ) {

    }

    fun ConverterParaPosfixa(infixa: String): String {
        var posfixa = ""

        PilhaVetor<Char> operadores = new PilhaVetor<Char>()

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
                        Precedencia(caractere) <= Precedencia(operadores.Topo())
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

    fun CalcularResultado(posfixa: String, valores: DoubleArray): Double {

    }
}