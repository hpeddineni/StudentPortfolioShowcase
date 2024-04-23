package uk.ac.tees.mad.w9643793.screens.profile.post

import android.content.ContentUris
import android.net.Uri

sealed class PostUiEvent {
    data class NameChange(val name: String) : PostUiEvent()
    data class ProfileImageChange(val profileImage: String) : PostUiEvent()
    data class CollegeChange(val currentCollege: String) : PostUiEvent()
    data class CollegeDurationChange(val collegeDuration: String) : PostUiEvent()
    data class CurrentProjectChange(val currentProjectWorking: String) : PostUiEvent()
    data class LocationChange(val location: String) : PostUiEvent()
    data class ProjectImageChange(val imageUris: List<Uri>) : PostUiEvent()
    data class ProjectTitleChange(val projectTitle: String) : PostUiEvent()
    data class ProjectDescriptionChange(val projectDescription: String) : PostUiEvent()
    data class ProjectLinkChange(val projectLinks: String) : PostUiEvent()
    data class ProjectDurationChange(val projectDuration: String) : PostUiEvent()
    data class SocialLinkChange(val socialLinks: String) : PostUiEvent()

    data object PostButtonClicked : PostUiEvent()

}