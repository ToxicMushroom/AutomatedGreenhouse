package me.melijn.gip.services.water

import com.pi4j.io.serial.*

class GrondService {

    init {
        val serial = SerialFactory.createInstance()
        // Luistert voor serieële data en zet dit in de grondWeerstand variabele
        serial.addListener(SerialDataEventListener {
            println(it.asciiString)
            groundResistance = it.asciiString.toDoubleOrNull() ?: 0.0
        })
        // Opent de seriele connectie
        serial.open(
            SerialConfig()
                .baud(Baud._9600)
                .dataBits(DataBits._8)
                .parity(Parity.NONE)
                .stopBits(StopBits._1)
                .flowControl(FlowControl.NONE)
        )
    }

    // Globale grondWeerstand variabele
    companion object {
        var groundResistance: Double = 0.0
    }
}