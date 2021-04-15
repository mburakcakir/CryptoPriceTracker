package com.mburakcakir.cryptopricetracker.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.mburakcakir.cryptopricetracker.data.model.CoinDetailItem
import com.mburakcakir.cryptopricetracker.databinding.FragmentCoinDetailBinding
import com.mburakcakir.cryptopricetracker.utils.Status

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
        coinDetailViewModel.getCoinByID(args.baseCoin.id)
        observeCoinDetails()
    }

    private fun observeCoinDetails() {
        coinDetailViewModel.coinInfo.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> setCoinDetails(it.data)
            }
//            binding.state = CoinViewState(it.status)
        }
    }

    private fun setCoinDetails(coinDetails: CoinDetailItem?) {
        Log.v("coinDetail", coinDetails.toString())
        binding.coinDetail = coinDetails
        binding.baseCoin = args.baseCoin
    }
}