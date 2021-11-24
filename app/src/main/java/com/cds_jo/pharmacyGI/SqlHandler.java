package com.cds_jo.pharmacyGI;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SqlHandler {

    @SuppressLint("SdCardPath")
    public static final String DATABASE_NAME = "PhDB.db";
   // public static final String DATABASE_NAME = "/mnt/sdcard/PhDB.db";
    public static final int DATABASE_VERSION =2;
    Context context;
    SQLiteDatabase sqlDatabase;
    SqlDbHelper dbHelper;

    public SqlHandler(Context context) {

        dbHelper = new SqlDbHelper(context, DATABASE_NAME, null, DATABASE_VERSION);

        //getApplicationContext().getExternalFilesDir("")

        try{
        sqlDatabase = dbHelper.getWritableDatabase();
         }
        catch (SQLException e){
        Toast.makeText(context.getApplicationContext(),"يرجى تفعيل الصلاحيات قبل تسجيل الدخول" + e.getMessage().toString(),Toast.LENGTH_SHORT ).show();
        }
    }

    public void executeQuery(String query) {
        try {

          /*  if (sqlDatabase.isOpen()) {
                sqlDatabase.close();
            }*/

            sqlDatabase = dbHelper.getWritableDatabase();
            sqlDatabase.execSQL(query);
            System.out.println("DATABASE Succ " + query);

            } catch (SQLException e) {
                System.out.println("DATABASE ERROR " + query + "  Error   " + e.getMessage().toString());
//            Toast.makeText(context.getApplicationContext(),"DATABASE ERROR " + query + "  Error   " + e.getMessage().toString(),Toast.LENGTH_SHORT ).show();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
            String currentDateandTime = sdf.format(new Date());


            SimpleDateFormat StartTime = new SimpleDateFormat("HH:mm:ss",Locale.ENGLISH);
            String StringTime = StartTime.format(new Date());


            try {
                File myFile = new File("/sdcard/Android/data/Galaxy/SQL_LOG.txt");
                if (!myFile.exists()) {
                    myFile.createNewFile();
                }
                FileOutputStream fOut = new FileOutputStream(myFile,true);
                OutputStreamWriter myOutWriter =
                        new OutputStreamWriter(fOut);
                myOutWriter.append( "\n\r"+ "--------------------START-----------------------------\r\n");
                myOutWriter.append(currentDateandTime+" "+ StringTime +" \r\n");
                myOutWriter.append("SQL :"+ query + "\n\r");
                myOutWriter.append("EEROR :"+ e.getMessage().toString() + "\n\r");
                myOutWriter.append("\n\r"+  "--------------------END------------------------------\r\n");
                myOutWriter.close();
                fOut.close();

            } catch (Exception ex) {

            }

            }


    }
    public long  Insert(String Table , String Col,ContentValues contentValues) {
        long i = -1 ;
        try {

           /* if (sqlDatabase.isOpen()) {
                sqlDatabase.close();
            }*/

            sqlDatabase = dbHelper.getWritableDatabase();
            i= sqlDatabase.insert(Table,null,contentValues);

        } catch (Exception e) {

            System.out.println("DATABASE ERROR" + contentValues);
        }
        return i ;
    }


    public long  Update(String Table ,ContentValues contentValues , String WhereStr) {
        long i = -1 ;
        try {

           /* if (sqlDatabase.isOpen()) {
                sqlDatabase.close();
            }
*/
            sqlDatabase = dbHelper.getWritableDatabase();
            i= sqlDatabase.update(Table, contentValues, WhereStr, null);

        } catch (Exception e) {

            System.out.println("DATABASE ERROR" + contentValues);
        }
        return i ;
    }


    public long  Delete(String Table  , String WhereStr) {
        long i = -1 ;
        try {

            /*if (sqlDatabase.isOpen()) {
                sqlDatabase.close();
            }*/

            sqlDatabase = dbHelper.getWritableDatabase();
            i= sqlDatabase.delete(Table, WhereStr, null);

        } catch (Exception e) {

            System.out.println("DATABASE ERROR" + WhereStr);
        }
        return i ;
    }

    public Cursor selectQuery(String query) {
        Cursor c1 = null;
        try {
/*
            if (sqlDatabase.isOpen()) {
                sqlDatabase.close();

            }*/
            sqlDatabase = dbHelper.getWritableDatabase();
            c1 = sqlDatabase.rawQuery(query, null);

        } catch (Exception e) {

            System.out.println("DATABASE ERROR" + query);

        }
        return c1;

    }

}