package com.cds_jo.pharmacyGI;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.pharmacyGI.assist.CallWebServices;
import com.cds_jo.pharmacyGI.assist.Customer_List;
import com.cds_jo.pharmacyGI.assist.Customers;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class Select_Customer_Dis extends DialogFragment implements View.OnClickListener  {
    View form ;
    Button add,cancel;
    ListView items_Lsit;
    TextView itemnm;
    private SearchView mSearchView;
    EditText filter   ;
    String  dis ="";
    ImageButton btn_filter_search ,RefillList;
    Button btn_refill ;
    @Override
    public View onCreateView( final LayoutInflater inflater   , ViewGroup container  ,Bundle savestate) {

        form = inflater.inflate(R.layout.activity_select__customer_dis, container, false);
         dis = DB.GetValue(this.getActivity(), "ComanyInfo", "GPSAccurent", "1=1 ");
        String  str = "اختيار العملاء حسب المسافة ";
        str = str + "(";
        str = str + " اقل من ";
        str = str +dis ;
        str = str + "متر تقريبا ";
        str = str + " ) ";
        getDialog().setTitle(  str);



        btn_refill=(Button) form.findViewById(R.id.btn_refill);
        filter =    (EditText) form.findViewById(R.id.et_Search_filter);


        btn_filter_search =(ImageButton) form.findViewById(R.id.brn_seachAcc);


        btn_refill.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                onProgressUpdate(filter.getText().toString());
            }
        });

        filter =    (EditText) form.findViewById(R.id.et_Search_filter);



        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                  onProgressUpdate(s.toString());

            }
        });
        onProgressUpdate("");

        btn_filter_search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                onProgressUpdate(filter.getText().toString());
            }
        });

        items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Customers customers = (Customers) arg0.getItemAtPosition(position);
                String nm = customers.getNm();
                Exist_Pop();

                ((MainActivity) getActivity()).Set_Cust(customers.getAcc(), customers.getNm());
             /*   if (getArguments().getString("Scr") == "SaleRound") {
                    ((SaleManRoundsActivity) getActivity()).Set_Cust(customers.getAcc(), customers.getNm());

                }
                else if (getArguments().getString("Scr") == "Gps") {
                    ((MainActivity) getActivity()).Set_Cust(customers.getAcc(), customers.getNm());

                }*/




            }


        });
        this.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return  form;
    }


    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);

        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search Here");
    }

    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            Toast.makeText(getActivity(),newText,Toast.LENGTH_SHORT).show();

            //mListView.clearTextFilter();
        } else {
            Toast.makeText(getActivity(),newText,Toast.LENGTH_SHORT).show();


        }
        return true;
    }

    public boolean onQueryTextSubmit(String query) {

        Thread rant = new Thread() {
            public void run() {
                CallWebServices ws = new CallWebServices(getActivity());
                ws.GetAccNo("s", "s");
            }
        };
        rant.start();
        try {
            rant.join();
            onProgressUpdate(We_Result.Msg);

        } catch (Exception ex) {

        }
        return false;
    }
    @SuppressLint("Range")
    public void onProgressUpdate(String t ){


        final List<String> items_ls = new ArrayList<String>();
        SqlHandler sqlHandler = new SqlHandler(getActivity());
        items_Lsit=(ListView) form.findViewById(R.id.listView2);
        items_Lsit.setAdapter(null);
        String query ,q ;

        GPSService mGPSService = new GPSService(getActivity());
        mGPSService.getLocation();

        float[] results = new float[1];
        int Dis ;

        Calendar c = Calendar.getInstance();
        final int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        q="-1";



        if (dayOfWeek== 7 )
            q = " sat = '1' ";

        else  if (dayOfWeek == 1 )
            q = " sun = '1' ";

        else  if (dayOfWeek == 2 )
            q = " mon = '1' ";


        else if (dayOfWeek == 3 )
            q = " tues = '1' ";

        else  if (dayOfWeek == 4 )
            q = " wens = '1' ";

        else  if  (dayOfWeek == 5 )
            q = " thurs = '1' ";


        if (t.toString().equals("")){
              query = "   Select DISTINCT  no,name ,Address , Note2 ,'' as Mobile  ,ifnull(Latitude,0) as Latitude   , ifnull(Longitude,0) as Longitude" +
                      "   ,ifnull(barCode,-1)  as barCode  from Customers where  " + q;
        }
        else {
            query = "   Select DISTINCT  no,name ,Address , Note2 ,'' as Mobile  ,ifnull(Latitude,0) as Latitude   , ifnull(Longitude,0) as Longitude " +
                    ",ifnull(barCode,-1)  as barCode from Customers where name like '%" + t + "%' or  no like '%" + t + "%'  and " + q;
        }


        String[] r;
        r = new String[10];

        String D1 = "" ;
        Location selected_location;
        Cursor c1 = sqlHandler.selectQuery(query);
        ArrayList<Customers> customersesList = new ArrayList<Customers>();
        if (c1!=null && c1.getCount()!=0 ){
            if(c1.moveToFirst()){
             do{
                 Customers     customers = new Customers();


                 customers.setAcc(c1.getString(c1.getColumnIndex("no")));
                 customers.setNm(c1.getString(c1.getColumnIndex("name")));


                   selected_location=new Location("locationA");
                 if (c1.getString(c1.getColumnIndex("Latitude")).equals("")){
                     selected_location.setLatitude(0.0);
                 }
                 else
                 {
                     selected_location.setLatitude(Double.parseDouble(c1.getString(c1.getColumnIndex("Latitude"))));
                 }
                 if (c1.getString(c1.getColumnIndex("Longitude")).equals("")){
                     selected_location.setLongitude(0.0);
                 }
                 else
                 {
                     selected_location.setLongitude(Double.parseDouble(c1.getString(c1.getColumnIndex("Longitude"))));
                 }


                 String tv_x = "";
                 String tv_y ="";
                 Location near_locations=new Location("locationA");

                 try {
                     // tv_x = (String.valueOf(mGPSService.getLatitude()).substring(0, String.valueOf(mGPSService.getLatitude()).indexOf(".") + 5));
                     // tv_y = (String.valueOf(mGPSService.getLongitude()).substring(0, String.valueOf(mGPSService.getLongitude()).indexOf(".") + 5));

                     tv_x = (String.valueOf(mGPSService.getLatitude()));
                     tv_y = (String.valueOf(mGPSService.getLongitude()));
                 }
                 catch (Exception ex){
                     tv_x="0.0";
                     tv_y ="0.0";
                 }


                 near_locations.setLatitude(Double.parseDouble(tv_x));
                 near_locations.setLongitude(Double.parseDouble(tv_y));




                 /*Location.distanceBetween(selected_location.getLatitude(), selected_location.getLongitude(),
                         near_locations.getLatitude(), near_locations.getLongitude(),
                         results);


*/

                 /*Cls_Http_Get_Dis task = new Cls_Http_Get_Dis();
                 try {
                     r= task.execute(String.valueOf( selected_location.getLatitude()),String.valueOf(selected_location.getLongitude()),
                             String.valueOf(near_locations.getLatitude()), String.valueOf(near_locations.getLongitude())).get();
                 } catch (Exception ex) {
                 }*/

                 /* D1 = getDistanceOnRoad(selected_location.getLatitude(), selected_location.getLongitude(),
                          near_locations.getLatitude(), near_locations.getLongitude());
*/
                // double distance=selected_location.distanceTo(near_locations);
                // distance=distance/1000;

                 GetLocation v = new GetLocation(getActivity());

                 float NewDis = 0 ;
                 double distance=selected_location.distanceTo(near_locations);
                Dis = (int) results[0] ;
                 NewDis=   v.DistanceBetweenTwoPoints(selected_location,near_locations);
                 if(NewDis>1000){
                     customers.setNo((NewDis/1000) + "  " + "كـم");

                 }else {
                     customers.setNo(NewDis + "  " + "متر");
                 }

                 customers.setNo(NewDis+"");

                    if(NewDis<= Integer.parseInt(dis.toString()) && selected_location.getLatitude() >0 && selected_location.getLongitude()>0 ){
                        customersesList.add(customers);
                    }

                 NewDis = 0 ;
                 selected_location.setLatitude(0.0);
                 selected_location.setLongitude(0.0);
             }while (c1.moveToNext());
            }
            c1.close();
        }
        Customer_List Customer_List_adapter = new Customer_List(
                    this.getActivity(), customersesList);

            items_Lsit.setAdapter(Customer_List_adapter);
    }

    private double getDistanceRADIUS (double latA, double longitude,
                                     double latB, double prelongitude) {



        final double RADIUS = 6371.01;
        double temp = Math.cos(Math.toRadians(latA))
                * Math.cos(Math.toRadians(latB))
                * Math.cos(Math.toRadians((latB) - (latA)))
                + Math.sin(Math.toRadians(latA))
                * Math.sin(Math.toRadians(latB));
        return temp * RADIUS * Math.PI / 180;

    }

    private String getDistanceOnRoad(double latitude, double longitude,
                                     double prelatitute, double prelongitude) {
        String result_in_kms = "";
        String url = "http://maps.google.com/maps/api/directions/xml?origin="
                + latitude + "," + longitude + "&destination=" + prelatitute
                + "," + prelongitude + "&sensor=false&units=metric";
        String tag[] = { "text" };
        HttpResponse response = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(url);
            response = httpClient.execute(httpPost, localContext);
            InputStream is = response.getEntity().getContent();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            Document doc = builder.parse(is);
            if (doc != null) {
                NodeList nl;
                ArrayList args = new ArrayList();
                for (String s : tag) {
                    nl = doc.getElementsByTagName(s);
                    if (nl.getLength() > 0) {
                        Node node = nl.item(nl.getLength() - 1);
                        args.add(node.getTextContent());
                    } else {
                        args.add(" - ");
                    }
                }
                result_in_kms = String.format("%s", args.get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result_in_kms;
    }
   public void Exist_Pop ()
       {
           this.dismiss();
       }

    @Override
    public void onClick(View v) {
        Button bu = (Button) v ;
        if (bu.getText().toString().equals("Cancel")){
            this.dismiss();
        }
        else  if (bu.getText().toString().equals("Add")){
            Toast.makeText(getActivity(),
                    "Your Message", Toast.LENGTH_SHORT).show();
        }
     }

    public void onListItemClick(ListView l, View v, int position, long id) {
        items_Lsit.setItemChecked(position, true);
        v.setBackgroundColor(Color.BLUE);

    }

}
