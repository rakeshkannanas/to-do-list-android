package com.kannan.todohometask.Utils;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteFullException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class DatabaseHandler extends SQLiteOpenHelper {

    //change version number incremental if any change in DB schema.
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "TodoHomeTaskDB";
    // Message table name
    private static final String TABLE_DATA = "TodoData";



    // Data Table Columns names
    private static final String KEY_MSGID = "msg_id";
    private static final String KEY_ID = "ID";
    private static final String KEY_DATA = "DATA";
    private static final String KEY_DATE = "DATE";
    private static final String KEY_TIME = "TIME";



    public DatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {

        String CREATE_MSG_TABLE = "CREATE TABLE " + TABLE_DATA + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_DATA + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_TIME + " TEXT" + ")";
        Log.d("Query message Table..",CREATE_MSG_TABLE);

        db.execSQL(CREATE_MSG_TABLE);


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

    /**
     * Storing details in database
     * */
    public String addMsg(String tododata, String date, String time)
    {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_DATA, tododata); // senttime
            values.put(KEY_TIME, time);
            values.put(KEY_DATE, date);

            Log.d("Inserting Data Table..",values.toString());
            db.insert(TABLE_DATA, null, values);
            db.close();
        }
        catch(SQLiteFullException se)
        {
            Log.d("SQLiteFullException",se.toString());
            return "full";
        }
        catch(Exception e)
        {
            Log.d("Exception",e.toString());
            return "false";
        }
        return "true";

    }

    /**
     * Delete Data details in database
     **/
    public boolean delMsg(String id)
    {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_DATA, KEY_ID + " = ?",
                    new String[] { id });
            db.close();

            return true;
        }
        catch(Exception e)
        {
            Log.d("Exception",e.toString());
        }

        return false;
    }

    //get all tasks from the db.
    public String getAll()
    {
        String selectQuery = "SELECT * FROM " + TABLE_DATA;
        SQLiteDatabase db = this.getReadableDatabase();
        String Time="";
        String Data="";
        String ID="";
        String Date="";
        Integer countcheck = 0;
        Cursor cursor = db.rawQuery(selectQuery, null);
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        try
        {
            jsonArray = new JSONArray();
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                countcheck++;
                jsonObject = new JSONObject();
                Data=cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATA));
                Time=cursor.getString(cursor.getColumnIndexOrThrow(KEY_TIME));
                ID=cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID));
                Date=cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE));

                try {

                    jsonObject.put("Data", Data);
                    jsonObject.put("Time", Time);
                    jsonObject.put("Date", Date);
                    jsonObject.put("ID", ID);
                    jsonArray.put(jsonObject);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Log.d("uidcheck", ID);
                Log.d("datecheck", Date);

                cursor.moveToNext();
            }
            Log.d("countcheck", countcheck.toString());
        }
        catch(Exception ce)
        {
            ce.printStackTrace();
        }
        if (cursor != null && !cursor.isClosed())
        {
            cursor.close();
        }

        db.close();
        Log.d("DBvalue", jsonArray.toString());
        return jsonArray.toString();
    }

    // update data in the database
    public boolean updatedata(String id, String data)
    {
        boolean isit = false;
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_DATA, data);
            Log.d("Updating Table..",values.toString());
            if((db.update(TABLE_DATA, values, KEY_ID + " = ?",new String[] { id }))>0)
                isit = true;
            db.close();

        }
        catch(SQLiteFullException se)
        {
            Log.d("SQLiteFullException",se.toString());
        }
        catch(Exception e)
        {
            Log.d("Exception",e.toString());
        }
        return isit;

    }

    //to get stored data count from db.
    public Integer getcount()
    {
        String selectQuery = "SELECT COUNT(*) FROM " + TABLE_DATA;
        SQLiteDatabase db = this.getReadableDatabase();
        Integer count=null;
        Cursor cursor = db.rawQuery(selectQuery, null);
        try
        {
            // Move to first row
            if(cursor.moveToFirst())
            {
                count= cursor.getInt(0);
                cursor.close();
            }
        }
        catch(Exception ce)
        {
        }
        if (cursor != null && !cursor.isClosed())
        {
            cursor.close();
        }

        db.close();
        return count;
    }

}