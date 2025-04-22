package com.example.matematika_cer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class BerandaGuruFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_beranda_guru, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val namaTextView = view.findViewById<TextView>(R.id.namaGuruBeranda)
        val namaGuru = arguments?.getString("nama") ?: ""
        namaTextView.text = "Halo Ibu $namaGuru !!"

        val daftarTopik = view.findViewById<CardView>(R.id.daftar_topik)
        val bankSoal = view.findViewById<CardView>(R.id.bank_soal)
        val buatTopik = view.findViewById<CardView>(R.id.buat_topikdansoal)
        val editTopik = view.findViewById<CardView>(R.id.edit_topikdansoal)

        daftarTopik.setOnClickListener {
            findNavController().navigate(R.id.action_beranda_to_daftarTopik)
        }
        bankSoal.setOnClickListener {
            findNavController().navigate(R.id.action_beranda_to_bankSoal)
        }
        buatTopik.setOnClickListener {
            findNavController().navigate(R.id.action_beranda_to_tambahTopik)
        }
        editTopik.setOnClickListener {
            findNavController().navigate(R.id.action_beranda_to_editTopik)
        }
    }
}