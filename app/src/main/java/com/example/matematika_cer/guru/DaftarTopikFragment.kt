package com.example.matematika_cer.guru

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.matematika_cer.R
import com.example.matematika_cer.model.TopikModel
import com.example.matematika_cer.guru.TopikPagerAdapter
import com.example.matematika_cer.viewmodel.SharedTopikViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.text.SimpleDateFormat
import java.util.*
class DaftarTopikFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var searchEditText: EditText
    private lateinit var tambahTopikBtn: Button
    private lateinit var topikAdapter: TopikPagerAdapter

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
        tambahTopikBtn = view.findViewById(R.id.tambahTopikBtn)

        // Inisialisasi adapter sekali saja
        topikAdapter = TopikPagerAdapter(
            requireActivity(),
            onEditClick = { topik ->
                topikViewModel.updateTopikLama(topik)
                refreshTopik()
            },
            onDeleteClick = { topik ->
                topikViewModel.hapusTopik(topik)
                refreshTopik()
            }
        )
        viewPager.adapter = topikAdapter
        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()

        // Awal
        refreshTopik()

        // Pencarian
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val hasil = topikViewModel.daftarTopikSementara.filter {
                    it.namaTopik.contains(s.toString(), ignoreCase = true)
                }
                topikAdapter.submitData(hasil.chunked(3))
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        // Tambah topik
        tambahTopikBtn.setOnClickListener { tampilkanDialogTambahTopik() }
    }

    private fun refreshTopik() {
        val grouped = topikViewModel.daftarTopikSementara.chunked(3)
        topikAdapter.submitData(grouped)
    }

    private fun tampilkanDialogTambahTopik() {
        val input = EditText(requireContext())
        input.hint = "Masukkan nama topik"

        AlertDialog.Builder(requireContext())
            .setTitle("Tambah Topik Baru")
            .setView(input)
            .setPositiveButton("Simpan") { _, _ ->
                val nama = input.text.toString().trim()
                if (nama.isBlank()) {
                    Toast.makeText(requireContext(), "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show()
                } else if (topikViewModel.isTopikSudahAda(nama)) {
                    Toast.makeText(requireContext(), "Topik sudah ada", Toast.LENGTH_SHORT).show()
                } else {
                    val topikBaru = TopikModel(
                        id = topikViewModel.generateTopikId(),
                        namaTopik = nama,
                        deskripsiTopik = "",
                        durasi = "0",
                        jumlahSoal = 0,
                        tanggal = getTodayDate(),
                        isAktif = false,
                        jumlahMenjawab = 0,
                        totalPeserta = 0
                    )

                    topikViewModel.tambahTopik(topikBaru)
                    refreshTopik()
                    Toast.makeText(requireContext(), "Topik ditambahkan", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun getTodayDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }
}


