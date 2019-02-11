package fractal

import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.image.BufferedImage
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.Timer
import javax.swing.WindowConstants

/*
 * Created with passion and love 
 *    for project NumberphileFractal
 *        by Jatzuk on 12-Feb-19
 *                                            *_____*
 *                                           *_*****_*
 *                                          *_(O)_(O)_*
 *                                         **____V____**
 *                                         **_________**
 *                                         **_________**
 *                                          *_________*
 *                                           ***___***
 */

class Canvas : JPanel(), ActionListener {
    private val bufferedImage = BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB)
    private val previous = Point()
    private val tracepoint = Point()
    private val timer = Timer(DELAY, this)

    init {
        background = Color.BLACK
        isFocusable = true
        preferredSize = Dimension(800, 700)
        createVertexes()
        timer.start()
    }

    private fun createVertexes() {
        with(bufferedImage.graphics as Graphics2D) {
            fillOval(A.x, A.y, THICKNESS, THICKNESS)
            fillOval(B.x, B.y, THICKNESS, THICKNESS)
            fillOval(C.x, C.y, THICKNESS, THICKNESS)

            drawString("A", A.x - (THICKNESS * 5), A.y + THICKNESS)
            drawString("B", B.x, B.y - 10)
            drawString("C", C.x - THICKNESS, C.y - 10)
        }
    }

    private fun nextDirection() = when ((Math.random() * 3).toInt()) {
        0 -> Direction.A
        1 -> Direction.B
        2 -> Direction.C
        else -> throw IllegalArgumentException()
    }

    override fun paint(g: Graphics) {
        super.paint(g)
        g.drawImage(bufferedImage, 0, 0, this)
    }

    @Suppress("NAME_SHADOWING")
    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        val g = bufferedImage.graphics as Graphics2D
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g.color = Color.GREEN
        with(nextDirection()) {
            tracepoint.x = (point.x - previous.x) / 2 + previous.x
            tracepoint.y = (point.y - previous.y) / 2 + previous.y
        }
        g.fillOval(tracepoint.x, tracepoint.y, THICKNESS, THICKNESS)
        previous.x = tracepoint.x
        previous.y = tracepoint.y
        iterations--
        (parent.parent.parent as JFrame).title = "$TITLE ${ITERATIONS - iterations}"
    }

    override fun actionPerformed(e: ActionEvent?) {
        repaint()
    }

    private enum class Direction(val point: Point) { A(Canvas.A), B(Canvas.B), C(Canvas.C); }

    companion object {
        private const val TITLE = "Numberphile fractal problem, iteration: "
        private const val WIDTH = 800
        private const val HEIGHT = 700
        private const val THICKNESS = 5
        private const val DELAY = 100
        private const val ITERATIONS = 10_000
        private val A = Point(WIDTH / 2, THICKNESS)
        private val B = Point(0, HEIGHT - THICKNESS)
        private val C = Point(WIDTH - THICKNESS, HEIGHT - THICKNESS)
        private var iterations = ITERATIONS
    }
}

object Frame : JFrame() {
    init {
        contentPane = Canvas()
        pack()
        isResizable = false
        setLocationRelativeTo(null)
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
    }
}

fun main() {
    EventQueue.invokeLater { Frame.isVisible = true }
}
