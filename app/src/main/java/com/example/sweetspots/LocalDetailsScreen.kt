package com.example.sweetspots

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sweetspots.databinding.LocalDetailsScreenBinding
import com.vishnusivadas.advanced_httpurlconnection.PutData
import java.text.SimpleDateFormat
import java.util.*

class LocalDetailsScreen : Fragment() {
    private var _binding: LocalDetailsScreenBinding? = null
    private val args: LocalDetailsScreenArgs by navArgs()
    private val binding get() = _binding!!
    private lateinit var stars: String

    override fun onCreateView(inflater:LayoutInflater, container:ViewGroup?, savedInstanceState:Bundle?): View {
        _binding = LocalDetailsScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val place = args.local
        val favoritos = (activity as MainActivity).favoritos
        val historico = (activity as MainActivity).historico
        val userLocation = (activity as MainActivity).userLocation

        binding.localName.text = place?.nome
        if(place?.preco!! > 0) binding.localPrice.text = place.preco.toString()
        else binding.localPrice.text = "N/A"
        binding.localClassification.text = place.stars.toString()
        binding.localLocalization.text = place.endereco
        binding.localHorario.text = place.horario
        val distance = (userLocation.distanceTo(place.localizacao)/1000)
        val textD = String.format("%.1f",distance) + " Km away"
        binding.textViewDistanceAway.text = textD

        if(favoritos.contains(place)){
            binding.buttonAdicionarFavorito.visibility = View.GONE
            binding.buttonRemoverFavorito.visibility = View.VISIBLE
        }else{
            binding.buttonAdicionarFavorito.visibility = View.VISIBLE
            binding.buttonRemoverFavorito.visibility = View.GONE
        }

        if(!historico.contains(place)){
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                val field = arrayOfNulls<String>(2)
                field[0] = "idUtilizador"
                field[1] = "idLugar"
                val data = arrayOfNulls<String>(2)
                data[0] = (activity as MainActivity).username
                data[1] = place.id
                val putData = PutData("http://10.0.2.2/SweetSpots/addVisited.php", "POST", field, data)
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        val result = putData.result
                        if (result == "Added Visited") {
                            historico.add(place)
                        } else {
                            Toast.makeText(activity, result, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        binding.buttonAdicionarFavorito.setOnClickListener {
            binding.buttonAdicionarFavorito.visibility = View.GONE
            binding.buttonRemoverFavorito.visibility = View.VISIBLE
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                val field = arrayOfNulls<String>(2)
                field[0] = "idUtilizador"
                field[1] = "idLugar"
                val data = arrayOfNulls<String>(2)
                data[0] = (activity as MainActivity).username
                data[1] = place.id
                val putData = PutData("http://10.0.2.2/SweetSpots/addFavorite.php", "POST", field, data)
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        val result = putData.result
                        if (result == "Added Favorite") {
                            favoritos.add(place)
                        } else {
                            Toast.makeText(activity, result, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        binding.buttonRemoverFavorito.setOnClickListener {
            binding.buttonAdicionarFavorito.visibility = View.VISIBLE
            binding.buttonRemoverFavorito.visibility = View.GONE
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                val field = arrayOfNulls<String>(2)
                field[0] = "idUtilizador"
                field[1] = "idLugar"
                val data = arrayOfNulls<String>(2)
                data[0] = (activity as MainActivity).username
                data[1] = place.id
                val putData = PutData("http://10.0.2.2/SweetSpots/removeFavorite.php", "POST", field, data)
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        val result = putData.result
                        if (result == "Removed Favorite") {
                            favoritos.remove(place)
                        } else {
                            Toast.makeText(activity, result, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, arg3: Long) {
                stars = parent.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }

        binding.buttonReview.setOnClickListener {
            if (this::stars.isInitialized && stars != "Stars"){
                val handler = Handler(Looper.getMainLooper())
                handler.post {
                    val field = arrayOfNulls<String>(5)
                    field[0] = "Comentario"
                    field[1] = "Data"
                    field[2] = "Classificacao"
                    field[3] = "idUtilizador"
                    field[4] = "idLugar"
                    val data = arrayOfNulls<String>(5)
                    data[0] = binding.editTextComentario.text.toString()
                    data[1] = SimpleDateFormat("yyyy/MM/dd",Locale.getDefault()).format(Calendar.getInstance().time)
                    data[2] = stars
                    data[3] = (activity as MainActivity).username
                    data[4] = args.local?.id
                    val putData = PutData("http://10.0.2.2/SweetSpots/putReview.php", "POST", field, data)
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            val result = putData.result
                            if (result == "Added Review") {
                                Toast.makeText(activity,"Review Added",Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(activity, result, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
            else{
                Toast.makeText(activity,"Stars is required",Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonVoltar.setOnClickListener {
            when(args.modo) {
                "search" -> findNavController().navigate(R.id.action_localDetailsScreen_to_searchResult)
                "favoritos" -> findNavController().navigate(R.id.action_localDetailsScreen_to_favoritesScreen)
                "historico" -> findNavController().navigate(R.id.action_localDetailsScreen_to_historyScreen)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
