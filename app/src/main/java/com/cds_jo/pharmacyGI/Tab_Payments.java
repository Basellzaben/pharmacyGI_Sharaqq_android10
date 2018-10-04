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

public class Tab_Payments extends Fragment {
    ArrayList<Cls_TabPayments> cls_Tab_Sales  ;
    ListView items_Lsit;
    TextView count,tv_Summatin;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_paments,container,false);
        items_Lsit=(ListView) v.findViewById(R.id.PaymentSummery);
        count= (TextView) v.findViewById(R.id.tv_count);
        tv_Summatin= (TextView) v.findViewById(R.id.tv_Summatin);
        FillList();
        return v;
    }

    private  void FillList( ){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd",Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        cls_Tab_Sales  = new ArrayList<Cls_TabPayments>();
        cls_Tab_Sales.clear();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String u =  sharedPreferences.getString("UserID", "");
        String q = "Select distinct RecVoucher.VouchType,  RecVoucher.Cash , RecVoucher.CheckTotal,     RecVoucher.CustAcc ,RecVoucher.DocNo,RecVoucher.TrDate,RecVoucher.Amnt,Customers.name,RecVoucher.Desc, COALESCE(RecVoucher.Post, -1)  as Post from RecVoucher left join Customers  on Customers.no =RecVoucher.CustAcc" +
                " where  RecVoucher.UserID ='"+u.toString()+"' and  RecVoucher.TrDate ='" + currentDateandTime + "'";
        SqlHandler sqlHandler = new SqlHandler(getActivity());
        Cursor c1 = sqlHandler.selectQuery(q);
        Double sum = 0.0 ;
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_TabPayments cls_searchRecVou= new Cls_TabPayments();

                    cls_searchRecVou.setDocNo(c1.getString(c1.getColumnIndex("DocNo")));
                    cls_searchRecVou.setDate(c1.getString(c1.getColumnIndex("CheckTotal")));
                    cls_searchRecVou.setAmt(c1.getString(c1.getColumnIndex("Amnt")));
                    cls_searchRecVou.setCustNm(c1.getString(c1.getColumnIndex("name")));
                    cls_searchRecVou.setNotes(c1.getString(c1.getColumnIndex("Cash")));
                    cls_searchRecVou.setPost(c1.getString(c1.getColumnIndex("Post")));
                    cls_searchRecVou.setAcc(c1.getString(c1.getColumnIndex("CustAcc")));
                    cls_searchRecVou.setType(c1.getString(c1.getColumnIndex("VouchType")));
                    sum=sum+SToD(c1.getString(c1.getColumnIndex("Amnt")));
                    cls_Tab_Sales.add(cls_searchRecVou);



                }while (c1.moveToNext());
            }
       c1.close();
    }
        tv_Summatin.setText(sum +"");
      count.setText((cls_Tab_Sales.size()) +"");
        Cls_Payments_Adapter cls_Payments_Adapter = new Cls_Payments_Adapter(
                this.getActivity(),cls_Tab_Sales);


      items_Lsit.setAdapter(cls_Payments_Adapter);




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
}