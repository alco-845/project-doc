package com.alcorp.kolamin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.alcorp.kolamin.R
import com.alcorp.kolamin.databinding.ActivityMonitoringBinding
import com.alcorp.kolamin.utils.LoadingDialog
import com.alcorp.kolamin.viewmodel.MonitoringViewModel
import com.alcorp.kolamin.viewmodel.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MonitoringActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMonitoringBinding
    private lateinit var monitoringViewModel: MonitoringViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonitoringBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        setupViewModel()
        init()
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance()
        monitoringViewModel = ViewModelProvider(this, factory)[MonitoringViewModel::class.java]
    }

    private fun setupViewText() {
        monitoringViewModel.getDataBatasSuhu().observe(this){ binding.edtBatasSuhu.setText(it) }
        monitoringViewModel.getDataSuhu().observe(this){ binding.tvSuhu.text = "$it ° C" }
        monitoringViewModel.getDataOxygen().observe(this){ binding.tvOxygen.text = "$it PPM" }
        monitoringViewModel.getDataPh().observe(this){ binding.tvPh.text = it }
        monitoringViewModel.getDataSwitch().observe(this){ binding.swSuhu.isChecked = it == 1 }
    }

    private fun init() {
        supportActionBar?.title = getString(R.string.toolbar_monitoring)
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

        binding.ivBatasSuhu.setOnClickListener {
            val suhu = binding.edtBatasSuhu.text.toString()

            if (suhu == "") {
                Toast.makeText(this, getString(R.string.toast_batas_suhu), Toast.LENGTH_SHORT).show()
            } else {
                if (suhu.toInt() > 125 || suhu.toInt() < -55) {
                    Toast.makeText(this, getString(R.string.toast_warning_batas_suhu), Toast.LENGTH_SHORT).show()
                } else {
                    monitoringViewModel.setDataBatasSuhu(suhu.toInt())
                    Toast.makeText(this, getString(R.string.toast_berhasil), Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.swSuhu.setOnCheckedChangeListener { _, isChecked ->
            val data = if (isChecked) 1 else 0
            val status = if (isChecked) resources.getString(R.string.txt_status_on) else resources.getString(R.string.txt_status_off)

            binding.txtStatus.text = status
            monitoringViewModel.setDataSwitch(data)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}