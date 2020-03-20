package me.melijn.gip.services.water

import com.pi4j.io.gpio.PinState
import com.pi4j.io.gpio.RaspiPin
import kotlinx.coroutines.delay
import me.melijn.gip.services.Service
import me.melijn.gip.threading.Task
import me.melijn.gip.utils.gpioController
import java.util.concurrent.TimeUnit

class RamenService : Service("Ramen", 1, 1, TimeUnit.SECONDS) {

    private val directionPin = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_20, "dirPin", PinState.LOW)
    private val stepPin = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_21, "stepPin", PinState.LOW)


    override val service = Task {
        logger.info("hi there ${stepPin.isExported}")
        stepPin.high()
        delay(1000)
        logger.info("lo there ${stepPin.isHigh}")
        stepPin.low()
        delay(1000)

    }
}