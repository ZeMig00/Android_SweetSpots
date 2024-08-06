package com.example.sweetspots

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.sweetspots.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import org.json.JSONObject

class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    var locais = mutableListOf<Place>()
    var username: String = ""
    var favoritos = mutableListOf<Place>()
    var historico = mutableListOf<Place>()
    private var categorias = HashMap<Int,String>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var userLocation: Location
    var distance: Float = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 99)
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnCompleteListener(this) { location ->
                userLocation = location.result
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val handler = Handler(Looper.getMainLooper())
        handler.post {
            val fetchData = FetchData("http://10.0.2.2/SweetSpots/fetchCategorias.php")
            if (fetchData.startFetch()) {
                if (fetchData.onComplete()) {
                    val result = fetchData.result
                    val jsonResult = JSONObject(result)
                    val success = jsonResult.getInt("success")
                    if (success == 1){
                        categorias = PlaceDAO.getCategorias(jsonResult)
                    }
                    else Toast.makeText(this,"Database is Empty",Toast.LENGTH_SHORT).show()
                }
            }
        }

        handler.post {
            val fetchData = FetchData("http://10.0.2.2/SweetSpots/fetchPlaces.php")
            if (fetchData.startFetch()) {
                if (fetchData.onComplete()) {
                    val result = fetchData.result
                    val jsonResult = JSONObject(result)
                    val success = jsonResult.getInt("success")
                    if (success == 1){
                        locais = PlaceDAO.getPlaces(jsonResult,categorias)
                    }
                    else Toast.makeText(this,"Database is Empty",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}