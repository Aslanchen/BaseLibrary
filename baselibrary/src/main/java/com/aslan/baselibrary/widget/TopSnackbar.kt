package com.aslan.baselibrary.widget

import android.view.LayoutInflater
import android.view.ViewGroup
import com.aslan.baselibrary.R
import com.google.android.material.snackbar.BaseTransientBottomBar

class TopSnackbar(
    parent: ViewGroup,
    content: TopSnackbarView
) : BaseTransientBottomBar<TopSnackbar>(parent, content, content) {

    init {

    }

    companion object {
        fun make(viewGroup: ViewGroup, title: String, content: String): TopSnackbar {
            val customSnackbar = LayoutInflater.from(viewGroup.context).inflate(R.layout.top_snackbar, viewGroup, false) as TopSnackbarView
            customSnackbar.setTitle(title)
            customSnackbar.setContent(content)

            val mTopSnackbar = TopSnackbar(viewGroup, customSnackbar)
            mTopSnackbar.setDuration(BaseTransientBottomBar.LENGTH_INDEFINITE)
            mTopSnackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE)
            return mTopSnackbar
        }
    }
}