package com.alcorp.oper.ui.driver.income_and_bill_driver

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.alcorp.oper.databinding.FragmentIncomeAndBillDriverBinding
import com.alcorp.oper.ui.account.AccountActivity
import com.alcorp.oper.ui.customer.customer_activity.CustomerPagerAdapter

class IncomeAndBillDriverFragment : Fragment() {

    private var _binding: FragmentIncomeAndBillDriverBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIncomeAndBillDriverBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarIncomeBill.toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = "Income & Bill"
        binding.toolbarIncomeBill.toolbar.setTitleTextColor(Color.WHITE)

        binding.toolbarIncomeBill.ivProfile.setOnClickListener {
            startActivity(Intent(context, AccountActivity::class.java))
        }

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