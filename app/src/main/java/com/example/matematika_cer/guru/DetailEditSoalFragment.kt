package com.example.matematika_cer.guru

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.matematika_cer.R
import com.example.matematika_cer.model.TopikModel
import com.example.matematika_cer.viewmodel.SharedTopikViewModel

class DetailEditSoalFragment : Fragment() {

    private lateinit var topik: TopikModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SoalEditAdapter
    private lateinit var btnTambahSoal: Button
    private val topikViewModel: SharedTopikViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_detail_edit_soal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerEditSoal)
        btnTambahSoal = view.findViewById(R.id.btnTambahSoal)

        topik = arguments?.getParcelable("topik") ?: run {
            Toast.makeText(requireContext(), "Data topik tidak ditemukan", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        setupRecyclerView()

        btnTambahSoal.setOnClickListener {
            val inputJumlah = EditText(requireContext()).apply {
                inputType = InputType.TYPE_CLASS_NUMBER
                hint = "Masukkan jumlah soal"
            }

            val sisaSoal = 20 - topik.soalList.size
            if (sisaSoal <= 0) {
                Toast.makeText(requireContext(), "Jumlah maksimal 20 soal per topik telah tercapai", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            AlertDialog.Builder(requireContext())
                .setTitle("Tambah Soal")
                .setMessage("Maksimal sisa soal yang bisa ditambahkan: $sisaSoal")
                .setView(inputJumlah)
                .setPositiveButton("Lanjut") { _, _ ->
                    val jumlah = inputJumlah.text.toString().toIntOrNull()
                    if (jumlah == null || jumlah <= 0) {
                        Toast.makeText(requireContext(), "Jumlah tidak valid", Toast.LENGTH_SHORT).show()
                    } else if (jumlah > sisaSoal) {
                        Toast.makeText(requireContext(), "Jumlah melebihi sisa maksimal ($sisaSoal)", Toast.LENGTH_SHORT).show()
                    } else {
                        val bundle = Bundle().apply {
                            putParcelable("topik", topik)
                            putString("mode", "EDIT")
                            putInt("tambahan", jumlah)
                        }
                        findNavController().navigate(
                            R.id.action_detailEditSoalFragment_to_buatSoalFragment,
                            bundle
                        )
                    }
                }
                .setNegativeButton("Batal", null)
                .show()
        }
    }

    override fun onResume() {
        super.onResume()
        // Ambil ulang topik yang diperbarui dari ViewModel
        topikViewModel.getTopikByNama(topik.namaTopik)?.let { topikBaru ->
            topik = topikBaru
            adapter.updateData(topik.soalList)
        }
    }

    private fun setupRecyclerView() {
        adapter = SoalEditAdapter(
            list = topik.soalList.toMutableList(),
            onItemClick = { soal ->
                Toast.makeText(requireContext(), "Klik soal: ${soal.pertanyaan}", Toast.LENGTH_SHORT).show()
            },
            onItemDelete = { soal ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Hapus Soal")
                    .setMessage("Yakin ingin menghapus soal ini?")
                    .setPositiveButton("Hapus") { _, _ ->
                        topik.soalList.remove(soal)
                        topik.jumlahSoal = topik.soalList.size
                        topikViewModel.updateTopikLama(topik)
                        adapter.updateData(topik.soalList)
                        Toast.makeText(requireContext(), "Soal dihapus", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }
}
