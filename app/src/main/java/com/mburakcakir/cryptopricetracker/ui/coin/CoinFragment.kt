package com.mburakcakir.cryptopricetracker.ui.coin

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mburakcakir.cryptopricetracker.R
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

        setToolbar()

        setSwipeRefreshLayout()

        checkInternetConnectionAndFetchData()

        observeCoins()

        setRecyclerView()

    }

    private fun setToolbar() {
        setHasOptionsMenu(true)
    }

    private fun setSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = true
            coinViewModel.getAllCoins()
        }
    }

    private fun checkInternetConnectionAndFetchData() {
        networkController.isNetworkConnected.observe(viewLifecycleOwner) { isInternetConnected ->
            if (isInternetConnected)
                checkStationDataAndSetState()
            else
                binding.state = CoinViewState(Status.ERROR)
        }
    }

    private fun observeCoins() {
        coinViewModel.allCoins.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    coinAdapter.submitList(it.data)
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
            binding.state = CoinViewState(it.status)
        }
    }

    private fun setRecyclerView() {
        binding.rvCoinList.adapter = coinAdapter

        coinAdapter.setCoinOnClickListener {
            this.navigate(CoinFragmentDirections.actionCoinFragmentToCoinDetailFragment(it))
        }
    }

    private fun checkStationDataAndSetState() {
        if (coinViewModel.allCoins.value == null)
            coinViewModel.getAllCoins()
        else {
            binding.state = CoinViewState(Status.SUCCESS)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_coin_list, menu)
        val searchItem = menu.findItem(R.id.action_search).apply {
            expandActionView()
        }
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = "Search a coin"

        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}