package me.melijn.gip.services.lucht

import com.pi4j.io.gpio.PinState
import com.pi4j.io.gpio.RaspiPin
import kotlinx.coroutines.delay
import me.melijn.gip.services.Service
import me.melijn.gip.services.bme280.BME280
import me.melijn.gip.threading.Task
import me.melijn.gip.utils.gpioController
import java.util.concurrent.TimeUnit

class RamenService : Service("Ramen", 1, 1, TimeUnit.SECONDS) {

    // if you want the actual addresses use:
    // gpio readall
    // Definieer de pinnen om de motoren mee te besturen
    private val directionPin = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_28, "dirPin", PinState.HIGH)
    private val stepPin = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_29, "stepPin", PinState.HIGH)


    override val service = Task {
        // Test de ingelezen temperatuur en status van het raam om te beslissen wat er moet gebeuren
        if (BME280.TEMP > 28.0 && !STATUS) {
            STATUS = true

            directionPin.low()
            repeat(5) { //1.8° per step

                // Maakt 1 stap
                stepPin.high()
                delay(200)
                stepPin.low()
                delay(200)
            }
        } else if (STATUS) {
            STATUS = false
            directionPin.high()
            repeat(5) { //1.8° per step

                // Maakt 1 stap
                stepPin.high()
                delay(200)
                stepPin.low()
                delay(200)
            }
        }
    }

    companion object {
        // true = open & false = gesloten
        var STATUS = false
    }
}