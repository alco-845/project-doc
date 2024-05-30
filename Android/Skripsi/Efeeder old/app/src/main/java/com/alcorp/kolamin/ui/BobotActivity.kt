package com.alcorp.kolamin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.alcorp.kolamin.R
import com.alcorp.kolamin.databinding.ActivityBobotBinding
import com.alcorp.kolamin.utils.LoadingDialog
import com.alcorp.kolamin.viewmodel.BobotViewModel
import com.alcorp.kolamin.viewmodel.MonitoringViewModel
import com.alcorp.kolamin.viewmodel.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BobotActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBobotBinding
    private lateinit var bobotViewModel: BobotViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBobotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        setupViewModel()
        init()
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance()
        bobotViewModel = ViewModelProvider(this, factory)[BobotViewModel::class.java]
    }

    private fun setupViewText() {
        bobotViewModel.getDataKapasitas().observe(this){ binding.tvKapasitas.text = it }
        bobotViewModel.getDataBeratTimbangan().observe(this){ binding.tvBeratTimbangan.text = it }
        bobotViewModel.getDataBatasKapasitas().observe(this){ binding.edtBatasKapasitas.setText(it) }
    }

    private fun init() {
        supportActionBar?.title = getString(R.string.toolbar_bobot)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val loadingDialog = LoadingDialog(this)

        lifecycleScope.launch {
            loadingDialog.showDialog()
            delay(1000)

            withContext(Dispatchers.Main) {
                setupViewText()
                loadingDialog.hideDialog()
            }
        }

        binding.ivBatasKapasitas.setOnClickListener {
            val kapasitas = binding.edtBatasKapasitas.text.toString()

            if (kapasitas == "") {
                Toast.makeText(this, getString(R.string.toast_batas_kapasitas), Toast.LENGTH_SHORT).show()
            } else {
                if (kapasitas.toInt() > 100 || kapasitas.toInt() < 0) {
                    Toast.makeText(this, getString(R.string.toast_warning_batas_kapasitas), Toast.LENGTH_SHORT).show()
                } else {
                    bobotViewModel.setDataBatasKapasitas(kapasitas.toInt())
                    Toast.makeText(this, getString(R.string.toast_berhasil), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}