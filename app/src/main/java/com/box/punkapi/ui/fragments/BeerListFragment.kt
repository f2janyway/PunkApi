package com.box.punkapi.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.box.punkapi.databinding.FragmentBeerListBinding
import com.box.punkapi.model.Beer
import com.box.punkapi.model.Result
import com.box.punkapi.ui.BeerListAdapter
import com.box.punkapi.ui.MainActivity
import com.google.android.material.transition.MaterialContainerTransform

class BeerListFragment : Fragment() {

    private val viewModel by lazy {
        (requireActivity() as MainActivity).mainViewModel
    }
    private val adapter = BeerListAdapter { beer, extras ->
        adapterClick(beer, extras)
    }
    private lateinit var binding: FragmentBeerListBinding

    private var isOnCreated = false
    private var page = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementReturnTransition = MaterialContainerTransform().apply {
            duration = 500
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBeerListBinding.inflate(inflater, container, false)
        setRecyclerView()
        if (!isOnCreated) {
            isOnCreated = true
            viewModel.load(page)
        }
        setObserves()
        setRefreshListener()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setScrollListener()
    }

    private fun setRefreshListener() {
        binding.refreshLayout.apply {
            setOnRefreshListener {
                page = 1
                viewModel.load(page)
                viewModel.resetBeerList()
                isRefreshing = false
            }
        }
    }

    private fun setRecyclerView() {
        binding.listRecyclerview.apply {
            adapter = this@BeerListFragment.adapter
            postponeEnterTransition()
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
        }
    }


    private fun setObserves() {
        viewModel.loadedBeers.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Success -> {
                    adapter.submitList(it.data)
                    adapter.notifyDataSetChanged()
                    page++
                }
                is Result.Error -> {
                    //error handling
                    Toast.makeText(context, "${it.exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun adapterClick(beer: Beer, extras: FragmentNavigator.Extras) {
        viewModel.selectBeer(beer)

        val directions = BeerListFragmentDirections.actionBeerListFragmentToBeerDetailFragment()
        findNavController().navigate(directions, extras)
    }

    private fun setScrollListener() {
        binding.listRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private val recyclerViewManager by lazy {
                binding.listRecyclerview.layoutManager as LinearLayoutManager
            }
            private var beforeSum = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val totalItemCount = recyclerViewManager.itemCount
                val visibleItemCount = recyclerViewManager.childCount
                val lastVisibleItem = recyclerViewManager.findLastVisibleItemPosition()
                val newSum = visibleItemCount + lastVisibleItem + 10
                Log.d("BeerListFragment", "onScrolled:$page <<")
                if (page == 2) beforeSum = 0
                if (newSum >= totalItemCount && beforeSum < newSum) {
                    Log.d("BeerListFragment", "onScrolled:inner <<")
                    viewModel.load(page)
                    beforeSum = newSum
                }
            }
        })
    }
}