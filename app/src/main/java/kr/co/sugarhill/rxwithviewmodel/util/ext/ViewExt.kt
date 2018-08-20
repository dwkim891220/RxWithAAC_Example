package kr.co.sugarhill.rxwithviewmodel.util.ext

import android.view.View

fun View.visibilityBoolean(show: Boolean){
    if(show) this.visibility = View.VISIBLE
    else this.visibility = View.GONE
}
