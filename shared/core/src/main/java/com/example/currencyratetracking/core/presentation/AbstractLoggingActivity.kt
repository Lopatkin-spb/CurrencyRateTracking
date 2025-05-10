package com.example.currencyratetracking.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.currencyratetracking.common_android.AppTag
import com.example.currencyratetracking.common_android.BaseLogger
import com.example.currencyratetracking.common_android.Tag
import javax.inject.Inject


abstract class AbstractLoggingActivity : ComponentActivity() {

    @Inject
    lateinit var logger: BaseLogger

    @Inject
    @AppTag
    lateinit var tag: Tag


    private var depth = 2

    protected val NAME_CLASS: String
        get() {
            val name = this.javaClass.simpleName
            return name
        }

    private val NAME_METHOD: String
        get() {
            val stackTrace = Throwable().stackTrace

            /**
             * normal name
             */
            var nameMethod = stackTrace[depth].methodName
            /**
             * check coroutine -> access$getNAME_METHOD
             */
            if (nameMethod.substringBefore("$") == "access") {
                //package.FavoritesViewModel$loadFavoritesList$1$1
                val package_Class_Method_Others = stackTrace[depth + 1].className
                //loadFavoritesList$1$1
                val Method_Others = package_Class_Method_Others.substringAfter("$")
                nameMethod = "${Method_Others.substringBefore("$")}.${stackTrace[depth + 1].methodName}"
            }
            return nameMethod
        }

    protected val NAME_FULL: String
        get() {
//            val name = "[$NAME_CLASS $NAME_METHOD()]"
            val name = "$NAME_CLASS $NAME_METHOD()::"
            return name
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        logger.d(tag.LOG, "$NAME_FULL started new")

        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        logger.v(tag.LOG, "$NAME_FULL started new")

        super.onStart()
    }

    override fun onRestart() {
        logger.v(tag.LOG, "$NAME_FULL started")

        super.onRestart()
    }

    override fun onResume() {
        logger.i(tag.LOG, "$NAME_FULL started new")

        super.onResume()
    }

    override fun onPause() {
        logger.i(tag.LOG, "$NAME_FULL started")

        super.onPause()
    }

    override fun onStop() {
        logger.v(tag.LOG, "$NAME_FULL started")

        super.onStop()
    }

    override fun onDestroy() {
        logger.d(tag.LOG, "$NAME_FULL started")

        super.onDestroy()
    }

}