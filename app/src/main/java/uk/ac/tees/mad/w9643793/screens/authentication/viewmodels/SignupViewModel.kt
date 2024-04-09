package uk.ac.tees.mad.w9643793.screens.authentication.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.tees.mad.w9643793.data.repository.FirebaseAuthRepository
import uk.ac.tees.mad.w9643793.domain.LoginState
import uk.ac.tees.mad.w9643793.domain.RegisterStatus
import uk.ac.tees.mad.w9643793.domain.Resource
import uk.ac.tees.mad.w9643793.domain.SignInResult
import uk.ac.tees.mad.w9643793.domain.UserData
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val repository: FirebaseAuthRepository,
    firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _signupUiState = MutableStateFlow(SignUpUiState())
    val signUpUiState = _signupUiState.asStateFlow()


    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun resetState() {
        _state.update { LoginState() }
        _googleSignInResult.update { SignInResult() }
    }

    private val _googleSignInResult = MutableStateFlow(SignInResult())
    val googleSignInResult = _googleSignInResult.asStateFlow()

    private val _signUpState = Channel<RegisterStatus>()
    val signUpState = _signUpState.receiveAsFlow()

    fun saveUserInFirestore(user: UserData) = viewModelScope.launch {
        repository.saveUser(email = user.email, username = user.username, userId = user.userId)
    }

    var username = mutableStateOf("Guest")
    private var myDatabase = Firebase.firestore
    private val uid = firebaseAuth.currentUser?.uid

    init {
        getUserDetails()
    }

    fun onSignInWithGoogleResult(result: SignInResult) {
        _state.update {
            it.copy(
                isSignInSuccessful = result.data != null, signInError = result.errorMessage
            )
        }
    }

    private fun getUserDetails() {
        viewModelScope.launch {
            if (uid != null) {
                myDatabase.collection("users").document(uid).get()
                    .addOnSuccessListener { mySnapshot ->
                        Log.d("USER", "$mySnapshot")

                        if (mySnapshot.exists()) {
                            val list = mySnapshot.data
                            if (list != null) {
                                for (items in list) {
                                    if (items.key == "username") {
                                        username.value = items.value.toString()
                                    }
                                }
                            }
                        } else {
                            println("No data found in Database")
                        }
                    }.addOnFailureListener {
                        println("$it")
                    }
            }

        }
    }

    fun updateSignUpState(value: SignUpUiState) {
        _signupUiState.value = value
    }

    fun registerUser(email: String, password: String, username: String) = viewModelScope.launch {
        repository.registerUser(email, password, username).collect { result ->
            when (result) {
                is Resource.Error -> {
                    _signUpState.send(RegisterStatus(isError = result.message))
                }

                is Resource.Loading -> {
                    _signUpState.send(RegisterStatus(isLoading = true))
                }

                is Resource.Success -> {
                    _signUpState.send(RegisterStatus(isSuccess = "Register Success"))
                }
            }
        }
    }
}


data class SignUpUiState(
    val name: String = "",
    val email: String = "",
    val password: String = ""
)