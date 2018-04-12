package ui.anwesome.com.linetotriwaveview

/**
 * Created by anweshmishra on 13/04/18.
 */

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.MotionEvent
import android.graphics.*
class LineToTriWaveView (ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val renderer = Renderer(this)

    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas, paint)
    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }

    data class State (var prevScale : Float = 0f, var dir : Float = 0f, var j : Int = 0) {
        val scales : Array<Float> = arrayOf(0f, 0f, 0f)
        fun update(stopcb : (Float) -> Unit) {
            scales[j] += 0.1f * dir
            if (Math.abs(scales[j] - prevScale) > 1) {
                this.scales[j] = this.prevScale + dir
                this.j += this.dir.toInt()
                if (this.j == scales.size || this.j == -1) {
                    this.j -= this.dir.toInt()
                    this.dir = 0f
                    this.prevScale = this.scales[j]
                    stopcb(this.prevScale)
                }
            }
        }

        fun startUpdating(startcb : () -> Unit) {
            if (this.dir == 0f) {
                this.dir = 1 - 2 * this.prevScale
                startcb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {
        fun animate(updatecb : () -> Unit) {
            if (animated) {
                updatecb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch (ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class LineToTriWave (var i : Int, private val state : State = State()) {

        fun draw(canvas : Canvas, paint : Paint) {
            val w : Float = canvas.width.toFloat()
            val h : Float = canvas.height.toFloat()
            val size : Float = (w / (3 * Math.sqrt(2.0).toFloat()))
            paint.strokeWidth = Math.min(w, h)/65
            paint.strokeCap = Paint.Cap.ROUND
            paint.color = Color.parseColor("#4A148C")
            var x : Float =  - size * Math.sqrt(2.0).toFloat()
            canvas.save()
            canvas.translate(w/2, h/2)
            for (i in 0..2) {
                canvas.save()
                canvas.translate(x * state.scales[1], -size/2)
                for (j in 0..1) {
                    canvas.save()
                    canvas.rotate(45f * (1 - 2 * j) * state.scales[2])
                    canvas.drawLine(0f, 0f, 0f, size * state.scales[0], paint)
                    canvas.restore()
                }
                canvas.restore()
                x += Math.sqrt(2.0).toFloat() * size
            }
            canvas.restore()
            x
        }

        fun update(stopcb : (Float) -> Unit) {
            state.update(stopcb)
        }

        fun startUpdating(startcb : () -> Unit) {
            state.startUpdating(startcb)
        }

    }

    data class Renderer(var view : LineToTriWaveView) {

        private val animator : Animator = Animator(view)

        private val lineToTriWave : LineToTriWave = LineToTriWave(0)

        fun render(canvas : Canvas, paint : Paint) {
            canvas.drawColor(Color.parseColor("#212121"))
            lineToTriWave.draw(canvas, paint)
            animator.animate {
                lineToTriWave.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            lineToTriWave.startUpdating {
                animator.start()
            }
        }
    }

    companion object {
        fun create(activity : Activity) : LineToTriWaveView {
            val view : LineToTriWaveView = LineToTriWaveView(activity)
            activity.setContentView(view)
            return view
        }
    }

}