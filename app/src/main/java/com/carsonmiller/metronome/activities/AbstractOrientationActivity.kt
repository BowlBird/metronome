package com.carsonmiller.metronome.activities

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import androidx.core.view.WindowCompat
import java.lang.RuntimeException

abstract class AbstractOrientationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window,false) //for edge-to-edge display

        //created function to streamline initialization
        initialize()
    }

    /**
     * function that should be overridden to set up start of each activity
     */
    abstract fun initialize()

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