package com.example.matematika_cer

import TopikModel
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class TopikPagerAdapter(
    fa: FragmentActivity,
    private val topikGroups: List<List<TopikModel>>
) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = topikGroups.size

    override fun createFragment(position: Int): Fragment {
        return TopikGroupFragment.newInstance(topikGroups[position])
    }
}
