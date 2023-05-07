package com.rehan.cryptoapp.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rehan.cryptoapp.R
import com.rehan.cryptoapp.adapters.MarketAdapter
import com.rehan.cryptoapp.api.ApiInterface
import com.rehan.cryptoapp.api.ApiUtilities
import com.rehan.cryptoapp.databinding.FragmentHomeBinding
import com.rehan.cryptoapp.databinding.FragmentWatchlistBinding
import com.rehan.cryptoapp.models.CryptoCurrency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WatchlistFragment : Fragment() {

    private lateinit var binding: FragmentWatchlistBinding
    private lateinit var watchlist: ArrayList<String>
    private lateinit var watchListItem: ArrayList<CryptoCurrency>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWatchlistBinding.inflate(layoutInflater)

        lifecycleScope.launch(Dispatchers.IO){
            val result = ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData()

            if(result.body() != null){
                withContext(Dispatchers.Main){
                    watchlist = ArrayList()
                    watchListItem.clear()

                    for(watchData in watchlist){
                        for(item in result.body()!!.data.cryptoCurrencyList){
                            if(watchData == item.symbol){
                                watchListItem.add(item)
                            }
                        }
                    }

                    binding.spinKitView.visibility = View.GONE
                    binding.watchlistRecyclerView.adapter = MarketAdapter(requireContext(), watchListItem, "watchlistfragment")

                }
            }
        }

        return binding.root
    }

    private fun readData() {
        val sharedPreferences = requireContext().getSharedPreferences("watchlist", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("watchlist", ArrayList<String>().toString())
        val type = object : TypeToken<ArrayList<String>>(){}.type
        watchlist = gson.fromJson(json, type)
    }

}