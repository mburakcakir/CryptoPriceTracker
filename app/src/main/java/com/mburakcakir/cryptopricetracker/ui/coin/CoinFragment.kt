package com.mburakcakir.cryptopricetracker.ui.coin

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.mburakcakir.cryptopricetracker.R
import com.mburakcakir.cryptopricetracker.databinding.FragmentCoinBinding
import com.mburakcakir.cryptopricetracker.ui.MainActivity
import com.mburakcakir.cryptopricetracker.util.NetworkControllerUtils
import com.mburakcakir.cryptopricetracker.util.SharedPreferences
import com.mburakcakir.cryptopricetracker.util.enums.Status
import com.mburakcakir.cryptopricetracker.util.navigate
import org.koin.androidx.viewmodel.ext.android.viewModel

class CoinFragment : Fragment() {
    private var _binding: FragmentCoinBinding? = null
    private val binding get() = _binding!!
    private var coinAdapter = CoinAdapter()

    private val coinViewModel by viewModel<CoinViewModel>()
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var sharedPreferences: SharedPreferences

    private val networkController: NetworkControllerUtils by lazy {
        NetworkControllerUtils(requireContext()).apply {
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
        checkIfUserLoggedIn()
    }

    private fun init() {

        setToolbar()

        setSwipeRefreshLayout()

        checkInternetConnectionAndFetchData()

        observeCoins()

        setRecyclerView()

        repeatRequestByRefreshInterval()
    }

    private fun setToolbar() {
        (requireActivity() as MainActivity).changeToolbarVisibility(View.VISIBLE)
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
        networkController.isNetworkConnected.observe(viewLifecycleOwner) { isInternetConnected ->
            if (isInternetConnected) checkCoinData()
            else binding.state = CoinViewState(Status.ERROR)
        }
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

    private fun checkIfUserLoggedIn() {
        firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseAuth.currentUser != null) {
                init()
        } else {
            this.navigate(CoinFragmentDirections.actionCoinFragmentToLoginFragment())
        }

    }

    private fun setRecyclerView() {
        binding.rvCoinList.adapter = coinAdapter

        coinAdapter.setCoinOnClickListener {
            this.navigate(CoinFragmentDirections.actionCoinFragmentToCoinDetailFragment(it))
        }
    }

    private fun checkCoinData() {
        if (coinViewModel.allCoins.value == null)
            coinViewModel.getAllCoins()
        else {
            binding.state = CoinViewState(Status.SUCCESS)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_coin_list, menu)

        val searchItem = menu.findItem(R.id.action_search).apply {
//            expandActionView()
        }

        val searchView = searchItem?.actionView as SearchView

        searchView.apply {
            queryHint = getString(R.string.search_coin)
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

    private fun repeatRequestByRefreshInterval() {
//        sharedPreferences = SharedPreferences(requireContext())
//        sharedPreferences.getRefreshInterval()?.let {
//            this.lifecycleScope.launch(Dispatchers.IO) {
//                while (true) {
//                    coinViewModel.getAllCoins()
//                    delay(Integer.parseInt(it).toLong() * 1000)
//                }
//            }
//
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}