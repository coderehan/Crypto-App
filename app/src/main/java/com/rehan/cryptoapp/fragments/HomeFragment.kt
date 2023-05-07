package com.rehan.cryptoapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.rehan.cryptoapp.R
import com.rehan.cryptoapp.adapters.TopGainLossAdapter
import com.rehan.cryptoapp.adapters.TopMarketAdapter
import com.rehan.cryptoapp.api.ApiInterface
import com.rehan.cryptoapp.api.ApiUtilities
import com.rehan.cryptoapp.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)

        getTopCurrencyList()
        setTabLayout()

        return binding.root
    }

    private fun setTabLayout() {
        val adapter = TopGainLossAdapter(this)
        binding.contentViewPager.adapter = adapter

        binding.contentViewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(position == 0){
                    binding.topGainIndicator.visibility = View.VISIBLE
                    binding.topLoseIndicator.visibility = View.GONE
                }else{
                    binding.topGainIndicator.visibility = View.GONE
                    binding.topLoseIndicator.visibility = View.VISIBLE
                }
            }
        })

        TabLayoutMediator(binding.tabLayout, binding.contentViewPager){
            tab, position ->
            var title = if(position == 0){
                "Top Gainers"
            }else{
                "Top Losers"
            }
            tab.text = title
        }.attach()
    }

    private fun getTopCurrencyList() {
        lifecycleScope.launch(Dispatchers.IO){
            val result = ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData()
            // This is how we see API response in logcat window
            Log.d("Crypto Data", "getTopCurrencyList: ${result.body()!!.data.cryptoCurrencyList}")

            // We will show the result data in main thread
            // No need to set layout manager for recyclerview adapter as we already set in xml itself
            withContext(Dispatchers.Main){
                binding.topCurrencyRecyclerView.adapter = TopMarketAdapter(requireContext(), result.body()!!.data.cryptoCurrencyList)
            }
        }
    }

}