package com.cds_jo.pharmacyGI;



import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Cls_HttpGet_Map2 extends AsyncTask<String, Void, String[]> {
    String lat , Long ,R;
    String  []result ;

    @Override
    protected String[] doInBackground(String... params) {
        // TODO Auto-generated method stub
        lat = params[0];
        Long = params[1];
        return GetSomething(params[0],params[1],params[2],params[3]);
    }

    final String[] GetSomething(String origin_lat , String origin_Lon,String dest_lat , String dest_Lon  )
    {

        result=new String[10];
        String  url = "http://maps.googleapis.com/maps/api/directions/xml?"
                + "origin=" +origin_lat  + "," + origin_Lon
                + "&destination=" + dest_lat + "," + dest_Lon
                + "&sensor=false&units=metric&mode=walking" ; //direction="walking" or "driving"


        BufferedReader inStream = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpRequest = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpRequest);
           /* inStream = new BufferedReader(
                    new InputStreamReader(
                            response.getEntity().getContent()));*/

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                inStream = new BufferedReader(
                        new InputStreamReader(
                                response.getEntity().getContent()));
                int r = -1;
                StringBuffer respStr = new StringBuffer();
                while ((r = inStream.read()) != -1)
                    respStr.append((char) r);

                String tagOpen = "<start_address>";
                String tagClose = "</start_address>";
                if (respStr.indexOf(tagOpen) != -1) {
                    int start = respStr.indexOf(tagOpen) + tagOpen.length();
                    int end = respStr.indexOf(tagClose);
                    String value = respStr.substring(start, end);
                    result[0] = value ;
                }


                tagOpen = "<end_address>";
                tagClose = "</end_address>";
                if (respStr.indexOf(tagOpen) != -1) {
                    int start = respStr.indexOf(tagOpen) + tagOpen.length();
                    int end = respStr.indexOf(tagClose);
                    String value = respStr.substring(start, end);
                    result[1] = value ;
                }

                tagOpen = "<maneuver>";
                tagClose = "</maneuver>";
                if (respStr.indexOf(tagOpen) != -1) {
                    int start = respStr.indexOf(tagOpen) + tagOpen.length();
                    int end = respStr.indexOf(tagClose);
                    String value = respStr.substring(start, end);
                    result[2] = value ;
                }


                tagOpen = "<distance>";
                tagClose = "</distance>";
                if (respStr.indexOf(tagOpen) != -1) {
                    int start = respStr.indexOf(tagOpen) + tagOpen.length();
                    int end = respStr.indexOf(tagClose);
                    String value = respStr.substring(start, end);
                    tagOpen = "<value>";
                    tagClose = "</value>";
                    start = value.indexOf(tagOpen) + tagOpen.length();
                    end = value.indexOf(tagClose);
                    value = value.substring(start, end);
                    result[3] = value ;
                }

                tagOpen = "<maneuver>";
                tagClose = "</maneuver>";
                if (respStr.indexOf(tagOpen) != -1) {
                    int start = respStr.indexOf(tagOpen) + tagOpen.length();
                    int end = respStr.indexOf(tagClose);
                    String value = respStr.substring(start, end);
                    result[4] = value ;
                }

                inStream.close();
            }

           /* StringBuffer buffer = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = inStream.readLine()) != null) {
                buffer.append(line + NL);
            }
            inStream.close();

            result = buffer.toString();
       */ } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    protected void onPostExecute(String page)
    {
        R=(page);
    }
}
