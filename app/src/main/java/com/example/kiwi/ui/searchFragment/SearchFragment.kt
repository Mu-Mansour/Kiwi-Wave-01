package com.example.kiwi.ui.searchFragment

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kiwi.R
import com.example.kiwi.adapters.UserAdapter
import com.example.kiwi.ui.profile.ProfileragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {



   val theViewModelForSearch:SearchViewModel by viewModels()
   val theRVAdapterforsearchingfragment=UserAdapter()

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      theViewModelForSearch.netWorkFound.value=isNetworkAvailable(requireContext())
   }

   override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View? {

     val theviewForSearch = inflater.inflate(R.layout.fragment_search,container,false)
      theViewModelForSearch.netWorkFound.observe(viewLifecycleOwner,{
         it?.let {
            if (!it)
            {
               findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToNoInternet())

            }
         }
      })

      return  theviewForSearch
   }



   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)


         //findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToNoInternet())



         recyclerView3forSearch.adapter = theRVAdapterforsearchingfragment
         recyclerView3forSearch.layoutManager = LinearLayoutManager(view.context)





         textFieldForSearcFragment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
               theRVAdapterforsearchingfragment.clearTheList()
               theRVAdapterforsearchingfragment.notifyDataSetChanged()


            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

               theViewModelForSearch.doTheWatchSearch(p0,theRVAdapterforsearchingfragment)

            }

         })

      }
   fun isNetworkAvailable(context: Context): Boolean {
      val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

      // For 29 api or above
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
         val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) ?: return false
         return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ->    true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ->   true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->   true
            else ->     false
         }
      }
      // For below 29 api
      else {
         if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnectedOrConnecting) {
            return true
         }
      }
      return false
   }

   }
