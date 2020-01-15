package com.github.leondevlifelog.moderntoast

import android.os.Bundle
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
            toast.show {
                outSideTouchable = true
                text = "show"
            }
        }
        btnShowInfo.setOnClickListener {
            toast.showInfo {
                outSideTouchable = true
                text = "showInfo"
            }
        }
        btnShowSuccess.setOnClickListener {
            toast.showSuccess {
                outSideTouchable = true
                text = "showSuccess"
            }
        }
        btnShowError.setOnClickListener {
            toast.showError {
                outSideTouchable = true
                text = "showError"
            }
        }
    }

}
