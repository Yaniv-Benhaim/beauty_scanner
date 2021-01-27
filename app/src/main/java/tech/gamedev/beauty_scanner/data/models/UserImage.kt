package tech.gamedev.beauty_scanner.data.models

data class UserImage(
    val user: String = "",
    val imageUrl: String = "",
    val likes: Long = 0,
    val disLikes: Long = 0,
    val uid: String = ""
)
