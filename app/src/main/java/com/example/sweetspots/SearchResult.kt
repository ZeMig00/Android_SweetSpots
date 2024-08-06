package com.example.sweetspots

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sweetspots.databinding.SearchResultBinding

class SearchResult : Fragment(), LocalAdapter.OnItemClickListener{
    private var _binding: SearchResultBinding? = null
    private val binding get() = _binding!!
    private lateinit var locaisDistance: MutableList<Place>
    private lateinit var locais: MutableList<Place>
    private lateinit var filterByStar: String
    private lateinit var filterByCategory: String
    private lateinit var filterByHour: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = SearchResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val distance = (activity as MainActivity).distance
        val userLocation = (activity as MainActivity).userLocation
        val allLocais = (activity as MainActivity).locais

        locaisDistance = if (distance > 0) {
            allLocais.filter { p -> p.withinDistance(distance,userLocation) } as MutableList<Place>
        }else allLocais.toMutableList()

        locais = locaisDistance.toMutableList()

        binding.localRecyclerView.adapter = LocalAdapter(locais,this)
        binding.localRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.localRecyclerView.setHasFixedSize(true)
        val results = locais.size.toString()+" results"
        binding.textViewResultNumber.text = results

        val categorias = locaisDistance.map { p -> p.categoria }.distinct().toMutableList()
        categorias.sort()
        categorias.add(0,"Category")
        val adp: ArrayAdapter<String> = ArrayAdapter<String>(activity as MainActivity, android.R.layout.simple_spinner_dropdown_item, categorias)
        binding.spinnerCategory.adapter = adp
        binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, arg3: Long) {
                filterByCategory = parent.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }

        binding.spinnerClassification.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, arg3: Long) {
                filterByStar = parent.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }

        binding.spinnerHorario.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, arg3: Long) {
                filterByHour = parent.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }

        binding.buttonFiltrar.setOnClickListener {
            locais = locaisDistance.toMutableList()
            if (locais.size>0) {
                if(filterByStar != "Stars"){
                    locais = locais.filter { p -> p.stars>= filterByStar.toInt() } as MutableList<Place>
                }
                if(filterByCategory != "Category"){
                    locais = locais.filter { p -> p.categoria == filterByCategory} as MutableList<Place>
                }
                if(filterByHour != "Open at"){
                    locais = locais.filter { p -> p.isOpen(filterByHour) } as MutableList<Place>
                }
            }
            binding.localRecyclerView.adapter = LocalAdapter(locais,this)
            binding.localRecyclerView.layoutManager = LinearLayoutManager(activity)
            binding.localRecyclerView.setHasFixedSize(true)
            val results = locais.size.toString()+" results"
            binding.textViewResultNumber.text = results
        }

        binding.buttonVoltarMenu.setOnClickListener {
            findNavController().navigate(R.id.action_searchResult_to_mainScreen)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(position: Int) {
        findNavController().navigate(SearchResultDirections.actionSearchResultToLocalDetailsScreen(locais[position],"search"))
    }
}