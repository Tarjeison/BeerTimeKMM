package com.pd.beertimer.feature.me

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.pd.beertimer.R
import com.pd.beertimer.databinding.FragmentMeBinding
import com.pd.beertimer.util.viewBinding
import com.tlapp.beertimemm.models.MePageNavigationType

class MeFragment : Fragment(R.layout.fragment_me) {

    private val binding by viewBinding(FragmentMeBinding::bind)
    private val viewModel = MeViewModel()
    private val onItemClick: (navigationType: MePageNavigationType) -> Unit = { navigationType ->
        val navigationId = when (navigationType) {
            MePageNavigationType.PROFILE -> R.id.action_meFragment_to_myDrinksFragment
            MePageNavigationType.MY_DRINKS -> R.id.action_meFragment_to_profileFragment
        }
        findNavController().navigate(navigationId)
    }

    private val adapter = MeAdapter(
        viewModel.getMeItems().toMutableList(),
        onItemClick
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvMe.layoutManager = GridLayoutManager(context, 2)
        binding.rvMe.adapter = adapter
    }
}
