package com.example.kiwi

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class  MainActivity  : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


            nav_host_fragment.findNavController()
                .addOnDestinationChangedListener { _, destination, _ ->
                    when (destination.id) {
                        R.id.notificationsFragment, R.id.searchFragment, R.id.postFragment, R.id.homeFragment, R.id.profileragment2,  R.id.noInternet-> {
                            nav_view.visibility = View.VISIBLE

                        }



                        else -> {
                            nav_view.visibility = View.GONE
                        }
                    }
                }



            nav_view.setupWithNavController(nav_host_fragment.findNavController())

            nav_view.setOnNavigationItemSelectedListener {

                when (it.itemId) {

                    R.id.notificationsFragment -> {
                        nav_host_fragment.findNavController()
                            .popBackStack(R.id.notificationsFragment, true)
                        nav_host_fragment.findNavController().navigate(R.id.notificationsFragment)
                    }


                    R.id.searchFragment -> {
                        nav_host_fragment.findNavController()
                            .popBackStack(R.id.searchFragment, true)
                        nav_host_fragment.findNavController().navigate(R.id.searchFragment)
                    }


                    R.id.postFragment -> {
                        nav_host_fragment.findNavController().popBackStack(R.id.postFragment, true)
                        nav_host_fragment.findNavController().navigate(R.id.postFragment)
                    }


                    R.id.homeFragment -> {
                        nav_host_fragment.findNavController().popBackStack(R.id.homeFragment, true)
                        nav_host_fragment.findNavController().navigate(R.id.homeFragment)
                    }


                    R.id.profileragment2 -> {
                        nav_host_fragment.findNavController()
                            .popBackStack(R.id.profileragment2, true)
                        nav_host_fragment.findNavController().navigate(R.id.profileragment2)
                    }


                }

                return@setOnNavigationItemSelectedListener true
            }
        }
    }




