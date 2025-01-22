package com.aslan.app.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.aslan.app.base.ActivityBase
import com.aslan.app.databinding.ActivityInputBinding

class InputActivity : ActivityBase<ActivityInputBinding>(ActivityInputBinding::inflate) {

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