package com.cds_jo.pharmacyGI;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Tab_SalesOrders extends Fragment {
    ArrayList<cls_Tab_Order_Sales> cls_Tab_Order_Sales  ;
    ListView items_Lsit;
    TextView count,tv_Summatin;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_sales_orders,container,false);
        items_Lsit=(ListView) v.findViewById(R.id.SalesSummery);
        cls_Tab_Order_Sales  = new ArrayList<cls_Tab_Order_Sales>();
        cls_Tab_Order_Sales.clear();
        count = (TextView)v.findViewById(R.id.tv_Count);
        tv_Summatin = (TextView)v.findViewById(R.id.tv_Summatin);

        FillList();
        return v;
    }
    private  Double SToD(String str){
        String f = "";
        final NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        final DecimalFormat df = (DecimalFormat)nf;
        str = str.replace(",", "");
        Double d = 0.0;
        if (str.length()==0) {
            str = "0";
        }
        if (str.length()>0)
            try {
                d =  Double.parseDouble(str);
                str = df.format(d).replace(",", "");

            }
            catch (Exception ex)
            {
                str="0";
            }

        df.setParseBigDecimal(true);

        d = Double.valueOf(str.trim()).doubleValue();

        return d;
    }
    private void FillList(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd",Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        String q ="";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        cls_Tab_Order_Sales.clear();
        items_Lsit.setAdapter(null);

    /*    q = "Select distinct ( s.Net_Total) as sumation,   s.Net_Total, s.OrderNo ,s.acc ,s.date , s.inovice_type,  CASE s.inovice_type WHEN '-1' THEN  c.name ELSE s.Nm END as  name   " +
                ", Post  from  Sal_invoice_Hdr s left join Customers c on c.no =s.acc where  UserID='"+sharedPreferences.getString("UserID", "")+"' and  s.date ='" + currentDateandTime + "'";
*/
        q =     " Select distinct po.orderno,po.date, c.name,po.acc ,po.posted  ,COALESCE(po.Net_Total,0) as total" +
                " from Po_Hdr po Left join Customers c on c.no = po.acc where  userid='"+sharedPreferences.getString("UserID", "") +
                "' and  po.date ='" + currentDateandTime + "'";

        SqlHandler sqlHandler = new SqlHandler(getActivity());
        Cursor c1 = sqlHandler.selectQuery(q);


        cls_Tab_Order_Sales cls_searchpos;


        Double sum = 0.0 ;
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    cls_searchpos= new cls_Tab_Order_Sales();
                    cls_searchpos.setCustNo(c1.getString(c1.getColumnIndex("orderno")));
                    cls_searchpos.setCustNm(c1.getString(c1.getColumnIndex("name")));
                    cls_searchpos.setDate(c1.getString(c1.getColumnIndex("date")));
                    cls_searchpos.setAcc(c1.getString(c1.getColumnIndex("acc")));
                   cls_searchpos.setNotes(c1.getString(c1.getColumnIndex("posted")));
                    cls_searchpos.setType("0");
                    cls_searchpos.setTot(c1.getString(c1.getColumnIndex("total")));
                    sum=sum+SToD(c1.getString(c1.getColumnIndex("total")));
                    cls_Tab_Order_Sales.add(cls_searchpos);
                }while (c1.moveToNext());
            }

            c1.close();
        }

        tv_Summatin.setText(sum +"");
        tv_Summatin.setText(SToD(tv_Summatin.getText().toString()).toString());
        count.setText((cls_Tab_Order_Sales.size()) +"");
        cls_Tab__Order_Sales_adapter SalesAdapter = new cls_Tab__Order_Sales_adapter(
                this.getActivity(),cls_Tab_Order_Sales);

        items_Lsit.setAdapter(SalesAdapter);
    }
}