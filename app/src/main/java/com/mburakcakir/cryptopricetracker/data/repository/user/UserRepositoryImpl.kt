package com.mburakcakir.cryptopricetracker.data.repository.user

class UserRepositoryImpl {

//    override fun getUserByUsername(username: String, password: String) = flow {
//        emit(Resource.Loading())
//        try {
//            if (checkIfUserExists(username, password))
//                emit(Resource.Success(userDao.getUserByUsername(username, password)))
//            else
//                emit(Resource.Success(null))
//        } catch (e: Exception) {
//            emit(Resource.Error(e))
//            e.printStackTrace()
//        }
//    }
//
//    override fun startSession(userModel: UserModel, context: Context) {
//        TODO("Not yet implemented")
//    }
//
//    override fun setUserImageUri(uri: String, username: String) {
//        userDao.setUserImageUri(uri, username)
//    }
//
//    override fun getUserImageUri(username: String): String = userDao.getUserImageUri(username)
//
//
//    override fun checkIfUserExists(userName: String, password: String) =
//        userDao.checkIfUserExists(userName, password) != 0
//
//    override suspend fun insertUser(userModel: UserModel) = flow {
//        emit(Resource.Loading())
//        try {
//            if (checkIfUserExists(userModel.userName, userModel.password))
//                emit(Resource.Success(false))
//            else {
//                emit(Resource.Success(true))
//                userDao.insertUser(userModel)
//            }
//
//        } catch (e: Exception) {
//            emit(Resource.Error(e))
//            e.printStackTrace()
//        }
//    }

}