package io.github.takusan23.batterylifeview.Database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BatterySQLiteHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    override fun onCreate(db: SQLiteDatabase) {
        println(SQL_CREATE_ENTRIES)
        // テーブル作成
        // SQLiteファイルがなければSQLiteファイルが作成される
        db.execSQL(
            SQL_CREATE_ENTRIES
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // アップデートの判別
        db.execSQL(
            SQL_DELETE_ENTRIES
        )
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        // データーベースのバージョン
        private val DATABASE_VERSION = 1

        // データーベース名
        private val DATABASE_NAME = "Battery.db"
        private val TABLE_NAME = "battery_db"
        private val MEMO = "memo"   //なんか
        private val LEVEL = "level" //電池残量
        private val CHARGE = "charge" //充電してるか 0:放電 1:AC 2:USB 3:不明 4:WirelessCharge
        private val DARK = "darkmode" //ダークモード？
        private val _ID = "_id"
        //こ↑こ↓から日付時刻だから・・・
        private val YEAR = "year"
        private val MONTH = "month"
        private val DATE = "date"
        private val HOUR = "hour"
        private val MINUTE = "minute"
        private val UNIX = "unix"

        // , を付け忘れるとエラー
        private val SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                LEVEL + " INTEGER ," +
                CHARGE + " INTEGER ," +
                DARK + " INTEGER ," +
                YEAR + " INTEGER ," +
                MONTH + " INTEGER ," +
                DATE + " INTEGER ," +
                HOUR + " INTEGER ," +
                MINUTE + " INTEGER ," +
                UNIX + " TEXT ," +
                MEMO + " TEXT" +
                ")"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}