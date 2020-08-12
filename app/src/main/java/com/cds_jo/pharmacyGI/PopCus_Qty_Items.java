
package com.cds_jo.pharmacyGI;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.pharmacyGI.assist.Cls_Deptf;
import com.cds_jo.pharmacyGI.assist.Cls_Deptf_adapter;
import com.cds_jo.pharmacyGI.assist.Cls_Invf;
import com.cds_jo.pharmacyGI.assist.Cls_Invf_Adapter;
import com.cds_jo.pharmacyGI.assist.Cls_UnitItems;
import com.cds_jo.pharmacyGI.assist.Cls_UnitItems_Adapter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Hp on 07/02/2016.
 */

public class PopCus_Qty_Items extends DialogFragment implements View.OnClickListener  {
    View form ;

    Button btn_Inc, btn_Dec  ,add,cancel;
    ListView items_Lsit;
    TextView itemnm ,Exp_Date;
    public String ItemNo = "";
    SqlHandler sqlHandler;
    float min = 0 ;
    EditText filter    , Batch;
    ImageButton btn_filter_search ;
    String UnitNo ="";
    String UnitName ="";
    String str= "";
    RadioButton rad_Per ;
    RadioButton rad_Amt;

    List<ContactListItems> UpdateItem ;
    public static final String CALCULATOR_PACKAGE ="com.android.calculator2";
    public static final String CALCULATOR_CLASS ="com.android.calculator2.Calculator";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
    }
    public static String intToString(int num, int digits) {
        String output = Integer.toString(num);
        while (output.length() < digits) output = "0" + output;
        return output;
    }
    Calendar myCalendar = Calendar.getInstance() ;//global
    public void showDatePickerDialog(){

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String M = intToString(Integer.valueOf(monthOfYear+1), 2);
            String D = intToString(Integer.valueOf(dayOfMonth), 2);
            Exp_Date.setText(year+"/"+M+"/"+D);
        }

    };
    @Override
    public void onStart() {
        super.onStart();

        // safety check
        if (getDialog() == null)
            return;

        int dialogWidth = WindowManager.LayoutParams.WRAP_CONTENT;//340; // specify a value here
        int dialogHeight = WindowManager.LayoutParams.MATCH_PARENT;//400; // specify a value here

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

    }
    @Override
    public View onCreateView( LayoutInflater inflater   , ViewGroup container  ,Bundle savestate){
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        form =inflater.inflate(R.layout.n_popcustqty,container,false);

        getDialog().setTitle("Galaxy");
        add =(Button) form.findViewById(R.id.btn_add_item);
        add.setOnClickListener(this);
        cancel=(Button) form.findViewById(R.id.btn_cancel_item);
        FillDeptf();
        cancel.setOnClickListener(this);
        btn_Inc = (Button) form.findViewById(R.id.btn_Inc);
        btn_Inc.setOnClickListener(this);


        btn_Dec = (Button) form.findViewById(R.id.btn_Dec);
        btn_Dec.setOnClickListener(this);
        items_Lsit=(ListView) form.findViewById(R.id.listView2);

        items_Lsit.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        final List<String> items_ls = new ArrayList<String>();
        final List<String> promotion_ls = new ArrayList<String>();

        EditText Price = (EditText)form.findViewById(R.id.et_price);
        Exp_Date = (TextView) form.findViewById(R.id.et_Date);
        Batch = (EditText)form.findViewById(R.id.et_Batch);

        Exp_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        final EditText qty = (EditText)form.findViewById(R.id.et_qty);

        sqlHandler =  new SqlHandler(getActivity());


        qty.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    qty.setText("", TextView.BufferType.EDITABLE);
                }
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
                // Toast.makeText(getActivity(),s.toString(),Toast.LENGTH_SHORT).show();
                FillItems();

            }
        });



        FillItems();
        fillUnit("-1");

        final  Spinner item_cat = (Spinner)form.findViewById(R.id.sp_item_cat);

        item_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                FillItems();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        final  Spinner sp_unite = (Spinner)form.findViewById(R.id.sp_units);

        sp_unite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Cls_UnitItems o = (Cls_UnitItems) sp_unite.getItemAtPosition(position);
                //    EditText Price = (EditText) form.findViewById(R.id.et_price);
                NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
                DecimalFormat df = (DecimalFormat)nf;



                // Price.setText(String.valueOf(df.format(Double.valueOf(o.getPrice()))));


                UnitNo=o.getUnitno().toString();
                UnitName=o.getUnitDesc().toString();
                min=Float.valueOf(o.getMin());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                for (int i = 0; i < items_Lsit.getChildCount(); i++) {
                    View listItem = items_Lsit.getChildAt(i);
                    if(i%2==0)
                        listItem.setBackgroundColor(Color.WHITE);
                    if(i%2==1)
                        listItem.setBackgroundColor(getActivity().getResources().getColor(R.color.Gray2));
                }


                arg1.setBackgroundColor(Color.GRAY);
                NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
                DecimalFormat df = (DecimalFormat) nf;

                Cls_Invf o = (Cls_Invf) items_Lsit.getItemAtPosition(position);


                EditText et_qty = (EditText) form.findViewById(R.id.et_qty);
                et_qty.setText("");


                str = (String) o.getItem_Name();//As you are using Default String Adapter
                // Toast.makeText(getActivity(),str,Toast.LENGTH_LONG).show();
                getDialog().setTitle(str);
                fillUnit(o.getItem_No().toString());
                ItemNo = o.getItem_No().toString();


            }
        });


        qty.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    qty.setText("", TextView.BufferType.EDITABLE);
                }
            }

        });
        return  form;
    }



    private void   FillDeptf(){
        final Spinner sp_items_cat = (Spinner) form.findViewById(R.id.sp_item_cat);
        sqlHandler = new SqlHandler(getActivity());

        String  query = "Select  Type_No , Type_Name , etname from deptf";
        ArrayList<Cls_Deptf>  cls_deptfs = new ArrayList<Cls_Deptf>();
        cls_deptfs.clear();

        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_Deptf clsDeptfs = new Cls_Deptf();

                    clsDeptfs.setType_No(c1.getString(c1
                            .getColumnIndex("Type_No")));
                    clsDeptfs.setType_Name(c1.getString(c1
                            .getColumnIndex("Type_Name")));

                    cls_deptfs.add(clsDeptfs);

                } while (c1.moveToNext());

            }
            c1.close();
        }

        Cls_Deptf_adapter cls_unitItems_adapter = new Cls_Deptf_adapter(
                getActivity(), cls_deptfs);

        sp_items_cat.setAdapter(cls_unitItems_adapter);
    }
    private  void FillItems() {


        filter = (EditText) form.findViewById(R.id.et_Search_filter);
        String query="";
        sqlHandler = new SqlHandler(getActivity());

        Bundle bundle = this.getArguments();
        UpdateItem = (List<ContactListItems>) bundle.getSerializable("List");
        if (UpdateItem != null && UpdateItem.size() > 0) {

            query = "Select * from invf   where   invf.Item_No ='"+UpdateItem.get(0).getno().toString()+"'";
            str=UpdateItem.get(0).getName();
            getDialog().setTitle(str);
            EditText qty = (EditText)form.findViewById(R.id.et_qty);
            qty.setText(UpdateItem.get(0).getQty());
            ItemNo = UpdateItem.get(0).getno();
            fillUnit(UpdateItem.get(0).getno());
            Cls_UnitItems cls_unitItems = new Cls_UnitItems();
            Spinner sp_unite = (Spinner) form.findViewById(R.id.sp_units);

            Cls_UnitItems_Adapter UnitItems = (Cls_UnitItems_Adapter) sp_unite.getAdapter();
            for (int i = 0; i < UnitItems.getCount(); i++) {
                cls_unitItems = (Cls_UnitItems) UnitItems.getItem(i);
                if (cls_unitItems.getUnitno().equals(UpdateItem.get(0).getUnite())) {
                    sp_unite.setSelection(i);
                    break;
                }
            }

        }

        else{
            if (filter.getText().toString().equals("")) {
                query = "Select * from invf where 1=1 ";
            } else {
                query = "Select * from invf where Item_Name  like '%" + filter.getText().toString() + "%'  or  Item_No like '%" + filter.getText().toString() + "%'  ";
            }
        }
        Spinner item_cat = (Spinner) form.findViewById(R.id.sp_item_cat);
        Integer indexValue = item_cat.getSelectedItemPosition();

        if (indexValue > 0) {

            Cls_Deptf o = (Cls_Deptf) item_cat.getItemAtPosition(indexValue);

            query = query +"and    Type_No = '"+ o.getType_No().toString()+"'";

        }
      /*  AlertView.showAlert( query,
                getResources().getString(R.string.dev_check_msg), getActivity());*/
        ArrayList<Cls_Invf> cls_invf_List = new ArrayList<Cls_Invf>();
        cls_invf_List.clear();
        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_Invf cls_invf = new Cls_Invf();

                    cls_invf.setItem_No(c1.getString(c1
                            .getColumnIndex("Item_No")));
                    cls_invf.setItem_Name(c1.getString(c1
                            .getColumnIndex("Item_Name")));
                    cls_invf.setPrice(c1.getString(c1
                            .getColumnIndex("Price")));
                    cls_invf.setTax(c1.getString(c1
                            .getColumnIndex("tax")));
                    cls_invf_List.add(cls_invf);

                } while (c1.moveToNext());

            }
            c1.close();
        }

        Cls_Invf_Adapter cls_invf_adapter = new Cls_Invf_Adapter(
                getActivity(), cls_invf_List);
        items_Lsit.setAdapter(cls_invf_adapter);





    }

    public  void fillUnit(String item_no){


        Spinner sp_unite = (Spinner)form.findViewById(R.id.sp_units);
        sqlHandler = new SqlHandler(getActivity());

        String  query = "Select UnitItems.unitno ,UnitItems.Min ,UnitItems.price,Unites.UnitName from UnitItems  left join  Unites on Unites.Unitno =UnitItems.unitno where UnitItems.item_no ='"+item_no+"'";
        ArrayList<Cls_UnitItems> cls_unitItemses = new ArrayList<Cls_UnitItems>();
        cls_unitItemses.clear();

        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_UnitItems cls_unitItems = new Cls_UnitItems();

                    cls_unitItems.setUnitno(c1.getString(c1
                            .getColumnIndex("unitno")));
                    cls_unitItems.setUnitDesc(c1.getString(c1
                            .getColumnIndex("UnitName")));
                    cls_unitItems.setPrice(c1.getString(c1
                            .getColumnIndex("price")));
                    cls_unitItems.setMin(c1.getString(c1
                            .getColumnIndex("Min")));
                    cls_unitItemses.add(cls_unitItems);

                } while (c1.moveToNext());

            }
            c1.close();
        }

        Cls_UnitItems_Adapter cls_unitItems_adapter = new Cls_UnitItems_Adapter(
                getActivity(), cls_unitItemses);

        sp_unite.setAdapter(cls_unitItems_adapter);
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
    @Override
    public void onClick(View v) {
        final EditText dis = (EditText)form.findViewById(R.id.et_disc_per);
        EditText qty = (EditText) form.findViewById(R.id.et_qty);
     if (v == btn_Inc) {
            if(qty.getText().toString().equalsIgnoreCase("")){
                qty.setText("0");
            }
        qty.setText((Double.parseDouble(qty.getText().toString() ) + 1) + "");

        if (Double.parseDouble(qty.getText().toString() ) > 1) {
            btn_Dec.setVisibility(View.VISIBLE);
        }
        if (Double.parseDouble(qty.getText().toString() ) < 1) {
            qty.setText(SToD(qty.getText().toString() ) + "");
        }

         double d = Math.round(SToD(qty.getText().toString()));
         long l = Math.round(d);
         int i = (int)l;
         qty.setText(i+"");

     }
        if (v == btn_Dec) {
            if(qty.getText().toString().equalsIgnoreCase("")){
                qty.setText("1");
            }
        qty.setText((Double.parseDouble(qty.getText().toString() ) - 1) + "");
        if (Double.parseDouble(qty.getText().toString()  ) < 1) {
            qty.setText("1");
            btn_Dec.setVisibility(View.INVISIBLE);
        }

            double d = Math.round(SToD(qty.getText().toString()));
            long l = Math.round(d);
            int i = (int)l;
            qty.setText(i+"");
    }


        if(v==add) {

            if (qty.getText().toString().length() == 0) {
                qty.setError("الرجاء ادخال القيمة");
                qty.requestFocus();
                return;
            }
            if( ItemNo.toString().length() == 0 ) {

                return;
            }


            if (getArguments().getString("Scr") == "CustQty") {

                ((CustomerQty) getActivity()).Save_List(ItemNo, qty.getText().toString(), UnitNo, str, UnitName , Exp_Date.getText().toString(),Batch.getText().toString());
                qty.setText("");
            }

            if (getArguments().getString("Scr") == "RetrnQty") {

                ((ReturnQty) getActivity()).Save_List(ItemNo, qty.getText().toString(), UnitNo, str, UnitName);

            }

            if (getArguments().getString("Scr") == "PrePareQty") {

                if (UpdateItem != null  && UpdateItem.size()>0) {
                    ((PreapareManQty) getActivity()).Update_List(ItemNo, qty.getText().toString(), UnitNo, str, UnitName);
                }else {

                    ((PreapareManQty) getActivity()).Save_List(ItemNo, qty.getText().toString(), UnitNo, str, UnitName);
                }
            }


            try {
                min = 0;
                qty.setText("");
                Exp_Date.setText("");
                Batch.setText("");

               // ItemNo = "";
                //UnitNo = "";


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(v==cancel){
            this.dismiss();
        }



    }


    public void onListItemClick(ListView l, View v, int position, long id) {


        // Set the item as checked to be highlighted
        items_Lsit.setItemChecked(position, true);
        v.setBackgroundColor(Color.BLUE);

        //conversationAdapter.notifyDataSetChanged();

    }


    public  void get_total()
    {
        EditText Price = (EditText)form.findViewById(R.id.et_price);
        EditText qty = (EditText)form.findViewById(R.id.et_qty);
        EditText tax = (EditText)form.findViewById(R.id.et_tax);
        EditText dis = (EditText)form.findViewById(R.id.et_disc_per);
        EditText bo = (EditText)form.findViewById(R.id.et_bo);
        EditText net_total = (EditText)form.findViewById(R.id.et_totl);


        String str_p,str_q ;

        str_p =  Price.getText().toString();
        str_q=   qty.getText().toString();

        if( Price.getText().toString().length() == 0 ) {
            str_p="0";
        }

        if( qty.getText().toString().length() == 0 ) {
            str_q="0";
        }
         /* if( dis.getText().toString().length() == 0 ) {
            dis.setText("0");
        }
        if( bo.getText().toString().length() == 0 ) {
            bo.setText("0");
        }*/
        Double p =  Double.parseDouble(str_p);
        Double q =  Double.parseDouble(str_q);

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat)nf;

        net_total.setText(String.valueOf(df.format(p*q)));

    }

}


