package com.cds_jo.pharmacyGI;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Tab_UnpostedTransaction extends Fragment {
    ArrayList<cls_Tab_Sales> cls_Tab_Sales  ;
    ListView items_Lsit;
    TextView count,tv_Summatin;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_unposted_transactions,container,false);
        items_Lsit=(ListView) v.findViewById(R.id.SalesSummery);
        cls_Tab_Sales  = new ArrayList<cls_Tab_Sales>();
        cls_Tab_Sales.clear();
        count = (TextView)v.findViewById(R.id.tv_Count);
        tv_Summatin = (TextView)v.findViewById(R.id.tv_Summatin);
        tv_Summatin.setText("0");
        count.setText("0");
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
    @SuppressLint("Range")
    private void FillList(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd",Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        String q ="";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        cls_Tab_Sales.clear();
        items_Lsit.setAdapter(null);

                   q = "Select distinct s.Net_Total as Amt, s.OrderNo ,s.acc ,s.date , 'Sales Invoice  ' as type,  CASE s.inovice_type WHEN '-1' THEN  c.name ELSE s.Nm END as  name   " +
                "  from  Sal_invoice_Hdr s left join Customers c on c.no =s.acc where  UserID='"+sharedPreferences.getString("UserID", "")+"' and  s.Post ='-1'";


                q  = q + " UNION ALL Select distinct  RecVoucher.Amnt as Amt ,RecVoucher.DocNo as OrderNo , RecVoucher.CustAcc as scc ,RecVoucher.TrDate as date, 'Receipt Voucher  ' as type ,Customers.name as name  from RecVoucher left join Customers  on Customers.no =RecVoucher.CustAcc" +
                " where  RecVoucher.UserID ='"+sharedPreferences.getString("UserID", "")+"' and  RecVoucher.Post ='-1'";

                q =   q+   "UNION ALL  Select distinct COALESCE(po.Net_Total,0) as Amt,po.orderno  as OrderNo,po.acc , po.date , 'Sales Orders' as type ,c.name  from Po_Hdr po Left join Customers c on c.no = po.acc " +
                        "where  userid='"+sharedPreferences.getString("UserID", "") +
                "' and po.posted ='-1'";


        SqlHandler sqlHandler = new SqlHandler(getActivity());
        Cursor c1 = sqlHandler.selectQuery(q);


        cls_Tab_Sales cls_searchpos;




        Double sum = 0.0 ;
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                     cls_searchpos= new cls_Tab_Sales();

                    cls_searchpos.setCustNo(c1.getString(c1.getColumnIndex("OrderNo")));
                    cls_searchpos.setCustNm(c1.getString(c1.getColumnIndex("name")));
                    cls_searchpos.setDate(c1.getString(c1.getColumnIndex("date")));
                    cls_searchpos.setAcc(c1.getString(c1.getColumnIndex("acc")));
                   // cls_searchpos.setNotes("");
                    cls_searchpos.setType(c1.getString(c1.getColumnIndex("type")));
                    cls_searchpos.setTot(c1.getString(c1.getColumnIndex("Amt")));
                    //sum=sum+SToD(c1.getString(c1.getColumnIndex("sumation")));
                    cls_Tab_Sales.add(cls_searchpos);
                }while (c1.moveToNext());
            }

        c1.close();

    }

        tv_Summatin.setText(sum +"");
        tv_Summatin.setText(SToD(tv_Summatin.getText().toString()).toString());


        count.setText((cls_Tab_Sales.size()) +"");
        cls_Tab_UnpostedTrans SalesAdapter = new cls_Tab_UnpostedTrans(
                this.getActivity(),cls_Tab_Sales);


        items_Lsit.setAdapter(SalesAdapter);
    }
}