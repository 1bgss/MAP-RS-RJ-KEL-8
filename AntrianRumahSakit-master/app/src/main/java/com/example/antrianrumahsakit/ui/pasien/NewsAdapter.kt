package com.example.antrianrumahsakit.ui.pasien

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.antrianrumahsakit.databinding.ItemNewsBinding

data class NewsItem(
    val title: String,
    val source: String,
    val imageUrl: String?,
    val url: String
)

class NewsAdapter(private val onClick: (String) -> Unit) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private val items = mutableListOf<NewsItem>()

    inner class ViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NewsItem) {
            binding.tvTitle.text = item.title
            binding.tvSource.text = item.source
            binding.imgNews.load(item.imageUrl)
            binding.root.setOnClickListener { onClick(item.url) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    fun submitList(newItems: List<NewsItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
