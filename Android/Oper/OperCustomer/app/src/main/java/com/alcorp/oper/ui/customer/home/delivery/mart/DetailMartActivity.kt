package com.alcorp.oper.ui.customer.home.delivery.mart

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.alcorp.oper.R
import com.alcorp.oper.databinding.ActivityDetailMartBinding
import com.alcorp.oper.ui.customer.home.delivery.AddOrderActivity
import com.google.android.material.bottomsheet.BottomSheetDialog

class DetailMartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailMartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.btnAdd.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)
            val btnClose = view.findViewById<Button>(R.id.idBtnDismiss)

            btnClose.setOnClickListener {
                dialog.dismiss()
            }

            dialog.setCancelable(true)
            dialog.setContentView(view)
            dialog.show()
        }

        binding.btnOrder.setOnClickListener {
            startActivity(Intent(this, AddOrderActivity::class.java))
        }
    }
}