package uk.ac.tees.mad.w9643793.screens.authentication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.tees.mad.w9643793.data.repository.FirebaseAuthRepository
import uk.ac.tees.mad.w9643793.domain.LoginState
import uk.ac.tees.mad.w9643793.domain.LoginStatus
import uk.ac.tees.mad.w9643793.domain.Resource
import uk.ac.tees.mad.w9643793.domain.SignInResult
import uk.ac.tees.mad.w9643793.domain.UserData
import javax.inject.Inject

@HiltViewModel
class SigninViewModel @Inject constructor(
    private val repository: FirebaseAuthRepository
): ViewModel() {

    private val _loginUiState = MutableStateFlow(SigninUiState())
    val loginUiState = _loginUiState.asStateFlow()

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _signInStatus = Channel<LoginStatus>()
    val signInState = _signInStatus.receiveAsFlow()

    private val _googleSignInResult = MutableStateFlow(SignInResult())
    val googleSignInResult = _googleSignInResult.asStateFlow()

    fun resetState() {
        _state.update { LoginState() }
        _googleSignInResult.update { SignInResult() }
    }

    fun updateLoginState(value: SigninUiState) {
        _loginUiState.value = value
    }

    fun onSignInWithGoogleResult(result: SignInResult) {
        _state.update {
            it.copy(
                isSignInSuccessful = result.data != null, signInError = result.errorMessage
            )
        }
    }

    fun loginUser(email: String, password: String) = viewModelScope.launch {
        repository.loginUser(email, password).collect { result ->
            when (result) {
                is Resource.Error -> {
                    _signInStatus.send(LoginStatus(isError = result.message))
                }

                is Resource.Loading -> {
                    _signInStatus.send(LoginStatus(isLoading = true))
                }

                is Resource.Success -> {
                    _signInStatus.send(LoginStatus(isSuccess = "Sign In Success"))

                }
            }
        }
    }

    fun saveUserInFirestore(user: UserData) = viewModelScope.launch {
        repository.saveUser(email = user.email, username = user.username, userId = user.userId)
    }
}

data class SigninUiState(
    val email: String = "", val password: String = ""
)