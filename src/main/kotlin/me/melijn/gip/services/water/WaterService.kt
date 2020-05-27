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

    // Beslist wanneer de water pomp moet aangaan
    override val service = Task {
        if (GrondService.groundResistance > 80_000) {
            pumpPin.high()
            delay(5000)
        }
        pumpPin.low()
    }
}