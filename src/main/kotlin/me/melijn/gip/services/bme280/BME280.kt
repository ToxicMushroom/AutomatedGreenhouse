package me.melijn.gip.services.bme280

import com.pi4j.io.i2c.I2CBus
import com.pi4j.io.i2c.I2CFactory
import me.melijn.gip.services.Service
import kotlin.experimental.and
import kotlin.Double as Double1


class BME280 : Service("bme280", 10) {
    // Create I2C bus
    val bus = I2CFactory.getInstance(I2CBus.BUS_1)

    override val service: Runnable = Runnable {


        // Get I2C device, BME280 I2C address is 0x76(108)
        val device = bus.getDevice(0x76)

        // Read 24 bytes of data from address 0x88(136)
        val b1 = ByteArray(24)
        device.read(0x88, b1, 0, 24)

        // Convert the data
        // temp coefficients

        val dig_T1: Int = (b1[0].toInt() and 0xFF) + (b1[1].toInt() and 0xFF) * 256
        var dig_T2: Int = (b1[2].toInt() and 0xFF) + (b1[3].toInt() and 0xFF) * 256
        if (dig_T2 > 32767) {
            dig_T2 -= 65536
        }
        var dig_T3: Int = (b1[4].toInt() and 0xFF) + (b1[5].toInt() and 0xFF) * 256
        if (dig_T3 > 32767) {
            dig_T3 -= 65536
        }

        // pressure coefficients

        val dig_P1: Int = (b1[6].toInt() and 0xFF) + (b1[7].toInt() and 0xFF) * 256
        var dig_P2: Int = (b1[8].toInt() and 0xFF) + (b1[9].toInt() and 0xFF) * 256
        if (dig_P2 > 32767) {
            dig_P2 -= 65536
        }
        var dig_P3: Int = (b1[10].toInt() and 0xFF) + (b1[11].toInt() and 0xFF) * 256
        if (dig_P3 > 32767) {
            dig_P3 -= 65536
        }
        var dig_P4: Int = (b1[12].toInt() and 0xFF) + (b1[13].toInt() and 0xFF) * 256
        if (dig_P4 > 32767) {
            dig_P4 -= 65536
        }
        var dig_P5: Int = (b1[14].toInt() and 0xFF) + (b1[15].toInt() and 0xFF) * 256
        if (dig_P5 > 32767) {
            dig_P5 -= 65536
        }
        var dig_P6: Int = (b1[16].toInt() and 0xFF) + (b1[17].toInt() and 0xFF) * 256
        if (dig_P6 > 32767) {
            dig_P6 -= 65536
        }
        var dig_P7: Int = (b1[18].toInt() and 0xFF) + (b1[19].toInt() and 0xFF) * 256
        if (dig_P7 > 32767) {
            dig_P7 -= 65536
        }
        var dig_P8: Int = (b1[20].toInt() and 0xFF) + (b1[21].toInt() and 0xFF) * 256
        if (dig_P8 > 32767) {
            dig_P8 -= 65536
        }
        var dig_P9: Int = (b1[22].toInt() and 0xFF) + (b1[23].toInt() and 0xFF) * 256
        if (dig_P9 > 32767) {
            dig_P9 -= 65536
        }

        // Read 1 byte of data from address 0xA1(161)
        val dig_H1: Int = (device.read(0xA1) and 0xFF)

        // Read 7 bytes of data from address 0xE1(225)
        device.read(0xE1, b1, 0, 7)


        // Convert the data
        // humidity coefficients
        var dig_H2: Int = (b1[0].toInt() and 0xFF) + b1[1] * 256
        if (dig_H2 > 32767) {
            dig_H2 -= 65536
        }
        val dig_H3: Int = b1[2].toInt() and 0xFF
        var dig_H4: Int = (b1[3].toInt() and 0xFF) * 16 + (b1[4].toInt() and 0xF)
        if (dig_H4 > 32767) {
            dig_H4 -= 65536
        }
        var dig_H5: Int = (b1[4].toInt() and 0xFF) / 16 + (b1[5].toInt() and 0xFF) * 16
        if (dig_H5 > 32767) {
            dig_H5 -= 65536
        }
        var dig_H6: Int = b1[6].toInt() and 0xFF
        if (dig_H6 > 127) {
            dig_H6 -= 256
        }

        // Select control humidity register
        // Humidity over sampling rate = 1

        device.write(0xF2, 0x01.toByte())

        // Select control measurement register
        // Normal mode, temp and pressure over sampling rate = 1
        device.write(0xF4, 0x27.toByte())

        // Select config register
        // Stand_by time = 1000 ms
        device.write(0xF5, 0xA0.toByte())

        // Read 8 bytes of data from address 0xF7(247)
        // pressure msb1, pressure msb, pressure lsb, temp msb1, temp msb, temp lsb, humidity lsb, humidity msb
        val data = ByteArray(8)
        device.read(0xF7, data, 0, 8)


        // Convert pressure and temperature data to 19-bits
        val adc_p =
            ((data[0].toInt() and 0xFF).toLong() * 65536 + (data[1].toInt() and 0xFF).toLong() * 256 + (data[2].toInt() and 0xF0).toLong()) / 16
        val adc_t =
            ((data[3].toInt() and 0xFF).toLong() * 65536 + (data[4].toInt() and 0xFF).toLong() * 256 + (data[5].toInt() and 0xF0).toLong()) / 16

        // Convert the humidity data
        val adc_h = (data[6].toLong() and 0xFF) * 256 + (data[7] and 0xFF.toByte()).toLong()

        // Temperature offset calculations
        var var1 =
            (adc_t.toDouble() / 16384.0 - dig_T1.toDouble() / 1024.0) * dig_T2.toDouble()
        var var2 = (adc_t.toDouble() / 131072.0 - dig_T1.toDouble() / 8192.0) *
                (adc_t.toDouble() / 131072.0 - dig_T1.toDouble() / 8192.0) * dig_T3.toDouble()
        val t_fine: Double1 = (var1 + var2)
        val cTemp = (var1 + var2) / 5120.0
        val fTemp = cTemp * 1.8 + 32

        // Pressure offset calculations
        var1 = t_fine / 2.0 - 64000.0
        var2 = var1 * var1 * dig_P6.toDouble() / 32768.0
        var2 += var1 * dig_P5.toDouble() * 2.0
        var2 = var2 / 4.0 + dig_P4.toDouble() * 65536.0
        var1 = (dig_P3.toDouble() * var1 * var1 / 524288.0 + dig_P2.toDouble() * var1) / 524288.0
        var1 = (1.0 + var1 / 32768.0) * dig_P1.toDouble()
        var p = 1048576.0 - adc_p.toDouble()
        p = (p - var2 / 4096.0) * 6250.0 / var1
        var1 = dig_P9.toDouble() * p * p / 2147483648.0
        var2 = p * dig_P8.toDouble() / 32768.0
        val pressure = (p + (var1 + var2 + dig_P7.toDouble()) / 16.0) / 100

        // Humidity offset calculations
        var var_H = t_fine - 76800.0
        var_H =
            (adc_h - (dig_H4 * 64.0 + dig_H5 / 16384.0 * var_H)) * (dig_H2 / 65536.0 * (1.0 + dig_H6 / 67108864.0 * var_H * (1.0 + dig_H3 / 67108864.0 * var_H)))
        var humidity = var_H * (1.0 - dig_H1 * var_H / 524288.0)
        if (humidity > 100.0) {
            humidity = 100.0
        } else if (humidity < 0.0) {
            humidity = 0.0
        }

        // Output data to screen

        // Output data to screen
        System.out.printf("Temperature in Celsius : %.2f C %n", cTemp)
        System.out.printf("Temperature in Fahrenheit : %.2f F %n", fTemp)
        System.out.printf("Pressure : %.2f hPa %n", pressure)
        System.out.printf("Relative Humidity : %.2f %% RH %n", humidity)
    }
}