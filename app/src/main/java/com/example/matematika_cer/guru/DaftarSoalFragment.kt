package com.example.matematika_cer.guru

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.matematika_cer.R
import com.example.matematika_cer.model.TopikModel
import com.example.matematika_cer.siswa.SoalModel
import com.example.matematika_cer.guru.SoalPageAdapter
import com.example.matematika_cer.viewmodel.SharedTopikViewModel

class DaftarSoalFragment : Fragment() {

    private lateinit var topik: TopikModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SoalPageAdapter
    private val daftarSoal: MutableList<SoalModel> = mutableListOf()
    private val topikViewModel: SharedTopikViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_daftar_soal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ambil data topik dari argument
        topik = arguments?.getParcelable("topik") ?: run {
            Toast.makeText(requireContext(), "Topik tidak ditemukan", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        recyclerView = view.findViewById(R.id.recyclerViewSoal)

        adapter = SoalPageAdapter(daftarSoal,
            onHapusClick = { soal ->
                daftarSoal.remove(soal)
                topik.soalList.remove(soal)
                topik.jumlahSoal = topik.soalList.size // ✅ update jumlah soal
                topikViewModel.updateTopikLama(topik)   // ✅ update topik ke ViewModel

                adapter.notifyDataSetChanged()
                Toast.makeText(requireContext(), "Soal dihapus", Toast.LENGTH_SHORT).show()
            }
        )


        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        val btnTambah = view.findViewById<Button>(R.id.btnTambahSoal)
        btnTambah.setOnClickListener {
            val input = EditText(requireContext())
            input.inputType = android.text.InputType.TYPE_CLASS_NUMBER
            input.hint = "Masukkan jumlah soal"

            AlertDialog.Builder(requireContext())
                .setTitle("Tambah Soal")
                .setMessage("Berapa soal yang ingin ditambahkan?")
                .setView(input)
                .setPositiveButton("Lanjut") { _, _ ->
                    val jumlah = input.text.toString().toIntOrNull()
                    if (jumlah == null || jumlah <= 0) {
                        Toast.makeText(requireContext(), "Jumlah tidak valid", Toast.LENGTH_SHORT).show()
                    } else {
                        val bundle = Bundle().apply {
                            putParcelable("topik", topik)
                            putInt("jumlahSoal", jumlah)
                            putString("mode", "TAMBAH") // pastikan ini untuk non-edit
                        }
                        findNavController().navigate(R.id.daftarsoal_ke_buatsoal, bundle)
                    }
                }
                .setNegativeButton("Batal", null)
                .show()
        }

        // Ambil soal dari topik (bukan dummy)
        loadDaftarSoal()
    }

    private fun loadDaftarSoal() {
        daftarSoal.clear()
        daftarSoal.addAll(topik.soalList)
        adapter.notifyDataSetChanged()
    }
}