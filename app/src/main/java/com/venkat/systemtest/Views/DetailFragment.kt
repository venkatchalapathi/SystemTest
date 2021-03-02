package com.venkat.systemtest.Views

import Items
import android.content.ClipData
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.venkat.systemtest.R


class DetailFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View

        val item:Items  = Gson().fromJson(arguments!!.getString("data"),Items::class.java)
//
        view = inflater.inflate(R.layout.fragment_detail, container, false)

        val imageView:ImageView = view.findViewById(R.id.imageView3)

        Glide.with(context!!).load(item.owner.avatar_url).into(imageView)

        val name:TextView = view.findViewById(R.id.textView2)
        name.text = item.full_name

        val description:TextView = view.findViewById(R.id.textView5)
        description.text = item.description

        val comment:TextView = view.findViewById(R.id.textView7)
        comment.text = item.comment
        // Inflate the layout for this fragment
        return view
    }


}