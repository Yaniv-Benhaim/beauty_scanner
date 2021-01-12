package tech.gamedev.beauty_scanner.ui.activity

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.ui.setupWithNavController
import com.suddenh4x.ratingdialog.AppRating
import com.suddenh4x.ratingdialog.preferences.RatingThreshold
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import tech.gamedev.beauty_scanner.R
import tech.gamedev.beauty_scanner.viewmodels.MainViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()


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
                    R.id.scanFragment, R.id.gradeFragment, R.id.settingsFragment, R.id.profileFragment, R.id.topRatedFragment ->
                        bottomNavigationView.visibility = View.VISIBLE
                    else -> bottomNavigationView.visibility = View.GONE
                }
            }

        subsribeToObservers()
    }

    private fun subsribeToObservers() {
        mainViewModel.userPosts.observe(this) {}
    }

    /*private fun uploadData() = CoroutineScope(Dispatchers.IO).launch {
        val images = ArrayList<UserImage>()
        images.add(UserImage(
            "Angelinas_Eyes",
            "https://firebasestorage.googleapis.com/v0/b/beauty-scanner-67434.appspot.com/o/images%2Fdummy7.jpg?alt=media&token=45b7e0d8-16c7-4ad8-bee3-501c597b2c29",
            1, 2, 3, 4, 5
        ))
        images.add(UserImage(
            "CasualSunday",
            "https://firebasestorage.googleapis.com/v0/b/beauty-scanner-67434.appspot.com/o/images%2Fdummy8.jpg?alt=media&token=2d01154f-c0cc-4913-8509-a518f8d892f5",
            1, 2, 3, 4, 5
        ))
        images.add(UserImage(
            "FlowerBoy",
            "https://firebasestorage.googleapis.com/v0/b/beauty-scanner-67434.appspot.com/o/images%2Fdummy9.jpg?alt=media&token=bf1c8f50-549e-4482-b7df-ac7db9873fc0",
            1, 2, 3, 4, 5
        ))
        images.add(UserImage(
            "GingyJulie",
            "https://firebasestorage.googleapis.com/v0/b/beauty-scanner-67434.appspot.com/o/images%2Fdummy10.jpg?alt=media&token=0c28b28c-dab0-4fd2-a106-6f306c1f9a46",
            1, 2, 3, 4, 5
        ))
        images.add(UserImage(
            "V4Vandella",
            "https://firebasestorage.googleapis.com/v0/b/beauty-scanner-67434.appspot.com/o/images%2Fdummy10.jpg?alt=media&token=0c28b28c-dab0-4fd2-a106-6f306c1f9a46",
            1, 2, 3, 4, 5
        ))
        images.add(UserImage(
            "Linda92",
            "https://firebasestorage.googleapis.com/v0/b/beauty-scanner-67434.appspot.com/o/images%2Fdummy12.jpg?alt=media&token=516b1b5f-d979-4a39-88a3-570cecd08351",
            1, 2, 3, 4, 5
        ))

        images.add(UserImage(
                "SmartGirl",
                "https://firebasestorage.googleapis.com/v0/b/beauty-scanner-67434.appspot.com/o/images%2Fdummy13.jpg?alt=media&token=ec2c40a1-8f61-4fd9-8396-8d252f815bc1",
                1, 2, 3, 4, 5
        ))

        images.add(UserImage(
                "PinkModels",
                "https://firebasestorage.googleapis.com/v0/b/beauty-scanner-67434.appspot.com/o/images%2Fdummy14.jpg?alt=media&token=cf7656d6-9969-4b32-8328-904f08d4e681",
                1, 2, 3, 4, 5
        ))

        images.add(UserImage(
                "EranIL",
                "https://firebasestorage.googleapis.com/v0/b/beauty-scanner-67434.appspot.com/o/images%2Fdummy15.jpg?alt=media&token=2d51b704-6cf8-484c-a2b6-8df35ad92326",
                1, 2, 3, 4, 5
        ))

        images.add(UserImage(
                "OniChan",
                "https://firebasestorage.googleapis.com/v0/b/beauty-scanner-67434.appspot.com/o/images%2Fdummy16.jpg?alt=media&token=74308b10-adf9-4efd-8502-77d32225fd7f",
                1, 2, 3, 4, 5
        ))

        images.add(UserImage(
                "BrianIndigo",
                "https://firebasestorage.googleapis.com/v0/b/beauty-scanner-67434.appspot.com/o/images%2Fdummy17.jpg?alt=media&token=a25b9658-29b6-48ef-81b4-8297e9672d0c",
                1, 2, 3, 4, 5
        ))
        for(image in images) {
            try {
                imageCollectionRef.add(image)
            } catch (e: Exception) {
                Log.d("upload", e.message.toString())
            }

        }
    }*/
}