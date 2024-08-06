package com.example.sweetspots

import android.location.Location
import java.io.Serializable

data class Place(
    val id: String, val nome: String, val endereco: String
    , val categoria: String, val localizacao: Location?
    , val stars: Double, val preco: Int, val horario: String) : Serializable {

    fun isOpen(filterByHour: String): Boolean {
        val tmp = filterByHour.substring(0,2)
        val filterHour: Int = if(tmp[0] != '0') tmp.toInt()
        else tmp.substring(1).toInt()
        val tmp2 = listOf(horario.substring(0,2),horario.substring(6,8))
        val startHour: Int = if(tmp2[0][0] != '0') tmp2[0].toInt()
        else tmp2[0].substring(1).toInt()
        val endHour: Int = if(tmp2[1][0] != '0') tmp2[1].toInt()
        else tmp2[1].substring(1).toInt()

        return (filterHour in startHour..endHour)
    }

    fun withinDistance(distance: Float, userLocation: Location): Boolean {
        return (userLocation.distanceTo(localizacao)/1000) <= distance
    }
}