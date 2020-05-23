package me.melijn.gip.services.water

import com.pi4j.io.gpio.PinState
import com.pi4j.io.gpio.RaspiPin
import kotlinx.coroutines.delay
import me.melijn.gip.services.Service
import me.melijn.gip.threading.Task
import me.melijn.gip.utils.gpioController
import java.util.concurrent.TimeUnit

class RamenService : Service("Ramen", 1, 1, TimeUnit.SECONDS) {

    // if you want the actual addresses use:
    // gpio readall
    private val directionPin = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_28, "dirPin", PinState.HIGH)
    private val stepPin = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_29, "stepPin", PinState.HIGH)


    override val service = Task {
        stepPin.high()
        delay(1000)
        stepPin.low()
        delay(1000)
    }
}