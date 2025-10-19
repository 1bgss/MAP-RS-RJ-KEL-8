package com.example.antrianrumahsakit.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.antrianrumahsakit.databinding.FragmentMlBinding
import com.google.android.material.snackbar.Snackbar
// Bagian ini dalam tahap pengembangan untuk projek akhir
class MLFragment : Fragment() {
    private var _binding: FragmentMlBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMlBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnAnalyze.setOnClickListener {
            Snackbar.make(binding.root, "Prediksi risiko kesehatan (dummy ML)", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
