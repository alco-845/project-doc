package com.alcorp.oper.ui.customer

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.alcorp.oper.databinding.FragmentAccountBinding
import com.alcorp.oper.ui.account.*

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarSmall.toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = "My Account"
        binding.toolbarSmall.toolbar.setTitleTextColor(Color.WHITE)

        binding.ivEdit.setOnClickListener {
            startActivity(Intent(context, EditProfileActivity::class.java))
        }

        binding.tvFaq.setOnClickListener {
            startActivity(Intent(context, FaqActivity::class.java))
        }

        binding.tvLanguage.setOnClickListener {
            startActivity(Intent(context, LanguageActivity::class.java))
        }

        binding.tvBank.setOnClickListener {
            startActivity(Intent(context, EditBankActivity::class.java))
        }

        binding.tvNotification.setOnClickListener {
            startActivity(Intent(context, NotificationActivity::class.java))
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}