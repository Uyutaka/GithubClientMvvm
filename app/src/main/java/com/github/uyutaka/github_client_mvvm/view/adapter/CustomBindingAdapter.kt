package com.github.uyutaka.github_client_mvvm.view.adapter

import android.databinding.BindingAdapter
import android.view.View

object CustomBindingAdapter {
    //xmlに定義する際のBindingAdapter
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }
}