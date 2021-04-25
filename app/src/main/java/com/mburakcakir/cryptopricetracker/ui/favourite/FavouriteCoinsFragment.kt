package com.mburakcakir.cryptopricetracker.ui.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mburakcakir.cryptopricetracker.util.enums.Status
import com.mburakcakir.cryptopricetracker.util.navigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteCoinsFragment : Fragment() {
    private var _binding: com.mburakcakir.cryptopricetracker.databinding.FragmentFavouriteCoinsBinding? =
        null
    private val binding get() = _binding!!

    private var favouriteCoinAdapter = FavouriteCoinsAdapter()

    private val favouriteCoinViewModel: FavouriteCoinsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            com.mburakcakir.cryptopricetracker.databinding.FragmentFavouriteCoinsBinding.inflate(
                inflater,
                container,
                false
            )
        return binding.root
    }

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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}