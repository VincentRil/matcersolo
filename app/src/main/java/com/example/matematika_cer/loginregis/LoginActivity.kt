package com.example.matematika_cer.loginregis

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.matematika_cer.R
import com.example.matematika_cer.model.User
import com.example.matematika_cer.network.ApiClient
import com.example.matematika_cer.network.UserApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val inputNama = findViewById<EditText>(R.id.inputname)
        val inputPassword = findViewById<EditText>(R.id.inputPassword)
        val spinnerKelas = findViewById<Spinner>(R.id.kelas)
        val btnLogin = findViewById<Button>(R.id.tombol_login)
        val btnRegis = findViewById<Button>(R.id.tombol_regis)

        btnLogin.setOnClickListener {
            val username = inputNama.text.toString().trim()
            val password = inputPassword.text.toString().trim()
            val selectedKelas = spinnerKelas.selectedItem.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Nama dan kata sandi harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = User(
                namaLengkap = null,
                username = username,
                password = password,
                role = null,
                kelas = selectedKelas
            )

            val userApi = ApiClient.instance.create(UserApi::class.java)
            userApi.login(user).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val userData = response.body()
                        if (userData != null) {
                            val nama = userData.namaLengkap ?: ""
                            val role = userData.role ?: ""
                            val kelasAsli = userData.kelas?.trim() ?: ""

                            if (kelasAsli != selectedKelas) {
                                Toast.makeText(this@LoginActivity, "Kelas yang dipilih tidak sesuai", Toast.LENGTH_SHORT).show()
                                return
                            }

                            // ✅ Simpan ke SharedPreferences
                            val sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE)
                            sharedPref.edit().apply {
                                putString("nama", nama)
                                putString("role", role)
                                putString("kelas", kelasAsli)
                                apply()
                            }

                            Toast.makeText(this@LoginActivity, "Login berhasil sebagai ${role.capitalize()}", Toast.LENGTH_SHORT).show()

                            // Arahkan ke MainActivity
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "Data kosong dari server", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Username atau password salah", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Gagal koneksi: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        btnRegis.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
