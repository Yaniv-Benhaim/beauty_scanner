package tech.gamedev.beauty_scanner.ui.fragments.mainfragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_top_rated.*
import tech.gamedev.beauty_scanner.R
import tech.gamedev.beauty_scanner.adapters.GridAdapter
import tech.gamedev.beauty_scanner.data.models.UserImage
import tech.gamedev.beauty_scanner.viewmodels.MainViewModel


class TopRatedFragment : Fragment(R.layout.fragment_top_rated), GridAdapter.OnPostClicked {

    private lateinit var gridAdapter: GridAdapter
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        mainViewModel.userPosts.observe(viewLifecycleOwner) {
            setupGridRecycler(it)
            gridAdapter.notifyDataSetChanged()
        }
    }

    private fun setupGridRecycler(topPosts: ArrayList<UserImage>) = rvGridImages.apply {
        gridAdapter = GridAdapter(topPosts, this@TopRatedFragment)
        val gridLayoutManager =
            GridLayoutManager(requireContext(), 3, LinearLayoutManager.VERTICAL, false)
        layoutManager = gridLayoutManager
        adapter = gridAdapter
    }

    override fun onPostClicked(position: Int) {
        val action = TopRatedFragmentDirections.actionGlobalToPostDetailFragment(position)
        findNavController().navigate(action)
    }
}