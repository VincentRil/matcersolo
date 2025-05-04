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

    private lateinit var inputNama: EditText
    private lateinit var inputPassword: EditText
    private lateinit var spinnerKelas: Spinner
    private lateinit var btnLogin: Button
    private lateinit var btnRegis: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        inputNama = findViewById(R.id.inputname)
        inputPassword = findViewById(R.id.inputPassword)
        spinnerKelas = findViewById(R.id.kelas)
        btnLogin = findViewById(R.id.tombol_login)
        btnRegis = findViewById(R.id.tombol_regis)

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
                            val role = userData.role ?: ""
                            val namaLengkap = userData.namaLengkap ?: ""
                            val kelasAsli = userData.kelas?.trim() ?: ""

                            // ✅ Validasi kelas WAJIB cocok untuk semua role
                            if (kelasAsli != selectedKelas) {
                                Toast.makeText(this@LoginActivity, "Kelas yang dipilih tidak sesuai", Toast.LENGTH_SHORT).show()
                                return
                            }

                            // Lanjut login
                            val intent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                                putExtra("nama", namaLengkap)
                                putExtra("role", role)
                                putExtra("kelas", kelasAsli)
                            }

                            Toast.makeText(
                                this@LoginActivity,
                                "Login berhasil sebagai ${role.capitalize()}",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(intent)
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
