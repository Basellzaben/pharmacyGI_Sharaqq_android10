
package com.cds_jo.pharmacyGI.PostTransActions;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.cds_jo.pharmacyGI.ComInfo;
import com.cds_jo.pharmacyGI.ContactListItems;
import com.cds_jo.pharmacyGI.DB;
import com.cds_jo.pharmacyGI.R;
import com.cds_jo.pharmacyGI.SqlHandler;
import com.cds_jo.pharmacyGI.assist.PoSummeryListAdapter;
import com.cds_jo.pharmacyGI.assist.PopOrderItemsAdapter;
import com.cds_jo.pharmacyGI.cls_Tab_Order_Sales;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;



public class PopOrderItems extends DialogFragment implements View.OnClickListener  {
    View form ;
    ImageButton add,cancel,acc,invoice,rec,order;
    Button back;
    SqlHandler sqlHandler;
    ListView items_Lsit;
      Methdes.MyTextView tv_CustName ;
    ArrayList<cls_Tab_Order_Sales> cls_Tab_Order_Sales  ;
String CustNo,CustNm;
    @Override
    public void onStart()
    {
        super.onStart();


        if (getDialog() == null)
            return;


       WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom( getDialog().getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes(lp);
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setWindowAnimations(0);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savestate){
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
       form =inflater.inflate(R.layout.pop_order_items,container,false);
        getDialog().setTitle("أخر حركات العميل");


        tv_CustName = (Methdes.MyTextView) form.findViewById(R.id.tv_CustName);
        back = (Button)form.findViewById(R.id.button42);
        items_Lsit=(ListView) form.findViewById(R.id.Lst1);
        cls_Tab_Order_Sales  = new ArrayList<cls_Tab_Order_Sales>();
        cls_Tab_Order_Sales.clear();
        FillListDetails( );
        back.setOnClickListener(this);

        tv_CustName.setText(getArguments().getString("CustName"));


       getDialog().getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        return  form;
    }
    private void FillListDetails(){
          String Order_no =getArguments().getString("Order_No");
        ArrayList<ContactListItems> contactList;
        contactList = new ArrayList<ContactListItems>();
        contactList.clear();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd",Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        String q ="";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        cls_Tab_Order_Sales.clear();
        items_Lsit.setAdapter(null);


        ContactListItems contactListItems  ;




        q = "  select Distinct Unites.UnitName, pod.OrgPrice ,  invf.Item_Name, pod.itemno,pod.price,pod.qty,pod.tax ,pod.unitNo ,pod.dis_Amt,pod.dis_per,pod.bounce_qty ,  pod.tax_Amt   , pod.total  " +
                " , pod.pro_Total    , pod.ProID , pod.Pro_bounce  ,pod.Pro_dis_Per,  pod.Pro_amt  " +
                ", 1 as Opraned  ,1 as ExpDate " +
                " ,1 as  Batch  from Po_dtl pod left join invf on invf.Item_No =  pod.itemno " +
                "   left join Unites on Unites.Unitno=  pod.unitNo  Where pod.orderno='" + Order_no+ "'";



        SqlHandler sqlHandler = new SqlHandler(getActivity());

        Cursor c1 = sqlHandler.selectQuery(q);
        int i = 0 ;
        Double sum = 0.0 ;
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    contactListItems = new ContactListItems();
                    contactListItems.setno(c1.getString(c1.getColumnIndex("pod.itemno")));
                    contactListItems.setName(c1.getString(c1.getColumnIndex(" invf.Item_Name")));
                    contactListItems.setprice(c1.getString(c1.getColumnIndex(" pod.OrgPrice")));
                    contactListItems.setQty(c1.getString(c1.getColumnIndex("pod.qty")));
                    contactListItems.setTax(c1.getString(c1.getColumnIndex("pod.tax")));
                    contactListItems.setUniteNm(c1.getString(c1.getColumnIndex("Unites.UnitName")));
                    contactListItems.setDiscount(c1.getString(c1.getColumnIndex("pod.dis_per")));
                    contactListItems.setDis_Amt(c1.getString(c1.getColumnIndex("pod.dis_Amt")));
                    contactListItems.setBounce(c1.getString(c1.getColumnIndex("pod.bounce_qty")));
                    contactListItems.setTotal(c1.getString(c1.getColumnIndex("pod.total")));
                    sum=sum+SToD(c1.getString(c1.getColumnIndex("pod.total")));
                    contactList.add(contactListItems);
                    i=i+1;
                }while (c1.moveToNext());
            }
            c1.close();
        }


        PopOrderItemsAdapter SalesAdapter = new PopOrderItemsAdapter(
                this.getActivity(),contactList);

        items_Lsit.setAdapter(SalesAdapter);
    }

    public void onClick(View v) {

         if (v == back) {
             this.dismiss();
        }
    }

    private Double SToD(String str) {
        String f = "";
        final NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        final DecimalFormat df = (DecimalFormat) nf;
        str = str.replace(",", "");
        Double d = 0.0;
        if (str.length() == 0) {
            str = "0";
        }
        if (str.length() > 0)
            try {
                d = Double.parseDouble(str);
                str = df.format(d).replace(",", "");

            } catch (Exception ex) {
                str = "0";
            }

        df.setParseBigDecimal(true);

        d = Double.valueOf(str.trim()).doubleValue();

        return d;
    }
}


