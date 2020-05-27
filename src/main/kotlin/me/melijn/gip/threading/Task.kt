package me.melijn.gip.threading

import kotlinx.coroutines.runBlocking


// Taak class die een functie later kan uitvoeren en mogelijke errors toont
class Task(private val func: suspend () -> Unit) : Runnable {

    override fun run() {
        runBlocking {
            try {
                func()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }
}