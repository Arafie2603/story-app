package com.dicoding.picodiploma.loginwithanimation.CustomView

import android.content.Context
import android.util.AttributeSet
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

class MyEditTextEmail @JvmOverloads constructor(
    context: Context, attrs: AttributeSet?=null
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
        super.onTextChanged(text, start, before, count)

        if (!Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
            setError("Tolong gunakan alamat email yang valid", null)
        } else {
            error = null
        }
    }
}