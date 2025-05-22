package com.example.matematika_cer.siswa

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.matematika_cer.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RiwayatKuisFragment : Fragment() {

    private lateinit var adapter: RiwayatKuisAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchBar: EditText
    private var riwayatList: List<RiwayatKuisModel> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_riwayat_kuis, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerViewRiwayat)
        searchBar = view.findViewById(R.id.searchBarRiwayat)

        // Siapkan RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        riwayatList = ambilRiwayatKuis(requireContext())
        adapter = RiwayatKuisAdapter(riwayatList)
        recyclerView.adapter = adapter

        // Fitur pencarian riwayat
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val keyword = s.toString().lowercase()
                val filtered = riwayatList.filter {
                    it.namaTopik.lowercase().contains(keyword)
                }
                adapter.filterList(filtered)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    // Ambil riwayat kuis dari SharedPreferences
    private fun ambilRiwayatKuis(context: Context): List<RiwayatKuisModel> {
        val prefs = context.getSharedPreferences("riwayat_kuis", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = prefs.getString("list_riwayat", "[]")
        val type = object : TypeToken<ArrayList<RiwayatKuisModel>>() {}.type
        return gson.fromJson(json, type)
    }
}
