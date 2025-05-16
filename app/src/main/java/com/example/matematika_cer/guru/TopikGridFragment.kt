package com.example.matematika_cer.guru

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.matematika_cer.R
import com.example.matematika_cer.model.TopikModel
class TopikGridFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TopikListAdapter
    private lateinit var topikList: MutableList<TopikModel>
    private lateinit var onEditClick: (TopikModel) -> Unit
    private lateinit var onDeleteClick: (TopikModel) -> Unit

    companion object {
        fun newInstance(
            topikList: List<TopikModel>,
            onEditClick: (TopikModel) -> Unit,
            onDeleteClick: (TopikModel) -> Unit
        ): TopikGridFragment {
            val fragment = TopikGridFragment()
            fragment.topikList = topikList.toMutableList()
            fragment.onEditClick = onEditClick
            fragment.onDeleteClick = onDeleteClick
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_topik_grid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewTopikGrid)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)

        adapter = TopikListAdapter(
            list = topikList,
            onEditClick = { topik -> showEditDialog(topik) },
            onDeleteClick = { topik -> showDeleteConfirmation(topik) }
        )
        recyclerView.adapter = adapter
    }

    private fun showEditDialog(topik: TopikModel) {
        val input = EditText(requireContext())
        input.setText(topik.namaTopik)

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Nama Topik")
            .setView(input)
            .setPositiveButton("Simpan") { _, _ ->
                val newName = input.text.toString().trim()
                if (newName.isNotEmpty()) {
                    topik.namaTopik = newName
                    adapter.notifyDataSetChanged()
                    onEditClick(topik)
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showDeleteConfirmation(topik: TopikModel) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Topik")
            .setMessage("Yakin ingin menghapus topik '${topik.namaTopik}'?")
            .setPositiveButton("Hapus") { _, _ ->
                topikList.remove(topik)
                adapter.notifyDataSetChanged()
                onDeleteClick(topik)
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
