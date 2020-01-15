package com.github.leondevlifelog.toast

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ModernToast(private val activity: FragmentActivity) {
    val TAG: String = "ModernToast"
    var duration: Long = 1000
    var cancelable: Boolean = false
    var outSideTouchable: Boolean = false
    var text: String? = null
    private lateinit var contentView: FrameLayout
    private var ivStatus: ImageView
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
        ivStatus = contentView.findViewById(R.id.ivStatus)
        tvTip = contentView.findViewById(R.id.tvTip)
    }

    fun show(f: ModernToast.() -> Unit): ModernToast {
        if (isShowing()) {
            Log.d(TAG, "show: cancel")
            timer?.cancel()
        }
        this.f()
        tvTip.text = text
        contentView.visibility = View.VISIBLE
        timer = activity.lifecycleScope.launch {
            delay(duration)
            if (isActive) {
                dismiss()
            }
        }
        contentView.isClickable = !outSideTouchable
        activity.onBackPressedDispatcher.addCallback(activity, onBackPressed)
        return this
    }

    fun showSuccess(f: ModernToast.() -> Unit): ModernToast {
        ivStatus.setImageResource(R.drawable.icon_success)
        return show(f)
    }

    fun showInfo(f: ModernToast.() -> Unit): ModernToast {
        ivStatus.setImageResource(R.drawable.icon_info)
        return show(f)
    }

    fun showError(f: ModernToast.() -> Unit): ModernToast {
        ivStatus.setImageResource(R.drawable.icon_error)
        return show(f)
    }

    fun showLoading(f: ModernToast.() -> Unit): ModernToast {
        // TODO: 20-1-15 loading动画待实现
        return show(f)
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