import com.phidget22.VoltageRatioInput
import org.openrndr.application

fun main() = application {
    configure {
        width = 768
        height = 576
    }

    program {
        var value = 0.0
        with (VoltageRatioInput()) {
            // Configure settings depending on what it connected where
            isHubPortDevice = true
            hubPort = 0

            // Configuration required to set after attach
            addAttachListener {
                dataInterval = 25
            }

            // Use event to get value
            addVoltageRatioChangeListener {
                value = it.voltageRatio
            }

            open()
            ended.listen { close() }
        }

        extend {
            val x = value * width
            drawer.circle(x, 20.0, 5.0)
        }
    }
}
