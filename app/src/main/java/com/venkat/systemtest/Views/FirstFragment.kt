package com.venkat.systemtest.Views

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.venkat.systemtest.Adapters.RepoListAdapter
import com.venkat.systemtest.R
import com.venkat.systemtest.ViewModels.RepoViewmodel

class FirstFragment : Fragment() {
    private lateinit var viewModel: RepoViewmodel
    private lateinit var newsListAdapter: RepoListAdapter
    lateinit var recycler_view: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_first, container, false)

        Toast.makeText(context,"Loading...",Toast.LENGTH_SHORT).show()
        recycler_view = view.findViewById(R.id.recyclerView)

        viewModel = ViewModelProviders.of(this)
            .get(RepoViewmodel::class.java)
        initAdapter()
        initState()


        return view
    }
    private fun initAdapter() {
        newsListAdapter = RepoListAdapter(context!!) { viewModel.retry() }
        recycler_view.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recycler_view.adapter = newsListAdapter
        viewModel.newsList.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "initAdapter: ${Gson().toJson(it)}")
            newsListAdapter.submitList(it)
        })
    }

    private fun initState() {
        /*txt_error.setOnClickListener { viewModel.retry() }
        viewModel.getState().observe(this, Observer { state ->
            progress_bar.visibility = if (viewModel.listIsEmpty() && state == State.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility = if (viewModel.listIsEmpty() && state == State.ERROR) View.VISIBLE else View.GONE
            if (!viewModel.listIsEmpty()) {
                newsListAdapter.setState(state ?: State.DONE)
            }
        })*/
    }
}