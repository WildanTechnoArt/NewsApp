package com.wildantechnoart.newsapp.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wildantechnoart.newsapp.databinding.ItemSourceBinding
import com.wildantechnoart.newsapp.model.Sources

class SourcesAdapter(private val view: View) :
    ListAdapter<Sources, SourcesAdapter.Holder>(MyDiffCallback()),
    Filterable {

    private var filterList: List<Sources>? = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemSourceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = getItem(position)

        with(holder.binding) {
            textTitle.text = data.name ?: "-"
            textDescription.text = data.description ?: "-"
            cardItem.setOnClickListener {
                toDetailFragment(data.id.toString(), data.name ?: "-")
            }
            btnDetail.setOnClickListener {
                toDetailFragment(data.id.toString(), data.name ?: "-")
            }
        }
    }

    private fun toDetailFragment(id: String?, name: String?) {
        val action = SourcesFragmentDirections.actionSourcesFragmentToArticlesFragment(
            sourceId = id.toString(),
            sourceName = name.toString()
        )
        Navigation.findNavController(view).navigate(action)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    class MyDiffCallback : DiffUtil.ItemCallback<Sources>() {
        override fun areItemsTheSame(oldItem: Sources, newItem: Sources): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Sources, newItem: Sources): Boolean {
            return oldItem == newItem
        }
    }

    class Holder(val binding: ItemSourceBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()

                if (filterList?.isEmpty() == true) {
                    filterList = currentList.toList()
                }

                val filteredList = if (constraint.isNullOrEmpty()) {
                    filterList
                } else {
                    val query = constraint.toString().lowercase()
                    filterList?.filter { item ->
                        item.name?.lowercase()
                            ?.contains(query) == true || item.description?.lowercase()
                            ?.contains(query) == true
                    }
                }

                results.count = filteredList?.size ?: 0
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                @Suppress("UNCHECKED_CAST")
                submitList(results?.values as? List<Sources> ?: emptyList())
            }
        }
    }
}