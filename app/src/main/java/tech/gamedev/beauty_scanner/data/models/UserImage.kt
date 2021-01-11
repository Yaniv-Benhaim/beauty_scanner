package tech.gamedev.beauty_scanner.data.models

data class UserImage(
    val user: String = "",
    val imageUrl: String ="",
    val usersRated1: Long = 1L,
    val usersRated2: Long = 1L,
    val usersRated3: Long = 1L,
    val usersRated4: Long = 1L,
    val usersRated5: Long = 1L,

)
