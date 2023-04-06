package com.aslan.baselibrary.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.aslan.baselibrary.R

/**
 * 等待框
 */
open class WaitingDialog : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        val isShownMsg = arguments?.getBoolean("isShownMsg") ?: true
        if (msg.isNullOrEmpty()) {
            msg = getString(R.string.progress_waiting)
        }

        view.findViewById<TextView>(R.id.tvText).setText(msg)
        view.findViewById<TextView>(R.id.tvText).isVisible = isShownMsg
    }

    open fun iniListener() {}

    open fun iniData() {
    }

    open fun show(manager: FragmentManager, msg: String? = null, isShownMsg: Boolean = true) {
        val bundle = Bundle()
        if (msg != null) {
            bundle.putString("msg", msg)
        }
        bundle.putBoolean("isShownMsg", isShownMsg)
        arguments = bundle
        super.show(manager, WaitingDialog::class.java.simpleName)
    }
}