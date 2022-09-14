package com.android.postfix_android_calculator

interface IPilha<T> {
    val Tamanho: Int
    val EstaVazia: Boolean
    fun empilhar(t: T)
    fun desempilhar(): T
    fun topo(): T
    fun dadosDaPilha(): List<T>
}