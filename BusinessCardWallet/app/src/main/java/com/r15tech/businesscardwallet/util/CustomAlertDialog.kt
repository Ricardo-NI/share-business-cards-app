package com.r15tech.businesscardwallet.util

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AlertDialog
import com.r15tech.businesscardwallet.R


class CustomAlertDialog(private val context: Context) {


    fun alertDialog1(message: String) {

        val dlg = AlertDialog.Builder(context)
        dlg.setTitle(R.string.tx_notice)
        val resIcon = if(isDarkModeOn()){
            R.drawable.ic_warning_white
        }else{
            R.drawable.ic_warning
        }
        dlg.setIcon(resIcon)
        dlg.setMessage(message)
        dlg.setPositiveButton(R.string.ok) { _, _ -> }
        dlg.show()
    }

    fun alertDialogInfo( message: String) {

        val dlg = AlertDialog.Builder(context)
        dlg.setTitle(R.string.information)
        val resIcon = if(isDarkModeOn()){
            R.drawable.ic_info_white
        }else{
            R.drawable.ic_info
        }
        dlg.setIcon(resIcon)
        dlg.setMessage(message)
        dlg.setPositiveButton(R.string.ok) { _, _ -> }
        dlg.show()
    }

    fun alertDialogSimilarColors(function: () -> (Unit)) {
        val dlg = AlertDialog.Builder(context)
        dlg.setTitle(R.string.tx_notice)
        val resIcon = if(isDarkModeOn()){
            R.drawable.ic_warning_white
        }else{
            R.drawable.ic_warning
        }
        dlg.setIcon(resIcon)
        dlg.setMessage(R.string.msg_dlg_cores)
        dlg.setPositiveButton(R.string.save_like_this) { _, _ ->
            function()
        }
        dlg.setNegativeButton(R.string.cancel) { _, _ -> }
        dlg.show()
    }

    private fun isDarkModeOn(): Boolean {
        val currentNightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

}

