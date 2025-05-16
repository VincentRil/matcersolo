package com.example.matematika_cer.siswa

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.matematika_cer.R
import com.example.matematika_cer.guru.TopikListAdapter
import com.example.matematika_cer.model.TopikModel

class TopikGroupFragment : Fragment() {

    private lateinit var topikList: MutableList<TopikModel>
    private lateinit var onEditClick: (TopikModel) -> Unit
    private lateinit var onDeleteClick: (TopikModel) -> Unit

    companion object {
        fun newInstance(
            list: List<TopikModel>,
            onEditClick: (TopikModel) -> Unit,
            onDeleteClick: (TopikModel) -> Unit
        ): TopikGroupFragment {
            val fragment = TopikGroupFragment()
            fragment.topikList = list.toMutableList()
            fragment.onEditClick = onEditClick
            fragment.onDeleteClick = onDeleteClick
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_topik_group, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_topik_group)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        recyclerView.adapter = TopikListAdapter(
            list = topikList,
            onEditClick = { topik -> showEditDialog(topik) },
            onDeleteClick = { topik -> showDeleteDialog(topik) }
        )

        return view
    }

    private fun showEditDialog(topik: TopikModel) {
        val editText = EditText(requireContext())
        editText.setText(topik.namaTopik)

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Nama Topik")
            .setView(editText)
            .setPositiveButton("Simpan") { _, _ ->
                val newName = editText.text.toString().trim()
                if (newName.isNotEmpty()) {
                    topik.namaTopik = newName
                    view?.findViewById<RecyclerView>(R.id.rv_topik_group)?.adapter?.notifyDataSetChanged()
                    onEditClick(topik)
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showDeleteDialog(topik: TopikModel) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Topik")
            .setMessage("Yakin ingin menghapus topik '${topik.namaTopik}'?")
            .setPositiveButton("Hapus") { _, _ ->
                topikList.remove(topik)
                view?.findViewById<RecyclerView>(R.id.rv_topik_group)?.adapter?.notifyDataSetChanged()
                onDeleteClick(topik)
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
