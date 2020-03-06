package me.melijn.gip

class Serre {

    private val container = Container()

    init {
        container.serviceManager.startServices()
    }
}

fun main() {
    Serre()
}