package com.example.sweetspots

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.sweetspots.databinding.MainScreenBinding
import com.vishnusivadas.advanced_httpurlconnection.PutData
import org.json.JSONObject

class MainScreen : Fragment() {
    private var _binding: MainScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = MainScreenBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSearch.setOnClickListener {
            val distance = binding.editTextDistance.text.toString()
            if (distance.isNotEmpty()) try {
                val value = distance.toFloat()
                (activity as MainActivity).distance = value
                findNavController().navigate(MainScreenDirections.actionMainScreenToSearchResult(value))
            } catch (e1: Exception) {
                Toast.makeText(activity,"Distance must be a number",Toast.LENGTH_SHORT).show()
            }
            else {
                (activity as MainActivity).distance = 0F
                findNavController().navigate(R.id.action_mainScreen_to_searchResult)
            }
        }

        binding.buttonFavorites.setOnClickListener {
            findNavController().navigate(R.id.action_mainScreen_to_favoritesScreen)
        }

        binding.buttonHistorico.setOnClickListener {
            findNavController().navigate(R.id.action_mainScreen_to_historyScreen)
        }

        binding.buttonLogout.setOnClickListener {
            findNavController().navigate(R.id.action_mainScreen_to_loginScreen)
        }

        val handler = Handler(Looper.getMainLooper())
        handler.post {
            val field = arrayOfNulls<String>(1)
            field[0] = "idUtilizador"
            val data = arrayOfNulls<String>(1)
            data[0] = (activity as MainActivity).username
            val putData = PutData("http://10.0.2.2/SweetSpots/fetchHistory.php", "POST", field, data)
            if (putData.startPut()) {
                if (putData.onComplete()) {
                    val result = putData.result
                    val jsonResult = JSONObject(result)
                    val success = jsonResult.getInt("success")
                    if (success == 1){
                        (activity as MainActivity).historico = PlaceDAO.getHistorico(jsonResult,(activity as MainActivity).locais)
                    }
                    else (activity as MainActivity).historico = mutableListOf()
                }
            }
        }

        handler.post {
            val field = arrayOfNulls<String>(1)
            field[0] = "idUtilizador"
            val data = arrayOfNulls<String>(1)
            data[0] = (activity as MainActivity).username
            val putData = PutData("http://10.0.2.2/SweetSpots/fetchFavorites.php", "POST", field, data)
            if (putData.startPut()) {
                if (putData.onComplete()) {
                    val result = putData.result
                    val jsonResult = JSONObject(result)
                    val success = jsonResult.getInt("success")
                    if (success == 1){
                        (activity as MainActivity).favoritos = PlaceDAO.getFavoritos(jsonResult,(activity as MainActivity).locais)
                    }
                    else (activity as MainActivity).favoritos = mutableListOf()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}