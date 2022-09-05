package com.android.postfix_android_calculator

interface IPilha<T> {
    fun Empilhar(t: T)
    fun Desempilhar(): T
    fun Topo(): T
    fun DadosDaPilha(): List<T>
    //val Tamanho: Int.Companion get() = Int
    //val EstaVazia: Boolean.Companion get() = Boolean
}