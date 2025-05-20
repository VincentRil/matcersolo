package com.example.matematika_cer.guru

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.matematika_cer.R
import com.example.matematika_cer.loginregis.LoginActivity

class BerandaGuruFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_beranda_guru, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val namaGuru = sharedPref.getString("nama", "Guru") ?: "Guru"
        val kelas = sharedPref.getString("kelas", "Guru")?: "Guru"

        val namaTextView = view.findViewById<TextView>(R.id.namaGuruBeranda)
        val kelasTextView = view.findViewById<TextView>(R.id.kelasGuruBeranda)
        namaTextView.text = "Halo $namaGuru !!"
        kelasTextView.text = "$kelas"

        val daftarTopik = view.findViewById<CardView>(R.id.daftar_topik)
        val bankSoal = view.findViewById<CardView>(R.id.bank_soal)
        val pengaturanTopik = view.findViewById<CardView>(R.id.pengaturantopik)
        val logoutBtn = view.findViewById<View>(R.id.logoutbtn)
        val Nilaisiswa = view.findViewById<CardView>(R.id.Nilai_siswa)

        daftarTopik.setOnClickListener {
            findNavController().navigate(R.id.action_beranda_to_daftarTopik)
        }

        bankSoal.setOnClickListener {
            findNavController().navigate(R.id.action_beranda_to_bankSoal)
        }

        pengaturanTopik.setOnClickListener {
            findNavController().navigate(R.id.action_beranda_to_pengaturanTopik)
        }
        Nilaisiswa.setOnClickListener {
            findNavController().navigate(R.id.actionberanda_to_nilaisiswa)
        }

        logoutBtn.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah Anda yakin ingin logout dan kembali ke halaman login?")
                .setPositiveButton("Ya") { dialog, _ ->
                    // Hapus sesi
                    sharedPref.edit().clear().apply()

                    Toast.makeText(requireContext(), "Logout berhasil", Toast.LENGTH_SHORT).show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(requireContext(), LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }, 1000)

                    dialog.dismiss()
                }
                .setNegativeButton("Batal") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}
