package me.melijn.gip.services

import me.melijn.gip.services.bme280.BME280
import me.melijn.gip.threading.TaskManager

class ServiceManager(taskManager: TaskManager) {

    var started = false

    private val services = mutableListOf(
        BME280()
    )

    fun startServices() {
        services.forEach { service ->
            service.start()
            service.logger.info("Started ${service.name}Service")
        }
        started = true
    }

    fun stopServices() {
        require(started) { "Never started!" }
        services.forEach { service ->
            service.stop()
            service.logger.info("Stopped ${service.name}Service")
        }
    }
}