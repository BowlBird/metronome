package com.carsonmiller.metronome.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Adapter class that takes fragments and allows a view pager 2 to use them
 */
class SettingsPagerAdapter(private val fragmentArray: Array<Fragment>, fa: FragmentActivity) : FragmentStateAdapter(fa) {
    /**
     * returns the size of the array passed in so that each view is displayed
     */
    override fun getItemCount(): Int {
        return fragmentArray.size
    }

    /**
     * creates each fragment in the order of the array
     */
    override fun createFragment(position: Int): Fragment {
        return fragmentArray[position]
    }
}