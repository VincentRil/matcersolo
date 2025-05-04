package com.example.matematika_cer.guru

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.matematika_cer.R
import com.example.matematika_cer.model.TopikModel
import com.example.matematika_cer.siswa.TopikPagerAdapter
import com.example.matematika_cer.viewmodel.SharedTopikViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DaftarTopikFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var searchEditText: EditText

    private lateinit var semuaTopik: List<TopikModel>
    private val topikViewModel: SharedTopikViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_daftar_topik, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = view.findViewById(R.id.viewPagerTopik)
        tabLayout = view.findViewById(R.id.tabDots)
        searchEditText = view.findViewById(R.id.searchEditText)

        semuaTopik = topikViewModel.daftarTopikSementara
        tampilkanTopikKeViewPager(semuaTopik)

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val hasilFilter = semuaTopik.filter {
                    it.namaTopik.contains(s.toString(), ignoreCase = true)
                }
                tampilkanTopikKeViewPager(hasilFilter)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun tampilkanTopikKeViewPager(list: List<TopikModel>) {
        val groupedTopik = list.chunked(3)

        val adapter = TopikPagerAdapter(
            requireActivity(),
            groupedTopik,
            onTopikClick = { topik ->
                // Aksi klik topik oleh guru, bisa kosong
            },
            onTopikLongClick = { topik ->
                // Aksi long click juga bisa kosong karena hanya lihat
            }
        )

        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()
    }
}
