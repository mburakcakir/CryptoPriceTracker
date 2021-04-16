package com.mburakcakir.cryptopricetracker.ui.detail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.mburakcakir.cryptopricetracker.R
import com.mburakcakir.cryptopricetracker.data.model.CoinDetailItem
import com.mburakcakir.cryptopricetracker.databinding.FragmentCoinDetailBinding
import com.mburakcakir.cryptopricetracker.ui.MainActivity
import com.mburakcakir.cryptopricetracker.utils.Status
import com.mburakcakir.cryptopricetracker.utils.toast


class CoinDetailFragment : Fragment() {
    private lateinit var _binding: FragmentCoinDetailBinding
    private val binding get() = _binding
    private val args by navArgs<CoinDetailFragmentArgs>()

    private val coinDetailViewModel: CoinDetailViewModel by lazy {
        ViewModelProvider(this).get(CoinDetailViewModel::class.java)
    }

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

        observeCoinDetails()

        setToolbar()
    }

    private fun setToolbar() {
        setHasOptionsMenu(true)
        (requireActivity() as MainActivity).supportActionBar?.apply {
            title = "${args.baseCoin.name} (${args.baseCoin.symbol})"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_coin_detail, menu)
        val favItem = menu.findItem(R.id.action_fav)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_fav -> requireContext().toast("Added to favourites.")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun observeCoinDetails() {
        coinDetailViewModel.getCoinByID(args.baseCoin.id)
        coinDetailViewModel.coinInfo.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> setCoinDetails(it.data)
            }
        }
    }

    private fun setCoinDetails(coinDetails: CoinDetailItem?) {
        val baseCoin = args.baseCoin
        val lastUpdated = "${baseCoin.last_updated.substring(0, 10)}, ${
            baseCoin.last_updated.substring(
                11,
                19
            )
        }"
//        val priceChange24h = String.format("%.3f", baseCoin.price_change_24h).toDouble()
        binding.coinDetail = coinDetails
        binding.baseCoin = args.baseCoin.copy(
            last_updated = lastUpdated
        )
    }
}