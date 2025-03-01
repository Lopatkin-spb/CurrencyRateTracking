package com.example.currencyratetracking.core

import androidx.lifecycle.ViewModel


abstract class AbstractViewModel : ViewModel() {

    protected val NAME_CLASS: String
        get() {
            val name = this.javaClass.simpleName
            return name
        }

}