package io.github.takusan23.batterylifeview

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.*
import android.database.sqlite.SQLiteDatabase
import android.os.BatteryManager
import android.os.IBinder
import io.github.takusan23.batterylifeview.Database.BatterySQLiteHelper
import java.util.*
import kotlin.concurrent.timerTask
import android.content.res.Configuration
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.preference.PreferenceManager


class BatteryService : Service() {

    lateinit var batterySQLiteHelper: BatterySQLiteHelper
    lateinit var sqLiteDatabase: SQLiteDatabase
    lateinit var pref_setting: SharedPreferences
    val timer = Timer()
    lateinit var timerTask: TimerTask
    lateinit var batteryStatus: Intent
    lateinit var batteryManager: BatteryManager


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        //通知送信
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //通知チャンネル
        val id = "batter_service_notification"
        if (notificationManager.getNotificationChannel(id) == null) {
            val channel = NotificationChannel(
                id,
                getString(R.string.menu_battery_service),
                NotificationManager.IMPORTANCE_MIN
            )
            notificationManager.createNotificationChannel(channel)
        }
        //通知作成
        val notification = Notification.Builder(this, id)
            .setContentTitle(getString(R.string.menu_battery_service))
            .setContentText(getString(R.string.service_description))
            .setSmallIcon(R.drawable.ic_timeline_24px)
            .build()
        //送信
        startForeground(1, notification)

        //記録
        pref_setting = PreferenceManager.getDefaultSharedPreferences(this)
        startBatteryLevel()

        return START_NOT_STICKY
    }

    //電池残量記録
    private fun startBatteryLevel() {
        batterySQLiteHelper = BatterySQLiteHelper(this)
        sqLiteDatabase = batterySQLiteHelper.writableDatabase
        batterySQLiteHelper.setWriteAheadLoggingEnabled(false)

        //間隔
        var interval = pref_setting.getString("interval", "10")?.toLong() ?: 10
        interval *= 60000

        //定期実行
        timerTask = timerTask {
            //電池
            val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            batteryStatus = registerReceiver(null, intentFilter)!!
            //残量
            val level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            //充電中？
            val charge = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
            //ダークモード？
            val currentNightMode =
                resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            //時間
            val calendar = Calendar.getInstance()
            val unix = System.currentTimeMillis() / 1000L
            //データベースに追加
            val contentValues = ContentValues()
            contentValues.put("level", level)
            contentValues.put("charge", charge)
            contentValues.put("darkmode", currentNightMode)
            contentValues.put("year", calendar[Calendar.YEAR])
            contentValues.put("month", calendar[Calendar.MONTH])
            contentValues.put("date", calendar[Calendar.DATE])
            contentValues.put("hour", calendar[Calendar.HOUR_OF_DAY])
            contentValues.put("minute", calendar[Calendar.MINUTE])
            contentValues.put("unix", unix)
            sqLiteDatabase.insert("battery_db", null, contentValues)
        }
        timer.schedule(timerTask, interval, interval)
    }

    override fun onDestroy() {
        super.onDestroy()
        timerTask.cancel()
        timer.cancel()
    }

}