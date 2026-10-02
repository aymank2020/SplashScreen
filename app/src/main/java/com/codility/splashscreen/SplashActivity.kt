package com.codility.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity


/**
 * Created by Govind on 2/1/2018.
 */
class SplashActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private var remainingDelay = 3000L
    private var deadline = 0L
    private var resumed = false

    private val runnable: Runnable = Runnable {
        if (resumed && !isFinishing) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)

        remainingDelay = savedInstanceState?.getLong("remainingDelay", 3000L) ?: 3000L
    }

    override fun onResume() {
        super.onResume()
        resumed = true
        deadline = SystemClock.uptimeMillis() + remainingDelay
        handler.postDelayed(runnable, remainingDelay)
    }

    override fun onPause() {
        resumed = false
        remainingDelay = (deadline - SystemClock.uptimeMillis()).coerceAtLeast(0L)
        handler.removeCallbacks(runnable)
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val delay = if (resumed) (deadline - SystemClock.uptimeMillis()).coerceAtLeast(0L) else remainingDelay
        outState.putLong("remainingDelay", delay)
        super.onSaveInstanceState(outState)
    }

    public override fun onDestroy() {
        handler.removeCallbacks(runnable)
        super.onDestroy()
    }
}
