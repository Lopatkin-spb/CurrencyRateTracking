package com.example.currencyratetracking.core


abstract class AbstractRepository {

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

}