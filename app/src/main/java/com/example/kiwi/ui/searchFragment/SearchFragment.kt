package com.example.kiwi.ui.searchFragment

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



   override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View? {

     val theviewForSearch = inflater.inflate(R.layout.fragment_search,container,false)

      theViewModelForSearch.getTheItems(theRVAdapterforsearchingfragment,theviewForSearch.textFieldForSearcFragment.editableText)

      return  theviewForSearch
   }



   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      if (!theViewModelForSearch.isOnline) {
         findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToNoInternet())
      } else {


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

               /**
                * here we create a query from the text watcher class over the fireBase data base to get us the user if it exists
                */
               theViewModelForSearch.doTheWatch()

            }

         })

      }
   }

   }
