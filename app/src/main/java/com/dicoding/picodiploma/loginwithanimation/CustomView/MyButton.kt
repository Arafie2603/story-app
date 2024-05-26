package com.dicoding.picodiploma.loginwithanimation.CustomView

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatButton


class MyButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatButton(context, attrs) {
    private val myEditText: MyEditText?=null
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val temp = myEditText.toString().length
        Log.d("TesterCode", "$temp")
    }
}