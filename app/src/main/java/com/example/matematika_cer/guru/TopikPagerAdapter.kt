package com.example.matematika_cer.guru

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.matematika_cer.model.TopikModel

class TopikPagerAdapter(
    activity: FragmentActivity,
    private val topikGroups: List<List<TopikModel>>,
    private val onTopikClick: (TopikModel) -> Unit,
    private val onTopikLongClick: (TopikModel) -> Unit
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = topikGroups.size

    override fun createFragment(position: Int): Fragment {
        return TopikGridFragment.newInstance(
            topikGroups[position],
            onTopikClick,
            onTopikLongClick
        )
    }
}