package com.example.myapplication

interface IMVVMObservable {
    val observers: ArrayList<IMVVMObserver>

    fun add(observer: IMVVMObserver) {
        observers.add(observer)
    }

    fun remove(observer: IMVVMObserver) {
        observers.remove(observer)
    }

    fun sendUpdateEvent(message: String, value: Double) {
        observers.forEach { it.update(message,value) }
    }
}