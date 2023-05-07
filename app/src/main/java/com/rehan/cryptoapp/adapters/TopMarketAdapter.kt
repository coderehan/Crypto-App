package com.rehan.cryptoapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rehan.cryptoapp.R
import com.rehan.cryptoapp.databinding.TopCurrencyLayoutBinding
import com.rehan.cryptoapp.fragments.HomeFragmentDirections
import com.rehan.cryptoapp.models.CryptoCurrency

class TopMarketAdapter(var context: Context, val list: List<CryptoCurrency>): RecyclerView.Adapter<TopMarketAdapter.TopMarketViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopMarketViewHolder {
        return TopMarketViewHolder(LayoutInflater.from(context).inflate(R.layout.top_currency_layout, parent, false))
    }

    override fun onBindViewHolder(holder: TopMarketViewHolder, position: Int) {
        val item = list[position]

        holder.binding.topCurrencyNameTextView.text = item.name

        Glide.with(context).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/" + item.id + ".png"
        ).thumbnail(Glide.with(context).load(R.drawable.spinner))   // We will show spinner image gif till we load the desired imageview from api
            .into(holder.binding.topCurrencyImageView)

        // If crypto 24 hours rate is greater than 0, we will show in green color with + symbol otherwise in red color
        if(item.quotes!![0].percentChange24h > 0){
            holder.binding.topCurrencyChangeTextView.setTextColor(context.resources.getColor(R.color.green))
            holder.binding.topCurrencyChangeTextView.text = "+ ${String.format("%.02f", item.quotes[0].percentChange24h)} %"
        }else{
            holder.binding.topCurrencyChangeTextView.setTextColor(context.resources.getColor(R.color.red))
            holder.binding.topCurrencyChangeTextView.text = "${String.format("%.02f", item.quotes[0].percentChange24h)} %"
        }

        holder.itemView.setOnClickListener{
            Navigation.findNavController(it).navigate(
                HomeFragmentDirections.actionHomeFragmentToDetailsFragment(item)
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class TopMarketViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var binding = TopCurrencyLayoutBinding.bind(view)
    }

}