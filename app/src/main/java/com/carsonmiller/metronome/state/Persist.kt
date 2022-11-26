package com.carsonmiller.metronome.state

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

/**
 * Abstract class that handles saving logic
 */
abstract class Persist(activity: Activity) {
    val sharedPref: SharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)

    /**
     * puts an item onto the hardDrive
     * MUST BE PRIMITIVE TYPE
     */
    fun <T> put(item: T, key: String): T {
        sharedPref.edit().putString(key, item.toString()).apply()
        return item
    }

    /**
     * Returns value referenced by key as type T
     * if it doesn't exist, throws exception
     */
    inline fun <reified T> get(key: String, defaultValue: T): T {
        val returnedValue = sharedPref.getString(key, null) ?: return defaultValue

        return when(T::class) {
            Double::class -> returnedValue.toDouble() as T
            Boolean::class -> returnedValue.toBoolean() as T
            Float::class -> returnedValue.toFloat() as T
            Long::class -> returnedValue.toLong() as T
            String::class -> returnedValue as T
            Int::class -> returnedValue.toInt() as T
            Short::class -> returnedValue.toShort() as T
            else -> throw IllegalArgumentException("Could not convert type")
        }
    }

    /**
     * Clears entire Shared Pref
     */
    fun clearAllPreferences() = sharedPref.edit().clear().apply()

    /**
     * Resets each element to defaults
     */
    abstract fun reset()
}