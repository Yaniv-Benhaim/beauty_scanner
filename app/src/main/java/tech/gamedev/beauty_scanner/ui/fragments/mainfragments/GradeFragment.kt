package tech.gamedev.beauty_scanner.ui.fragments.mainfragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagedList
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_people.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tech.gamedev.beauty_scanner.R
import tech.gamedev.beauty_scanner.adapters.GradeAdapter
import tech.gamedev.beauty_scanner.adapters.PostAdapter
import tech.gamedev.beauty_scanner.data.models.UserImage
import tech.gamedev.beauty_scanner.viewmodels.MainViewModel

@AndroidEntryPoint
class GradeFragment : Fragment(R.layout.fragment_people) {

    private lateinit var gradeAdapter: GradeAdapter
    private val mainViewModel: MainViewModel by activityViewModels()
    lateinit var db: FirebaseFirestore
    lateinit var firestoreAdapter: PostAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = FirebaseFirestore.getInstance()

        subscribeToObservers()
        setFirestoreViewpager()


    }

    private fun subscribeToObservers() {
        mainViewModel.userPosts.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                lifecycleScope.launch {
                    delay(300)
                    withContext(Dispatchers.Main) {

                    }
                }
            } else {

            }
        }
    }

    /*private fun setupViewPager(userPosts: ArrayList<UserImage>) = vvRatePeople.apply {
        gradeAdapter = GradeAdapter(userPosts)
        adapter = gradeAdapter
    }*/

    private fun setFirestoreViewpager() = vvRatePeople.apply {
        val query = db.collection("community_images")
        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(10)
            .setPageSize(4)
            .build()

        val options = FirestorePagingOptions.Builder<UserImage>()
            .setQuery(query, config, UserImage::class.java)
            .setLifecycleOwner(this@GradeFragment).build()

        firestoreAdapter = PostAdapter(options)
        adapter = firestoreAdapter
    }

}