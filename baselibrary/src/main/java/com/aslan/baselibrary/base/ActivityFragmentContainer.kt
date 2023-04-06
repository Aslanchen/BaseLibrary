package com.aslan.baselibrary.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.aslan.baselibrary.R
import com.aslan.baselibrary.databinding.ActivityFragmentContainerBinding
import kotlin.reflect.KClass

/**
 * Fragment容器，利用反射实例化Fragment，然后加载到Activity中
 *
 * @author Aslan
 * @date 2023/04/06
 */
class ActivityFragmentContainer :
    VBBaseActivity<ActivityFragmentContainerBinding>(ActivityFragmentContainerBinding::inflate) {

    companion object {
        const val TAG_TITLE = "title"
        const val TAG_CLASS_NAME = "className"
        const val TAG_BUNDLE = "bundle"

        @JvmStatic
        fun newInstance(
            context: Context,
            @StringRes title: Int,
            mClass: KClass<out Fragment>,
            args: Bundle
        ): Intent {
            return newInstance(context, title, mClass.qualifiedName!!, args)
        }

        @JvmStatic
        fun newInstance(
            context: Context,
            @StringRes title: Int,
            className: String,
            args: Bundle
        ): Intent {
            return newInstance(context, context.getString(title), className, args)
        }

        @JvmStatic
        fun newInstance(
            context: Context,
            title: String?,
            mClass: KClass<out Fragment>,
            args: Bundle
        ): Intent {
            return newInstance(context, title, mClass.qualifiedName!!, args)
        }

        @JvmStatic
        fun newInstance(context: Context, title: String?, className: String, args: Bundle): Intent {
            val intent = Intent(context, ActivityFragmentContainer::class.java)
            intent.putExtra(TAG_TITLE, title)
            intent.putExtra(TAG_CLASS_NAME, className)
            intent.putExtra(TAG_BUNDLE, args)
            return intent
        }
    }

    private lateinit var className: String
    private var args: Bundle? = null
    private var title: String? = null

    override fun iniBundle(bundle: Bundle) {
        className = bundle.getString(TAG_CLASS_NAME)!!
        title = bundle.getString(TAG_TITLE)
        args = bundle.getBundle(TAG_BUNDLE)
    }

    override fun iniView() {

    }

    override fun iniListener() {

    }

    override fun iniData() {
        if (!title.isNullOrEmpty()) {
            mViewBinding.titleBar.setTitle(title)
        }

        val mFragmentClass = FragmentFactory.loadFragmentClass(classLoader, className)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.flFragmentContainerView, mFragmentClass, args)
            .commitAllowingStateLoss()
    }
}