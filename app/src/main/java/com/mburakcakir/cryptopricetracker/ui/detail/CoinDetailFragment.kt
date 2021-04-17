package com.mburakcakir.cryptopricetracker.ui.detail

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.mburakcakir.cryptopricetracker.R
import com.mburakcakir.cryptopricetracker.data.model.CoinDetailItem
import com.mburakcakir.cryptopricetracker.data.model.CoinMarketItem
import com.mburakcakir.cryptopricetracker.databinding.FragmentCoinDetailBinding
import com.mburakcakir.cryptopricetracker.ui.MainActivity
import com.mburakcakir.cryptopricetracker.util.*
import com.mburakcakir.cryptopricetracker.util.enums.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class CoinDetailFragment : Fragment() {
    private var _binding: FragmentCoinDetailBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<CoinDetailFragmentArgs>()
    private lateinit var baseCoin: CoinMarketItem

    private var state: Boolean = false
    private val coinDetailViewModel by viewModel<CoinDetailViewModel>()

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCoinDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {

        setData()

        observeCoinDetails()

        setToolbar()
    }

    private fun setData() {
        baseCoin = args.baseCoin
        binding.state = CoinDetailViewState(Status.LOADING)


        sharedPreferences = SharedPreferences(requireContext())
        sharedPreferences.getRefreshInterval()?.let {
            binding.edtInterval.setText(sharedPreferences.getRefreshInterval())
        }

        binding.edtInterval.setText(sharedPreferences.getRefreshInterval())
        binding.btnApprove.setOnClickListener {
            repeatRequestByRefreshInterval(Integer.parseInt(binding.edtInterval.text.toString()))
        }
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
                requireContext().toast(setFavouriteMessage(state))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun observeCoinDetails() {
        coinDetailViewModel.getCoinByID(baseCoin.cryptoID)

        coinDetailViewModel.coinInfo.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    setCoinDetails(it.data!!)
                    Log.v("dataRequest", it.data.toString())
                }
            }
            binding.state = CoinDetailViewState(it.status)
        }
    }

    private fun setCoinDetails(coinDetails: CoinDetailItem) {
        val lastUpdated = coinDetails.lastUpdated.formatUpdatedTime()
        val priceChange24h = coinDetails.marketData.priceChange24h.formatPriceChange()
        val priceChangePercentage24h =
            coinDetails.marketData.priceChangePercentage24h.formatPriceChange()

        val marketData = coinDetails.marketData.copy(
            priceChange24h = priceChange24h,
            priceChangePercentage24h = priceChangePercentage24h
        )

        val copiedDetail = coinDetails.copy(
            lastUpdated = lastUpdated,
            marketData = marketData
        )

        binding.coinDetail = copiedDetail
        binding.baseCoin = baseCoin

        Glide.with(requireContext()).load(baseCoin.cryptoImage).into(binding.imgIconImage)
    }

    private infix fun MenuItem.changeIconColor(isFavourite: Boolean) {
        val color = if (isFavourite) R.color.yellow else R.color.white
        icon.colorFilter = PorterDuffColorFilter(
            ContextCompat.getColor(requireContext(), color),
            PorterDuff.Mode.SRC_IN
        )
    }

    private fun repeatRequestByRefreshInterval(refreshInterval: Int) {
        this.lifecycleScope.launch(Dispatchers.IO) {
            while (true) {
                coinDetailViewModel.getCoinByID(baseCoin.cryptoID)
                sharedPreferences.saveRefreshInterval(refreshInterval)
                delay(refreshInterval.toLong() * 1000)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}