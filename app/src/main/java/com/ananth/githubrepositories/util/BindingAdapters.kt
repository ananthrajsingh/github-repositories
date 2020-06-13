package com.ananth.githubrepositories.util

import android.view.View
import androidx.databinding.BindingAdapter
import com.ananth.githubrepositories.model.Repository


@BindingAdapter("showOnlyWhenEmpty")
fun View.showOnlyWhenEmpty(data: List<Repository>?) {
    visibility = when {
        data == null || data.isEmpty() -> View.VISIBLE
        else -> View.GONE
    }
}