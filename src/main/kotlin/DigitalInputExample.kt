import com.phidget22.DigitalInput
import org.openrndr.application
import org.openrndr.color.ColorRGBa

fun main() = application {
    configure {
        width = 768
        height = 576
    }

    program {
        var state = false
        with (DigitalInput()) {
            hubPort = 0

            addStateChangeListener {
                // Note: event handle callback is run on
                // Phidget library's own thread, so you can't run
                // or call to drawing code here unless you wrap it in
                // launch {} clause to get back to the normal OPENRNDR thread
                state = it.state
                println("DigitalInput state changed to ${it.state}")
            }

            open()
            ended.listen { close() }
        }

        extend {
            if (state) {
                drawer.clear(ColorRGBa.GREEN)
            } else {
                drawer.clear(ColorRGBa.RED)
            }
        }
    }
}
