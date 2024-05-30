package com.alcorp.oper.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.alcorp.oper.databinding.ActivityRegisBinding
import com.alcorp.oper.ui.customer.CustomerMainActivity
import com.alcorp.oper.ui.driver.DriverMainActivity
import com.alcorp.oper.ui.partner.PartnerMainActivity

class RegisActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisBinding
    private var stepperPosition = 0
    private var spPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.stepView.done(false)
        binding.itemAccountData.spRole.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    spPosition = position
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }

        binding.next.setOnClickListener {
            when (stepperPosition) {
                0 -> {
                    binding.itemAccountData.spRole.requestLayout()
                    binding.itemAccountData.itemAccountData.visibility = View.GONE

                    when (spPosition) {
                        0 -> binding.itemDataCustomer.itemDataCustomer.visibility = View.VISIBLE
                        1 -> binding.itemDataDriver.itemDataDriver.visibility = View.VISIBLE
                        2 -> binding.itemDataPartner.itemDataPartner.visibility = View.VISIBLE
                    }
                    stepperPosition = 1
                    binding.stepView.done(false)
                    binding.stepView.go(stepperPosition, true)
                    binding.next.text = "Next"
                    binding.back.visibility = View.VISIBLE
                }
                1 -> {
                    when (spPosition) {
                        0 -> binding.itemDataCustomer.itemDataCustomer.visibility = View.GONE
                        1 -> binding.itemDataDriver.itemDataDriver.visibility = View.GONE
                        2 -> binding.itemDataPartner.itemDataPartner.visibility = View.GONE
                    }
                    binding.itemDataAgreement.itemDataAgreement.visibility = View.VISIBLE
                    stepperPosition = 2
                    binding.stepView.done(false)
                    binding.stepView.go(stepperPosition, true)
                    binding.next.text = "Submit"
                }

                else -> {
                    when (spPosition) {
                        0 -> startActivity(Intent(this, CustomerMainActivity::class.java))
                        1 -> startActivity(Intent(this, DriverMainActivity::class.java))
                        2 -> startActivity(Intent(this, PartnerMainActivity::class.java))
                    }
                    finish()

                    stepperPosition = 0
                    binding.stepView.done(true)
                    binding.stepView.go(3, true)
                }
            }
        }

        binding.back.setOnClickListener {
            when (stepperPosition) {
                0 -> {
                    binding.back.visibility = View.GONE
                    onBackPressed()
                }

                1 -> {
                    when (spPosition) {
                        0 -> binding.itemDataCustomer.itemDataCustomer.visibility = View.GONE
                        1 -> binding.itemDataDriver.itemDataDriver.visibility = View.GONE
                        2 -> binding.itemDataPartner.itemDataPartner.visibility = View.GONE
                    }
                    binding.itemAccountData.itemAccountData.visibility = View.VISIBLE
                    stepperPosition = 0
                    binding.stepView.done(false)
                    binding.stepView.go(stepperPosition, true)
                    binding.back.visibility = View.GONE
                    binding.next.text = "Next"
                }

                else -> {
                    when (spPosition) {
                        0 -> binding.itemDataCustomer.itemDataCustomer.visibility = View.VISIBLE
                        1 -> binding.itemDataDriver.itemDataDriver.visibility = View.VISIBLE
                        2 -> binding.itemDataPartner.itemDataPartner.visibility = View.VISIBLE
                    }
                    binding.itemDataAgreement.itemDataAgreement.visibility = View.GONE
                    stepperPosition = 1
                    binding.stepView.done(false)
                    binding.stepView.go(stepperPosition, true)
                    binding.back.visibility = View.VISIBLE
                    binding.next.text = "Next"
                }
            }
        }
    }

}