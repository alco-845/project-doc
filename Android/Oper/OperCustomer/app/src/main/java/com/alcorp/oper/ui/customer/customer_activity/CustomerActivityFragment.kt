package com.alcorp.oper.ui.customer.customer_activity

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.alcorp.oper.databinding.FragmentCustomerActivityBinding

class CustomerActivityFragment : Fragment() {

    private var _binding: FragmentCustomerActivityBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCustomerActivityBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar.toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = "Activity"
        binding.toolbar.toolbar.setTitleTextColor(Color.WHITE)

        val customerPagerAdapter =
            CustomerPagerAdapter(childFragmentManager, binding.tabLayout.tabCount)
        binding.pager.adapter = customerPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.pager)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}