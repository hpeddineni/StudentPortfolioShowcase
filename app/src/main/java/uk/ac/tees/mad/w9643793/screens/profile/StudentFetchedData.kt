package uk.ac.tees.mad.w9643793.screens.profile

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//@Parcelize
data class StudentFetchedData (
    val id: Int = 0,
    val name:String ="",
    val profileImage: String = "",
    val currentCollege: String = "",
    val collegeDuration: String = "",
    val currentProjectWorking: String = "",
    val location: String = "",
    val projectImages: List<String> = listOf(),
    val projectTitle: String = "",
    val projectDescription: String = "",
    val projectDuration: String = "",
    val projectLink: String = "",
    val linkedinLink: String = "",
    )