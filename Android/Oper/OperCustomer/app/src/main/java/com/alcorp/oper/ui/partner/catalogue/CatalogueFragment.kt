package com.alcorp.oper.ui.partner.catalogue

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.alcorp.oper.databinding.FragmentCatalogueBinding
import com.alcorp.oper.ui.account.AccountActivity

class CatalogueFragment : Fragment() {

    private var _binding: FragmentCatalogueBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatalogueBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarCatalogue.toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = "My Catalogue"
        binding.toolbarCatalogue.toolbar.setTitleTextColor(Color.WHITE)

        binding.toolbarCatalogue.ivProfile.setOnClickListener {
            startActivity(Intent(context, AccountActivity::class.java))
        }

        binding.btnAdd.setOnClickListener {
            startActivity(Intent(context, AddProductActivity::class.java))
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}