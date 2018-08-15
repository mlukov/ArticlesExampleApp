package com.example.articles.presentation.articles.list.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.articles.R
import com.example.articles.presentation.articles.list.model.ArticleViewData


class ArticlesAdapter(private val mUserDataList: List<ArticleViewData>?) : RecyclerView.Adapter<ArticlesAdapter.UserViewHolder>() {


    class UserViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        private val mUserNamesTextView: TextView? = itemView?.findViewById(R.id.articleTitleTextView)

        internal fun bind(userData: ArticleViewData?) {

            mUserNamesTextView?.text = userData?.title ?: ""
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.adapter_list_item, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {

        return mUserDataList?.size ?: 0
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        if (mUserDataList == null || mUserDataList.size <= position)
            holder.bind(null)
        else
            holder.bind(mUserDataList[position])
    }
}
