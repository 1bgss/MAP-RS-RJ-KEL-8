package com.example.antrianrumahsakit.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.antrianrumahsakit.R

class PlaceholderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_placeholder, container, false)
        val textView = view.findViewById<TextView>(R.id.tvPlaceholder)

        val pageName = arguments?.getString("pageName") ?: "Halaman"
        textView.text = "🩺 $pageName sedang dalam pengembangan..."

        return view
    }

    companion object {
        fun newInstance(name: String): PlaceholderFragment {
            val fragment = PlaceholderFragment()
            val bundle = Bundle()
            bundle.putString("pageName", name)
            fragment.arguments = bundle
            return fragment
        }
    }
}
