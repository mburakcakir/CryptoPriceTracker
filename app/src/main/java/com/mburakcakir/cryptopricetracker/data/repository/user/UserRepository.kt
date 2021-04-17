package com.mburakcakir.cryptopricetracker.data.repository.user

import android.content.Context
import com.mburakcakir.cryptopricetracker.data.model.UserModel
import com.mburakcakir.cryptopricetracker.util.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun checkIfUserExists(userName: String, password: String): Boolean
    suspend fun insertUser(userModel: UserModel): Flow<Resource<Boolean>>
    fun getUserByUsername(username: String, password: String): Flow<Resource<UserModel>>
    fun startSession(userModel: UserModel, context: Context)
    fun setUserImageUri(uri: String, username: String)
    fun getUserImageUri(username: String): String
}