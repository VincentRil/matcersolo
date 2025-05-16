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
import com.example.matematika_cer.viewmodel.SharedTopikViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class BankSoalFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabDots: TabLayout
    private lateinit var edtCari: EditText
    private lateinit var pagerAdapter: BankSoalPagerAdapter

    private val topikViewModel: SharedTopikViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bank_soal, container, false)

        viewPager = view.findViewById(R.id.viewPagerBankSoal)
        tabDots = view.findViewById(R.id.tabDotsbanksoal)
        edtCari = view.findViewById(R.id.edtCari)

        // Adapter inisialisasi
        pagerAdapter = BankSoalPagerAdapter(emptyList())
        viewPager.adapter = pagerAdapter
        TabLayoutMediator(tabDots, viewPager) { _, _ -> }.attach()

        // Tampilkan topik awal
        refreshTopik(topikViewModel.daftarTopikSementara)

        // 🔍 Pencarian
        edtCari.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val hasil = topikViewModel.daftarTopikSementara.filter {
                    it.namaTopik.contains(s.toString(), ignoreCase = true)
                }
                refreshTopik(hasil)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        return view
    }

    private fun refreshTopik(list: List<TopikModel>) {
        val grouped = list.chunked(3)
        pagerAdapter.submitData(grouped)
    }
}
