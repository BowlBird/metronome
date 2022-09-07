package com.carsonmiller.metronome.activities

import android.content.res.Configuration
import android.os.Bundle
import androidx.core.view.WindowCompat
import com.carsonmiller.metronome.R

/**
 * Activity that handles app when in landscape view
 */
class LandscapeActivity : AbstractOrientationActivity(R.layout.activity_landscape) {

    /**
     * Method that handles creation of activity
     */
    override fun initialize() {
    }

    /**
     * Handles rotation event
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        switchActivityToOnOrientation(PortraitActivity(), Configuration.ORIENTATION_PORTRAIT)
    }
}