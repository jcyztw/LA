package com.example.uscclab.line_la;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by uscclab on 2018/6/10.
 */

public class SQLiteManager extends SQLiteOpenHelper {


    private final static int DB_VERSION = 1; // 資料庫版本
    private final static String DB_NAME = "LineLASQLite.db"; //資料庫名稱，附檔名為db
    private final static String INFO_TABLE = "info_table";
    private final static String ROW_ID = "rowId"; //欄位名稱

    private final static String NAME = "name"; //欄位名稱

    private final static String PHONE = "phone"; //欄位名稱

    public SQLiteManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
