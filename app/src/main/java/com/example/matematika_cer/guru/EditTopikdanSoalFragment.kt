package com.example.matematika_cer.guru

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.matematika_cer.R
import com.example.matematika_cer.model.TopikModel
import com.example.matematika_cer.viewmodel.SharedTopikViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class EditTopikdanSoalFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var searchEditText: EditText
    private val topikViewModel: SharedTopikViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_topikdan_soal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = view.findViewById(R.id.viewPagerEditTopik)
        tabLayout = view.findViewById(R.id.tabDots)
        searchEditText = view.findViewById(R.id.searchEditText)

        val semuaTopik = topikViewModel.daftarTopikSementara
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
                val topikTerbaru = topikViewModel.daftarTopikSementara.find {
                    it.namaTopik == topik.namaTopik
                } ?: topik

                val bundle = Bundle().apply {
                    putParcelable("topik", topikTerbaru)
                }

                findNavController().navigate(
                    R.id.action_editTopikdanSoalFragment_to_detailEditSoalFragment,
                    bundle
                )
            },
            onTopikLongClick = { topik ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Hapus Topik")
                    .setMessage("Yakin ingin menghapus topik '${topik.namaTopik}'?")
                    .setPositiveButton("Ya") { _, _ ->
                        topikViewModel.hapusTopik(topik)
                        Toast.makeText(requireContext(), "Topik dihapus", Toast.LENGTH_SHORT).show()
                        tampilkanTopikKeViewPager(topikViewModel.daftarTopikSementara)
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            }
        )

        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()
    }
}