package com.mburakcakir.cryptopricetracker.ui.coin

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.mburakcakir.cryptopricetracker.R
import com.mburakcakir.cryptopricetracker.databinding.FragmentCoinBinding
import com.mburakcakir.cryptopricetracker.ui.BaseFragment
import com.mburakcakir.cryptopricetracker.util.NetworkControllerUtils
import com.mburakcakir.cryptopricetracker.util.SharedPreferences
import com.mburakcakir.cryptopricetracker.util.enums.Status
import com.mburakcakir.cryptopricetracker.util.navigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CoinFragment : BaseFragment<FragmentCoinBinding>() {

    private var coinAdapter = CoinAdapter()
    private val coinViewModel: CoinViewModel by viewModels()

    private lateinit var sharedPreferences: SharedPreferences

    private val networkController: NetworkControllerUtils by lazy {
        NetworkControllerUtils(requireContext()).apply {
            startNetworkCallback()
        }
    }

    override fun getFragmentView(): Int = R.layout.fragment_coin

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkIfUserLoggedIn()
    }

    private fun init() {

        setToolbar()

        setSwipeRefreshLayout()

        checkInternetConnectionAndFetchData()

        setRecyclerView()

        observeCoins()

        repeatRequestByRefreshInterval()

    }

    private fun checkIfUserLoggedIn() {
        val sessionState = coinViewModel.checkIfUserLoggedIn()

        if (sessionState)
            init()
        else
            this.navigate(CoinFragmentDirections.actionCoinFragmentToLoginFragment())

    }

    private fun setToolbar() {
        setHasOptionsMenu(true)
    }

    private fun setSwipeRefreshLayout() {
        binding.state = CoinViewState(Status.LOADING)

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = true
            coinViewModel.getAllCoins()

        }
    }

    private fun checkInternetConnectionAndFetchData() {
        networkController.isNetworkConnected.observe(viewLifecycleOwner) { internetConnected ->
            if (internetConnected) checkIsDataFetched()
            else binding.state = CoinViewState(Status.ERROR)
        }
    }

    private fun checkIsDataFetched() {
        if (coinViewModel.allCoins.value?.data == null)
            coinViewModel.getAllCoins()
        else {
            binding.state = CoinViewState(Status.SUCCESS)
        }
    }

    private fun setRecyclerView() {
        binding.rvCoinList.adapter = coinAdapter

        coinAdapter.setCoinOnClickListener {
            this.navigate(CoinFragmentDirections.actionCoinFragmentToCoinDetailFragment(it.cryptoID))
        }

        coinAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                binding.rvCoinList.smoothScrollToPosition(positionStart)
            }
        })
    }

    private fun observeCoins() {
        coinViewModel.allCoins.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    coinAdapter.submitList(it.data)
                    coinViewModel.insertAllCoins(it.data!!)

                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
            binding.state = CoinViewState(it.status)
        }
    }

    private fun repeatRequestByRefreshInterval() {
        sharedPreferences = SharedPreferences(requireContext())
        sharedPreferences.getRefreshInterval()?.let {
            this.lifecycleScope.launch(Dispatchers.IO) {
                while (true) {
                    coinViewModel.getAllCoins()
                    delay(Integer.parseInt(it).toLong() * 1000)
                }
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_coin_list, menu)

        val searchItem = menu.findItem(R.id.action_search).apply {
//            expandActionView()
        }

        val searchView = searchItem?.actionView as SearchView

        searchView.apply {
            queryHint = getString(R.string.coin_search)
            setOnQueryTextListener(onQueryTextListener)
        }

        return super.onCreateOptionsMenu(menu, inflater)
    }

    private val onQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            if (newText.isNullOrEmpty().not()) coinViewModel.getCoinsByParameter(newText!!)
            else coinViewModel.getAllCoins()

            return true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_exit_app -> {
                coinViewModel.endSession()
                this.navigate(CoinFragmentDirections.actionCoinFragmentToLoginFragment())
            }
        }
        return super.onOptionsItemSelected(item)
    }
}