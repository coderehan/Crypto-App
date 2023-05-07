package com.rehan.cryptoapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.rehan.cryptoapp.adapters.MarketAdapter
import com.rehan.cryptoapp.api.ApiInterface
import com.rehan.cryptoapp.api.ApiUtilities
import com.rehan.cryptoapp.databinding.FragmentTopGainLossBinding
import com.rehan.cryptoapp.models.CryptoCurrency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class TopGainLossFragment : Fragment() {

    private lateinit var binding: FragmentTopGainLossBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTopGainLossBinding.inflate(layoutInflater)

        getMarketData()

        return binding.root
    }

    private fun getMarketData() {
        val position = requireArguments().getInt("position")

        lifecycleScope.launch(Dispatchers.IO){
            val result = ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData()

            if(result.body() != null){
                withContext(Dispatchers.Main){
                    val dataItem = result.body()!!.data.cryptoCurrencyList

                    Collections.sort(dataItem){
                        var1, var2 -> (var2.quotes[0].percentChange24h.toInt())
                        .compareTo(var1.quotes[0].percentChange24h.toInt())
                    }

                    binding.spinKitView.visibility = View.GONE

                    val list = ArrayList<CryptoCurrency>()

                    if(position == 0){
                        list.clear()
                        for(i in 0..9){
                            list.add(dataItem[i])
                        }
                        binding.topGainLoseRecyclerView.adapter = MarketAdapter(
                            requireContext(),
                            list,
                            "home"
                        )

                    }else{
                        list.clear()
                        for(i in 0..9){
                            list.add(dataItem[dataItem.size-1-i])
                        }
                        binding.topGainLoseRecyclerView.adapter = MarketAdapter(
                            requireContext(),
                            list,
                            "home"
                        )
                    }

                }
            }
        }
    }

}