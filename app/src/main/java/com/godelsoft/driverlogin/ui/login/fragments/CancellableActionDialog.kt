package com.godelsoft.driverlogin.ui.login.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class CancellableActionDialog(
    private val title: String,
    private val message: String,
    private val doActionText: String,
    private val cancelActionText: String,
    private val action: () -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton(doActionText) { _, _ -> action() }
                .setNegativeButton(cancelActionText) { _, _ -> }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}