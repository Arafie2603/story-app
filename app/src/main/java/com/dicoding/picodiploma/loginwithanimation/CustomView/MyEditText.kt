package com.dicoding.picodiploma.loginwithanimation.CustomView

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

class MyEditText @JvmOverloads constructor(
    context: Context,attrs: AttributeSet?= null
) : AppCompatEditText(context, attrs), View.OnTouchListener {
    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        return true
    }

    override fun onTextChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {
        if (s.toString().length < 8) {
            setError("Password tidak boleh kurang dari 8 karakter", null)
        } else {
            error = null
        }
    }
}