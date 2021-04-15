package com.mburakcakir.cryptopricetracker.ui.coin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mburakcakir.cryptopricetracker.databinding.FragmentCoinBinding
import com.mburakcakir.cryptopricetracker.utils.Status

class CoinFragment : Fragment() {
    private var _binding: FragmentCoinBinding? = null
    private val binding get() = _binding!!

    private var coinAdapter = CoinAdapter()
    private lateinit var coinViewModel: CoinViewModel

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
        coinViewModel = ViewModelProvider(this).get(CoinViewModel::class.java)
        binding.rvCoinList.adapter = coinAdapter

        coinViewModel.coinInfo.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> coinAdapter.submitList(it.data)
            }
            binding.state = CoinViewState(it.status)
        }


//        coinViewModel.result.observe(viewLifecycleOwner) { result ->
//            when {
//                result.success != null -> getString(result.success)
//                result.loading != null -> getString(result.loading)
//                result.error != null -> getString(result.error)
//                else ->getString(R.string.error_result)
//            }.let { message ->
//                requireContext() toast message
//            }
//        }
    }
}