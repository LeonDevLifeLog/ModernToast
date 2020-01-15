package com.github.leondevlifelog.toast

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.IntRange
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ModernToast(private val activity: FragmentActivity) {
    val TAG: String = "ModernToast"
    var duration: Long = 1000
    var cancelable: Boolean = false
    var outSideTouchable: Boolean = true
    var text: String? = null
    private lateinit var contentView: FrameLayout
    private var ivStatus: ImageView
    private var cpbProgress: CircularProgressBar
    private var tvTip: TextView
    private var timer: Job? = null
    private val onBackPressed = object : OnBackPressedCallback(cancelable) {
        override fun handleOnBackPressed() {
            dismiss()
        }
    }

    init {
        val rootLayout = activity.findViewById<FrameLayout>(android.R.id.content)
        rootLayout.findViewById<FrameLayout?>(R.id.libToastLayout)?.let {
            contentView = it
        }
        if (!::contentView.isInitialized) {
            contentView =
                LayoutInflater.from(activity).inflate(
                    R.layout.toast_layout,
                    rootLayout,
                    false
                ) as FrameLayout
            rootLayout.addView(contentView)
        }
        cpbProgress = contentView.findViewById(R.id.cpbProgress)
        ivStatus = contentView.findViewById(R.id.ivStatus)
        tvTip = contentView.findViewById(R.id.tvTip)
    }

    private fun show(imageMode: Boolean, f: (ModernToast.() -> Unit)? = null): ModernToast {
        if (imageMode) {
            ivStatus.visibility = View.VISIBLE
            cpbProgress.visibility = View.GONE
        } else {
            ivStatus.visibility = View.GONE
            cpbProgress.visibility = View.VISIBLE
        }
        if (isShowing()) {
            Log.d(TAG, "show: cancel")
            timer?.cancel()
        }
        f?.invoke(this)
        tvTip.text = text
        contentView.visibility = View.VISIBLE
        if (imageMode) {
            timer = activity.lifecycleScope.launch {
                delay(duration)
                if (isActive) {
                    dismiss()
                }
            }
        }
        contentView.isClickable = !outSideTouchable
        activity.onBackPressedDispatcher.addCallback(activity, onBackPressed)
        return this
    }

    fun showSuccess(@StringRes id: Int, f: (ModernToast.() -> Unit)? = null): ModernToast {
        text = activity.getString(id)
        ivStatus.setImageResource(R.drawable.icon_success)
        return show(true, f)
    }

    fun showSuccess(text: String, f: (ModernToast.() -> Unit)? = null): ModernToast {
        ivStatus.setImageResource(R.drawable.icon_success)
        this.text = text
        return show(true, f)
    }

    fun showInfo(f: (ModernToast.() -> Unit)? = null): ModernToast {
        ivStatus.setImageResource(R.drawable.icon_info)
        return show(true, f)
    }

    fun showError(f: (ModernToast.() -> Unit)? = null): ModernToast {
        ivStatus.setImageResource(R.drawable.icon_error)
        return show(true, f)
    }

    fun showProgress(
        @IntRange(
            from = 0,
            to = 100
        ) progress: Long, text: String = "加载中...", f: (ModernToast.() -> Unit)? = null
    ): ModernToast {
        this.text = text
        cpbProgress.progress = progress.toFloat()
        return show(false, f)
    }

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
        contentView.visibility = View.GONE
    }

    /**
     *判断当前Toast是否在显示
     */
    fun isShowing(): Boolean {
        return contentView.visibility == View.VISIBLE
    }
}