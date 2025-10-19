package com.example.antrianrumahsakit.ui.pasien

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.antrianrumahsakit.databinding.FragmentNewsHealthBinding
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class NewsHealthFragment : Fragment() {

    private lateinit var binding: FragmentNewsHealthBinding
    private lateinit var adapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsHealthBinding.inflate(inflater, container, false)
        adapter = NewsAdapter { articleUrl ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(articleUrl))
            startActivity(intent)
        }
        binding.rvNews.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNews.adapter = adapter

        loadNews()
        return binding.root
    }

    private fun loadNews() {
        thread {
            try {
                // Ambil API key dari file assets (tidak disimpan di kode, disembunyikan, agar aman)
                val apiKey = requireContext().assets
                    .open("news_api_key.txt")
                    .bufferedReader()
                    .use { it.readText().trim() }

                val apiUrl =
                    "https://newsapi.org/v2/top-headlines?category=health&language=en&apiKey=$apiKey"

                val connection = URL(apiUrl).openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("User-Agent", "Mozilla/5.0")

                val responseCode = connection.responseCode
                Log.d("NewsAPI", "Response code: $responseCode")

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    val articles = JSONObject(response).getJSONArray("articles")

                    val newsList = mutableListOf<NewsItem>()
                    for (i in 0 until articles.length()) {
                        val item = articles.getJSONObject(i)
                        val title = item.getString("title")
                        val source = item.getJSONObject("source").getString("name")
                        val url = item.getString("url")
                        val imageUrl = item.optString("urlToImage", "")
                        newsList.add(NewsItem(title, source, imageUrl, url))
                    }

                    requireActivity().runOnUiThread {
                        adapter.submitList(newsList)
                    }
                } else {
                    val errorMsg =
                        connection.errorStream?.bufferedReader()?.use { it.readText() } ?: "Unknown error"
                    requireActivity().runOnUiThread {
                        Toast.makeText(
                            requireContext(),
                            "Gagal memuat berita (HTTP $responseCode)",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.e("NewsAPI", "Error: $errorMsg")
                    }
                }
            } catch (e: Exception) {
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    Log.e("NewsAPI", "Exception: ${e.message}", e)
                }
            }
        }
    }
}
