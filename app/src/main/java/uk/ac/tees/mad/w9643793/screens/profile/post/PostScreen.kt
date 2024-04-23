package uk.ac.tees.mad.w9643793.screens.profile.post

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import uk.ac.tees.mad.w9643793.MainActivity
import uk.ac.tees.mad.w9643793.NavigationDestination
import uk.ac.tees.mad.w9643793.R
import uk.ac.tees.mad.w9643793.screens.dashboard.DashboardDestination
import uk.ac.tees.mad.w9643793.screens.dashboard.MyTextFieldComponent
import uk.ac.tees.mad.w9643793.screens.dashboard.MyTextFieldComponent2Painter
import uk.ac.tees.mad.w9643793.screens.profile.ProfileDestination
import uk.ac.tees.mad.w9643793.ui.theme.StudentPortfolioShowcaseTheme

@Composable
fun PostScreen(
    postViewModel: PostViewmodel = viewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val firebase = Firebase.auth.currentUser

    LaunchedEffect(key1 = true) {
        postViewModel.navigateToProfile = {
            navController.navigate(ProfileDestination.routeName)
        }
    }

    /**
    Multiple photo picker
     **/

    var imageUris by remember {
        mutableStateOf<List<Uri>>(emptyList())
    }

    val multiplePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris ->
            if (uris.size > 3) {
                Toast.makeText(context, "You can only select 3 images", Toast.LENGTH_SHORT).show()
                return@rememberLauncherForActivityResult
            } else {
                imageUris = uris
                postViewModel.onEvent(PostUiEvent.ProjectImageChange(uris))
            }
        }
    )

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
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
                text = "Update Profile",
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {


            MyTextFieldComponent(
                labelValue = "Name",
                onTextChanged = {
                    postViewModel.onEvent(PostUiEvent.NameChange(it))
                }
            )

            MyTextFieldComponent(
                labelValue = "Location",
                onTextChanged = {
                    postViewModel.onEvent(PostUiEvent.LocationChange(it))
                }
            )

            MyTextFieldComponent(
                labelValue = "College Name",
                onTextChanged = {
                    postViewModel.onEvent(PostUiEvent.CollegeChange(it))
                }
            )

            MyTextFieldComponent(
                labelValue = "College Duration (YYYY-YYYY)",
                onTextChanged = {
                    postViewModel.onEvent(PostUiEvent.CollegeDurationChange(it))
                }
            )

            MyTextFieldComponent(
                labelValue = "Current Project Title",
                onTextChanged = {
                    postViewModel.onEvent(PostUiEvent.CurrentProjectChange(it))
                }
            )

            Button(onClick = {
                multiplePhotoPicker.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }) {
                Text(text = "Upload Project Images")
            }


            //Uploading image

            LazyRow {
                items(imageUris) { uri ->
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(150.dp)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(24.dp))
                    )
                }
            }

            MyTextFieldComponent(
                labelValue = "Past Project Title",
                onTextChanged = {
                    postViewModel.onEvent(PostUiEvent.ProjectTitleChange(it))
                }
            )

            MyTextFieldComponent(
                labelValue = "Project Description",
                onTextChanged = {
                    postViewModel.onEvent(PostUiEvent.ProjectDescriptionChange(it))
                }
            )

            MyTextFieldComponent(
                labelValue = "Project Duration (MM-MM)",
                onTextChanged = {
                    postViewModel.onEvent(PostUiEvent.ProjectDurationChange(it))
                }
            )

            MyTextFieldComponent2Painter(
                labelValue = "Project Github Links",
                onTextChanged = {
                    postViewModel.onEvent(PostUiEvent.ProjectLinkChange(it))
                },
                painterResource = painterResource(id = R.drawable.github)
            )

            MyTextFieldComponent2Painter(
                labelValue = "Linkedin Links",
                onTextChanged = {
                    postViewModel.onEvent(PostUiEvent.SocialLinkChange(it))
                },
                painterResource = painterResource(id = R.drawable.icons8_linkedin)
            )

            Button(
                onClick = { postViewModel.onEvent(PostUiEvent.PostButtonClicked) }
            ) {
                Text(text = "Submit")
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .size(400.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (postViewModel.postInProgress.value) {
                CircularProgressIndicator()
            }
        }
    }
}

val PostDestination = object : NavigationDestination {
    override val routeName = "post"
}

@Preview(showSystemUi = true)
@Composable
fun PostScreenPreview() {
    StudentPortfolioShowcaseTheme {
        PostScreen(
            navController = rememberNavController()
        )
    }
}