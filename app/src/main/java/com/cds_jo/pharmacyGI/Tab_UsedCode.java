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

public class Tab_UsedCode extends Fragment {
    ArrayList<Cls_UsedCodes> cls_Tab_Sales  ;
    ListView UsedCodes_Lsit;
    TextView count,tv_Summatin;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_usedcode,container,false);
        UsedCodes_Lsit=(ListView) v.findViewById(R.id.LstUsedCodes);
        count= (TextView) v.findViewById(R.id.tv_count);
        tv_Summatin= (TextView) v.findViewById(R.id.tv_Summatin);
        FillList();
        return v;
    }

    private  void FillList( ){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd",Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        cls_Tab_Sales  = new ArrayList<Cls_UsedCodes>();
        cls_Tab_Sales.clear();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String u =  sharedPreferences.getString("UserID", "");


        String q = "Select distinct  invf.Item_Name, c.name,u.No ,  u.Status, u.Code , u.OrderNo ,u.Tr_Date, u.Tr_Time , u.UserNo , u.Posted" +
                " from UsedCode u   left join Customers c on c.no =u.CustomerNo" +
                " left join invf on invf.Item_No =  u.ItemNo  where  u.UserNo ='"+u.toString()+"' and  u.Tr_Date ='" + currentDateandTime + "'";
        SqlHandler sqlHandler = new SqlHandler(getActivity());
        Cursor c1 = sqlHandler.selectQuery(q);
        Double sum = 0.0 ;
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_UsedCodes cls_searchRecVou= new Cls_UsedCodes();

                    cls_searchRecVou.setNo(c1.getString(c1.getColumnIndex("No")));
                    cls_searchRecVou.setCustomerNo(c1.getString(c1.getColumnIndex("name")));
                    cls_searchRecVou.setItemNo(c1.getString(c1.getColumnIndex("Item_Name")));
                    cls_searchRecVou.setStatus(c1.getString(c1.getColumnIndex("Status")));
                    cls_searchRecVou.setCode(c1.getString(c1.getColumnIndex("Code")));
                    cls_searchRecVou.setOrderNo(c1.getString(c1.getColumnIndex("OrderNo")));
                    cls_searchRecVou.setTr_Date(c1.getString(c1.getColumnIndex("Tr_Date")));
                    cls_searchRecVou.setTr_Time(c1.getString(c1.getColumnIndex("Tr_Time")));

                    sum=sum+1;
                    cls_Tab_Sales.add(cls_searchRecVou);



                }while (c1.moveToNext());
            }
       c1.close();
    }
        tv_Summatin.setText(sum +"");
        count.setText((cls_Tab_Sales.size()) +"");
        Cls_UsedCodes_Adapter cls_Payments_Adapter = new Cls_UsedCodes_Adapter(
                this.getActivity(),cls_Tab_Sales);


        UsedCodes_Lsit.setAdapter(cls_Payments_Adapter);




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