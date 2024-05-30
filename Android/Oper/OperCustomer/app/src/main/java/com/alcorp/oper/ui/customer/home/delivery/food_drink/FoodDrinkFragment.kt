package com.alcorp.oper.ui.customer.home.delivery.food_drink

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alcorp.oper.databinding.FragmentFoodDrinkBinding

class FoodDrinkFragment : Fragment() {

    private var _binding: FragmentFoodDrinkBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodDrinkBinding.inflate(inflater, container, false)

        binding.itemFood1.root.setOnClickListener {
            startActivity(Intent(context, DetailFoodDrinkActivity::class.java))
        }

        binding.itemFood2.root.setOnClickListener {
            startActivity(Intent(context, DetailFoodDrinkActivity::class.java))
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}