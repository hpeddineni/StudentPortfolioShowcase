package uk.ac.tees.mad.w9643793.screens.profile.post

data class PostUiState(
    val profileImage: String = "",
    val location: String = "",
    val projectImages: List<String> = listOf(),
    val projectTitle: String = "",
    val projectDescription: String = "",
    val projectLinks: List<String> = listOf(),
    val projectDuration: String = "",
    val socialLinks: List<String> = listOf(),
)