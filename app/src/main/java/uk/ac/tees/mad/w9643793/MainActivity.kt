package uk.ac.tees.mad.w9643793

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.tees.mad.w9643793.screens.SplashScreen
import uk.ac.tees.mad.w9643793.screens.SplashScreenDestination
import uk.ac.tees.mad.w9643793.screens.authentication.SigninDestination
import uk.ac.tees.mad.w9643793.screens.authentication.SigninScreen
import uk.ac.tees.mad.w9643793.screens.authentication.SignupDestination
import uk.ac.tees.mad.w9643793.screens.authentication.SignupScreen
import uk.ac.tees.mad.w9643793.screens.authentication.authclient.GoogleAuthClient
import uk.ac.tees.mad.w9643793.screens.dashboard.Dashboard
import uk.ac.tees.mad.w9643793.screens.dashboard.DashboardDestination
import uk.ac.tees.mad.w9643793.screens.profile.ProfileDestination
import uk.ac.tees.mad.w9643793.screens.profile.ProfileScreen
import uk.ac.tees.mad.w9643793.screens.profile.post.PostDestination
import uk.ac.tees.mad.w9643793.screens.profile.post.PostScreen
import uk.ac.tees.mad.w9643793.ui.theme.StudentPortfolioShowcaseTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudentPortfolioShowcaseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val scope = rememberCoroutineScope()
                    val firebase = FirebaseAuth.getInstance()
                    val currentUser = firebase.currentUser
                    val context = LocalContext.current

                    val googleAuthUiClient by lazy {
                        GoogleAuthClient(
                            oneTapClient = Identity.getSignInClient(context)
                        )
                    }
                    val start =
                        if ((currentUser != null) || (googleAuthUiClient.getSignedInUser() != null)) {
                            DashboardDestination.routeName
//                            PostDestination.routeName
                        } else {
                            SigninDestination.routeName
                        }

                    NavHost(
                        navController = navController,
                        startDestination = SplashScreenDestination.routeName
                    ) {
                        composable(SplashScreenDestination.routeName) {
                            SplashScreen(
                                onComplete = {
                                    scope.launch(Dispatchers.Main) {
                                        navController.popBackStack()
                                        navController.navigate(start)
                                    }
                                }
                            )
                        }
                        composable(SigninDestination.routeName) {
                            SigninScreen(
                                onSignUp = {
                                    navController.navigate(SignupDestination.routeName)
                                },
                                onLogin = {
                                    Toast.makeText(
                                        applicationContext,
                                        "Sign in successful",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navController.navigate(DashboardDestination.routeName)
                                }
                            )
                        }
                        composable(SignupDestination.routeName) {
                            SignupScreen(
                                onSignup = {
                                    Toast.makeText(
                                        applicationContext,
                                        "Sign up successful",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navController.navigate(DashboardDestination.routeName)
                                },
                                onLogin = {
                                    navController.navigate(SigninDestination.routeName)
                                }
                            )
                        }
                        composable(DashboardDestination.routeName) {
                            Dashboard(
                                navController = navController
                            )
                        }
                        composable(ProfileDestination.routeName) {
                            ProfileScreen(
                                onLogout = {
                                    scope.launch {
                                        firebase.signOut()
                                        googleAuthUiClient.signOut()
                                        navController.navigate(SigninDestination.routeName)
                                    }
                                },
                                navController = navController

                            )

                        }
                        composable(PostDestination.routeName) {
                            PostScreen(
                                navController = navController

                            )
                        }
                    }
                }
            }
        }
    }
}

interface NavigationDestination {
    val routeName: String
}