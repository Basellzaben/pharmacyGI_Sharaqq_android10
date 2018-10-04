package com.cds_jo.pharmacyGI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Hp on 20/06/2016.
 */
public  class WriteTxtFile {

    public   static void   MakeText( String Title,String Str ){

        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
            String currentDateandTime = sdf.format(new Date());


            SimpleDateFormat StartTime = new SimpleDateFormat("HH:mm:ss",Locale.ENGLISH);
            String StringTime = StartTime.format(new Date());




            File myFile = new File("/sdcard/Android/data/Galaxy/SQL_LOG.txt");
            if (!myFile.exists()) {
                myFile.createNewFile();
            }
            FileOutputStream fOut = new FileOutputStream(myFile,true);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            myOutWriter.append("\n\r"+ "--------------------START-----------------------------\r\n");
            myOutWriter.append(currentDateandTime+" "+ StringTime +" \r\n");
            myOutWriter.append(Title +"\n\r");
            myOutWriter.append("EEROR :"+ Str + "\n\r");
            myOutWriter.append("\n\r"+ "---------------------END------------------------------\r\n");
            myOutWriter.close();
            fOut.close();

        } catch (Exception e) {

        }

    }
}
