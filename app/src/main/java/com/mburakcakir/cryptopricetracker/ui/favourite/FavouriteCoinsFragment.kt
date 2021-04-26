package com.mburakcakir.cryptopricetracker.ui.favourite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.mburakcakir.cryptopricetracker.R
import com.mburakcakir.cryptopricetracker.databinding.FragmentFavouriteCoinsBinding
import com.mburakcakir.cryptopricetracker.ui.BaseFragment
import com.mburakcakir.cryptopricetracker.util.enums.Status
import com.mburakcakir.cryptopricetracker.util.navigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteCoinsFragment : BaseFragment<FragmentFavouriteCoinsBinding>() {

    private var favouriteCoinAdapter = FavouriteCoinsAdapter()
    private val favouriteCoinViewModel: FavouriteCoinsViewModel by viewModels()

    override fun getFragmentView(): Int = R.layout.fragment_favourite_coins

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {

        setRecyclerView()

        observeCoins()
    }

    private fun setRecyclerView() {
        binding.state = FavouriteCoinsViewState(Status.LOADING)

        binding.rvFavouriteCoinList.adapter = favouriteCoinAdapter

        favouriteCoinAdapter.setCoinOnClickListener {
            this.navigate(
                FavouriteCoinsFragmentDirections.actionFavouriteCoinsFragmentToCoinDetailFragment(
                    it
                )
            )
        }
    }


    private fun observeCoins() {
        favouriteCoinViewModel.getAllFavourites()

        favouriteCoinViewModel.favouriteCoins.observe(viewLifecycleOwner) {
            favouriteCoinAdapter.submitList(it)
        }

        favouriteCoinViewModel.coinState.observe(viewLifecycleOwner) {
            val status = if (it) Status.SUCCESS else Status.ERROR
            binding.state = FavouriteCoinsViewState(status)
        }
    }
}