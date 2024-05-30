package com.alcorp.oper.ui.partner.income_and_bill_partner

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class PartnerPagerAdapter(fm: FragmentManager, private val numberTabs: Int) :
    FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> IncomePartnerFragment()
            1 -> BillPartnerFragment()
            else -> IncomePartnerFragment()
        }
    }

    override fun getCount(): Int {
        return numberTabs
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> {
                return "Income"
            }
            1 -> {
                return "Bill"
            }
        }
        return super.getPageTitle(position)
    }
}
