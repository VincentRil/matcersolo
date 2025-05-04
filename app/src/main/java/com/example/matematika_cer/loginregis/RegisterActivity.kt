package com.example.matematika_cer.loginregis

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.matematika_cer.R
import com.example.matematika_cer.model.User
import com.example.matematika_cer.network.ApiClient
import com.example.matematika_cer.network.UserApi
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var namaLengkap: EditText
    private lateinit var namaPengguna: EditText
    private lateinit var kataSandi: EditText
    private lateinit var kelasSpinner: Spinner
    private lateinit var roleSpinner: Spinner
    private lateinit var kodeAkses: EditText
    private lateinit var kodeLayout: TextInputLayout
    private lateinit var tombolDaftar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        namaLengkap = findViewById<TextInputLayout>(R.id.namalengkapRegis).editText!!
        namaPengguna = findViewById<TextInputLayout>(R.id.namapenggunaRegis).editText!!
        kataSandi = findViewById(R.id.katasandiRegis)
        kelasSpinner = findViewById(R.id.kelas)
        roleSpinner = findViewById(R.id.spinnerRole)
        kodeLayout = findViewById(R.id.inputKodeRegis)
        kodeAkses = findViewById(R.id.kodeRegis)
        tombolDaftar = findViewById(R.id.tombolRegis)

        // Tampilkan input kode akses hanya jika role adalah Guru
        roleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedRole = parent.getItemAtPosition(position).toString()
                kodeLayout.visibility = if (selectedRole == "Guru") View.VISIBLE else View.GONE
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        tombolDaftar.setOnClickListener {
            val nama = namaLengkap.text.toString().trim()
            val username = namaPengguna.text.toString().trim()
            val sandi = kataSandi.text.toString().trim()
            val kelas = kelasSpinner.selectedItem.toString()
            val role = roleSpinner.selectedItem.toString()
            val kode = kodeAkses.text.toString().trim()

            if (nama.isEmpty() || username.isEmpty() || sandi.isEmpty()) {
                Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (role == "Guru") {
                if (kode.isEmpty()) {
                    Toast.makeText(this, "Masukkan kode akses guru", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (kode != "GURU123") {
                    Toast.makeText(this, "Kode akses guru salah!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            val user = User(nama, username, sandi, role.lowercase(), kelas)
            val userApi = ApiClient.instance.create(UserApi::class.java)

            userApi.register(user).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    Log.d("REGISTER", "onResponse: code=${response.code()}, body=${response.body()}")
                    if (response.isSuccessful) {
                        Toast.makeText(this@RegisterActivity, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@RegisterActivity, "Registrasi gagal (server error)", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.e("REGISTER", "onFailure: ${t.message}")
                    Toast.makeText(this@RegisterActivity, "Gagal koneksi: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
