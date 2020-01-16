/*
 * Copyright 2020 Leon<leondevlifelog@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
        btnLoading.setOnClickListener {
            toast.showLoading()
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
