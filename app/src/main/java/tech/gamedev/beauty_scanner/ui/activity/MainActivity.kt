package tech.gamedev.beauty_scanner.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.suddenh4x.ratingdialog.AppRating
import com.suddenh4x.ratingdialog.preferences.RatingThreshold
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.gamedev.beauty_scanner.R
import tech.gamedev.beauty_scanner.data.models.UserImage

class MainActivity : AppCompatActivity() {

    private val imageCollectionRef = Firebase.firestore.collection("community_images")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            AppRating.Builder(this)
                .setMinimumLaunchTimes(5)
                .setMinimumDays(2)
                .setMinimumLaunchTimesToShowAgain(3)
                .setMinimumDaysToShowAgain(3)
                .setRatingThreshold(RatingThreshold.FOUR)
                .setIconDrawable(ContextCompat.getDrawable(this,R.drawable.ic_heart_fill))
                .showIfMeetsConditions()
        }



        AppRating.Builder(this)
            .setMinimumLaunchTimes(5)
            .setMinimumDays(2)
            .setMinimumLaunchTimesToShowAgain(3)
            .setMinimumDaysToShowAgain(3)
            .setRatingThreshold(RatingThreshold.FOUR)
            .setIconDrawable(ContextCompat.getDrawable(this,R.drawable.ic_heart_fill))
            .showIfMeetsConditions()
        /*uploadData()*/

        //BOTTOM NAVIGATION SETUP
        bottomNavigationView.setupWithNavController(navHostFragment.findNavController())
        bottomNavigationView.setOnNavigationItemReselectedListener {/* NO-OP */ }

        //BOTTOM NAVIGATION HIDE WHEN NOT NEEDED
        navHostFragment.findNavController()
                .addOnDestinationChangedListener { _, destination, _ ->
                    when (destination.id) {
                        R.id.scanFragment, R.id.peopleFragment, R.id.settingsFragment, R.id.profileFragment, R.id.topRatedFragment ->
                            bottomNavigationView.visibility = View.VISIBLE
                        else -> bottomNavigationView.visibility = View.GONE
                    }
                }

        //END BOTTOM NAVIGATION SETUP
    }

    private fun uploadData() = CoroutineScope(Dispatchers.IO).launch {
        val images = ArrayList<UserImage>()
        images.add(UserImage(
            "dummy",
            "https://firebasestorage.googleapis.com/v0/b/beauty-scanner-67434.appspot.com/o/images%2Fdummy1.jpg?alt=media&token=e1c28bf6-f46e-454e-94ab-ce3c4d179e9f",
            1, 2, 3, 4, 5
        ))
        images.add(UserImage(
            "dummy",
            "https://firebasestorage.googleapis.com/v0/b/beauty-scanner-67434.appspot.com/o/images%2Fdummy1.jpg?alt=media&token=e1c28bf6-f46e-454e-94ab-ce3c4d179e9f",
            1, 2, 3, 4, 5
        ))
        images.add(UserImage(
            "dummy",
            "https://firebasestorage.googleapis.com/v0/b/beauty-scanner-67434.appspot.com/o/images%2Fdummy1.jpg?alt=media&token=e1c28bf6-f46e-454e-94ab-ce3c4d179e9f",
            1, 2, 3, 4, 5
        ))
        images.add(UserImage(
            "dummy",
            "https://firebasestorage.googleapis.com/v0/b/beauty-scanner-67434.appspot.com/o/images%2Fdummy1.jpg?alt=media&token=e1c28bf6-f46e-454e-94ab-ce3c4d179e9f",
            1, 2, 3, 4, 5
        ))
        images.add(UserImage(
            "dummy",
            "https://firebasestorage.googleapis.com/v0/b/beauty-scanner-67434.appspot.com/o/images%2Fdummy1.jpg?alt=media&token=e1c28bf6-f46e-454e-94ab-ce3c4d179e9f",
            1, 2, 3, 4, 5
        ))
        images.add(UserImage(
            "dummy",
            "https://firebasestorage.googleapis.com/v0/b/beauty-scanner-67434.appspot.com/o/images%2Fdummy1.jpg?alt=media&token=e1c28bf6-f46e-454e-94ab-ce3c4d179e9f",
            1, 2, 3, 4, 5
        ))
        for(image in images) {
            try {
                imageCollectionRef.add(image)
            } catch (e: Exception) {
                Log.d("upload", e.message.toString())
            }

        }
    }
}