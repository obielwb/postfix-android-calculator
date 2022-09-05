package com.android.postfix_android_calculator

class PilhaVetor<T>:  IPilha<T>{
    private var topo: Int = -1
    private lateinit var p: Array<T>

    var Tamanho: Int = 0
        get() {
            return topo + 1
        }
    var EstaVazia: Boolean = false
        get() {
            return topo < 0
        }

    constructor() {
        p = arrayOfNulls<Class<T>>(500) as Array<T>
    }

    constructor(tamanho: Int) {
        p =  arrayOfNulls<Class<T>>(tamanho) as Array<T>
    }

    override fun Empilhar(t: T) {
        if ()
    }

    override fun Desempilhar(): T {
        TODO("Not yet implemented")
    }

    override fun Topo(): T {
        TODO("Not yet implemented")
    }

    override fun DadosDaPilha(): List<T> {
        TODO("Not yet implemented")
    }
}