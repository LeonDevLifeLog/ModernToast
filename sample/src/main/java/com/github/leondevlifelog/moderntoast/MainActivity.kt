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
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.github.leondevlifelog.toast.ModernToast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var toast: ModernToast
    private var index: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toast = ModernToast.with(this)
        btnShow.setOnClickListener {
            lifecycleScope.launch {
                index = 0
                while (index < 101) {
                    toast.showProgress(index) {
                        outSideTouchable = false
                    }
                    delay(15)
                    index++
                }
                toast.dismiss()
            }
        }
        btnShowInfo.setOnClickListener {
            toast.showInfo("提示信息")
        }
        btnShowSuccess.setOnClickListener {
            toast.showSuccess("成功信息")
        }
        btnShowError.setOnClickListener {
            toast.showError("错误信息")
        }
        btnLoading.setOnClickListener {
            toast.showLoading() {
                cancelable = true
            }
        }
        btnCustom.setOnClickListener {
            toast.showCustom("自定义", R.mipmap.ic_launcher)
        }
    }

}
