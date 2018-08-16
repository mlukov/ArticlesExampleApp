package com.mlukov.articles.presentation.articles.list.view

import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.mlukov.articles.R
import com.mlukov.articles.presentation.articles.details.model.ArticleDetailsViewData
import com.mlukov.articles.presentation.articles.list.model.ArticleViewData
import com.mlukov.articles.presentation.main.IMainNavigator
import com.mlukov.articles.utils.toSimpleDayTimeString
import javax.inject.Inject



class ArticlesAdapter

@Inject
constructor( val mainNavigator: IMainNavigator) : RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder>() {

    private val articlesList: MutableList<ArticleViewData> = mutableListOf()


    class ArticleViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        private val titleTextView: AppCompatTextView? = itemView?.findViewById(R.id.titleListTextView)
        private val subTitleTextView: AppCompatTextView? = itemView?.findViewById(R.id.subtitleListTextView)
        private val dateTextView: AppCompatTextView? = itemView?.findViewById(R.id.dateListTextView)

        internal fun bind(userData: ArticleViewData?) {

            titleTextView?.text = userData?.title ?: ""
            subTitleTextView?.text = userData?.subtitle ?: ""
            dateTextView?.text = userData?.date?.toSimpleDayTimeString() ?: ""
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.adapter_list_item, parent, false)
        val viewHodler = ArticleViewHolder(view)

        viewHodler.itemView.setOnClickListener{

            val position = viewHodler.getAdapterPosition()
            if (position != RecyclerView.NO_POSITION) {

                val article = articlesList.get(position)
                mainNavigator.showArticleDetails( ArticleDetailsViewData( article ) )
            }
        }

        return viewHodler
    }

    override fun getItemCount(): Int {

        return articlesList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {

        if (articlesList == null || articlesList.size <= position)
            holder.bind(null)
        else
            holder.bind(articlesList[position])
    }

    fun updateList( articleViewDataList: List<ArticleViewData>?) {

        articlesList.clear()
        if ( articleViewDataList?.isEmpty() == false ) {

            articlesList.addAll( articleViewDataList )
        }
        notifyDataSetChanged()
    }
}
