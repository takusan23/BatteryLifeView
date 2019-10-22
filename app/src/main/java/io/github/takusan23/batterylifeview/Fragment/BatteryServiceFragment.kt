package io.github.takusan23.batterylifeview.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import io.github.takusan23.batterylifeview.BatteryService
import io.github.takusan23.batterylifeview.R
import kotlinx.android.synthetic.main.fragment_battery_service.*

class BatteryServiceFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_battery_service, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref_setting = PreferenceManager.getDefaultSharedPreferences(context)

        //サービス開始
        fragment_service_start_button.setOnClickListener {
            activity?.startForegroundService(Intent(context, BatteryService::class.java))
        }

        //サービス終了
        fragment_service_stop_button.setOnClickListener {
            activity?.stopService(Intent(context, BatteryService::class.java))
        }

        //間隔取得
        fragment_service_text_interval.setText(pref_setting.getString("interval", "10"))
        //間隔保存
        fragment_service_text_interval_save_button.setOnClickListener {
            //保存
            val interval = fragment_service_text_interval.text.toString()
            val editor = pref_setting.edit()
            editor.putString("interval", interval)
            editor.apply()
        }

    }

}