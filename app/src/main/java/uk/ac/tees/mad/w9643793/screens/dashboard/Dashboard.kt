package uk.ac.tees.mad.w9643793.screens.dashboard

import android.app.Activity
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.app
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import uk.ac.tees.mad.w9643793.NavigationDestination
import uk.ac.tees.mad.w9643793.R
import uk.ac.tees.mad.w9643793.screens.profile.ProfileDestination
import uk.ac.tees.mad.w9643793.screens.profile.StudentFetchedData
import uk.ac.tees.mad.w9643793.screens.profile.post.PostDestination
import uk.ac.tees.mad.w9643793.screens.profile.post.PostViewmodel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Dashboard(
    postViewmodel: PostViewmodel = viewModel(),
    navController: NavController
) {
    val firebase = Firebase.auth.currentUser
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(PostDestination.routeName)
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) {

        Column {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    if (firebase != null) {
                        Text(
                            text = "Welcome ${firebase.displayName}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))

                Image(
                    painter = painterResource(id = R.drawable.download),
                    contentDescription = "Image",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = CircleShape
                        )
                        .clickable {
                            navController.navigate(ProfileDestination.routeName)
                        },
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                LazyColumn(modifier = Modifier.weight(1.0f)) {
                    items(postViewmodel.posts.value) { studentProject ->
                        StudentDetailsCard(studentFetchedData = studentProject) {
                        }
                    }
                }
            }
        }
    }
}

object DashboardDestination : NavigationDestination {
    override val routeName = "dashboard"
}