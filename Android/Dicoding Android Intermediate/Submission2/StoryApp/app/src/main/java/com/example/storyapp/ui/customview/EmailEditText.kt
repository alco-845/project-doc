package com.example.storyapp.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener

class EmailEditText : AppCompatEditText {

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

        hint = "Email"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {
        addTextChangedListener(onTextChanged = { s, _, _, _ ->
            if (!Patterns.EMAIL_ADDRESS.matcher(s!!).matches()) {
                this@EmailEditText.error = "Email is not valid"
            } else if (s.isEmpty()) {
                this@EmailEditText.error = "Email must be filled"
            }
        })
    }
}