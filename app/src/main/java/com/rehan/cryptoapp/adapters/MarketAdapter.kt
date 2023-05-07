package com.rehan.cryptoapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rehan.cryptoapp.R
import com.rehan.cryptoapp.databinding.CurrencyItemLayoutBinding
import com.rehan.cryptoapp.fragments.HomeFragmentDirections
import com.rehan.cryptoapp.fragments.MarketFragmentDirections
import com.rehan.cryptoapp.fragments.WatchlistFragmentDirections
import com.rehan.cryptoapp.models.CryptoCurrency

class MarketAdapter(var context: Context, var list: List<CryptoCurrency>, var type: String) : RecyclerView.Adapter<MarketAdapter.MarketViewHolder>() {

    fun updateData(dataItem: List<CryptoCurrency>){
        list = dataItem
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        return MarketViewHolder(LayoutInflater.from(context).inflate(R.layout.currency_item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        val item = list[position]

        holder.binding.currencyNameTextView.text = item.name
        holder.binding.currencySymbolTextView.text = item.symbol

        Glide.with(context).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/" + item.id + ".png"
        ).thumbnail(Glide.with(context).load(R.drawable.spinner))   // We will show spinner image gif till we load the desired imageview from api
            .into(holder.binding.currencyImageView)

        Glide.with(context).load(
            "https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/" + item.id + ".png"
        ).thumbnail(Glide.with(context).load(R.drawable.spinner))   // We will show spinner image gif till we load the desired imageview from api
            .into(holder.binding.currencyChartImageView)

        holder.binding.currencyPriceTextView.text = "${String.format("$%.02f", item.quotes[0].price)}"

        // If crypto 24 hours rate is greater than 0, we will show in green color with + symbol otherwise in red color
        if(item.quotes!![0].percentChange24h > 0){
            holder.binding.currencyChangeTextView.setTextColor(context.resources.getColor(R.color.green))
            holder.binding.currencyChangeTextView.text = "+ ${String.format("%.02f", item.quotes[0].percentChange24h)} %"
        }else{
            holder.binding.currencyChangeTextView.setTextColor(context.resources.getColor(R.color.red))
            holder.binding.currencyChangeTextView.text = "${String.format("%.02f", item.quotes[0].percentChange24h)} %"
        }

        // Passing data between fragments using safe args
        holder.itemView.setOnClickListener {

            if(type == "home") {
                findNavController(it).navigate(
                    HomeFragmentDirections.actionHomeFragmentToDetailsFragment(item)
                )
            }else if(type == "market"){
                findNavController(it).navigate(
                    MarketFragmentDirections.actionMarketFragmentToDetailsFragment(item)
                )
            }else if(type == "watchlistfragment"){
                findNavController(it).navigate(
                    WatchlistFragmentDirections.actionWatchlistFragmentToDetailsFragment(item)
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MarketViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var binding = CurrencyItemLayoutBinding.bind(view)
    }
}