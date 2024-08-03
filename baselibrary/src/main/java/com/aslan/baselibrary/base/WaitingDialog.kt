package com.aslan.baselibrary.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.aslan.baselibrary.R

/**
 * 等待框
 */
open class WaitingDialog : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.layout_wait_dialog, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniView(view)
        iniListener()
        iniData()
    }

    open fun iniView(view: View) {
        var msg = arguments?.getString("msg")
        if (msg == null) {
            msg = getString(R.string.progress_waiting)
        }

        if (msg.isBlank()) {
            view.findViewById<TextView>(R.id.tvText).isGone = true
        } else {
            view.findViewById<TextView>(R.id.tvText).isVisible = true
        }
        view.findViewById<TextView>(R.id.tvText).setText(msg)
    }

    open fun iniListener() {}

    open fun iniData() {}

    open fun show(manager: FragmentManager, builder: Builder) {
        val bundle = Bundle()
        if (builder.msg != null) {
            bundle.putString("msg", builder.msg.toString())
        }
        arguments = bundle
        super.show(manager, WaitingDialog::class.java.simpleName)
    }

    data class Builder(val mContext: Context) {
        var cancelable: Boolean? = null
        var msg: CharSequence? = null

        fun setCancelable(cancelable: Boolean): Builder {
            this.cancelable = cancelable
            return this
        }

        fun setMessage(msg: String): Builder {
            this.msg = msg
            return this
        }

        fun setMessage(@StringRes resId: Int): Builder {
            msg = mContext.getText(resId)
            return this
        }

        fun show(manager: FragmentManager): WaitingDialog {
            val dialog = WaitingDialog()
            dialog.isCancelable = (cancelable ?: true)
            dialog.show(manager, this)
            return dialog
        }
    }
}