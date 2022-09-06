package com.carsonmiller.metronome.activities


import android.content.res.Configuration
import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.carsonmiller.metronome.R
import com.carsonmiller.metronome.pager.*

/**
 * Class that handles the app when in portrait mode
 */
class PortraitActivity : AbstractOrientationActivity() {

    /**
     * Instance vars
     */
    private lateinit var viewPager: ViewPager2
    private var portraitViews = arrayOf(MetronomeSettingsFragment() as Fragment,
                                        TrainingSettingsFragment() as Fragment,
                                        SoundSettingsFragment() as Fragment,
                                        SupportSettingsFragment() as Fragment)

    /**
     * method that creates an instance of this activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window,false) //for edge-to-edge display
        setContentView(R.layout.activity_portrait)

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.vp2Features)

        // The pager adapter, which provides the pages to the view pager widget.
        val pagerAdapter = SettingsPagerAdapter(portraitViews,this)
        viewPager.adapter = pagerAdapter
    }

    /**
     * Handles rotation event to landscape mode
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        switchActivityToOnOrientation(LandscapeActivity(), Configuration.ORIENTATION_LANDSCAPE)
    }
}