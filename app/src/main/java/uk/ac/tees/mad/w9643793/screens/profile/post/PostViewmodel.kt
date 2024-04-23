package uk.ac.tees.mad.w9643793.screens.profile.post

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import uk.ac.tees.mad.w9643793.screens.profile.StudentFetchedData
import java.util.UUID


class PostViewmodel : ViewModel() {

    private val postUiState = mutableStateOf(PostUiState())
    val postInProgress = mutableStateOf(false)
    private val database = FirebaseDatabase.getInstance().reference
    var imageUris = mutableStateOf<List<Uri>>(emptyList())
    val storage = Firebase.storage
    var navigateToProfile: (() -> Unit)? = null

    fun onEvent(event: PostUiEvent) {
        when (event) {
            is PostUiEvent.NameChange -> {
                postUiState.value = postUiState.value.copy(name = event.name)
            }

            is PostUiEvent.ProfileImageChange -> {
                postUiState.value = postUiState.value.copy(profileImage = event.profileImage)
            }

            is PostUiEvent.CollegeChange -> {
                postUiState.value = postUiState.value.copy(currentCollege = event.currentCollege)
            }

            is PostUiEvent.CollegeDurationChange -> {
                postUiState.value = postUiState.value.copy(collegeDuration = event.collegeDuration)
            }

            is PostUiEvent.CurrentProjectChange -> {
                postUiState.value =
                    postUiState.value.copy(currentProjectWorking = event.currentProjectWorking)
            }

            is PostUiEvent.LocationChange -> {
                postUiState.value = postUiState.value.copy(location = event.location)
            }


            is PostUiEvent.ProjectTitleChange -> {
                postUiState.value = postUiState.value.copy(projectTitle = event.projectTitle)
            }

            is PostUiEvent.ProjectDescriptionChange -> {
                postUiState.value =
                    postUiState.value.copy(projectDescription = event.projectDescription)
            }

            is PostUiEvent.ProjectImageChange -> {
                imageUris.value = event.imageUris
            }

            is PostUiEvent.ProjectLinkChange -> {
                postUiState.value = postUiState.value.copy(linkedinLink = event.projectLinks)
            }

            is PostUiEvent.ProjectDurationChange -> {
                postUiState.value = postUiState.value.copy(projectDuration = event.projectDuration)
            }

            is PostUiEvent.SocialLinkChange -> {
                postUiState.value = postUiState.value.copy(projectLink = event.socialLinks)
            }

            is PostUiEvent.PostButtonClicked -> {
                submitProfile()
            }
        }
    }

    private fun submitProfile() {
        viewModelScope.launch {
            postInProgress.value = true

            val name = postUiState.value.name
            val currentCollege = postUiState.value.currentCollege
            val collegeDuration = postUiState.value.collegeDuration
            val currentProjectWorking = postUiState.value.currentProjectWorking
            val location = postUiState.value.location
            val profileImage = postUiState.value.profileImage
            val projectTitle = postUiState.value.projectTitle
            val projectDescription = postUiState.value.projectDescription
            val projectLinks = postUiState.value.projectLink
            val projectDuration = postUiState.value.projectDuration
            val socialLinks = postUiState.value.linkedinLink

            //get unique key for the post
//            val key = database.child("posts").push().key

            //From :P
            val key = database.push().key

            val username = Firebase.auth.currentUser?.displayName

            val imageUrls = mutableListOf<String>()

            //Upload each image to Firebase Storage
            for (uri in imageUris.value) {
                val imageRef = storage.reference.child("projectImages/${UUID.randomUUID()}")

                //uploading
                val uploadTask = imageRef.putFile(uri)

                //get the download url and add it to imageURL list
                try {
                    val downloadUrl = uploadTask.continueWithTask { task ->
                        Log.d(TAG, "Image uploading.......downloading.....?")
                        if (!task.isSuccessful) {
                            task.exception?.let {
                                throw it
                            }
                        }
                        imageRef.downloadUrl
                    }.await()
                    imageUrls.add(downloadUrl.toString())
                    Log.d(TAG, "Image uploaded successfully")
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to upload image: ${e.localizedMessage}")
                }
            }


//            if (username != null) {
            val post = hashMapOf(
                "name" to name,
                "currentCollege" to currentCollege,
                "collegeDuration" to collegeDuration,
                "currentProjectWorking" to currentProjectWorking,
                "location" to location,
                "profileImage" to profileImage,
                "projectTitle" to projectTitle,
                "projectDescription" to projectDescription,
                "projectLinks" to projectLinks,
                "projectDuration" to projectDuration,
                "socialLinks" to socialLinks
            )

            try {
                if (key != null) {
                    withContext(Dispatchers.IO) {
//                        database.child(username!!).child("posts").child(key).setValue(post).await()
//                        database.child(username).child("posts").child(key).child("images").setValue(imageUrls).await()
                        database.child(key).setValue(post).await()
//                        postInProgress.value = false
//                        navigateToProfile?.invoke()
                    }
                    postInProgress.value = false
                    Log.d(TAG, "Data saved successfully...")
                    navigateToProfile?.invoke()
                }
            } catch (e: Exception) {
                Log.d(TAG, "Failed to upload post: ${e.localizedMessage}")
                postInProgress.value = false
            }
        }
    }

    val posts = mutableStateOf<List<StudentFetchedData>>(emptyList())

    init {
        fetchPosts()
    }

    private fun fetchPosts() {
        viewModelScope.launch {
            try {
//                // Get reference to Firebase Database
//                val dbRef = FirebaseDatabase.getInstance().getReference()
//
//                // Get the current user's username
//                val username = Firebase.auth.currentUser?.displayName
//
//                // Check if username is not null
//                if (username != null) {
//                    // Fetch data from Firebase under the specific username and "posts" child
//                    val dataSnapshot = dbRef.child(username).child("posts").get().await()
//
//                    // Convert the data to a list of PostUiState objects
//                    val posts = dataSnapshot.children.mapNotNull { it.getValue(StudentFetchedData::class.java) }
//
//                    // Log the fetched data
//                    posts.forEach { post ->
//                        Log.d(TAG, "Fetched post: $post")
//                    }
//
//                    // Update state variable
//                    this@PostViewmodel.posts.value = posts
//                }

                //Get reference to Firebase Database
                val dbRef = FirebaseDatabase.getInstance().getReference()

                //Fetch data from Firebase
                val dataSnapshot = dbRef.get().await()

                //Convert the data to a list of RoomData objects
                val posts =
                    dataSnapshot.children.mapNotNull { it.getValue(StudentFetchedData::class.java) }

                Log.d(TAG, "Fetching posts....")

                // Log the fetched data
                posts.forEach { post ->
                    Log.d(TAG, "Fetched post: $post")
                }
                //update saate var
                this@PostViewmodel.posts.value = posts
            } catch (e: Exception) {
                Log.e(TAG, "Failed to fetch posts: ${e.localizedMessage}")
            }
        }
    }
}