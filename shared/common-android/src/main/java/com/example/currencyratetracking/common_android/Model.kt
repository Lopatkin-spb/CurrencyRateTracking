package com.example.currencyratetracking.common_android


data class Tag(
    val appAcronym: String,
    val moduleType: String = "",
    val moduleName: String,
) {
    val LOG: String
        get() {
            if (moduleType.isEmpty()) return "$appAcronym.$moduleName"
            return "$appAcronym.$moduleType.$moduleName"
        }
}