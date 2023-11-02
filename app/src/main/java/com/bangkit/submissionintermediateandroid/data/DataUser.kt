package com.bangkit.submissionintermediateandroid.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataUser (
    val id: String,
    val imgUrl: String,
    val name: String,
    val description: String
) : Parcelable