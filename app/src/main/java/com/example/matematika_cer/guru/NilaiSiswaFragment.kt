package com.example.matematika_cer.guru

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.matematika_cer.R
import com.example.matematika_cer.model.TopikModel
import com.example.matematika_cer.viewmodel.SharedTopikViewModel

class NilaiSiswaFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TopikNilaiAdapter
    private val topikViewModel: SharedTopikViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_nilai_siswa, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.recyclerViewTopikNilai)
        val listTopik = topikViewModel.daftarTopikSementara

        adapter = TopikNilaiAdapter(listTopik) { topik ->
            val bundle = Bundle().apply {
                putParcelable("topik", topik)
            }
            findNavController().navigate(R.id.nilaisiswa_ke_daftarnilai, bundle)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }
}
