package com.rehan.cryptoapp.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rehan.cryptoapp.fragments.TopGainLossFragment

// This is Fragment Adapter for TabLayout
class TopGainLossAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2        // This 2 is because we have only 2 fragments inside tab layout
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = TopGainLossFragment()
        val bundle = Bundle()
        bundle.putInt("position", position)
        fragment.arguments = bundle
        return fragment
    }
}