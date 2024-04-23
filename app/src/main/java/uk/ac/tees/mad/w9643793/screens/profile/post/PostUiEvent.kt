package uk.ac.tees.mad.w9643793.screens.profile.post

sealed class PostUiEvent {
    data class OnProfileImageSelected(val profileImage: String) : PostUiEvent()
    data class OnLocationSelected(val location: String) : PostUiEvent()
    data class OnProjectImagesSelected(val projectImages: List<String>) : PostUiEvent()
    data class OnProjectTitleSelected(val projectTitle: String) : PostUiEvent()
    data class OnProjectDescriptionSelected(val projectDescription: String) : PostUiEvent()
    data class OnProjectLinksSelected(val projectLinks: List<String>) : PostUiEvent()
    data class OnProjectDurationSelected(val projectDuration: String) : PostUiEvent()
    data class OnSocialLinksSelected(val socialLinks: List<String>) : PostUiEvent()
}