package ui.anwesome.com.kotlinlinetotriwaveview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.linetotriwaveview.LineToTriWaveView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LineToTriWaveView.create(this)
    }
}
