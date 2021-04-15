package com.mburakcakir.cryptopricetracker.ui.coin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mburakcakir.cryptopricetracker.databinding.FragmentCoinBinding
import com.mburakcakir.cryptopricetracker.utils.NetworkController
import com.mburakcakir.cryptopricetracker.utils.Status
import com.mburakcakir.cryptopricetracker.utils.navigate

class CoinFragment : Fragment() {
    private var _binding: FragmentCoinBinding? = null
    private val binding get() = _binding!!
    private var coinAdapter = CoinAdapter()

    private val coinViewModel: CoinViewModel by lazy {
        ViewModelProvider(this).get(CoinViewModel::class.java)
    }

    private val networkController: NetworkController by lazy {
        NetworkController(requireContext()).apply {
            startNetworkCallback()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoinBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {

        checkInternetConnection()
        setAdapter()
        observeCoins()



    }

    private fun setAdapter() {
        binding.rvCoinList.adapter = coinAdapter

        coinAdapter.setCoinOnClickListener {
            this.navigate(CoinFragmentDirections.actionCoinFragmentToCoinDetailFragment(it))
        }
    }

    private fun observeCoins() {
        coinViewModel.allCoins.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> coinAdapter.submitList(it.data)
            }
            binding.state = CoinViewState(it.status)
        }
    }

    private fun checkStationDataAndSetState() {
        if (coinViewModel.allCoins.value == null)
            coinViewModel.getAllCoins()
        else {
            binding.state = CoinViewState(Status.SUCCESS)
        }
    }

    private fun checkInternetConnection() {

        networkController.isNetworkConnected.observe(viewLifecycleOwner) { isInternetConnected ->
            if (isInternetConnected)
                checkStationDataAndSetState()
            else
                binding.state = CoinViewState(Status.ERROR)
        }
    }
}