package com.carsonmiller.metronome.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.carsonmiller.metronome.R

/**
 * Fragment for the Metronome Settings Layout that contains time signature and subdivision
 */
class MetronomeSettingsFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.metronome_settings, container, false)
}