package io.github.takusan23.batterylifeview.Fragment

import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import io.github.takusan23.batterylifeview.Adapter.BatteryRecyclerViewAdapter
import io.github.takusan23.batterylifeview.Database.BatterySQLiteHelper
import io.github.takusan23.batterylifeview.R
import kotlinx.android.synthetic.main.fragment_battery_life.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BatteryListFragment : Fragment() {

    lateinit var batterySQLiteHelper: BatterySQLiteHelper
    lateinit var sqLiteDatabase: SQLiteDatabase
    lateinit var batteryRecyclerViewAdapter: BatteryRecyclerViewAdapter
    val recyclerViewList = arrayListOf<ArrayList<String>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_battery_life, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        batterySQLiteHelper = BatterySQLiteHelper(context)
        sqLiteDatabase = batterySQLiteHelper.writableDatabase
        batterySQLiteHelper.setWriteAheadLoggingEnabled(false)

        //RecyclerView
        fragment_battery_life_recyclerview.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        fragment_battery_life_recyclerview.layoutManager = layoutManager
        batteryRecyclerViewAdapter = BatteryRecyclerViewAdapter(recyclerViewList)
        fragment_battery_life_recyclerview.adapter = batteryRecyclerViewAdapter

        //グラフ用意
        initLineChart()
        //データ読み込み
        loadData()

    }

    //RecyclerViewへ
    private fun loadData() {
        val calendar = Calendar.getInstance()
        val date = calendar[Calendar.DATE]
        val month = calendar[Calendar.MONTH]
        val year = calendar[Calendar.YEAR]
        val cursor = sqLiteDatabase.query(
            "battery_db",
            arrayOf("level", "darkmode", "charge", "unix"),
            "date=? AND month=? AND year=?",
            arrayOf(date.toString(), month.toString(), year.toString()),
            null,
            null,
            null
        )

        cursor.moveToFirst()

        //グラフ
        val grapthList = mutableListOf<Entry>()
        //グラフ横軸
        val yokoziku = mutableListOf<String>()

        for (i in 0 until cursor.count) {
            //RecyclerView追加
            val item = arrayListOf<String>()
            item.add("")
            item.add(cursor.getInt(0).toString())   //電池残量
            item.add(cursor.getInt(1).toString())   //ダークモード？
            item.add(cursor.getInt(2).toString())   //充電してる？
            item.add(cursor.getString(3))           //UNIX時間
            recyclerViewList.add(0, item)

            //折れ線グラフ追加
            val y = i.toFloat()
            //縦軸
            val pos = cursor.getInt(0).toFloat()
            grapthList.add(Entry(y, pos))
            //横軸
            val simpleDateFormat = SimpleDateFormat("HH:mm")
            val date = Date(cursor.getString(3).toLong() * 1000)
            yokoziku.add(simpleDateFormat.format(date))

            cursor.moveToNext()
        }
        //とじる
        cursor.close()

        //更新
        batteryRecyclerViewAdapter.notifyDataSetChanged()

        //グラフ
        val line = LineDataSet(grapthList, getString(R.string.menu_battery_life)).apply {
            axisDependency = YAxis.AxisDependency.LEFT
            color = Color.BLACK
            highLightColor = Color.RED
            setDrawCircles(false)
            setDrawCircleHole(false)
            setDrawValues(false)
            lineWidth = 2f
        }
        val data = LineData(line)
        data.setValueTextColor(Color.BLACK)
        data.setValueTextSize(9f)
        fragment_battery_linechart.data = data
        //横軸
        fragment_battery_linechart.xAxis.valueFormatter =
            IndexAxisValueFormatter(yokoziku)
        //更新＋再描画
        fragment_battery_linechart.notifyDataSetChanged()
        fragment_battery_linechart.invalidate()
    }

    private fun initLineChart() {
        //グラフの説明。右下に出る
        val description = Description()
        description.text = getString(R.string.menu_battery_life)
        fragment_battery_linechart.description = description
/*
        //ハイライト機能
        fragment_battery_linechart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onNothingSelected() {

            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                //選択時
            }
        })
*/
    }

}