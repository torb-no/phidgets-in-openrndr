import com.phidget22.DigitalInput
import com.phidget22.VoltageRatioInput
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.isolatedWithTarget
import org.openrndr.draw.renderTarget
import org.openrndr.launch

const val MOVEMENT_MULTIPLIER = 15.0

fun main() = application {
    configure {
        width = 768
        height = 576
    }

    program {
        // Use this to draw on
        val rtCanvas = renderTarget(width, height) { colorBuffer() }
        drawer.isolatedWithTarget(rtCanvas) {
            drawer.clear(ColorRGBa.BLACK)
        }

        var y = drawer.bounds.center.y
        with (VoltageRatioInput()) {
            hubPort = 0
            channel = 0
            addAttachListener {
                dataInterval = 25
            }
            addVoltageRatioChangeListener {
                y += -it.voltageRatio * MOVEMENT_MULTIPLIER
            }
            open()
            ended.listen { close() }
        }

        var x = drawer.bounds.center.x
        with (VoltageRatioInput()) {
            hubPort = 0
            channel = 1
            addVoltageRatioChangeListener {
                x += it.voltageRatio * MOVEMENT_MULTIPLIER
            }
            open()
            ended.listen { close() }
        }

        with (DigitalInput()) {
            hubPort = 0
            addStateChangeListener {
                // Launch a coroutine in the program context
                // to get back to OpenRNDR drawing thread
                // since phidget event handlers are run in
                // a different thread/context
                launch {
                    drawer.isolatedWithTarget(rtCanvas) {
                        drawer.clear(ColorRGBa.BLACK)
                    }
                }
            }
            open()
            ended.listen { close() }
        }

        extend {
            drawer.isolatedWithTarget(rtCanvas) {
                drawer.stroke = ColorRGBa.WHITE
                drawer.circle(x, y, 10.0)
            }

            drawer.image(rtCanvas.colorBuffer(0))
        }
    }
}
