package com.alcorp.oper.ui.customer.customer_activity.ongoing

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alcorp.oper.databinding.FragmentOngoingBinding

class OngoingFragment : Fragment() {

    private var _binding: FragmentOngoingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentOngoingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.cvItem.setOnClickListener {
            startActivity(Intent(context, DetailOngoingActivity::class.java))
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}