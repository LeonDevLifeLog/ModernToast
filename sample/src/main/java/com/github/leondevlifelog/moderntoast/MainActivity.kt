package com.github.leondevlifelog.moderntoast

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.github.leondevlifelog.toast.ModernToast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var toast: ModernToast
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toast = ModernToast(this)
        btnShow.setOnClickListener {
        }
        btnShowInfo.setOnClickListener {
            toast.showInfo {
                outSideTouchable = true
                text = "showInfo"
            }
        }
        btnShowSuccess.setOnClickListener {
            toast.showSuccess("成功")
        }
        btnShowError.setOnClickListener {
            toast.showError {
                outSideTouchable = true
                text = "showError"
            }
        }
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                toast.showProgress(progress.toLong())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

}
