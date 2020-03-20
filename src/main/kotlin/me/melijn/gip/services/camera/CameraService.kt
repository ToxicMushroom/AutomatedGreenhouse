package me.melijn.gip.services.camera

import me.melijn.gip.services.Service
import me.melijn.gip.threading.Task
import uk.co.caprica.picam.Camera
import uk.co.caprica.picam.CameraConfiguration
import uk.co.caprica.picam.CameraConfiguration.cameraConfiguration
import uk.co.caprica.picam.PicamNativeLibrary.installTempLibrary
import uk.co.caprica.picam.PictureCaptureHandler
import uk.co.caprica.picam.enums.Encoding
import uk.co.caprica.picam.enums.ExposureMode
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.sqrt


class CameraService : Service("camera", 30) {
    private val config: CameraConfiguration

    init {
        installTempLibrary()
        config = cameraConfiguration()
            .width(1920)
            .height(1080)
            .encoding(Encoding.JPEG)
            .quality(80)
            .exposureMode(ExposureMode.AUTO)
    }

    override val service: Task = Task {
        Camera(config).use { camera ->
            camera.takePicture(FosPictureCaptureHandler {
                var total: Int = 0
                for (x in 0 until it.width) {
                    for (y in 0 until it.height) {
                        val color = it.getRGB(x, y)
                        val r = color shr 16 and 0xff
                        val g = color shr 8 and 0xff
                        val b = color and 0xff
                        total += getBrightness(r, g, b)
                    }
                }
                println(total / (it.width * it.height))
                val f = File("${System.currentTimeMillis()}.jpg")
                ImageIO.write(it, "jpg", f)
            })
        }
    }

    private fun getBrightness(r: Int, g: Int, b: Int): Int {
        return sqrt(r * r * .241 + g * g * .691 + b * b * .068).toInt()
    }
}

class FosPictureCaptureHandler(private val func: (BufferedImage) -> Unit) : PictureCaptureHandler<BufferedImage> {

    private lateinit var baos: ByteArrayOutputStream

    override fun result(): BufferedImage {
        val bais = ByteArrayInputStream(baos.toByteArray())

        val newImg = ImageIO.read(bais)
        bais.close()

        func(newImg)
        return newImg
    }

    override fun end() {
        baos.close()
    }

    override fun pictureData(data: ByteArray): Int {
        baos.write(data)
        return data.size
    }

    override fun begin() {
        baos = ByteArrayOutputStream()
    }
}