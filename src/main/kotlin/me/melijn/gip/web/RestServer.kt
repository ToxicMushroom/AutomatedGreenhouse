package me.melijn.gip.web

import org.jooby.Jooby
import org.jooby.json.Jackson

// Webserver waar er sensor data op bepaalde locaties wordt geserveerd
// Dit kunnen we desnoods in een groot punt verzamellen en verwerken in grafieken
class RestServer : Jooby() {

    init {
        use(Jackson())

        get("/*") { _, rsp ->
            rsp.send("test")
        }
    }
}