package uk.ac.tees.mad.w9643793.data.repository

import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.w9643793.domain.Resource

interface FirebaseAuthRepository {
    fun loginUser(email: String, password: String): Flow<Resource<AuthResult>>
    fun registerUser(email: String, password: String, username: String): Flow<Resource<AuthResult>>
    fun forgotPassword(email: String): Flow<Resource<Boolean>>
    suspend fun saveUser(email: String?, username: String?, userId: String?)
}
