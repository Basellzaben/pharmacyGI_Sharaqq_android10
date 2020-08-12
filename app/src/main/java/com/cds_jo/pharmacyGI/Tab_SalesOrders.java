package com.cds_jo.pharmacyGI;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.pharmacyGI.assist.OrdersItems;
import com.cds_jo.pharmacyGI.assist.PoSummeryListAdapter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import Methdes.MyTextView;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Tab_SalesOrders extends Fragment {
    ArrayList<cls_Tab_Order_Sales> cls_Tab_Order_Sales  ;
    ListView items_Lsit;
    TextView count,tv_Summatin;
    Button    DeleteOrders ;
    String Order_no = "";
    SharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_sales_orders,container,false);
        items_Lsit=(ListView) v.findViewById(R.id.SalesSummery);


        items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (position >= 0) {

                    try {

                        cls_Tab_Order_Sales cls_searchpos = new cls_Tab_Order_Sales();
                        cls_searchpos = (cls_Tab_Order_Sales) items_Lsit.getAdapter().getItem(position);
                        Order_no = cls_searchpos.getCustNo();
                        Toast.makeText(getActivity(), cls_searchpos.getCustNo(), Toast.LENGTH_SHORT).show();

                        FillListDetails();
                    }catch(Exception ex ){
                        FillList();
                    }

                }
            }
        });


        cls_Tab_Order_Sales  = new ArrayList<cls_Tab_Order_Sales>();
        cls_Tab_Order_Sales.clear();
        count = (TextView)v.findViewById(R.id.tv_Count);
        tv_Summatin = (TextView)v.findViewById(R.id.tv_Summatin);

        FillList();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        DeleteOrders=(Button) v.findViewById(R.id.DeleteOrders);
        DeleteOrders.setText(sharedPreferences.getString("DeleteOrdersTime", ""));
        if (DeleteOrders.getText().toString().equalsIgnoreCase("")){
            DeleteOrders.setText("حذف الطلبات المرحلة");
        }
        DeleteOrders.setText("حذف الطلبات المرحلة");
        DeleteOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteOrders();
            }
        });
        return v;
    }
    private  void DeleteOrders(){
        String SERVERDATE,SERVERTIME;
        SERVERDATE= DB.GetValue(getActivity(),"SERVER_DATETIME","SERVERDATE","1=1");
        SqlHandler sqlHandler = new SqlHandler(getActivity());

        SimpleDateFormat StartTime = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        SERVERTIME= StartTime.format(new Date());
            String query = "" ;
            query ="Delete from  Po_dtl  where orderno in   ( select orderno from  Po_Hdr   where ifnull(posted,'0') !='-1'  )  " ;
            sqlHandler.executeQuery(query);
            query ="Delete from  Po_Hdr where ifnull(posted,'0')!='-1'     " ;
            sqlHandler.executeQuery(query);



        new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("حذف طلبات البيع")
                .setContentText("تمت  عملية الحذف بنجاح")
                .setCustomImage(R.drawable.tick)
                .setConfirmText("رجـوع")
                .show();

        FillList();


        SharedPreferences.Editor editor    = sharedPreferences.edit();
        SERVERDATE= "وقت الحذف : "+SERVERTIME+" "+SERVERDATE;
        editor.putString("DeleteOrdersTime", SERVERDATE);
        DeleteOrders.setText(SERVERDATE);
        editor.commit();


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
                "'";// and  po.date ='" + currentDateandTime + "'";

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

    private void FillListDetails(){

        ArrayList<ContactListItems> contactList;
        contactList = new ArrayList<ContactListItems>();
        contactList.clear();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd",Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        String q ="";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        cls_Tab_Order_Sales.clear();
        items_Lsit.setAdapter(null);
      //  tv_lbl_Count.setText("عدد المواد");

        ContactListItems contactListItems = new ContactListItems();
        contactListItems.setno("رقم المادة");
        contactListItems.setName("اسم المادة");
        contactListItems.setprice("السعر");
        contactListItems.setQty("الكمية");
        contactListItems.setTax("الضريبة");
        contactListItems.setUniteNm("الوحدة");
        contactListItems.setDiscount("الخصم %");
        contactListItems.setDis_Amt("قيمة الخصم");
        contactListItems.setBounce("البونص");
        contactListItems.setTotal("المجموع");
        contactListItems.setItemOrgPrice("");
        contactListItems.setDis_Amt("");
        contactListItems.setUnite("");
        contactListItems.setTax_Amt("");
        contactListItems.setProID("");
        contactListItems.setPro_bounce("0");
        contactListItems.setPro_dis_Per("0");
        contactListItems.setPro_amt("0");
        contactListItems.setPro_Total("0");
        contactListItems.setDisAmtFromHdr("0");
        contactListItems.setDisPerFromHdr("0");
        contactListItems.setBatch("");
        contactListItems.setExpDate("");
        contactListItems.setOperand("");
        contactList.add(contactListItems);



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

        tv_Summatin.setText(sum +"");
        tv_Summatin.setText(SToD(tv_Summatin.getText().toString()).toString());
        count.setText(i +"");
        PoSummeryListAdapter SalesAdapter = new PoSummeryListAdapter(
                this.getActivity(),contactList);

        items_Lsit.setAdapter(SalesAdapter);
    }
}