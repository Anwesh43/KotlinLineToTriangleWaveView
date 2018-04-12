package ui.anwesome.com.linetotriwaveview

/**
 * Created by anweshmishra on 13/04/18.
 */

import android.content.Context
import android.view.View
import android.view.MotionEvent
import android.graphics.*
class LineToTriWaveView (ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

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
}