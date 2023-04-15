package news.my.kotlin.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import news.my.kotlin.R
import news.my.kotlin.databinding.ArticleItemBinding
import news.my.kotlin.model.Article

class AllNewsAdapter : RecyclerView.Adapter<AllNewsAdapter.ViewHolder>() {

    private lateinit var binding: ArticleItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ArticleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(differ.currentList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = differ.currentList.size


    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    inner class ViewHolder() : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("CheckResult")
        fun setData(item: Article) {
            binding.apply {
                tvArticleDescription.text = item.description
                tvArticleTitle.text = item.title
                  item.urlToImage?.let {
                    Glide.with(binding.root.context).load(it).centerCrop().into(ivArticleImage)
                }
                  }
        }
    }

}