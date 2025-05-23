package com.example.matematika_cer.siswa

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.matematika_cer.model.TopikModel

class TopikPagerAdapter(
    fa: FragmentActivity,
    private val topikGroups: List<List<TopikModel>>,
    private val onTopikClick: (TopikModel) -> Unit,
    private val onTopikLongClick: (TopikModel) -> Unit
) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = topikGroups.size

    override fun createFragment(position: Int): Fragment {
        return TopikGroupFragment.newInstance(
            topikGroups[position],
            onTopikClick,
            onTopikLongClick
        )
    }
}
