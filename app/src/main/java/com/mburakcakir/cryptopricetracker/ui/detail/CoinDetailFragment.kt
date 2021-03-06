package com.mburakcakir.cryptopricetracker.ui.detail

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.mburakcakir.cryptopricetracker.R
import com.mburakcakir.cryptopricetracker.data.model.CoinDetailItem
import com.mburakcakir.cryptopricetracker.databinding.FragmentCoinDetailBinding
import com.mburakcakir.cryptopricetracker.ui.BaseFragment
import com.mburakcakir.cryptopricetracker.ui.MainActivity
import com.mburakcakir.cryptopricetracker.util.SharedPreferences
import com.mburakcakir.cryptopricetracker.util.enums.Status
import com.mburakcakir.cryptopricetracker.util.setCoinDetail
import com.mburakcakir.cryptopricetracker.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CoinDetailFragment : BaseFragment<FragmentCoinDetailBinding>() {

    private val args by navArgs<CoinDetailFragmentArgs>()
    private lateinit var coinID: String

    private val coinDetailViewModel: CoinDetailViewModel by viewModels()
    private var isFavourite: Boolean = false
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var menuItem: MenuItem

    override fun getFragmentView(): Int = R.layout.fragment_coin_detail

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {

        setToolbar()

        setCoinData()

        observeData()

    }

    private fun setToolbar() {
        setHasOptionsMenu(true)
    }

    private fun setCoinData() {
        coinID = args.coinID
        coinDetailViewModel.isFavourite(coinID)

        sharedPreferences = SharedPreferences(requireContext())
        sharedPreferences.getRefreshInterval()?.let {
            binding.edtInterval.setText(sharedPreferences.getRefreshInterval())
        }

        binding.apply {
            state = CoinDetailViewState(Status.LOADING)
            edtInterval.setText(sharedPreferences.getRefreshInterval())

            edtInterval.setOnKeyListener(onKeyListener)
        }
    }

    private fun setRefreshInterval() {
        val refreshInterval = binding.edtInterval.text.toString()
        repeatRequestByRefreshInterval(Integer.parseInt(refreshInterval))
        requireContext() toast "All data and details will be refreshed every $refreshInterval seconds."
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

    private infix fun MenuItem.changeIconColor(isFavourite: Boolean) {
        val color = if (isFavourite) R.color.yellow else R.color.white

        icon.colorFilter = PorterDuffColorFilter(
            ContextCompat.getColor(requireContext(), color),
            PorterDuff.Mode.SRC_IN
        )
    }

    private val onKeyListener = object : View.OnKeyListener {
        override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event?.action == KeyEvent.ACTION_UP) {
                setRefreshInterval()
                return true
            }
            return false
        }

    }

}