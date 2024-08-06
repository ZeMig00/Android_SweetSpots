package com.example.sweetspots

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sweetspots.databinding.HistoryScreenBinding

class HistoryScreen : Fragment(), LocalAdapter.OnItemClickListener {
    private var _binding: HistoryScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = HistoryScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val historico = (activity as MainActivity).historico
        if(historico.size>0) {
            binding.textViewEmptyHistory.visibility = View.GONE
            binding.localRecyclerView.adapter = LocalAdapter(historico, this)
            binding.localRecyclerView.layoutManager = LinearLayoutManager(activity)
            binding.localRecyclerView.setHasFixedSize(true)
        }
        else binding.textViewEmptyHistory.visibility = View.VISIBLE

        binding.buttonVoltarMenu2.setOnClickListener {
            findNavController().navigate(R.id.action_historyScreen_to_mainScreen)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(position: Int) {
        val historico = (activity as MainActivity).historico
        findNavController().navigate(HistoryScreenDirections.actionHistoryScreenToLocalDetailsScreen(historico[position],"historico"))
    }

}