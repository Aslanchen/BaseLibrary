package com.aslan.app.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int? = null,
    val username: String? = null,
) : Parcelable {

}