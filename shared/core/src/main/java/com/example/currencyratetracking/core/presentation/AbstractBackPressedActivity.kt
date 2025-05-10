package com.example.currencyratetracking.core.presentation

import android.os.Bundle
import androidx.activity.OnBackPressedCallback


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
                logger.i(tag.LOG, "$NAME_FULL started")

                if (isEnabled) {
                    prepareAppForColdClose()
                    finish()
                }
            }
        })
    }

    protected abstract fun prepareAppForColdClose()

}