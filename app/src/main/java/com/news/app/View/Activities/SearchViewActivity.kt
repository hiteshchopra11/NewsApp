package com.news.app.View.Activities

import android.app.SearchManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.news.app.Model.ArticleX
import com.news.app.Model.Everything
import com.news.app.R
import com.news.app.Utils.Adapters.SearchAdapter
import com.news.app.Utils.Status
import com.news.app.ViewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_search_view.*
import org.koin.android.viewmodel.ext.android.viewModel
import retrofit2.Response


class SearchViewActivity : AppCompatActivity() {
    //Lateinit variables to initialize later
    private val viewModel: MainViewModel by viewModel()
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchManager: SearchManager
    private lateinit var query: String
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_view)

        //Initialize Views, Setup UI(RecycleView and Adapters) and setup ViewModels
        initViews()
        setupUI()
        recyclerView.adapter = searchAdapter
        searchAdapter.notifyDataSetChanged()


        //Change color of SearchView text to white programmatically
        val editText = search_view.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        editText.setHintTextColor(Color.LTGRAY)
        editText.setTextColor(Color.WHITE)

        //Implement Search and call from API using LiveData ViewModels
        search_view.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        search_view.queryHint = "Search News..."
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(qString: String): Boolean {
                progressBar.visibility = View.VISIBLE
                if (qString.length > 2) {
                    query = qString
                    setupObservers(qString)
                }
                return false
            }

            override fun onQueryTextSubmit(qString: String): Boolean {
                query = qString
                progressBar.visibility = View.VISIBLE
                setupObservers(qString)
                return false
            }
        })
        progressBar.visibility = View.GONE
    }

    //Setup recycleView and attach adapter to RecycleView
    private fun setupUI() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        searchAdapter = SearchAdapter(arrayListOf())
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = searchAdapter
    }

    //Initialize RecycleView and SearchManager
    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerview)
        searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
    }


    //Set up observers to perform network operations
    private fun setupObservers(q: String) {
        //Pass q which is our search query to ViewModel function searchnews()
        //so that api call can be made based on our query
        viewModel.searchNews(q).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        textViewEmpty.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        resource.data?.let { news -> retrieveList(news) }
                    }
                    Status.ERROR -> {
                        textViewEmpty.text = "Some error occurred please try again later"
                        textViewEmpty.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        recyclerView.visibility = View.GONE
                    }
                }
            }
        })
    }

    //Retrieve response here and send to adapter which then furthers binds data to the views using ViewHolder
    private fun retrieveList(news: Response<Everything>) {
        val articles: List<ArticleX> = news.body()!!.articles
        searchAdapter.apply {
            addNews(articles)
            notifyDataSetChanged()
        }
        if (searchAdapter.itemCount == 0) {
            textViewEmpty.text = "No results Found"
            textViewEmpty.visibility = View.VISIBLE
        }
    }
}
