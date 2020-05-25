package me.melijn.gip.web

import org.jooby.Jooby
import org.jooby.json.Jackson

class RestServer : Jooby() {

    init {
        use(Jackson())

        get("/*") { _, rsp ->
            rsp.send("test")
        }
    }
}