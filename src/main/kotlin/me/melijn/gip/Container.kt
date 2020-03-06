package me.melijn.gip

import me.melijn.gip.models.Settings
import me.melijn.gip.services.ServiceManager
import me.melijn.gip.threading.TaskManager
import me.melijn.gip.utils.Constants.OBJECT_MAPPER
import java.io.File

class Container {

    var settings: Settings = OBJECT_MAPPER.readValue(File("config.json"), Settings::class.java)
    val taskManager = TaskManager()
    val serviceManager = ServiceManager(taskManager)

}