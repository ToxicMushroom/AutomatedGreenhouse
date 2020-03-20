package me.melijn.gip.services.water

import me.melijn.gip.services.Service
import me.melijn.gip.threading.Task
import java.util.concurrent.TimeUnit

class WaterService : Service("water", 10, 10, TimeUnit.MINUTES) {

    override val service = Task {
        println("test")
    }
}