package com.news.app.View.Activities

import HeadlinesFragment
import SourcesFragment
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.news.app.R


class MainActivity : AppCompatActivity() {

    //Lateinit to initialize the variables later(in initViews())
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var toolbar: Toolbar

    //Title of viewpager
    private val titles = arrayOf("Headlines", "Sources")
    private val fragmentList: MutableList<Fragment> = ArrayList()

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragmentList.add(HeadlinesFragment())
        fragmentList.add(SourcesFragment())
        //Initialize Views, Attach Adapter to ViewPager and set Toolbar in our MainActivity
        initViews()
        attachAdapter()
        setSupportActionBar(toolbar)
        //Set title of toolbar
        toolbar.title = "News"
    }

    //Initialize the views
    private fun initViews() {
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tab_layout)
        toolbar = findViewById(R.id.toolbar)
    }

    //Attach Fragment adapter to connect Fragments to our Activity ViewPager
    private fun attachAdapter() {
        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return fragmentList.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragmentList[position]
            }
        }
        val tabLayoutMediator = TabLayoutMediator(
            tabLayout, viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = titles[position]
        }

        //Attach to TabLayoutMediator(Viewpager2)
        tabLayoutMediator.attach()
        viewPager.offscreenPageLimit = 2
    }


    // Inflate the menu; this adds items to the action bar if it is present.
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    //Handle menu search item option here and set others to false so that implementation given in the
    //fragments can be used
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Provide search button implementation
        return when (item.itemId) {
            R.id.search_view -> {
                //Start new SearchActivity to perform search operations
                val intent = Intent(this, SearchViewActivity::class.java)
                startActivity(intent)
                true
            }
            //Set items false here so that it can be implemented and used in Fragments
            R.id.grid_layout -> {
                false
            }
            R.id.linear_layout -> {
                false
            }
            R.id.staggered_layout -> {
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}