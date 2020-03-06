package me.melijn.gip

import me.melijn.gip.web.RestServer

class Serre {

    private val container = Container()

    init {
        container.serviceManager.startServices()
        val restServer = RestServer()
        restServer.start()
    }
}

fun main() {
    Serre()
}