import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.*
import com.news.app.Model.Source
import com.news.app.Model.SourceX
import com.news.app.R
import com.news.app.Utils.Adapters.SourceGridAdapter
import com.news.app.Utils.Status
import com.news.app.ViewModel.MainViewModel
import com.sources.app.Adapters.SourceLinearAdapter
import kotlinx.android.synthetic.main.fragment_headlines.*
import org.koin.android.viewmodel.ext.android.viewModel
import retrofit2.Response

class SourcesFragment : Fragment() {
    //Late initialize views
    private val viewModel: MainViewModel by viewModel()
    private lateinit var linearAdapter: SourceLinearAdapter
    private lateinit var gridAdapter: SourceGridAdapter
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sources, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Enable menu
        setHasOptionsMenu(true)

        //Initialize lately initialized views,setup ViewModels,attach adapters to recyclerview
        //and setup Observers(for API calls) using user defined functions
        initViews()
        setupUI()
        setupObservers()
        //Make network error TextView invisible
        textViewEmpty.visibility = View.GONE
    }

    //Attach adapters to recyclerview
    private fun setupUI() {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        linearAdapter = SourceLinearAdapter(arrayListOf())
        gridAdapter = SourceGridAdapter(arrayListOf())
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
        recyclerView.layoutManager = LinearLayoutManager(activity)
        activity?.let { it ->
            viewModel.getSources().observe(it, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            progressBar.visibility = View.GONE
                            recyclerView.visibility = View.VISIBLE
                            resource.data?.let { sources -> retrieveList(sources) }
                        }
                        Status.ERROR -> {
                            progressBar.visibility = View.GONE
                            recyclerView.visibility = View.VISIBLE
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
    private fun retrieveList(source: Response<Source>) {
        val sources: List<SourceX> = source.body()!!.sources
        linearAdapter.apply {
            addSources(sources)
            notifyDataSetChanged()
        }
        gridAdapter.apply {
            addSources(sources)
            notifyDataSetChanged()
        }
    }

    private fun initViews() {
        recyclerView = requireView().findViewById(R.id.sourcesRecycle)
    }

    //Handle menu options which were disabled in Activity and meant for Fragments
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.grid_layout -> {
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
        }
        return false
    }
}