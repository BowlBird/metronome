package com.carsonmiller.metronome.state

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

/**
 * class that handles saving logic
 */
class Store(activity: Activity) {
    val sharedPref: SharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)

    /* for volatile storage of values */
    companion object {
        val map = HashMap<String, String>()
    }

    /**
     * puts an item onto the hardDrive
     * MUST BE PRIMITIVE TYPE
     */
    fun <T> put(item: T, key: String): T {
        sharedPref.edit().putString(key, item.toString()).apply()
        return item
    }

    /**
     * puts an item into RAM
     * MUST BE PRIMITIVE TYPE
     */
    fun <T> volatilePut(item: T, key: String): T {
        map[key] = item.toString()
        return item
    }

    /**
     * Returns value referenced by key as type T on Hard Drive
     * if it doesn't exist, throws exception
     */
    inline fun <reified T> get(key: String, defaultValue: T): T {
        val returnedValue =  sharedPref.getString(key, null) ?: return defaultValue

        return when(T::class) {
            Int::class -> returnedValue.toInt() as T
            Boolean::class -> returnedValue.toBoolean() as T
            String::class -> returnedValue as T
            Double::class -> returnedValue.toDouble() as T
            Float::class -> returnedValue.toFloat() as T
            Long::class -> returnedValue.toLong() as T
            Short::class -> returnedValue.toShort() as T
            else -> throw IllegalArgumentException("Could not convert type")
        }
    }

    /**
     * Returns value referenced by key as type T in RAM
     * if it doesn't exist, throws exception
     */
    inline fun <reified T> volatileGet(key: String, defaultValue: T): T {
        val returnedValue =  map[key] ?: return defaultValue

        return when(T::class) {
            Int::class -> returnedValue.toInt() as T
            Boolean::class -> returnedValue.toBoolean() as T
            String::class -> returnedValue as T
            Double::class -> returnedValue.toDouble() as T
            Float::class -> returnedValue.toFloat() as T
            Long::class -> returnedValue.toLong() as T
            Short::class -> returnedValue.toShort() as T
            else -> throw IllegalArgumentException("Could not convert type")
        }

    }

    /**
     * Clears entire Shared Pref
     */
    fun clearAllPreferences() = sharedPref.edit().clear().apply()
}