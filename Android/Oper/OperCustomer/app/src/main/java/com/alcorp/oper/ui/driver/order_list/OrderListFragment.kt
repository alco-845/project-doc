package com.alcorp.oper.ui.driver.order_list

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.alcorp.oper.databinding.FragmentOrderListBinding
import com.alcorp.oper.ui.account.AccountActivity

class OrderListFragment : Fragment() {

    private var _binding: FragmentOrderListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderListBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarOrderList.toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = "Order List"
        binding.toolbarOrderList.toolbar.setTitleTextColor(Color.WHITE)

        binding.toolbarOrderList.ivProfile.setOnClickListener {
            startActivity(Intent(context, AccountActivity::class.java))
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}