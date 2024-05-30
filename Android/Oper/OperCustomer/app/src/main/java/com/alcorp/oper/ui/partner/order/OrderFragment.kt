package com.alcorp.oper.ui.partner.order

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.alcorp.oper.databinding.FragmentOrderBinding
import com.alcorp.oper.ui.account.AccountActivity

class OrderFragment : Fragment() {

    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarOrder.toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = "My Shop"
        binding.toolbarOrder.toolbar.setTitleTextColor(Color.WHITE)

        binding.toolbarOrder.ivProfile.setOnClickListener {
            startActivity(Intent(context, AccountActivity::class.java))
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}