package com.example.matematika_cer.siswa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.matematika_cer.R
import com.example.matematika_cer.guru.TopikListAdapter
import com.example.matematika_cer.model.TopikModel

class TopikGroupFragment : Fragment() {

    private lateinit var topikList: List<TopikModel>
    private var onTopikClick: ((TopikModel) -> Unit)? = null
    private var onTopikLongClick: ((TopikModel) -> Unit)? = null

    companion object {
        fun newInstance(
            list: List<TopikModel>,
            onTopikClick: (TopikModel) -> Unit,
            onTopikLongClick: (TopikModel) -> Unit
        ): TopikGroupFragment {
            val fragment = TopikGroupFragment()
            val args = Bundle()
            args.putParcelableArrayList("topikList", ArrayList(list))
            fragment.arguments = args
            fragment.onTopikClick = onTopikClick
            fragment.onTopikLongClick = onTopikLongClick
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        topikList = arguments?.getParcelableArrayList("topikList") ?: emptyList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_topik_group, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_topik_group)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        recyclerView.adapter = TopikListAdapter(
            topikList,
            onItemClick = { topik -> onTopikClick?.invoke(topik) },
            onItemLongClick = { topik -> onTopikLongClick?.invoke(topik) }
        )

        return view
    }
}
