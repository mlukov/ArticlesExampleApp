package com.mlukov.articles.presentation.articles.list.view


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.mlukov.articles.R

class ArticlesFragment : Fragment() {

    companion object {

        public val TAG = ArticlesFragment::class.simpleName

        public fun newInstance(): ArticlesFragment{
            return ArticlesFragment()
        }
    }


    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?,
                              savedInstanceState : Bundle?) : View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_articles, container, false)
    }


    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }
}
