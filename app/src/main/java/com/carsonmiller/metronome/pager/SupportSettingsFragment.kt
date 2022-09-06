package com.carsonmiller.metronome.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.carsonmiller.metronome.R

/**
 * Fragment that shows way of contacting yours truly :)
 */
class SupportSettingsFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.support_settings, container, false)
}