package com.ananth.githubrepositories.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Owner(
    @Json(name = "login") val username: String,
    @Json(name = "avatar_url") val imageUrl: String,
    @Json(name = "html_url") val profileUrl: String
) : Parcelable