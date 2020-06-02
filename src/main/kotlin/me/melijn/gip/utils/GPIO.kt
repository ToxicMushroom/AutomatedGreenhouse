package me.melijn.gip.utils

import com.pi4j.io.gpio.GpioController
import com.pi4j.io.gpio.GpioFactory

// Globale controller van pi4j om de GPIO mee aan te sturen
val gpioController: GpioController = GpioFactory.getInstance()