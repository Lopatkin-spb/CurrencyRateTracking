package com.example.currencyratetracking.core


abstract class AbstractRepository {

    protected val NAME_CLASS: String
        get() {
            val name = this.javaClass.simpleName
            return name
        }

}