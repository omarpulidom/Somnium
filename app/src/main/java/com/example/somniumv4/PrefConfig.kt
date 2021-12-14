package com.example.somniumv4

import android.content.Context
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.collections.ArrayList

val array = ArrayList<String>()

object PrefConfig {

    fun writeListInPref(context: Context?, key: String, list: ArrayList<String>) {
        val gson = Gson()
        val jsonString = gson.toJson(list)
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = pref.edit()
        editor.putString(key, jsonString)
        editor.apply()
    }

    fun readListFromPref(context: Context?, key: String): ArrayList<String> {

        array.clear()
        array.add("00 00 0")

        val pref = PreferenceManager.getDefaultSharedPreferences(context)

        val jsonString = pref.getString(key, "")

        if (jsonString.toString().isNotEmpty())
        {
            val gson = Gson()
            val type = object : TypeToken<ArrayList<String?>?>() {}.type
            return gson.fromJson(jsonString, type)
        }
        else
        {
            return array
        }
    }
}