package com.example.currencyratetracking.core

import androidx.activity.ComponentActivity


abstract class AbstractActivity : ComponentActivity() {

    protected val NAME_CLASS: String
        get() {
            val name = this.javaClass.simpleName
            return name
        }

}