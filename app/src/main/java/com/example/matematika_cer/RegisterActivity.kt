package com.example.matematika_cer

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {

    private lateinit var namaLengkap: EditText
    private lateinit var namaPengguna: EditText
    private lateinit var kataSandi: EditText
    private lateinit var kelas: Spinner
    private lateinit var radioGuru: RadioButton
    private lateinit var radioSiswa: RadioButton
    private lateinit var tombolDaftar: Button
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inisialisasi view
        namaLengkap = findViewById<TextInputLayout>(R.id.namalengkapRegis).editText!!
        namaPengguna = findViewById<TextInputLayout>(R.id.namapenggunaRegis).editText!!
        kataSandi = findViewById(R.id.katasandiRegis)
        kelas = findViewById(R.id.KelasRegis)
        radioGuru = findViewById(R.id.RegisGuru)
        radioSiswa = findViewById(R.id.RegisSiswa)
        tombolDaftar = findViewById(R.id.tombolRegis)

        prefs = getSharedPreferences("user_data", MODE_PRIVATE)

        // Tampilkan spinner kelas hanya jika role siswa dipilih
        val radioGroup = findViewById<RadioGroup>(R.id.radiogrupRegis)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.RegisSiswa) {
                kelas.visibility = View.VISIBLE
            } else {
                kelas.visibility = View.GONE
            }
        }

        tombolDaftar.setOnClickListener {
            val nama = namaLengkap.text.toString().trim()
            val username = namaPengguna.text.toString().trim()
            val sandi = kataSandi.text.toString().trim()
            val role = if (radioGuru.isChecked) "guru" else "siswa"
            val kelasDipilih = if (role == "siswa") kelas.selectedItem.toString() else ""

            // Validasi input
            if (nama.isEmpty() || username.isEmpty() || sandi.isEmpty()) {
                Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (role == "siswa" && kelasDipilih.isEmpty()) {
                Toast.makeText(this, "Pilih kelas terlebih dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Simpan ke SharedPreferences
            val editor = prefs.edit()
            editor.putString("${role}_username", username)
            editor.putString("${role}_password", sandi)
            if (role == "siswa") editor.putString("siswa_kelas", kelasDipilih)
            editor.apply()

            Toast.makeText(this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
