package com.carsonmiller.metronome.activities


import android.content.Intent
import android.content.res.Configuration
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.carsonmiller.metronome.ComposeActivity
import com.carsonmiller.metronome.R
import com.carsonmiller.metronome.pager.*


/**
 * Class that handles the app when in portrait mode
 */
class PortraitActivity : AbstractOrientationActivity(R.layout.activity_portrait) {

    /**
     * vars
     */
    private lateinit var viewPager: ViewPager2
    private var portraitViews = arrayOf(MetronomeSettingsFragment() as Fragment,
                                        TrainingSettingsFragment() as Fragment,
                                        SoundSettingsFragment() as Fragment,
                                        SupportSettingsFragment() as Fragment)

    /**
     * initializes the activity
     */
    override fun initialize() {
        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.vp2Features)

        // The pager adapter, which provides the pages to the view pager widget.
        val pagerAdapter = SettingsPagerAdapter(portraitViews,this)
        viewPager.adapter = pagerAdapter
    }

    /*
    TEMPORARY
     */
    open fun goToAnActivity(view: View?) {
        val intent = Intent(this, ComposeActivity()::class.java)
        startActivity(intent)
    }

    /**
     * Handles rotation event to landscape mode
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        switchActivityToOnOrientation(LandscapeActivity(), Configuration.ORIENTATION_LANDSCAPE)
    }
}