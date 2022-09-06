package com.android.postfix_android_calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    lateinit var operacoesGridView: GridView
    lateinit var listaOperacoes: List<GridViewModal>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        operacoesGridView = findViewById(R.id.operacoesGridView)
        listaOperacoes = ArrayList<GridViewModal>()

        val operacoes = arrayOf("^", "/", "*", "-",
                                "7", "8", "9", "+",
                                "4", "5", "6", ".",
                                "1", "2", "3", "(",
                                "0", "=", "C", ")")

        for (operacao in operacoes) {
            listaOperacoes = listaOperacoes + GridViewModal(operacao)
        }

        val operacaoAdapter = GridRVAdapter(listaOperacoes = listaOperacoes, this@MainActivity)
        
        operacoesGridView.adapter = operacaoAdapter
        
        operacoesGridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            Toast.makeText(
                applicationContext, listaOperacoes[position].label,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}