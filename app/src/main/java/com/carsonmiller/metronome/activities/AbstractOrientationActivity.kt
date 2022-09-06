package com.carsonmiller.metronome.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

abstract class AbstractOrientationActivity : AppCompatActivity() {

    /**
     * Switches activity to selected activity
     */
    protected fun switchActivity(activity: Activity) {
        val myIntent = Intent(this, activity::class.java)
        startActivity(myIntent)
    }

    /**
     * Switches to activity if and only if orientation is equal to argument currently
     */
    protected fun switchActivityToOnOrientation(activity: Activity, orientation: Int) {
        if(this.resources.configuration.orientation == orientation)
            switchActivity(activity)
    }
}