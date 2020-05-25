package me.melijn.gip.services

import me.melijn.gip.services.bme280.BME280
import me.melijn.gip.services.camera.CameraService
import me.melijn.gip.services.water.GrondService
import me.melijn.gip.services.water.RamenService
import me.melijn.gip.services.water.WaterService

class ServiceManager {

    private val services = mutableListOf(
        BME280(),
        CameraService(),
        RamenService(),
        WaterService()
    )

    fun startServices() {
        services.forEach { service ->
            service.start()
            service.logger.info("Started ${service.name}Service")
        }
        GrondService()
    }
}