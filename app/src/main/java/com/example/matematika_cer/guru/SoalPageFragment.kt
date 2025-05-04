package com.example.matematika_cer.guru

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.matematika_cer.R
import com.example.matematika_cer.siswa.SoalModel

class SoalPageFragment : Fragment() {

    private lateinit var soalList: List<SoalModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        soalList = arguments?.getParcelableArrayList("soalList") ?: emptyList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_soal_page, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewSoalPerHalaman)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = SoalPageAdapter(soalList)
        return view
    }

    companion object {
        fun newInstance(soalList: List<SoalModel>): SoalPageFragment {
            val fragment = SoalPageFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList("soalList", ArrayList(soalList))
            fragment.arguments = bundle
            return fragment
        }
    }
}
