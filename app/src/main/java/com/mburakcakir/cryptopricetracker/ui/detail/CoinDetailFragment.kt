package com.mburakcakir.cryptopricetracker.ui.detail

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.mburakcakir.cryptopricetracker.R
import com.mburakcakir.cryptopricetracker.data.model.CoinDetailItem
import com.mburakcakir.cryptopricetracker.databinding.FragmentCoinDetailBinding
import com.mburakcakir.cryptopricetracker.ui.MainActivity
import com.mburakcakir.cryptopricetracker.util.SharedPreferences
import com.mburakcakir.cryptopricetracker.util.enums.Status
import com.mburakcakir.cryptopricetracker.util.setCoinDetail
import com.mburakcakir.cryptopricetracker.util.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class CoinDetailFragment : Fragment() {
    private var _binding: FragmentCoinDetailBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<CoinDetailFragmentArgs>()
    private lateinit var coinID: String

    private val coinDetailViewModel by viewModel<CoinDetailViewModel>()
    private var isFavourite: Boolean = false
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var menuItem: MenuItem

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

        observeData()

        setToolbar()
    }

    private fun setData() {
        coinID = args.coinID
        binding.state = CoinDetailViewState(Status.LOADING)
        sharedPreferences = SharedPreferences(requireContext())
        sharedPreferences.getRefreshInterval()?.let {
            binding.edtInterval.setText(sharedPreferences.getRefreshInterval())
        }
        binding.edtInterval.setText(sharedPreferences.getRefreshInterval())
        coinDetailViewModel.isFavourite(coinID)
        coinDetailViewModel.getAllFavourites()

        binding.btnApprove.setOnClickListener {
            repeatRequestByRefreshInterval(Integer.parseInt(binding.edtInterval.text.toString()))
        }
    }

    private fun setToolbar() {
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_coin_detail, menu)
        menuItem = menu.findItem(R.id.action_fav)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_fav -> {
                if (isFavourite)
                    coinDetailViewModel.deleteFavourite(coinID)
                else
                    coinDetailViewModel.addToFavourites(coinDetailViewModel.coinInfo.value?.data!!)

                isFavourite = !isFavourite
                item.changeIconColor(isFavourite)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun observeData() {
        coinDetailViewModel.getCoinByID(coinID)
        coinDetailViewModel.coinInfo.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> setCoinDetails(it.data!!)
            }
            binding.state = CoinDetailViewState(it.status)
        }

        coinDetailViewModel.isFavouriteAdded.observe(viewLifecycleOwner) {
            if (it)
                requireContext() toast getString(R.string.favourite_add_success)
        }

        coinDetailViewModel.isFavouriteDeleted.observe(viewLifecycleOwner) {
            if (it)
                requireContext() toast getString(R.string.favourite_delete_success)
        }

        coinDetailViewModel.isFavourite.observe(viewLifecycleOwner) {
            if (it) {
                isFavourite = it
                menuItem.changeIconColor(isFavourite)
            }
        }

    }

    private fun setCoinDetails(coinDetails: CoinDetailItem) {
        binding.coinDetail = setCoinDetail(coinDetails)
        (requireActivity() as MainActivity).supportActionBar?.apply {
            title = "${coinDetails.name} (${coinDetails.symbol})"
        }
        Glide.with(requireContext()).load(coinDetails.image.small).into(binding.imgIconImage)
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
                coinDetailViewModel.getCoinByID(coinID)
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