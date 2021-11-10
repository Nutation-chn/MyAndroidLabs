package algonquin.cst2335.zhan0703;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.material.tabs.TabLayout;


public class MyOpenHelper extends SQLiteOpenHelper {

    public static final String name = "TheDatabase"; //name of database
    public static final int version =1;         //database version
    public static final String TABLE_NAME = "Messages";
    public static final String col_message="Message";
    public static final String col_send_receive = "SendOrReceive";
    public static final String col_time_sent = "TimeSent";
    public MyOpenHelper( Context context) {
        super(context, name, null, version);
    }

    //create database to store chat messages
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table " + TABLE_NAME+"(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            +col_message+" TEXT,"
                +col_send_receive+" INTEGER,"
                /* more columns here: */
                +col_time_sent+" Text);");
    }

    //delete current database and create a new one
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        this.onCreate(db);
    }
}
