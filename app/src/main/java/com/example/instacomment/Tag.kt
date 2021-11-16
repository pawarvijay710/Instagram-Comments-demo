package com.example.instacomment

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast

class Tag {
    class UserTag(private val text: String) : ClickableSpan() {
        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
        }

        override fun onClick(widget: View) {
            Toast.makeText(widget.context, text, Toast.LENGTH_SHORT).show()
        }
    }
}