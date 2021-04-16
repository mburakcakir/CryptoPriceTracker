package com.mburakcakir.cryptopricetracker.ui.detail

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.mburakcakir.cryptopricetracker.R
import com.mburakcakir.cryptopricetracker.data.model.CoinDetailItem
import com.mburakcakir.cryptopricetracker.data.model.CoinMarketItem
import com.mburakcakir.cryptopricetracker.databinding.FragmentCoinDetailBinding
import com.mburakcakir.cryptopricetracker.ui.MainActivity
import com.mburakcakir.cryptopricetracker.utils.Status
import com.mburakcakir.cryptopricetracker.utils.toast
import org.koin.androidx.viewmodel.ext.android.viewModel


class CoinDetailFragment : Fragment() {
    private var _binding: FragmentCoinDetailBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<CoinDetailFragmentArgs>()
    private lateinit var baseCoin: CoinMarketItem
    private var state: Boolean = false
    private val coinDetailViewModel by viewModel<CoinDetailViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCoinDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        baseCoin = args.baseCoin
        observeCoinDetails()

        setToolbar()
    }

    private fun setToolbar() {
        setHasOptionsMenu(true)

        (requireActivity() as MainActivity).supportActionBar?.apply {
            title = "${baseCoin.name} (${baseCoin.symbol})"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_coin_detail, menu)

        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_fav -> {
                state = !state
                item.changeIconColor(state)
                setFavouriteMessage(state)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun observeCoinDetails() {
        coinDetailViewModel.getCoinByID(baseCoin.id)
        coinDetailViewModel.coinInfo.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> setCoinDetails(it.data)
            }
            binding.state = CoinDetailViewState(it.status)
        }
    }

    private fun setCoinDetails(coinDetails: CoinDetailItem?) {
        val lastUpdated = "${baseCoin.last_updated.substring(0, 10)}, ${
            baseCoin.last_updated.substring(
                11,
                19
            )
        }"
        val priceChange24h =
            String.format("%.2f", baseCoin.price_change_24h).replace(",", ".").toDouble()
        binding.coinDetail = coinDetails
        val copiedBaseCoin = baseCoin.copy(
            last_updated = lastUpdated,
            price_change_24h = priceChange24h
        )
        binding.baseCoin = copiedBaseCoin

    }

    // DEPRECATED
    private infix fun MenuItem.changeIconColor(isFavourite: Boolean) {
        val color = if (isFavourite) R.color.yellow else R.color.white
        icon.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                color
            ), PorterDuff.Mode.SRC_IN
        )
//        BlendModeColorFilterCompat.createBlendModeColorFilterCompat(color, BlendModeCompat.SRC_ATOP).apply {
//            icon.colorFilter = this
//        }
    }

    private fun setFavouriteMessage(isFavourite: Boolean) {
        val message = if (isFavourite) "Added" else "Removed"
        requireContext().toast(message)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}