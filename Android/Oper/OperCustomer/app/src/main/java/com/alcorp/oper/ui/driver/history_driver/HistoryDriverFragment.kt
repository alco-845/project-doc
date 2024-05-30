package com.alcorp.oper.ui.driver.history_driver

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.alcorp.oper.databinding.FragmentHistoryDriverBinding
import com.alcorp.oper.ui.account.AccountActivity

class HistoryDriverFragment : Fragment() {

    private var _binding: FragmentHistoryDriverBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryDriverBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar.toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = "History"
        binding.toolbar.toolbar.setTitleTextColor(Color.WHITE)

        binding.toolbar.ivProfile.setOnClickListener {
            startActivity(Intent(context, AccountActivity::class.java))
        }

        binding.cvItem.setOnClickListener {
            startActivity(Intent(context, DetailHistoryDriverActivity::class.java))
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}