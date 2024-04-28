package com.ifs21044.lostfoundapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DelcomLostFound (
    val id: Int,
    val title: String,
    val description: String,
    val status: String,
    val isCompleted: Boolean,
    val cover: String?,
) : Parcelable