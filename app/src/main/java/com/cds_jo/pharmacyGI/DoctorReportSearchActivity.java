package com.cds_jo.pharmacyGI;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class DoctorReportSearchActivity extends DialogFragment implements View.OnClickListener  {
    View form ;
    Button add,cancel;
    ListView items_Lsit;
   // TextView itemnm;
    ArrayList<Cls_DoctorReport> cls_search_pos_list;
    @Override
    public View onCreateView( final LayoutInflater inflater   , ViewGroup container  ,Bundle savestate){
        form =inflater.inflate(R.layout.activity_rec_voucher_search,container,false);

        FillList("");
        EditText  filter =    (EditText) form.findViewById(R.id.et_Search_filter);
        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                FillList(s.toString());

            }
        });



        items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Cls_DoctorReport po_obj = (Cls_DoctorReport) arg0.getItemAtPosition(position);
                String nm = po_obj.getCustName();

                if (getArguments().getString("Scr") == "DoctorReport") {
                    ((DoctorReportActivity) getActivity()).Set_Order(po_obj.getNo());

              } else if (getArguments().getString("Scr") == "EditeRec") {
                    ((EditeTransActivity) getActivity()).Set_Order(po_obj.getNo());
              }

                 Exist_Pop();

            }


        });
        this.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return  form;
    }
    private  void FillList(String s){
        cls_search_pos_list  = new ArrayList<Cls_DoctorReport>();
        cls_search_pos_list.clear();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String u =  sharedPreferences.getString("UserID", "");
        //COALESCE(RecVoucher.Post, -1)  as Post
        // DoctorReport  "( ID ,VType ,No,CustNo ,LocatNo , Sp1 , SampleType  , VNotes ,SNotes  ,Tr_Date , Tr_Time   ,UserNo , Posted




        String q = "Select distinct   CASE  VType  WHEN '1' THEN  D.Dr_AName ELSE Customers.name   END   as Name,  DoctorReport.ID ,VType ,DoctorReport.No,CustNo ,LocatNo , Sp1 , SampleType  , VNotes ,SNotes  ,Tr_Date , Tr_Time   ,UserNo , COALESCE(DoctorReport.Posted, -1)  as Post  " +
                "from DoctorReport left join Customers  on Customers.no =DoctorReport.CustNo  " +
                " left join Doctor D  on D.Dr_No = DoctorReport.CustNo    " +
                " where  DoctorReport.UserNo ='"+u.toString()+"' And ifnull(Customers.name,'') like  '%"+s.toString()+"%' ";
        SqlHandler sqlHandler = new SqlHandler(getActivity());
        Cursor c1 = sqlHandler.selectQuery(q);

        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_DoctorReport cls_searchRecVou= new Cls_DoctorReport();

                    cls_searchRecVou.setVType(c1.getString(c1.getColumnIndex("VType")));
                    cls_searchRecVou.setNo(c1.getString(c1.getColumnIndex("No")));
                    cls_searchRecVou.setCustNo(c1.getString(c1.getColumnIndex("CustNo")));
                    cls_searchRecVou.setLocatNo(c1.getString(c1.getColumnIndex("LocatNo")));
                    cls_searchRecVou.setSp1(c1.getString(c1.getColumnIndex("Sp1")));
                    cls_searchRecVou.setSampleType(c1.getString(c1.getColumnIndex("SampleType")));
                    cls_searchRecVou.setVNotes(c1.getString(c1.getColumnIndex("VNotes")));
                    cls_searchRecVou.setSNotes(c1.getString(c1.getColumnIndex("SNotes")));
                    cls_searchRecVou.setTr_Date(c1.getString(c1.getColumnIndex("Tr_Date")));
                    cls_searchRecVou.setTr_Time(c1.getString(c1.getColumnIndex("Tr_Time")));
                    cls_searchRecVou.setTr_Time(c1.getString(c1.getColumnIndex("Tr_Time")));
                    cls_searchRecVou.setPosted(c1.getString(c1.getColumnIndex("Post")));
                    cls_searchRecVou.setCustName(c1.getString(c1.getColumnIndex("Name")));



                    cls_search_pos_list.add(cls_searchRecVou);


                }while (c1.moveToNext());
            }

        c1.close();
    }

        Cls_SearchDoctorReport_Adapter cls_searchRecVou_adapter = new Cls_SearchDoctorReport_Adapter(
                this.getActivity(),cls_search_pos_list);

        items_Lsit=(ListView) form.findViewById(R.id.listView2);
        items_Lsit.setAdapter(cls_searchRecVou_adapter);




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