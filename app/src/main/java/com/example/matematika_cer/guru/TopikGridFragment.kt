package com.example.matematika_cer.guru

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.matematika_cer.R
import com.example.matematika_cer.model.TopikModel

class TopikGridFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var topikList: List<TopikModel>
    private var onTopikClick: ((TopikModel) -> Unit)? = null
    private var onTopikLongClick: ((TopikModel) -> Unit)? = null

    companion object {
        fun newInstance(
            topikList: List<TopikModel>,
            onTopikClick: (TopikModel) -> Unit,
            onTopikLongClick: (TopikModel) -> Unit
        ): TopikGridFragment {
            val fragment = TopikGridFragment()
            fragment.topikList = topikList
            fragment.onTopikClick = onTopikClick
            fragment.onTopikLongClick = onTopikLongClick
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

        recyclerView.adapter = TopikListAdapter(
            topikList,
            onItemClick = { topik: TopikModel -> onTopikClick?.invoke(topik) },
            onItemLongClick = { topik: TopikModel -> onTopikLongClick?.invoke(topik) }
        )
    }
}
