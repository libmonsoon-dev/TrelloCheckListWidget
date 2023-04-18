package com.github.libmonsoon.trellochecklistwidget

import android.app.Activity
import android.os.Bundle
import android.util.Log

//TODO: add retry button
class ErrorActivity: Activity()   {
    private var error: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.getStringExtra(EXTRA_KEY).let {
            error = it
            Log.e("ErrorActivity", it!!)
        }

        //TODO: button with come back functionality
//        Thread {
//            Log.v("ErrorActivity", "before sleep")
//            Thread.sleep(3000)
//            Log.v("ErrorActivity", "after sleep")
//            finishAffinity()
//        }.start()
    }

    companion object {
        const val EXTRA_KEY = "error"
    }
}