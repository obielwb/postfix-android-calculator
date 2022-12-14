package com.android.postfix_android_calculator

class PilhaVetor<T>:  IPilha<T>{
    private var topo: Int = -1
    private var p: Array<T>

    override val Tamanho: Int
        get() {
            return topo + 1
        }

    override val EstaVazia: Boolean
        get() {
            return topo < 0
        }

    constructor() {
        p =  arrayOfNulls<Any?>(500) as Array<T>
    }

    constructor(tamanho: Int) {
        p =  arrayOfNulls<Any?>(tamanho) as Array<T>
    }

    override fun empilhar(t: T) {
        if (Tamanho == p.size)
            throw Exception("Pilha cheia (Stack Overflow)!")

        topo += 1
        p[topo] = t
    }

    override fun desempilhar(): T {
        if (EstaVazia)
            throw Exception("Pilha vazia (Stack Underflow)!")


        val dado = p[topo]
        topo -= 1

        return dado
    }

    override fun topo(): T {
        if (EstaVazia)
            throw Exception("Pilha vazia (Stack Underflow)!")

        return p[topo]
    }

    override fun dadosDaPilha(): List<T> {
        val dados = ArrayList<T>()

        for (i in p)
            dados.add(i)

        return dados
    }
}