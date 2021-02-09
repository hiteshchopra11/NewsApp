package com.news.app.Utils.Adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.news.app.Model.SourceX
import com.news.app.R
import kotlinx.android.synthetic.main.source_linear_item.view.*

class SourceGridAdapter(private val sources: ArrayList<SourceX>) :
    RecyclerView.Adapter<SourceGridAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(sources: SourceX) {
            //Bind data to Views
            itemView.apply {
                sourceName.text = sources.name
                sourceDescription.text = sources.description
                sourceCountry.text = "Country : " + sources.country
            }
            //Get url of the article and launch an intent to open web browser on item click
            itemView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(sources.url))
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.source_grid_item, parent, false)
        )

    override fun getItemCount(): Int = sources.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(sources[position])
    }

    fun addSources(sources: List<SourceX>) {
        this.sources.apply {
            clear()
            addAll(sources)
        }
    }
}