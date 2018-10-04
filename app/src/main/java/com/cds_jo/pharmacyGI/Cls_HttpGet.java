package com.cds_jo.pharmacyGI;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;

        import org.apache.http.HttpEntity;
        import org.apache.http.HttpResponse;
        import org.apache.http.client.HttpClient;
        import org.apache.http.client.methods.HttpGet;
        import org.apache.http.impl.client.DefaultHttpClient;

        import android.os.AsyncTask;

public class Cls_HttpGet   extends AsyncTask<String, Void, String> {
    String lat , Long ,R;
    String result = "fail";

    @Override
    protected String doInBackground(String... params) {
        // TODO Auto-generated method stub
        lat = params[0];
        Long = params[1];
        return GetSomething(lat,Long);
    }

    final String GetSomething(String lat , String Lon)
    {
        String url = "http://maps.googleapis.com/maps/api/elevation/"
                + "xml?locations=" + String.valueOf(lat)
                + "," + String.valueOf(Lon)
                + "&sensor=true";


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
                String tagOpen = "<elevation>";
                String tagClose = "</elevation>";
                if (respStr.indexOf(tagOpen) != -1) {
                    int start = respStr.indexOf(tagOpen) + tagOpen.length();
                    int end = respStr.indexOf(tagClose);
                    String value = respStr.substring(start, end);
                    result = (String.valueOf(Double.parseDouble(value)*3.2808399)); // convert from meters to feet
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
