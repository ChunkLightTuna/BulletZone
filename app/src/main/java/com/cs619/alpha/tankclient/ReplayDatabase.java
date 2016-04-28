package com.cs619.alpha.tankclient;

/**
 * Created by Riddle on 4/26/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cs619.alpha.tankclient.util.GridWrapper;
import com.cs619.alpha.tankclient.util.GridWrapper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import org.androidannotations.annotations.Background;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.security.spec.ECField;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Glenn on 4/25/16.
 */
public class ReplayDatabase extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final String TAG = ReplayDatabase.class.getSimpleName();
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ReplayReader.db";
    private static final String TABLE_REPLAYS = "replays";
    private static final String KEY_ID = "id";
    private static final String KEY_TIME = "time";
    private static final String KEY_GRID = "grid";

    private static final String[] COLUMNS = {KEY_ID,KEY_TIME,KEY_GRID};

    public ReplayDatabase(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create replay table
//        String CREATE_REPLAY_TABLE = "CREATE TABLE replays ( " +
//                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "time TEXT, " +
//                "grid)";
        String CREATE_REPLAY_TABLE = "CREATE TABLE " + TABLE_REPLAYS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TIME + " TEXT,"
                + KEY_GRID + " TEXT" + ");";
        db.execSQL(CREATE_REPLAY_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older replay table if existed
        db.execSQL("DROP TABLE IF EXISTS replays");

        // create fresh replays table
        this.onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void addGrid( GridWrapper gw ){
        byte[] serialObj = null;

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(gw.getGrid());
            serialObj = baos.toByteArray();

        } catch (Exception e) {
            Log.e(TAG, "addGrid: ", e);
        }

        ContentValues values = new ContentValues();
        values.put(KEY_TIME, gw.getTimeStamp());
        values.put(KEY_GRID, serialObj);

        db.insert(TABLE_REPLAYS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        Log.d(TAG, "Added " + serialObj.toString() + " to " + gw.getTimeStamp() );

        db.close();
    }

    public List<int[][]> readGrid(){
        List<int[][]> gridList = new LinkedList<>();

        byte[] serialObj = null;

        String query = "SELECT  * FROM " + TABLE_REPLAYS;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        int[][] grid = null;
        if (cursor.moveToFirst()) {
            do {
                try{
                    serialObj = cursor.getBlob(cursor.getColumnIndex(KEY_GRID));
                    final ByteArrayInputStream bis = new ByteArrayInputStream(serialObj);
                    final ObjectInputStream ois = new ObjectInputStream(bis);
                    grid = (int[][]) ois.readObject();
                }catch( Exception e ){
                    Log.e(TAG, "readGrid: ", e );
                }
                gridList.add(grid);
            } while (cursor.moveToNext());
        }

        return gridList;
    }
}