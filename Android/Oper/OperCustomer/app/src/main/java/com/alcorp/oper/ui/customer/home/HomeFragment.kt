package com.alcorp.oper.ui.customer.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alcorp.oper.databinding.FragmentHomeBinding
import com.alcorp.oper.ui.customer.home.delivery.DeliveryActivity
import com.alcorp.oper.ui.customer.home.taxi_bike.TaxiBikeActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.constTaxiBike.setOnClickListener {
            startActivity(Intent(context, TaxiBikeActivity::class.java))
        }

        binding.constDelivery.setOnClickListener {
            startActivity(Intent(context, DeliveryActivity::class.java))
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}