package com.cds_jo.pharmacyGI;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by Hp on 20/06/2016.
 */
public  class DB {

    public  static String GetValue(Context _context,String Tbl ,String ClnNam ,String WhereStr){
        SqlHandler sqlHandler = new SqlHandler(_context);
        String R = "-1";
        String q = "";
        q= "Select ifnull ("+ClnNam+",'')  as v From " +  Tbl + "    Where "+ WhereStr;
        Cursor c= sqlHandler.selectQuery(q);
        try {
            if(c!=null && c.getCount() >0){
                c.moveToFirst();
                R=c.getString(c.getColumnIndex("v"));
              c.close();
            }
        }
        catch ( Exception ex){
            R="-1";
        }



        return R;

    }
}
