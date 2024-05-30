package com.alcorp.oper.ui.customer.home.delivery.goods

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alcorp.oper.databinding.FragmentGoodsBinding

class GoodsFragment : Fragment() {
    private var _binding: FragmentGoodsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoodsBinding.inflate(inflater, container, false)

        binding.btnAdd.setOnClickListener {
            startActivity(Intent(context, AddShippingActivity::class.java))
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}