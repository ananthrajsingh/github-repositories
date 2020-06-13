package com.ananth.githubrepositories.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * This class defines what all data will be fetched from the API. This model is used by
 * [RepoApiService] to parse JSON response to objects of this class
 */
@Parcelize
data class Repository(
    val id: Int,
    val name: String,
    @Json(name = "full_name") val fullName: String,
    @Json(name = "html_url") val repoUrl: String,
    val description: String?,
    val owner: Owner
) : Parcelable