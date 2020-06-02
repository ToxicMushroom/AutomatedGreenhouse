package me.melijn.gip.services.water

import com.pi4j.io.gpio.PinState
import com.pi4j.io.gpio.RaspiPin
import kotlinx.coroutines.delay
import me.melijn.gip.services.Service
import me.melijn.gip.threading.Task
import me.melijn.gip.utils.gpioController
import java.util.concurrent.TimeUnit

class WaterService : Service("water", 10, 10, TimeUnit.MINUTES) {

    private val pumpPin = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_13, "pumpPin", PinState.HIGH)

    override val service = Task {
        if (GrondService.RESISTANCE > 80_000) {
            pumpPin.high()
            STATUS = true
            delay(5000)
            STATUS = false
        }
        pumpPin.low()
    }

    companion object {
        // true = aan & false = uit
        var STATUS = false
    }
}