package io.github.takusan23.batterylifeview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import io.github.takusan23.batterylifeview.Fragment.BatteryListFragment
import io.github.takusan23.batterylifeview.Fragment.BatteryServiceFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //電池履歴
        val trans = supportFragmentManager.beginTransaction()
        trans.replace(R.id.main_activity_fragment, BatteryListFragment())
        trans.commit()


        main_activity_bottom_nav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_battery_life -> {
                    //電池履歴
                    val trans = supportFragmentManager.beginTransaction()
                    trans.replace(R.id.main_activity_fragment, BatteryListFragment())
                    trans.commit()
                }
                R.id.menu_battery_service -> {
                    //電池記録サービス
                    val trans = supportFragmentManager.beginTransaction()
                    trans.replace(R.id.main_activity_fragment, BatteryServiceFragment())
                    trans.commit()
                }
            }
            true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_licence -> {
                //ライセンス画面に
                val intent = Intent(this, LicenceActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
