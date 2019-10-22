package io.github.takusan23.batterylifeview.Adapter

import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.takusan23.batterylifeview.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BatteryRecyclerViewAdapter(var list: ArrayList<ArrayList<String>>) :
    RecyclerView.Adapter<BatteryRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_battery, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.darkModeTextView.context
        val item = list[position]
        val level = item[1] + "%"
        val darkmode = item[2]
        val charge = item[3]
        val date = item[4]
        //TextViewへ
        holder.batteryLevelTextView.text = level
        holder.darkModeTextView.text = getDarkmode(darkmode, context)
        holder.chargeTextView.text = getCharging(charge, context)
        holder.dateTextView.text = getDate(date)
    }

    private fun getDate(unix: String): CharSequence? {
        val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val date = Date(unix.toLong()*1000)
        return simpleDateFormat.format(date)
    }

    private fun getDarkmode(darkmode: String, context: Context): CharSequence? {
        when (darkmode.toInt()) {
            Configuration.UI_MODE_NIGHT_NO -> {
                return context.getString(R.string.lite_theme)
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                return context.getString(R.string.darkmode)
            }
        }
        return context.getString(R.string.lite_theme)
    }

    private fun getCharging(charge: String, context: Context): CharSequence? {
        val list = arrayListOf<String>(
            context.getString(R.string.charge_no),
            context.getString(R.string.charge_ac),
            context.getString(R.string.charge_usb),
            context.getString(R.string.charge_error),
            context.getString(R.string.charge_wireless)
        )
        return list[charge.toInt()]
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var batteryLevelTextView =
            itemView.findViewById<TextView>(R.id.adapter_battery_level_textview)
        var darkModeTextView = itemView.findViewById<TextView>(R.id.adapter_darkmode_textview)
        var chargeTextView = itemView.findViewById<TextView>(R.id.adapter_charge_textview)
        var dateTextView = itemView.findViewById<TextView>(R.id.adapter_date_textview)
    }

}