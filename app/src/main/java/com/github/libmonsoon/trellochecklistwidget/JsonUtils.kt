package com.github.libmonsoon.trellochecklistwidget

import com.google.gson.Gson
import com.google.gson.GsonBuilder

class JsonUtils {
    companion object {
        val instance: JsonUtils
            get()  {
                if (inst == null) {
                    inst = JsonUtils()
                }

                return inst!!
            }

        private var inst: JsonUtils? = null
        val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    }



    //TODO: parse JsonSyntaxException and try to find problem using JsonPath lib
    // https://stackoverflow.com/a/70926102
    // TODO: Fix dependency resolution errors
    // https://developer.android.com/build/dependencies#resolution_errors
    fun prettyPrintingJson(data: String?): String = data ?: ""
    // gson.toJson(JsonPath.parse((data)))
}