package com.example.storyapp.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener

class PasswordEditText : AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        hint = "Password"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {
        addTextChangedListener(onTextChanged = { s, _, _, _ ->
            if (s!!.length < 8) {
                this@PasswordEditText.error = "Password must be at least 8 characters long"
            } else if (s.isEmpty()) {
                this@PasswordEditText.error = "Password must be filled"
            }
        })
    }
}