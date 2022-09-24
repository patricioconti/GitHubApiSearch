package com.example.bankuishchallenge.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bankuishchallenge.data.remote.results.searchrepo.Repo
import com.example.bankuishchallenge.databinding.RepoListItemBinding

/**
 * Adapter to inflate the appropriate list item layout and populate the view with information
 * from the appropriate data source
 */

//Using PagingDataAdapter and DiffUtils to update changes on the recycler view elements
//Pass lambda fun val repoClickedListener: (Repo) -> Unit for event click on Repo Item
class RepoPagedListAdapter (private val repoClickedListener: (Repo) -> Unit) :
    PagingDataAdapter<Repo, RepoPagedListAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RepoListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get the data at the current position
        val repo = getItem(position)
        // Bind Repo Data with UI components
        if (repo != null) {
            holder.bind(repo)
            // When clicking on an itemView of the recycler view then
            // call lambda repoClickedListener passing parameter repo (repo in current position)
            holder.itemView.setOnClickListener { repoClickedListener(repo)
            }
        }
    }

    /**
     * Initialize view elements
     */
    class ViewHolder(private val binding: RepoListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // Declare and initialize all of the list item UI components
        fun bind(repo: Repo) {

            with(binding) {
                repoName.text = repo.name
                repoAuthor.text = repo.owner.author
            }
        }
    }

    //Companion Object for DiffUtils
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Repo>() {
            override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return oldItem == newItem
            }
        }
    }
}