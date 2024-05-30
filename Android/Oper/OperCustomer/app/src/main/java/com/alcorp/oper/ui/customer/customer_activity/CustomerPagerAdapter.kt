package com.alcorp.oper.ui.customer.customer_activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.alcorp.oper.ui.customer.customer_activity.history.HistoryCustomerFragment
import com.alcorp.oper.ui.customer.customer_activity.ongoing.OngoingFragment


class CustomerPagerAdapter(fm: FragmentManager, private val numberTabs: Int) :
    FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> OngoingFragment()
            1 -> HistoryCustomerFragment()
            else -> OngoingFragment()
        }
    }

    override fun getCount(): Int {
        return numberTabs
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> {
                return "On going"
            }
            1 -> {
                return "History"
            }
        }
        return super.getPageTitle(position)
    }
}
