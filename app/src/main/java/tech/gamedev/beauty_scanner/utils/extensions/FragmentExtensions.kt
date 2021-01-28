package tech.gamedev.beauty_scanner.utils.extensions

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import tech.gamedev.beauty_scanner.R
import tech.gamedev.beauty_scanner.utils.FirestoreUtil
import kotlin.random.Random

private var backPressed: Int = 0

fun getRating() = Random.nextInt(5, 10)

fun calculateAvarageRating(rating1: Int, rating2: Int, rating3: Int): Int =
    (rating1 + rating2 + rating3) / 3.toInt()

fun Fragment.backToHome() {
    backPressed++
    if (backPressed > 0) {
        this.findNavController().navigate(R.id.actionGlobalToGradeFragment)
    } else {
        createToast("Press 1 more time to cancel post :)")
    }
}

fun Fragment.backToHomeAfterPost() {
    this.findNavController().navigate(R.id.actionGlobalToGradeFragment)
    createToast("Posted to community successfully")

}

fun Fragment.createToast(message: String) {
    Toast.makeText(this.requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Fragment.signOut() {
    FirestoreUtil.signOut()
    this.findNavController().navigate(R.id.actionGlobalToLoginFragment)
    createToast("Sign out successfully")
}