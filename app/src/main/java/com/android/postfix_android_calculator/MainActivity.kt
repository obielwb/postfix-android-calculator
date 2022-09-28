package com.android.postfix_android_calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import com.android.postfix_android_calculator.exceptions.*

class MainActivity : AppCompatActivity() {
    lateinit var operacoesGridView: GridView
    lateinit var listaOperacoes: List<GridViewModal>

    lateinit var visorEditText: EditText
    lateinit var infixaTextView: TextView
    lateinit var posfixaTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        operacoesGridView = findViewById(R.id.operacoesGridView)
        listaOperacoes = ArrayList()
        visorEditText = findViewById(R.id.visorEditText)

        infixaTextView = findViewById(R.id.infixaTextView)
        posfixaTextView = findViewById(R.id.posfixaTextView)

        val operacoes = arrayOf("^", "/", "*", "-",
                                "7", "8", "9", "+",
                                "4", "5", "6", ".",
                                "1", "2", "3", "(",
                                "0", "=", "C", ")")

        for (operacao in operacoes)
            listaOperacoes += GridViewModal(operacao)

        operacoesGridView.adapter = GridRVAdapter(listaOperacoes, this)

        operacoesGridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val infixaPosfixa = InfixaPosfixa()

            val caractereDigitado = listaOperacoes[position].label[0]

            Toast.makeText(applicationContext, caractereDigitado.toString(), Toast.LENGTH_LONG)
            val expressao = visorEditText.text.toString()

            if (caractereDigitado == 'C') {
                visorEditText.setText("")
                infixaTextView.setText("Infixa")
                posfixaTextView.setText("Pósfixa")
            } else if (caractereDigitado == '=') {
                if (expressao.isNotEmpty()) {
                    if (!infixaPosfixa.temParentesesBalanceados(expressao)) {
                        Toast.makeText(
                            applicationContext, "A expressão não possui parênteses balanceados!",
                            Toast.LENGTH_LONG
                        )
                    }
                    else {
                        val (infixa, valores) = infixaPosfixa.converterParaInfixa(expressao)
                        val posfixa = infixaPosfixa.converterParaPosfixa(infixa)
                        val resultado = infixaPosfixa.calcularResultado(posfixa, valores).toString()

                        infixaTextView.setText("Infixa   $infixa")
                        posfixaTextView.setText("Pósfixa   $posfixa")
                        visorEditText.setText(resultado)
                    }
                }
            } else {
                try {
                    infixaPosfixa.validarEntrada(expressao, caractereDigitado)
                    visorEditText.setText(expressao + listaOperacoes[position].label)
                } catch (e: OperadorSubsequenteException) {
                    Toast.makeText(
                        applicationContext, e.message.toString(),
                        Toast.LENGTH_LONG
                    )

                    visorEditText.setText(
                        expressao.substring(0, visorEditText.text.length - 1)
                    )
                } catch (e: InicioComOperadorException) {
                    Toast.makeText(
                        applicationContext, e.message.toString(),
                        Toast.LENGTH_LONG
                    )
                    visorEditText.setText("")
                }
            }
        }
    }
}