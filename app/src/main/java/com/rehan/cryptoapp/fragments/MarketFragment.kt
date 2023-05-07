package com.rehan.cryptoapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.rehan.cryptoapp.adapters.MarketAdapter
import com.rehan.cryptoapp.api.ApiInterface
import com.rehan.cryptoapp.api.ApiUtilities
import com.rehan.cryptoapp.databinding.FragmentMarketBinding
import com.rehan.cryptoapp.models.CryptoCurrency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class MarketFragment : Fragment() {

    private lateinit var binding: FragmentMarketBinding
    private lateinit var list: List<CryptoCurrency>
    private lateinit var adapter: MarketAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMarketBinding.inflate(layoutInflater)

        // Initializing list & adapter that we declared above
        list = listOf()
        adapter = MarketAdapter(requireContext(), list, "market")
        binding.currencyRecyclerView.adapter = adapter

        lifecycleScope.launch(Dispatchers.IO){
            val result = ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData()

            if(result.body() != null){
                withContext(Dispatchers.Main){
                    list = result.body()!!.data.cryptoCurrencyList

                    adapter.updateData(list)
                    binding.spinKitView.visibility = View.GONE
                }
            }
        }

        searchCoin()

        return binding.root
    }

    lateinit var searchText : String

    private fun searchCoin(){
        binding.searchEditText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                searchText = p0.toString().toLowerCase()    // Whatever user search coin, we will convert them into lowercase

                updateRecyclerView()
            }

        })
    }

    // We will update the recyclerview after user search the particular coin
    private fun updateRecyclerView() {
        val data = ArrayList<CryptoCurrency>()

        for(item in list){
            val coinName = item.name.lowercase(Locale.getDefault())     // Converting search coin into lowercase
            val coinSymbol = item.name.lowercase(Locale.getDefault())

            if(coinName.contains(searchText) || coinSymbol.contains(searchText)){
                data.add(item)
            }

        }

        adapter.updateData(data)
    }

}