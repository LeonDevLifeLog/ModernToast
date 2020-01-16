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

package com.github.leondevlifelog.toast

import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * 现代化的Toast
 */
class ModernToast private constructor(
    private val activity: FragmentActivity,
    private val contentView: FrameLayout
) {
    companion object {
        /**
         * 获取[ModernToast]实例
         * @param activity 需要显示Toast的Activity
         */
        @JvmStatic
        fun with(activity: FragmentActivity): ModernToast {
            var modernToast: ModernToast? = null
            val rootLayout = activity.findViewById<FrameLayout>(android.R.id.content)
            var contentView = rootLayout.findViewById<FrameLayout?>(R.id.libToastLayout)
            if (contentView == null) {
                contentView = (LayoutInflater.from(activity).inflate(
                    R.layout.toast_layout,
                    rootLayout,
                    false
                ) as FrameLayout).apply {
                    modernToast = ModernToast(activity, this)
                    tag = modernToast
                }
                rootLayout.addView(contentView)
            } else {
                if (contentView.tag is ModernToast) {
                    modernToast = contentView.tag as ModernToast
                }
            }
            if (modernToast == null) {
                modernToast = ModernToast(activity, contentView)
            }
            return modernToast!!
        }
    }

    private val durationDefault: Long = 1000
    /**
     * 显示时长
     */
    var duration: Long = durationDefault
    private val cancelableDefault = false
    /**
     * 是否可通过返回键取消
     */
    var cancelable: Boolean = cancelableDefault
    private val outSideTouchableDefault = true
    /**
     * Toast外部是否可点击
     */
    var outSideTouchable: Boolean = outSideTouchableDefault
    /**
     * 提示信息
     */
    var text: String? = null
    private var ivStatus: ImageView
    private var cpbProgress: CircularProgressBar
    private var tvTip: TextView
    private var timer: Job? = null

    /**
     * 显示模式
     */
    private enum class MODE {
        /**
         * 无进度加载
         */
        LOADING,
        /**
         * 含进度加载
         */
        PROGRESS,
        /**
         * 普通Toast
         */
        TOAST
    }

    private val onBackPressed = object : OnBackPressedCallback(cancelable) {
        override fun handleOnBackPressed() {
            dismiss()
        }
    }

    init {
        activity.onBackPressedDispatcher.addCallback(activity, onBackPressed)
        cpbProgress = contentView.findViewById(R.id.cpbProgress)
        ivStatus = contentView.findViewById(R.id.ivStatus)
        tvTip = contentView.findViewById(R.id.tvTip)
    }

    private fun show(mode: MODE, f: (ModernToast.() -> Unit)? = null): ModernToast {
        when (mode) {
            MODE.LOADING -> {
                ivStatus.visibility = View.GONE
                cpbProgress.visibility = View.VISIBLE
            }
            MODE.PROGRESS -> {
                ivStatus.visibility = View.GONE
                cpbProgress.visibility = View.VISIBLE
            }
            MODE.TOAST -> {
                ivStatus.visibility = View.VISIBLE
                cpbProgress.visibility = View.GONE
            }
        }
        if (isShowing()) {
            cancelTimer()
        }
        f?.invoke(this)
        tvTip.text = text
        contentView.visibility = View.VISIBLE
        if (mode != MODE.PROGRESS) {
            timer = activity.lifecycleScope.launch {
                delay(duration)
                if (isActive) {
                    dismiss()
                }
            }
        }
        contentView.isClickable = !outSideTouchable
        onBackPressed.isEnabled = cancelable
        return this
    }

    private fun cancelTimer() {
        timer?.cancel()
    }

    private fun resetState() {
        cancelable = cancelableDefault
        duration = durationDefault
        outSideTouchable = outSideTouchableDefault
    }

    /**
     *显示成功提示的Toast
     * @param id 提示字符资源id
     * @param f 对显示配置的回调
     * @return 返回Toast实例
     */
    fun showSuccess(@StringRes id: Int, f: (ModernToast.() -> Unit)? = null): ModernToast {
        resetState()
        text = activity.getString(id)
        ivStatus.setImageResource(R.drawable.icon_success)
        return show(MODE.TOAST, f)
    }

    /**
     *显示成功提示的Toast
     * @param text 提示字符
     * @param f 对显示配置的回调
     * @return 返回Toast实例
     */
    fun showSuccess(text: String, f: (ModernToast.() -> Unit)? = null): ModernToast {
        resetState()
        ivStatus.setImageResource(R.drawable.icon_success)
        this.text = text
        return show(MODE.TOAST, f)
    }

    /**
     * 显示提示Toast
     * @param f 对显示配置的回调
     * @param text 提示字符
     * @return 返回Toast实例
     */
    fun showInfo(text: String, f: (ModernToast.() -> Unit)? = null): ModernToast {
        resetState()
        this.text = text
        ivStatus.setImageResource(R.drawable.icon_info)
        return show(MODE.TOAST, f)
    }

    /**
     * 显示提示Toast
     * @param f 对显示配置的回调
     * @param id 提示字符资源id
     * @return 返回Toast实例
     */
    fun showInfo(@StringRes id: Int, f: (ModernToast.() -> Unit)? = null): ModernToast {
        resetState()
        this.text = activity.getString(id)
        ivStatus.setImageResource(R.drawable.icon_info)
        return show(MODE.TOAST, f)
    }

    /**
     * 显示错误Toast
     * @param f 对显示配置的回调
     * @param text 提示字符
     * @return 返回Toast实例
     */
    fun showError(text: String, f: (ModernToast.() -> Unit)? = null): ModernToast {
        resetState()
        this.text = text
        ivStatus.setImageResource(R.drawable.icon_error)
        return show(MODE.TOAST, f)
    }

    /**
     * 显示错误Toast
     * @param f 对显示配置的回调
     * @param id 提示字符资源id
     * @return 返回Toast实例
     */
    fun showError(@StringRes id: Int, f: (ModernToast.() -> Unit)? = null): ModernToast {
        resetState()
        this.text = activity.getString(id)
        ivStatus.setImageResource(R.drawable.icon_error)
        return show(MODE.TOAST, f)
    }

    /**
     * 显示自定义Toast
     * @param strRes 字符串资源id
     * @param res 图片资源id
     * @param f 对显示配置的回调
     * @return 返回Toast实例
     */
    fun showCustom(@StringRes strRes: Int, @DrawableRes res: Int, f: (ModernToast.() -> Unit)? = null): ModernToast {
        resetState()
        this.text = activity.getString(strRes)
        ivStatus.setImageResource(res)
        return show(MODE.TOAST, f)
    }

    /**
     * 显示自定义Toast
     * @param text 提示
     * @param res 图片资源id
     * @param f 对显示配置的回调
     * @return 返回Toast实例
     */
    fun showCustom(
        text: String, @DrawableRes res: Int,
        f: (ModernToast.() -> Unit)? = null
    ): ModernToast {
        resetState()
        this.text = text
        ivStatus.setImageResource(res)
        return show(MODE.TOAST, f)
    }

    /**
     * 显示加载进度Toast
     * @param text 提示字符
     * @param progress 进度[0,100]
     * @param f 对显示配置的回调
     * @return 返回Toast实例
     */
    fun showProgress(
        @IntRange(
            from = 0,
            to = 100
        ) progress: Long, text: String = "加载中...", f: (ModernToast.() -> Unit)? = null
    ): ModernToast {
        resetState()
        if (progress == 100.toLong()) {
            dismiss()
        }
        this.text = text
        cpbProgress.progress = progress.toFloat()
        cpbProgress.indeterminateMode = false
        return show(MODE.PROGRESS, f)
    }

    /**
     * 显示加载Toast
     * @param text 提示字符
     * @param duration 显示时长
     * @param f 对显示配置的回调
     * @return 返回Toast实例
     */
    fun showLoading(
        text: String = "加载中...", duration: Long = 3000, f: (ModernToast.() -> Unit)? = null
    ): ModernToast {
        resetState()
        this.text = text
        this.duration = duration
        cpbProgress.indeterminateMode = true
        return show(MODE.LOADING, f)
    }

    /**
     *设置进度
     * @param progress 具体进度[0,100]
     * @return 返回Toast实例
     */
    fun setProgress(
        @IntRange(
            from = 0,
            to = 100
        ) progress: Long
    ): Unit {
        cpbProgress.progress = progress.toFloat()
    }

    /**
     *取消toast显示
     */
    fun dismiss() {
        resetState()
        contentView.visibility = View.GONE
    }

    /**
     *判断当前Toast是否在显示
     */
    fun isShowing(): Boolean {
        return contentView.visibility == View.VISIBLE
    }
}