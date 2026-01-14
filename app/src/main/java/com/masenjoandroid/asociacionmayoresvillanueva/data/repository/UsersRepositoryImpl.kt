package com.masenjoandroid.asociacionmayoresvillanueva.data.repository

import android.content.Context
import com.masenjoandroid.asociacionmayoresvillanueva.data.firebase.FirebaseUsersDataSource
import com.masenjoandroid.asociacionmayoresvillanueva.domain.model.User

class UsersRepositoryImpl(context: Context) {

  private val dataSource = FirebaseUsersDataSource(context)

  suspend fun getUsers(): List<User> {
    // Solo delega al data source
    return dataSource.fetchUsers()
  }

  suspend fun getUserById(userId: String): User? = dataSource.fetchUser(userId)
}
