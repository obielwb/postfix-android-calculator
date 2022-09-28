package com.android.postfix_android_calculator

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import androidx.core.content.getSystemService

internal class GridRVAdapter (
        private val listaOperacoes: List<GridViewModal>,
        private val context: Context
    ): BaseAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private lateinit var operacaoButton: Button

    override fun getCount(): Int {
        return listaOperacoes.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView;

        if (layoutInflater == null) {
            layoutInflater =
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.grid_item, null)
        }

        operacaoTextView = convertView!!.findViewById(R.id.idOperacaoTextView)
        operacaoTextView.text = listaOperacoes[position].label

        return convertView
    }
}