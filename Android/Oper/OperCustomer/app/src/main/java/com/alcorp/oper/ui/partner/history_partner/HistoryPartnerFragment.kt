package com.alcorp.oper.ui.partner.history_partner

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.alcorp.oper.databinding.FragmentHistoryPartnerBinding
import com.alcorp.oper.ui.account.AccountActivity

class HistoryPartnerFragment : Fragment() {

    private var _binding: FragmentHistoryPartnerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryPartnerBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarHistory.toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = "History"
        binding.toolbarHistory.toolbar.setTitleTextColor(Color.WHITE)

        binding.toolbarHistory.ivProfile.setOnClickListener {
            startActivity(Intent(context, AccountActivity::class.java))
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}