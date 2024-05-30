package com.alcorp.kolamin.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ContextThemeWrapper
import com.alcorp.kolamin.R

class LoadingDialog(private val context: Context) {
    private lateinit var dialog: Dialog

    fun showDialog() {
        dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_loading)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.create()
        dialog.show()
    }

    fun hideDialog() {
        dialog.dismiss()
    }
}