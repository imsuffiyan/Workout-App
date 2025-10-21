package com.fitness.workout.ui.common

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.fitness.workout.R

object DialogUtils {
    fun showEditDialog(fragment: Fragment, title: String, currentValue: String, inputType: Int, onSave: (String) -> Unit) {
        val inflater = fragment.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_edit, null)
        val et = dialogView.findViewById<EditText>(R.id.etEditValue)
        val tvTitle = dialogView.findViewById<TextView>(R.id.tvEditTitle)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnEditCancel)
        val btnSave = dialogView.findViewById<Button>(R.id.btnEditSave)

        tvTitle.text = title
        et.setText(currentValue)
        et.inputType = inputType

        val builder = AlertDialog.Builder(fragment.requireContext())
        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        btnCancel.setOnClickListener { dialog.dismiss() }
        btnSave.setOnClickListener {
            onSave(et.text.toString())
            dialog.dismiss()
        }

        dialog.show()
        et.requestFocus()
        val imm = fragment.requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT)
    }
}

