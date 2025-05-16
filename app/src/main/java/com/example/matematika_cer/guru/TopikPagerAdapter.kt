package com.example.matematika_cer.guru

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.matematika_cer.model.TopikModel

class TopikPagerAdapter(
    activity: FragmentActivity,
    private val onEditClick: (TopikModel) -> Unit,
    private val onDeleteClick: (TopikModel) -> Unit
) : FragmentStateAdapter(activity) {

    private var topikGroups: List<List<TopikModel>> = emptyList()

    fun submitData(newGroups: List<List<TopikModel>>) {
        topikGroups = newGroups
        notifyDataSetChanged() // 🔁 Beri tahu adapter bahwa datanya berubah
    }

    override fun getItemCount(): Int = topikGroups.size

    override fun createFragment(position: Int): Fragment {
        return TopikGridFragment.newInstance(
            topikList = topikGroups[position],
            onEditClick = onEditClick,
            onDeleteClick = onDeleteClick
        )
    }

    // 🔁 Penting agar ViewPager tahu bahwa ID halaman berubah → agar bisa refresh isinya
    override fun getItemId(position: Int): Long {
        val idString = topikGroups[position].joinToString { it.namaTopik }
        return idString.hashCode().toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        return topikGroups.any {
            it.joinToString { topik -> topik.namaTopik }.hashCode().toLong() == itemId
        }
    }
}
