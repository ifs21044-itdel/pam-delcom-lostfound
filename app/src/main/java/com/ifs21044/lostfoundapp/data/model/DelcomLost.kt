package com.ifs21044.lostfoundapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DelcomLost(
    val id: Int,
    val userId: Int,
    val title: String,
    val description: String,
    val status: String,
    var isComplete: Boolean,
) : Parcelable