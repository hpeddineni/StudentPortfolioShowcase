package uk.ac.tees.mad.w9643793.domain

data class RegisterStatus(
    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = ""
)