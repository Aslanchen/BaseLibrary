package com.aslan.baselibrary.utils

import android.view.LayoutInflater
import android.view.ViewGroup

typealias InflateActivity<T> = (LayoutInflater) -> T
typealias InflateFragment<T> = (LayoutInflater, ViewGroup?, Boolean) -> T