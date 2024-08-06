package com.example.sweetspots

import android.location.Location
import org.json.JSONArray
import org.json.JSONObject

object PlaceDAO {
    fun getPlaces(jsonResult: JSONObject, categorias: HashMap<Int, String>): MutableList<Place> {
        val locais = mutableListOf<Place>()
        val places : JSONArray = jsonResult.getJSONArray("data")
        for (i in 0 until places.length()){
            val place: JSONObject = places.getJSONObject(i)
            val id = place.getString("idLugar")
            val nome = place.getString("Nome")
            val endereco = place.getString("Endereco")
            val latitude = place.getDouble("Latitude")
            val longitude = place.getDouble("Longitude")
            val localizacao = Location(nome)
            localizacao.latitude = latitude
            localizacao.longitude = longitude
            val stars = place.getDouble("Classificacao")
            val preco = place.getInt("LvlPreco")
            val horario = place.getString("Horario")
            val idCategoria = place.getInt("idCategoria")
            val categoria = categorias[idCategoria]
            locais.add(Place(id,nome,endereco,categoria!!,localizacao,stars,preco,horario))
        }
        return locais
    }

    fun getCategorias(jsonResult: JSONObject): HashMap<Int, String> {
        val categorias = HashMap<Int,String>()
        val c : JSONArray = jsonResult.getJSONArray("data")
        for (i in 0 until c.length()){
            val categoria: JSONObject = c.getJSONObject(i)
            val id = categoria.getInt("idCategoria")
            val idCategoria = categoria.getString("Descricao")
            categorias[id] = idCategoria
        }
        return categorias
    }

    fun getHistorico(jsonResult: JSONObject, locais: MutableList<Place>): MutableList<Place> {
        val historico = mutableListOf<Place>()
        val places : JSONArray = jsonResult.getJSONArray("data")
        for (i in 0 until places.length()){
            val place: JSONObject = places.getJSONObject(i)
            val id = place.getString("idLugar")
            val visitado = locais.find { p -> p.id == id }
            if (visitado != null) {
                historico.add(visitado)
            }
        }
        return historico
    }

    fun getFavoritos(jsonResult: JSONObject, locais: MutableList<Place>): MutableList<Place> {
        val favoritos = mutableListOf<Place>()
        val places : JSONArray = jsonResult.getJSONArray("data")
        for (i in 0 until places.length()){
            val place: JSONObject = places.getJSONObject(i)
            val id = place.getString("idLugar")
            val favorito = locais.find { p -> p.id == id }
            if (favorito != null) {
                favoritos.add(favorito)
            }
        }
        return favoritos
    }
}