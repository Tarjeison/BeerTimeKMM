package com.pd.beertimer.feature.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pd.beertimer.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class InfoFragment : Fragment() {

    private val infoViewModel: InfoViewModel by viewModel()
    private lateinit var infoAdapter: InfoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        infoAdapter = InfoAdapter(infoViewModel.getInfoDtos())
        view.findViewById<RecyclerView>(R.id.rvInfo).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = infoAdapter
        }
        infoAdapter.notifyDataSetChanged()
    }
}
