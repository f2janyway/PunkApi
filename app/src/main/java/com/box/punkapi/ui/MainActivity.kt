package com.box.punkapi.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.box.punkapi.R
import com.box.punkapi.databinding.ActivityMainBinding
import com.box.punkapi.di.Injector

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(
            this,
            Injector.provideViewModel()
        )[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mainViewModel.isLoading.observe(this){
            binding.isLoading = it
        }
        setUpActionBar()
        setGraphViewControl()
    }
    private fun setUpActionBar(){
        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.apply {
            title = getString(R.string.app_name)
        }
    }
    private fun setGraphViewControl(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
        navHostFragment.navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id){
                R.id.beerDetailFragment->{
                    supportActionBar?.apply {
                        setDisplayHomeAsUpEnabled(true)
                    }
                }
                R.id.beerListFragment->{
                    supportActionBar?.apply {
                        setDisplayHomeAsUpEnabled(false)
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                onBackPressed()
            }
        }
        return true
    }



}