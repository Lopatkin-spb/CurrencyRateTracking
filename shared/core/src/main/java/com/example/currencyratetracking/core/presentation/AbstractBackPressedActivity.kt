package com.example.currencyratetracking.core.presentation

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.example.currencyratetracking.model.LogLevel


abstract class AbstractBackPressedActivity : AbstractLoggingActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBackPressed()
    }

    private fun setupBackPressed() {
        //if need disable this logic - need disable this dispatcher:
        // isEnabled = false => your logic => isEnabled = true
        this.onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {
                logging(LogLevel.INFO, "$NAME_FULL started")

                if (isEnabled) {
                    prepareAppForColdClose()
                    finish()
                }
            }
        })
    }

    protected abstract fun prepareAppForColdClose()

}