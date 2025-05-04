package com.example.matematika_cer.guru

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.matematika_cer.siswa.SoalModel

class SoalPagerAdapter(
    fa: FragmentActivity,
    private val soalGroups: List<List<SoalModel>>
) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = soalGroups.size

    override fun createFragment(position: Int): Fragment {
        return SoalPageFragment.newInstance(soalGroups[position])
    }
}
