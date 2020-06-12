package com.ananth.githubrepositories.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
// TODO Add fields
@Parcelize
data class Repository(
    val name: String,
    val private: Boolean
) : Parcelable