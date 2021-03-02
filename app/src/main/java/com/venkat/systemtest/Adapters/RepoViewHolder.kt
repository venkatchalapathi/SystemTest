package com.venkat.systemtest.Adapters

import Items
import Json4Kotlin_Base
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.venkat.systemtest.R
import org.greenrobot.eventbus.EventBus

class RepoViewHolder(val context: Context,view: View) : RecyclerView.ViewHolder(view) {

    val name: TextView = view.findViewById(R.id.textView)
    val profile:ImageView = view.findViewById(R.id.imageView)
    val description:TextView = view.findViewById(R.id.textView3)
    val stars:TextView = view.findViewById(R.id.textView4)
    val row:CardView = view.findViewById(R.id.fullname)
    val comment:EditText = view.findViewById(R.id.editTextTextPersonName)

    private var repo: Items? = null


    fun bind(repo: Items?) {
        if (repo == null) {
        } else {
            showRepoData(repo)
        }
    }

    private fun showRepoData(repo: Items) {
        this.repo = repo
        name.text = repo.full_name
        stars.text = repo.stargazers_count.toString()
        Glide.with(context).load(repo.owner.avatar_url).into(profile)
        description.text = repo.description

        row.setOnClickListener {
            val re:Items = repo
            re.comment = comment.text.toString()
            EventBus.getDefault().post(repo)
        }
    }

    companion object {
        fun create(context: Context,parent: ViewGroup): RepoViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_repos, parent, false)
            return RepoViewHolder(context,view)
        }
    }}