package com.example.matematika_cer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast

class LoginActivity : AppCompatActivity() {

    private lateinit var inputNama: EditText
    private lateinit var inputPassword: EditText
    private lateinit var spinnerRole: Spinner
    private lateinit var spinnerKelas: Spinner
    private lateinit var btnLogin: Button
    private lateinit var btnRegis: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inisialisasi view
        inputNama = findViewById(R.id.inputname)
        inputPassword = findViewById(R.id.inputPassword)
        spinnerRole = findViewById(R.id.Role)
        spinnerKelas = findViewById(R.id.kelas)
        btnLogin = findViewById(R.id.tombol_login)
        btnRegis = findViewById(R.id.tombol_regis)

        // Tampilkan spinner kelas hanya kalau role = Siswa
        spinnerRole.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val role = spinnerRole.selectedItem.toString()
                spinnerKelas.visibility = if (role.equals("Siswa", ignoreCase = true)) View.VISIBLE else View.GONE
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Login
        btnLogin.setOnClickListener {
            val nama = inputNama.text.toString().trim()
            val password = inputPassword.text.toString().trim()
            val role = spinnerRole.selectedItem.toString().lowercase()
            val kelas = if (role == "siswa") spinnerKelas.selectedItem.toString() else ""

            if (nama.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Nama dan kata sandi harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (role == "siswa" && kelas.isEmpty()) {
                Toast.makeText(this, "Pilih kelas terlebih dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Contoh login dummy (nanti ganti ke SharedPreferences/Database)
            val validLogin = (role == "guru" && nama == "Sali" && password == "123") ||
                    (role == "siswa" && nama == "Sali" && password == "123")

            if (validLogin) {
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("nama", nama)
                    putExtra("role", role)
                    if (role == "siswa") putExtra("kelas", kelas)
                }
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Login gagal. Periksa kembali data kamu.", Toast.LENGTH_SHORT).show()
            }
        }

        // Daftar
        btnRegis.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
