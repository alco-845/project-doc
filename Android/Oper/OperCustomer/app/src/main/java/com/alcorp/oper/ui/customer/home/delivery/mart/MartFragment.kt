package com.alcorp.oper.ui.customer.home.delivery.mart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alcorp.oper.databinding.FragmentMartBinding

class MartFragment : Fragment() {
    private var _binding: FragmentMartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMartBinding.inflate(inflater, container, false)

        binding.itemMart1.root.setOnClickListener {
            startActivity(Intent(context, DetailMartActivity::class.java))
        }

        binding.itemMart2.root.setOnClickListener {
            startActivity(Intent(context, DetailMartActivity::class.java))
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}