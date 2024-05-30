package com.alcorp.oper.ui.customer.customer_activity.history

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alcorp.oper.databinding.FragmentHistoryCustomerBinding

class HistoryCustomerFragment : Fragment() {

    private var _binding: FragmentHistoryCustomerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHistoryCustomerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.cvItem.setOnClickListener {
            startActivity(Intent(context, DetailHistoryCustomerActivity::class.java))
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}