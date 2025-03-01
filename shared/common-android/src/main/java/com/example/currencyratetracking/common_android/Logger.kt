package com.example.currencyratetracking.common_android

import android.util.Log
import javax.inject.Inject


interface BaseLogger {

    fun v(tag: String, msg: String)
    fun d(tag: String, msg: String)
    fun i(tag: String, msg: String)
    fun w(tag: String, msg: String, cause: Throwable)
    fun e(tag: String, msg: String, cause: Throwable)

}

internal class LogcatBaseLogger @Inject constructor() : BaseLogger {

    override fun v(tag: String, msg: String) {
        if (tag.isEmpty()) {
            TODO("Tag cannot be empty")
        } else {
            Log.v(tag, msg)
        }
    }

    override fun d(tag: String, msg: String) {
        if (tag.isEmpty()) {
            TODO("Tag cannot be empty")
        } else {
            Log.d(tag, msg)
        }
    }

    override fun i(tag: String, msg: String) {
        if (tag.isEmpty()) {
            TODO("Tag cannot be empty")
        } else {
            Log.i(tag, msg)
        }
    }

    override fun w(tag: String, msg: String, cause: Throwable) {
        if (tag.isEmpty()) {
            TODO("Tag cannot be empty")
        } else {
            Log.w(tag, msg, cause)
        }
    }

    override fun e(tag: String, msg: String, cause: Throwable) {
        if (tag.isEmpty()) {
            TODO("Tag cannot be empty")
        } else {
            Log.e(tag, msg, cause)
        }
    }

}

interface UpdatedBaseLogger {

    fun v(tag: String, hdr: String, msg: String)
    fun d(tag: String, hdr: String, msg: String)
    fun i(tag: String, hdr: String, msg: String)
    fun w(tag: String, hdr: String, msg: String, cause: Throwable)
    fun e(tag: String, hdr: String, msg: String, cause: Throwable)

}

internal class LogcatUpdatedBaseLogger : UpdatedBaseLogger {
    override fun v(tag: String, hdr: String, msg: String) {
        TODO("Not yet implemented")
    }

    override fun d(tag: String, hdr: String, msg: String) {
        TODO("Not yet implemented")
    }

    override fun i(tag: String, hdr: String, msg: String) {
        TODO("Not yet implemented")
    }

    override fun w(tag: String, hdr: String, msg: String, cause: Throwable) {
        TODO("Not yet implemented")
    }

    override fun e(tag: String, hdr: String, msg: String, cause: Throwable) {
        TODO("Not yet implemented")
    }

}
