package com.mburakcakir.cryptopricetracker.ui.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mburakcakir.cryptopricetracker.data.model.FavouriteCoinModel
import com.mburakcakir.cryptopricetracker.databinding.RvItemFavouriteCoinBinding

class FavouriteCoinsAdapter :
    ListAdapter<FavouriteCoinModel, FavouriteCoinViewHolder>(CoinCallback()) {

    private lateinit var coinOnClick: (String) -> Unit

    fun setCoinOnClickListener(coinOnClick: (String) -> Unit) {
        this.coinOnClick = coinOnClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteCoinViewHolder =
        FavouriteCoinViewHolder(
            RvItemFavouriteCoinBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), coinOnClick
        )

    override fun onBindViewHolder(holder: FavouriteCoinViewHolder, position: Int) =
        holder.bind(getItem(position))

}

class FavouriteCoinViewHolder(
    private val binding: RvItemFavouriteCoinBinding,
    private val coinOnClick: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(favouriteCoinModel: FavouriteCoinModel) {
        binding.coin = favouriteCoinModel

        itemView.setOnClickListener {
            coinOnClick(favouriteCoinModel.id)
        }
    }
}

class CoinCallback : DiffUtil.ItemCallback<FavouriteCoinModel>() {
    override fun areItemsTheSame(
        oldItem: FavouriteCoinModel,
        newItem: FavouriteCoinModel
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: FavouriteCoinModel,
        newItem: FavouriteCoinModel
    ): Boolean = oldItem == newItem

}