package com.carsonmiller.metronome.activities

import android.content.res.Configuration
import android.os.Bundle
import com.carsonmiller.metronome.R

/**
 * Activity that handles app when in landscape view
 */
class LandscapeActivity : AbstractOrientationActivity() {

    /**
     * Method that handles creation of activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_landscape)
    }

    /**
     * Handles rotation event
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        switchActivityToOnOrientation(PortraitActivity(), Configuration.ORIENTATION_PORTRAIT)
    }
}