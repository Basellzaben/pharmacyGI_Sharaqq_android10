package com.cds_jo.pharmacyGI;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cds_jo.pharmacyGI.assist.CallWebServices;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import Methdes.MyTextView;

public class Tab_Visits_Summery extends Fragment {
    ArrayList<Cls_SaleManDailyRound> cls_Tab_Sales  ;
    ListView items_Lsit;
   TextView tv_Count;
   Button ShareVisits,DeleteVisits;
   SqlHandler sqlHandler;
    SharedPreferences sharedPreferences;
  MyTextView tv_DeleteTime;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab1,container,false);
        items_Lsit=(ListView) v.findViewById(R.id.SalesSummery);
        ShareVisits=(Button) v.findViewById(R.id.ShareVisits);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        tv_DeleteTime=(MyTextView) v.findViewById(R.id.tv_DeleteTime);
        tv_DeleteTime.setText(sharedPreferences.getString("DeleteVisitsTime", ""));
        DeleteVisits=(Button) v.findViewById(R.id.DeleteVisits);
        cls_Tab_Sales  = new ArrayList<Cls_SaleManDailyRound>();
        tv_Count  = (TextView)v.findViewById(R.id.tv_Count) ;

        cls_Tab_Sales.clear();
        ShareVisits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareVisitNew();
            }
        });
        DeleteVisits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteVisitNew();
            }
        });
        sqlHandler = new SqlHandler(getActivity());
        FillList();
        return v;
    }
    private  void DeleteVisitNew(){
        String SERVERDATE,SERVERTIME;
        SERVERDATE= DB.GetValue(getActivity(),"SERVER_DATETIME","SERVERDATE","1=1");
        //SERVERTIME= DB.GetValue(getActivity(),"SERVER_DATETIME","SERVERTIME","1=1");
        SimpleDateFormat StartTime = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        SERVERTIME= StartTime.format(new Date());


        String query = " delete from   SaleManRounds   where ifnull(Posted,-1) !=-1 ";
        sqlHandler.executeQuery(query);
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog = new AlertDialog.Builder(
                getActivity()).create();
        alertDialog.setTitle("حذف الزيارات");
        alertDialog.setMessage("تمت عملية الحذف الزيارة المرحلة بنجاح");
        alertDialog.setIcon(R.drawable.error_new);
        alertDialog.setButton(getResources().getText(R.string.Ok    ), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
        FillList();
        SharedPreferences.Editor editor    = sharedPreferences.edit();
        SERVERDATE= "وقت الحذف : "+SERVERTIME+" "+SERVERDATE;
        editor.putString("DeleteVisitsTime", SERVERDATE);
        tv_DeleteTime.setText(SERVERDATE);
        editor.commit();
    }
    @SuppressLint("Range")
    private  void ShareVisitNew(){

        String query = "  select  Po_Hdr.orderno as Po_Order ,s.Notes  , s.no as no ,s.ManNo as ManNo, s.CusNo as CusNo , s.DayNum as DayNum,s.Tr_Data as Tr_Data ," +
                " s.Start_Time as Start_Time ,s.End_Time as End_Time, s.Duration as Duration , s.VisitType1 as VisitType1, " +
                " s.VisitType2 as VisitType2 ,s.VisitType3 as VisitType3 ,s.VisitType4 as VisitType4  ,s.X as X,s.Y as Y ,s.Locat as  Locat" +
                " , s.OrderNo as  OrderNo from SaleManRounds  s " +
                " Left join  Po_Hdr on Po_Hdr.V_OrderNo = s.OrderNo   where s.Posted = -1 and s.Closed='1' and  s.End_Time!='' order by s.no desc  Limit 1 ";

        String    COMPUTERNAME= Settings.Secure.getString(getActivity().getContentResolver(), "bluetooth_name"  );
        COMPUTERNAME=COMPUTERNAME+" (" + Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID  )+")";

        Cursor c1 = sqlHandler.selectQuery(query);

        if(c1!=null && c1.getCount()>0)
        {
            Cls_SaleManDailyRound cls_saleManDailyRound = new Cls_SaleManDailyRound();
            c1.moveToFirst();
            cls_saleManDailyRound.setNo(c1.getString(c1.getColumnIndex("no")));
            cls_saleManDailyRound.setManNo(c1.getString(c1.getColumnIndex("ManNo")));
            cls_saleManDailyRound.setCusNo(c1.getString(c1.getColumnIndex("CusNo")));
            cls_saleManDailyRound.setDayNum(c1.getString(c1.getColumnIndex("DayNum")));
            cls_saleManDailyRound.setTr_Data(c1.getString(c1.getColumnIndex("Tr_Data")));
            cls_saleManDailyRound.setStart_Time(c1.getString(c1.getColumnIndex("Start_Time")));
            cls_saleManDailyRound.setEnd_Time(c1.getString(c1.getColumnIndex("End_Time")));
            cls_saleManDailyRound.setDuration(c1.getString(c1.getColumnIndex("Duration")));
            cls_saleManDailyRound.setOrderNo(c1.getString(c1.getColumnIndex("OrderNo")));
            cls_saleManDailyRound.setVisitType1(c1.getString(c1.getColumnIndex("VisitType1")));
            cls_saleManDailyRound.setVisitType2(c1.getString(c1.getColumnIndex("VisitType2")));
            cls_saleManDailyRound.setVisitType3(c1.getString(c1.getColumnIndex("VisitType3")));
            cls_saleManDailyRound.setVisitType4(c1.getString(c1.getColumnIndex("VisitType4")));
            cls_saleManDailyRound.setX(c1.getString(c1.getColumnIndex("X")));
            cls_saleManDailyRound.setY(c1.getString(c1.getColumnIndex("Y")));
            cls_saleManDailyRound.setLocat(c1.getString(c1.getColumnIndex("Locat")));
            cls_saleManDailyRound.setPo_Order(c1.getString(c1.getColumnIndex("Po_Order")));
            cls_saleManDailyRound.setNotes(c1.getString(c1.getColumnIndex("Notes")));
            c1.close();
            cls_saleManDailyRound.setCOMPUTERNAME(COMPUTERNAME);
            Do_share_Visits(cls_saleManDailyRound);

        }else{
            AlertDialog alertDialog = new AlertDialog.Builder(
                    getActivity()).create();
            alertDialog.setTitle("إعتماد الزيارات");
            alertDialog.setMessage("لا يوجد زيارات مغلقة غير مرحلة ");            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setButton("رجـــوع", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.show();
            return;}
    }
    public ProgressDialog loadingdialog;
    public void Do_share_Visits( final Cls_SaleManDailyRound obj) {
        final String str;
        loadingdialog = ProgressDialog.show(getActivity(), "الرجاء الانتظار ...",    "العمل جاري على اعتماد جولة رقم :" + obj.getOrderNo(), true);
        loadingdialog.setCancelable(false);
        loadingdialog.setCanceledOnTouchOutside(true);
        loadingdialog.dismiss();
        loadingdialog.show();
        final Handler _handler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(getActivity());
                long i =   ws.SaveManVisitsNew(obj );
                try {
                    if (i> 0) {
                        String query = " Update  SaleManRounds  set Posted='"+We_Result.ID+"'  where OrderNo ='"+obj.getOrderNo() +"'";
                        sqlHandler.executeQuery(query );

                        _handler.post(new Runnable() {
                            public void run() {
                                loadingdialog.dismiss();
                                FillList();
                                ShareVisitNew();
                            }
                        });
                    } else {
                        _handler.post(new Runnable() {
                            public void run() {
                                loadingdialog.dismiss();

                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        getActivity()).create();
                                alertDialog.setTitle("إعتماد الزيارات");
                                alertDialog.setMessage("عملية اعتماد الزيارات لم تتم بنجاح، الرجاء المحاولة لاحقا");            // Setting Icon to Dialog
                                alertDialog.setIcon(R.drawable.error_new);
                                alertDialog.setButton("رجـــوع", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                alertDialog.show();


                            }
                        });
                    }
                } catch (final Exception e) {
                    _handler.post(new Runnable() {
                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    getActivity()).create();
                            alertDialog.setTitle("إعتماد الزيارات");
                            alertDialog.setMessage("عملية اعتماد الزيارات لم تتم بنجاح، الرجاء المحاولة لاحقا");            // Setting Icon to Dialog

                            alertDialog.setIcon(R.drawable.error_new);
                            alertDialog.setButton("رجـــوع", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            alertDialog.show();
                            loadingdialog.dismiss();
                        }
                    });
                }
            }
        }).start();
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
        cls_Tab_Sales.clear();
        items_Lsit.setAdapter(null);

        cls_Tab_Sales.clear();

         q   = "  select  s.Posted, c.name as name ,s.Notes  , s.no as no ,s.ManNo as ManNo, s.CusNo as CusNo , s.DayNum as DayNum,s.Tr_Data as Tr_Data ," +
                " s.Start_Time as Start_Time ,s.End_Time as End_Time, s.Duration as Duration , s.VisitType1 as VisitType1, " +
                " s.VisitType2 as VisitType2 ,s.VisitType3 as VisitType3 ,s.VisitType4 as VisitType4  ,s.X as X,s.Y as Y ,s.Locat as  Locat" +
                " , s.OrderNo as  OrderNo from SaleManRounds  s " +
                " Left join  Customers c on c.no = s.CusNo   order by no desc  ";





        SqlHandler sqlHandler = new SqlHandler(getActivity());
        Cursor c1 = sqlHandler.selectQuery(q);


        Cls_SaleManDailyRound obj;




        Double sum = 0.0 ;
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                     obj= new Cls_SaleManDailyRound();

                    obj.setOrderNo(c1.getString(c1.getColumnIndex("OrderNo")));
                    obj.setCusNo(c1.getString(c1.getColumnIndex("CusNo")));
                    obj.setNotes(c1.getString(c1.getColumnIndex("name")));
                    obj.setTr_Data(c1.getString(c1.getColumnIndex("Tr_Data")));
                    obj.setStart_Time(c1.getString(c1.getColumnIndex("Start_Time")));
                    obj.setEnd_Time(c1.getString(c1.getColumnIndex("End_Time")));
                    obj.setPosted(c1.getString(c1.getColumnIndex("Posted")));

                    cls_Tab_Sales.add(obj);
                }while (c1.moveToNext());
            }

        c1.close();
    }

        cls_Tab_Visits_adapter SalesAdapter = new cls_Tab_Visits_adapter(
                this.getActivity(),cls_Tab_Sales);

        tv_Count.setText(cls_Tab_Sales.size()+"");
        items_Lsit.setAdapter(SalesAdapter);
    }
}