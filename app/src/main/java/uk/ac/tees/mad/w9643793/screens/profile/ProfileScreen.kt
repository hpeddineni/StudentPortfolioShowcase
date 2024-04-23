package uk.ac.tees.mad.w9643793.screens.profile

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import uk.ac.tees.mad.w9643793.NavigationDestination
import uk.ac.tees.mad.w9643793.R
import uk.ac.tees.mad.w9643793.screens.dashboard.DashboardDestination
import uk.ac.tees.mad.w9643793.screens.profile.post.PostDestination

@Composable
fun ProfileScreen(
    studentFetchedData: StudentFetchedData = StudentFetchedData(),
    navController: NavController,
    onLogout: () -> Unit
) {

    val context = LocalContext.current
    val fireabse = Firebase.auth.currentUser

    var profileImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val bitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        profileImageUri = uri
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    navController.navigate(DashboardDestination.routeName)
                }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = ""
                    )
                }
                Text(
                    text = "Profile",
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = {
                    navController.navigate(PostDestination.routeName)
                }) {
                    Icon(Icons.Filled.Edit, contentDescription = "Update Profile")
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                //User can add and change, profile picture of own
                profileImageUri?.let {
                    if (Build.VERSION.SDK_INT < 28) {
                        bitmap.value =
                            MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                    } else {
                        val source = ImageDecoder.createSource(context.contentResolver, it)
                        bitmap.value = ImageDecoder.decodeBitmap(source)
                    }

                    bitmap.value?.let { btm ->
                        Image(
                            bitmap = btm.asImageBitmap(),
                            contentDescription = "Image",
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape)
                                .clickable {
                                    launcher.launch("image/*")
                                },
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                if (profileImageUri == null) {
                    Image(
//                    painter = rememberImagePainter(data = R.drawable.profile_pic),
                        painter = painterResource(id = R.drawable.download),
                        contentDescription = "Image",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.onSurface,
                                shape = CircleShape
                            )
                            .clickable {
                                launcher.launch("image/*")
                            },
                        contentScale = ContentScale.Crop

                    )
                }

                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    if (fireabse != null) {
                        fireabse.displayName?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                ProfileInfoCard(
                    icon = Icons.Filled.Person,
                    text = "Edit Profile"
                )
                Spacer(modifier = Modifier.height(30.dp))

                ProfileInfoCard(
                    icon = Icons.Filled.PostAdd,
                    text = "Posts",
                    onClick = {
                        //TODO - Add navigation to Post screen
                        navController.navigate(PostDestination.routeName)
                    }
                )
                Spacer(modifier = Modifier.height(30.dp))

                ProfileInfoCard(
                    icon = Icons.Filled.Language,
                    text = "Language"
                )
                Spacer(modifier = Modifier.height(30.dp))

                ProfileInfoCard(
                    icon = Icons.Filled.DarkMode,
                    text = "Switch Theme"
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
//                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.colorText))
            ) {
                Text(text = "Sign Out")
            }
        }
    }
}

object ProfileDestination : NavigationDestination {
    override val routeName = "profile"
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(onLogout = {}, navController = rememberNavController())
}