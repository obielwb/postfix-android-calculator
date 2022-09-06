package com.android.postfix_android_calculator

interface IPilha<T> {
    fun empilhar(t: T)
    fun desempilhar(): T
    fun topo(): T
    fun dadosDaPilha(): List<T>
    //val Tamanho: Int.Companion get() = Int
    //val EstaVazia: Boolean.Companion get() = Boolean
}