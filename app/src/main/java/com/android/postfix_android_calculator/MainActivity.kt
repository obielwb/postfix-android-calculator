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
            listaOperacoes = listaOperacoes + GridViewModal(operacao)

        val operacaoAdapter = GridRVAdapter(listaOperacoes, this)
        
        operacoesGridView.adapter = operacaoAdapter
        var text: TextView = findViewById(R.id.infixaTextView)

        operacoesGridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            var infixaPosfixa = InfixaPosfixa()

            var caractereDigitado: Char = listaOperacoes[position].label.toCharArray()[0]

            var expressao = visorEditText.text.toString()

            if (!"C=".contains(caractereDigitado)) {
                try {
                    infixaPosfixa.validarEntrada(expressao, caractereDigitado)
                } catch (e: OperadorSubsequenteException) {
                    Toast.makeText(
                        applicationContext, e.message,
                        Toast.LENGTH_SHORT
                    )

                    visorEditText.setText(
                        expressao.substring(0, visorEditText.text.length - 1)
                    )
                } catch (e: InicioComOperadorException) {
                    Toast.makeText(
                        applicationContext, e.message,
                        Toast.LENGTH_SHORT
                    )

                    visorEditText.setText("")
                }
                visorEditText.setText(expressao + listaOperacoes[position].label)
            } else if (caractereDigitado == 'C') {
                visorEditText.setText("")
            } else if (caractereDigitado == '=') {
                if (expressao.isNotEmpty()) {
                    if (!infixaPosfixa.temParentesesBalanceados(expressao))
                        Toast.makeText(
                            applicationContext, "A expressão não possui parênteses balanceados",
                            Toast.LENGTH_LONG
                        )
                    else {
                        infixaTextView.setText()
                    }
                }
            }
        }

        /*operacoesGridView.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Get the GridView selected/clicked item text
                val selectedItem = parent.getItemAtPosition(position).toString()

                // Display the selected/clicked item text and position on TextView
                text.text = "${listaOperacoes[position].label}GridView item clicked : $selectedItem \nAt index position : $position"
            }
        }*/
    }
}