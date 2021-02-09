package com.box.punkapi.utils

import android.widget.TextView
import androidx.core.text.HtmlCompat

fun String.appendBoldTag(): String = "<b>$this</b>"
fun String.appendHTag(oneToSix: Int): String {
    return when (oneToSix) {
        1 -> {
            "<h1>$this</h1>"
        }
        2 -> {
            "<h2>$this</h2>"
        }
        3->{
            "<h3>$this</h3>"
        }
        4->{
            "<h4>$this</h4>"
        }
        5->{
            "<h5>$this</h5>"
        }
        6->{
            "<h6>$this</h6>"
        }
        else->this
    }
}

fun TextView.setHtmlText(text: String) {
    setText(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_COMPACT))
}