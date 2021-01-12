package tech.gamedev.beauty_scanner.ui.fragments.mainfragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_people.*
import tech.gamedev.beauty_scanner.R
import tech.gamedev.beauty_scanner.adapters.GradeAdapter
import tech.gamedev.beauty_scanner.data.models.UserImage
import tech.gamedev.beauty_scanner.viewmodels.MainViewModel

@AndroidEntryPoint
class GradeFragment : Fragment(R.layout.fragment_people) {

    private lateinit var gradeAdapter: GradeAdapter
    private val mainViewModel: MainViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()

    }

    private fun subscribeToObservers() {
        mainViewModel.userPosts.observe(viewLifecycleOwner) {
            setupViewPager(it)
        }
    }

    private fun setupViewPager(userPosts: ArrayList<UserImage>) = vvRatePeople.apply {
        gradeAdapter = GradeAdapter(userPosts)
        adapter = gradeAdapter
        gradeAdapter.notifyDataSetChanged()

    }

}