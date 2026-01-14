package com.masenjoandroid.asociacionmayoresvillanueva.data.firebase

import android.content.Context
import com.masenjoandroid.asociacionmayoresvillanueva.domain.model.Activity
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class FirebaseActivitiesDataSource(private val context: Context) {

  suspend fun fetchActivities(): List<Activity> {
    val jsonString = context.assets.open("firestore.json")
      .bufferedReader()
      .use { it.readText() }

    val root = Json.parseToJsonElement(jsonString).jsonObject
    val collections = root["data"]?.jsonObject
      ?.get("__collections__")?.jsonObject ?: return emptyList()
    val activitiesCollection = collections["actividades"]?.jsonObject ?: return emptyList()

    return activitiesCollection.map { (docId, docData) ->
      val obj = docData.jsonObject
      Activity(
        id = docId,
        nombre = obj["nombre"]?.jsonPrimitive?.contentOrNull,
        fecha_hora = obj["fecha_hora"]?.jsonObject?.get("__time__")?.jsonPrimitive?.contentOrNull,
        etiquetas = obj["etiquetas"]?.jsonArray?.mapNotNull { it.jsonPrimitive.contentOrNull },
        imagen = obj["imagen "]?.jsonPrimitive?.contentOrNull,
        ubicacion = obj["ubicación"]?.jsonPrimitive?.contentOrNull
      )
    }
  }
}
