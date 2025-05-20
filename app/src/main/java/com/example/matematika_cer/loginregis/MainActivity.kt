package com.example.matematika_cer.loginregis

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.matematika_cer.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val role = sharedPref.getString("role", "") ?: ""
        val nama = sharedPref.getString("nama", "") ?: ""
        val kelas = sharedPref.getString("kelas", "")?: ""

        Log.d("MAIN_ACTIVITY", "Role: $role, Nama: $nama")

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Atur graph & startDestination berdasarkan role
        val navGraph = navController.navInflater.inflate(R.navigation.navigasi)
        val startDestinationId = when (role.lowercase()) {
            "guru" -> R.id.berandaGuruFragment
            "siswa" -> R.id.berandaSiswaFragment
            else -> R.id.berandaGuruFragment // fallback
        }
        navGraph.setStartDestination(startDestinationId)

        val bundle = Bundle().apply {
            putString("nama", nama)
        }
        navController.setGraph(navGraph, bundle)

        // Setup Bottom Navigation
        val bottomNav = findViewById<BottomNavigationView>(R.id.navbar)
        bottomNav.menu.clear()

        when (role.lowercase()) {
            "guru" -> {
                bottomNav.inflateMenu(R.menu.menu_navbar)
                Log.d("MAIN_ACTIVITY", "Menu guru dimuat")
            }
            "siswa" -> {
                bottomNav.inflateMenu(R.menu.menu_navbar_siswa)
                Log.d("MAIN_ACTIVITY", "Menu siswa dimuat")
            }
            else -> {
                Log.d("MAIN_ACTIVITY", "Role tidak dikenali, tidak memuat menu")
            }
        }

        // Handle item click dari bottom nav
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.beranda_navbar -> {
                    navController.navigate(R.id.berandaGuruFragment)
                    true
                }
                R.id.daftar_siswa_navbar -> {
                    navController.navigate(R.id.daftarsiswaFragment)
                    true
                }
                R.id.beranda_siswa_navbar -> {
                    navController.navigate(R.id.berandaSiswaFragment)
                    true
                }
                R.id.riwayat_kuis_navbar_siswa -> {
                    navController.navigate(R.id.riwayatkuissiswaFragment)
                    true
                }
                else -> false
            }
        }
    }
}
