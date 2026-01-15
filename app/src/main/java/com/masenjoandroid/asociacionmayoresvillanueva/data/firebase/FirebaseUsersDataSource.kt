package com.masenjoandroid.asociacionmayoresvillanueva.data.firebase

import android.content.Context
import com.masenjoandroid.asociacionmayoresvillanueva.domain.model.User
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class FirebaseUsersDataSource(private val context: Context) {

  suspend fun fetchUsers(): List<User> {
    val jsonString = context.assets.open("firestore.json")
      .bufferedReader()
      .use { it.readText() }

    val root = Json.parseToJsonElement(jsonString).jsonObject
    val collections = root["data"]?.jsonObject
      ?.get("__collections__")?.jsonObject ?: return emptyList()
    val usersCollection = collections["usuarios"]?.jsonObject ?: return emptyList()

    return usersCollection.map { (docId, docData) ->
      val obj = docData.jsonObject
      User(
        id = docId,
        nombre = obj["nombre"]?.jsonPrimitive?.contentOrNull,
        experiencia = obj["experiencia"]?.jsonPrimitive?.intOrNull,
        rol = obj["rol"]?.jsonArray?.mapNotNull { it.jsonPrimitive.contentOrNull },
        email = obj["email"]?.jsonPrimitive?.contentOrNull,
        password = obj["password"]?.jsonPrimitive?.contentOrNull,
        restricciones = obj["restricciones"]?.jsonArray?.mapNotNull {
          it.jsonPrimitive.contentOrNull
        },
        quejas = obj["quejas"]?.jsonArray?.mapNotNull { it.jsonPrimitive.contentOrNull },
        actividades = obj["actividades"]?.jsonArray?.mapNotNull { it.jsonPrimitive.contentOrNull }
      )
    }
  }

  suspend fun fetchUser(userId: String): User? = fetchUsers().find { it.id == userId }
}
