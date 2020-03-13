package me.melijn.gip.services.water

import com.pi4j.io.gpio.PinState
import com.pi4j.io.gpio.RaspiPin
import me.melijn.gip.services.Service
import me.melijn.gip.utils.gpioController
import java.util.concurrent.TimeUnit

class RamenService : Service("ramen", 1, 1, TimeUnit.MINUTES) {


    val directionPin = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_20, PinState.LOW)
    val stepPin = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_21, PinState.LOW)

    init {
        directionPin.setShutdownOptions(true, PinState.LOW)
        stepPin.setShutdownOptions(true, PinState.LOW)
    }

    override val service: Runnable = Runnable {
        stepPin.pulse(10, TimeUnit.MILLISECONDS)
    }
}