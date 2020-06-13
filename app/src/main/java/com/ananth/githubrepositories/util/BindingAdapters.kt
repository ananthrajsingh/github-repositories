package com.ananth.githubrepositories.util

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ananth.githubrepositories.list.RepoListAdapter
import com.ananth.githubrepositories.model.Repository



@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Repository>?) {
    val adapter = recyclerView.adapter as RepoListAdapter?
    adapter?.submitList(data) {
        // scroll the list to the top after the diffs are calculated and posted
        recyclerView.scrollToPosition(0)
    }
}


@BindingAdapter("showOnlyWhenEmpty")
fun View.showOnlyWhenEmpty(data: List<Repository>?) {
    visibility = when {
        data == null || data.isEmpty() -> View.VISIBLE
        else -> View.GONE
    }
}