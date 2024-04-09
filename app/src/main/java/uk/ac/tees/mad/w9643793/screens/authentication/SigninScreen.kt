package uk.ac.tees.mad.w9643793.screens.authentication

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch
import uk.ac.tees.mad.w9643793.NavigationDestination
import uk.ac.tees.mad.w9643793.R
import uk.ac.tees.mad.w9643793.screens.authentication.authclient.GoogleAuthClient
import uk.ac.tees.mad.w9643793.screens.authentication.viewmodels.SigninViewModel

object SigninDestination : NavigationDestination {
    override val routeName: String
        get() = "signin"

}

@Composable
fun SigninScreen(
    signInViewModel: SigninViewModel = hiltViewModel(),
    onSignUp: () -> Unit,
    onLogin: () -> Unit
) {
    val signInStatusState = signInViewModel.signInState.collectAsState(initial = null)
    val signInState = signInViewModel.state.collectAsState().value
    val loginUiState = signInViewModel.loginUiState.collectAsState().value
    val focusManager = LocalFocusManager.current
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val googleAuthUiClient by lazy {
        GoogleAuthClient(
            oneTapClient = Identity.getSignInClient(context)
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                scope.launch {
                    val signInResult = googleAuthUiClient.signInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    signInViewModel.onSignInWithGoogleResult(signInResult)
                }
            }
        }
    )

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(90.dp))
            Text(
                text = "Welcome",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Glad to see you!",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = 1.5.sp
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = loginUiState.email,
                onValueChange = {
                    signInViewModel.updateLoginState(loginUiState.copy(email = it))
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = "Email address", color = Color.White)
                },
                maxLines = 1,
                visualTransformation = VisualTransformation.None,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.White,
                    focusedBorderColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    unfocusedContainerColor = Color.White.copy(alpha = 0.2f)
                )
            )
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = loginUiState.password,
                onValueChange = {
                    signInViewModel.updateLoginState(loginUiState.copy(password = it))
                },

                modifier = Modifier.fillMaxWidth(),

                maxLines = 1,
                trailingIcon = {
                    val image = if (isPasswordVisible)
                        Icons.Default.Visibility
                    else Icons.Filled.VisibilityOff

                    val description =
                        if (isPasswordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = image,
                            description,
                        )
                    }
                },
                label = {
                    Text(text = "Password", color = Color.White)
                },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.White,
                    focusedBorderColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    unfocusedContainerColor = Color.White.copy(alpha = 0.2f)
                )
            )

            Spacer(modifier = Modifier.height(50.dp))

            Button(
                onClick = { signInViewModel.loginUser(loginUiState.email, loginUiState.password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (signInStatusState.value?.isLoading == true) {
                    CircularProgressIndicator(color = Color.Black)
                } else {
                    Text(text = "Login", fontSize = 18.sp, color = Color.Black)
                }
            }
            Spacer(modifier = Modifier.height(50.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(Color.White)
                )
                Text(
                    text = "Or Login with",
                    color = Color.White,
                    modifier = Modifier.padding(18.dp)
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(Color.White)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    scope.launch {
                        val signInIntentSender = googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest
                                .Builder(
                                    signInIntentSender ?: return@launch
                                )
                                .build()
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "login with google",
                    modifier = Modifier.size(28.dp)
                )
            }
            Box(modifier = Modifier.weight(1f))

            Text(
                text = "Don't have an account? Sign Up Now",
                textDecoration = TextDecoration.Underline,
                color = Color.White,
                modifier = Modifier.clickable {
                    onSignUp()
                }
            )

            LaunchedEffect(key1 = signInState.isSignInSuccessful) {
                if (signInState.isSignInSuccessful) {
                    Toast.makeText(
                        context,
                        "Sign in successful",
                        Toast.LENGTH_LONG
                    ).show()
                    val user = googleAuthUiClient.getSignedInUser()
                    if (user != null) {
                        signInViewModel.saveUserInFirestore(user)
                    }
                    signInViewModel.resetState()
                    onLogin()
                }
            }
            LaunchedEffect(key1 = signInState.signInError) {
                signInState.signInError?.let {
                    Toast.makeText(
                        context,
                        it,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            LaunchedEffect(key1 = signInStatusState.value?.isSuccess) {
                scope.launch {
                    if (signInStatusState.value?.isSuccess?.isNotEmpty() == true) {
                        focusManager.clearFocus()
                        val success = signInStatusState.value?.isSuccess
                        Toast.makeText(context, "$success", Toast.LENGTH_LONG).show()
                        onLogin()
                    }
                }
            }

            LaunchedEffect(key1 = signInStatusState.value?.isError) {
                scope.launch {
                    if (signInStatusState.value?.isError?.isNotEmpty() == true) {
                        val error = signInStatusState.value?.isError
                        Toast.makeText(context, "$error", Toast.LENGTH_LONG).show()
                    }
                }
            }
            LaunchedEffect(key1 = signInState.signInError) {
                scope.launch {
                    if (signInState.signInError?.isNotEmpty() == true) {
                        val error = signInState.signInError
                        Toast.makeText(context, "$error", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
