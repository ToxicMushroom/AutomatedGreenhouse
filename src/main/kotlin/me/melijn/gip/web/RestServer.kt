package me.melijn.gip.web

import me.melijn.gip.services.bme280.BME280
import me.melijn.gip.services.camera.CameraService
import me.melijn.gip.services.lucht.RamenService
import me.melijn.gip.services.water.GrondService
import me.melijn.gip.services.water.WaterService
import org.jooby.Jooby
import org.jooby.json.Jackson

// Webserver waar er sensor data op bepaalde locaties wordt geserveerd
// Dit kunnen we later eventueel in een groot punt verzamellen en verwerken in grafieken
class RestServer : Jooby() {

    init {
        use(Jackson())

        get("/status") { _, rsp ->
            val map = mapOf(
                Pair("temperatuur", "${BME280.TEMP}"),
                Pair("luchtvochtigheid", "${BME280.HUMIDITY}"),
                Pair("luchtdruk", "${BME280.PRESSURE}"),
                Pair("lichtintensiteit", "${CameraService.BRITGHTNESS}"),
                Pair("ramenstand", "${RamenService.STATUS}"),
                Pair("ramenstand", "${GrondService.RESISTANCE}"),
                Pair("waterpompstand", "${WaterService.STATUS}")
            )
            rsp.send(map)
        }
    }
}