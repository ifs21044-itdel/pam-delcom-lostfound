package com.ifs21044.lostfoundapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DelcomLostFound (
    val id: Int,
    val title: String,
    val description: String,
    val status: String,
    val iscompleted: Boolean,
    val cover: String?,
) : Parcelable