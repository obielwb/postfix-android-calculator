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

    fun TemParentesesBalanceados(expressao: String) {

    }

}