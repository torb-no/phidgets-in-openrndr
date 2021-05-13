import com.phidget22.DigitalOutput
import org.openrndr.application
import org.openrndr.color.ColorRGBa

fun main() = application {
    configure {
        width = 768
        height = 576
    }

    program {
        // Storing the value and using apply now since
        // we want to interact with the object later
        // in order to control the output
        val digitalOutput = DigitalOutput().apply {
            channel = 0
            open()
            ended.listen { close() }
        }

        var outputActive = false
        mouse.buttonUp.listen {
            // Flip the state
            digitalOutput.state = !digitalOutput.state

            // Reflect changes in local variable
            outputActive = digitalOutput.state
        }

        extend {
            if (outputActive) {
                drawer.clear(ColorRGBa.WHITE)
            } else {
                drawer.clear(ColorRGBa.BLACK)
            }
        }
    }
}
