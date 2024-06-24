package com.aslan.app.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.DigitsKeyListener
import com.aslan.app.databinding.ActivityInputBinding
import com.aslan.baselibrary.base.VBBaseActivity

class InputActivity : VBBaseActivity<ActivityInputBinding>(ActivityInputBinding::inflate) {

    companion object {
        @JvmStatic
        fun newIntent(context: Context): Intent {
            return Intent(context, InputActivity::class.java)
        }
    }

    override fun iniBundle(bundle: Bundle) {
    }

    override fun iniView() {
    }

    override fun iniListener() {
    }

    override fun iniData() {
//        mViewBinding.vInput.getEditText().keyListener = (DigitsKeyListener.getInstance("0123456789X"))
    }
}