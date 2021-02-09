import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.*
import com.news.app.Model.Article
import com.news.app.Model.News
import com.news.app.R
import com.news.app.Utils.Adapters.HeadlinesAdapter
import com.news.app.Utils.Adapters.HeadlinesGridAdapter
import com.news.app.Utils.Status
import com.news.app.ViewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_headlines.*
import org.koin.android.viewmodel.ext.android.viewModel
import retrofit2.Response


open class HeadlinesFragment : Fragment() {
    //Late initialize views
    private val viewModel: MainViewModel by viewModel()
    private lateinit var linearAdapter: HeadlinesAdapter
    private lateinit var gridAdapter: HeadlinesGridAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onConfigurationChanged(newConfig: Configuration) {
        Log.e("a","a")
        super.onConfigurationChanged(newConfig)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("CREATED", "onCreateView")
        return inflater.inflate(R.layout.fragment_headlines, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.e("CREATED", "onViewCreated")
        //Enable menu
        setHasOptionsMenu(true)
        //Initialize lately initialized views,setup ViewModels,attach adapters to recyclerview
        //and setup Observers(for API calls) using user defined functions
        //Make network error TextView invisible

        initViews()
        setupUI()
        setupObservers()
        textViewEmpty.visibility = View.GONE
    }

    //Attach adapters to recyclerview
    private fun setupUI() {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        linearAdapter = HeadlinesAdapter(arrayListOf())
        gridAdapter = HeadlinesGridAdapter(arrayListOf())
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = linearAdapter
    }


    //Setup Observers
    private fun setupObservers() {
        activity?.let { it ->
            viewModel.getUsers("us").observe(it, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            progressBar.visibility = View.GONE
                            recyclerView.visibility = View.VISIBLE
                            //Retrieve response and add to adapters using this function
                            resource.data?.let { news -> retrieveList(news) }
                        }
                        Status.ERROR -> {
                            progressBar.visibility = View.GONE
                            recyclerView.visibility = View.GONE
                            //Show network error textView
                            textViewEmpty.visibility = View.VISIBLE
                        }
                        Status.LOADING -> {
                            recyclerView.visibility = View.GONE
                        }
                    }
                }
            })
        }
    }

    //Retrieve data and add in our adapters
    private fun retrieveList(news: Response<News>) {
        val articles: List<Article> = news.body()!!.articles

        linearAdapter.apply {
            addNews(articles)
            notifyDataSetChanged()
        }

        gridAdapter.apply {
            addNews(articles)
            notifyDataSetChanged()
        }
    }

    //Handle menu options which were disabled in Activity and meant for Fragments
    //and set others(search_view) false which we are already handled in our activity
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.grid_layout -> {
                Log.e("A", "Aa")
                val manager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
                recyclerView.layoutManager = manager
                recyclerView.adapter = gridAdapter

            }
            R.id.staggered_layout -> {
                val staggeredGridLayoutManager =
                    StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
                recyclerView.layoutManager = staggeredGridLayoutManager
                recyclerView.adapter = linearAdapter
            }
            R.id.linear_layout -> {
                recyclerView.layoutManager = LinearLayoutManager(activity)
                recyclerView.adapter = linearAdapter
            }
            R.id.search_view -> {
                return false
            }
        }
        return false
    }

    //Initialize recyclerView
    private fun initViews() {
        recyclerView = requireView().findViewById(R.id.headlinesRecycle)
    }
}


