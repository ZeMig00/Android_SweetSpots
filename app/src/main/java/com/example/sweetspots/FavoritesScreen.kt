package com.example.sweetspots

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sweetspots.databinding.FavoritesScreenBinding

class FavoritesScreen : Fragment(), LocalAdapter.OnItemClickListener {
    private var _binding: FavoritesScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FavoritesScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val favoritos = (activity as MainActivity).favoritos
        if(favoritos.size>0) {
            binding.textViewEmptyFavorites.visibility = View.GONE
            binding.localRecyclerView.adapter = LocalAdapter(favoritos, this)
            binding.localRecyclerView.layoutManager = LinearLayoutManager(activity)
            binding.localRecyclerView.setHasFixedSize(true)
        }
        else binding.textViewEmptyFavorites.visibility = View.VISIBLE

        binding.buttonVoltarMenu3.setOnClickListener {
            findNavController().navigate(R.id.action_favoritesScreen_to_mainScreen)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(position: Int) {
        val favoritos = (activity as MainActivity).favoritos
        findNavController().navigate(FavoritesScreenDirections.actionFavoritesScreenToLocalDetailsScreen(favoritos[position],"favoritos"))
    }
}