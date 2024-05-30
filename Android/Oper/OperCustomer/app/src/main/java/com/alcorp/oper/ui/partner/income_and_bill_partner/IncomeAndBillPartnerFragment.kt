package com.alcorp.oper.ui.partner.income_and_bill_partner

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.alcorp.oper.databinding.FragmentIncomeAndBillPartnerBinding
import com.alcorp.oper.ui.account.AccountActivity

class IncomeAndBillPartnerFragment : Fragment() {

    private var _binding: FragmentIncomeAndBillPartnerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIncomeAndBillPartnerBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarIncomeBill.toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = "Income & Bill"
        binding.toolbarIncomeBill.toolbar.setTitleTextColor(Color.WHITE)

        binding.toolbarIncomeBill.ivProfile.setOnClickListener {
            startActivity(Intent(context, AccountActivity::class.java))
        }

        val partnerPagerAdapter =
            PartnerPagerAdapter(childFragmentManager, binding.tabLayout.tabCount)
        binding.pager.adapter = partnerPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.pager)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}