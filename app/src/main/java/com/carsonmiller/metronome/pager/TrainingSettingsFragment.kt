package com.carsonmiller.metronome.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.carsonmiller.metronome.R

/**
 * Fragment that contains settings to slowly increase bpm
 */
class TrainingSettingsFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.training_settings, container, false)
}