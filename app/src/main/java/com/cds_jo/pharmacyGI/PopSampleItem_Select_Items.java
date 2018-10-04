
package com.cds_jo.pharmacyGI;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.pharmacyGI.assist.Cls_Deptf;
import com.cds_jo.pharmacyGI.assist.Cls_Deptf_adapter;
import com.cds_jo.pharmacyGI.assist.Cls_Invf;
import com.cds_jo.pharmacyGI.assist.Cls_Invf_Adapter;
import com.cds_jo.pharmacyGI.assist.Cls_UnitItems;
import com.cds_jo.pharmacyGI.assist.Cls_UnitItems_Adapter;
import com.cds_jo.pharmacyGI.assist.DoctorVisitNew;
import com.cds_jo.pharmacyGI.assist.Sale_InvoiceActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Methdes.MethodToUse;
import Methdes.MyTextView;


public class PopSampleItem_Select_Items extends DialogFragment implements View.OnClickListener {
    public static final String CALCULATOR_PACKAGE = "com.android.calculator2";
    public static final String CALCULATOR_CLASS = "com.android.calculator2.Calculator";
    View form;
    Button add, cancel;
    ImageButton OpenCal;
    ListView items_Lsit,listDoctor;
    TextView itemnm;
    TextView Store_Qty;
    public String ItemNo = "";
    SqlHandler sqlHandler;
    float min = 0;
    int ResultCode;

    EditText filter,filterDoctor;
    ImageButton btn_filter_search;
    String UnitNo = "";
    String Operand = "";
    String pass = "";
    String UnitName = "";
    String str,DoctorNm,DoctorNo = "";

MyTextView Item_Name,tv_DoctorNm ;
    List<Cls_SampleItem> UpdateItem;
    int AllowSalInvMinus;
    EditText input ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savestate) {

        form = inflater.inflate(R.layout.layout_pop_samplei_tem_select_items, container, false);

        getDialog().setTitle("Galaxy");
        add = (Button) form.findViewById(R.id.btn_add_item);
        add.setOnClickListener(this);
        cancel = (Button) form.findViewById(R.id.btn_cancel_item);

        FillDeptf();
        cancel.setOnClickListener(this);


        AllowSalInvMinus = Integer.parseInt(DB.GetValue(this.getActivity(), "ComanyInfo", "AllowSalInvMinus", "1=1"));
        TextView Store_Qty = (TextView) form.findViewById(R.id.tv_StoreQty);


        items_Lsit = (ListView) form.findViewById(R.id.listView2);
        listDoctor = (ListView) form.findViewById(R.id.listDoctor);


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

        listDoctor.setOnTouchListener(new ListView.OnTouchListener() {
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

        final EditText qty = (EditText) form.findViewById(R.id.et_qty);
        final NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        final DecimalFormat df = (DecimalFormat) nf;
        Item_Name = (MyTextView) form.findViewById(R.id.Item_Name) ;
        tv_DoctorNm = (MyTextView) form.findViewById(R.id.DoctorNm) ;


        sqlHandler = new SqlHandler(getActivity());

        qty.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    qty.setText("", TextView.BufferType.EDITABLE);
                }
            }

        });

        qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                GetQtyPerc();
            }
        });

        filterDoctor = (EditText) form.findViewById(R.id.et_Search_filterDoctor);

        filterDoctor.setInputType(InputType.TYPE_NULL);

        filterDoctor.setInputType(InputType.TYPE_CLASS_TEXT);
        filterDoctor.requestFocus();
        filterDoctor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Toast.makeText(getActivity(),s.toString(),Toast.LENGTH_SHORT).show();
                FillDoctors();

            }
        });



        filter = (EditText) form.findViewById(R.id.et_Search_filter);

        filter.setInputType(InputType.TYPE_NULL);

        filter.setInputType(InputType.TYPE_CLASS_TEXT);
        filter.requestFocus();


        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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

        FillDoctors();
        FillItems();
        fillUnit("-1");


        final Spinner item_cat = (Spinner) form.findViewById(R.id.sp_item_cat);

        item_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                FillItems();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        final Spinner sp_unite = (Spinner) form.findViewById(R.id.sp_units);

        sp_unite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Cls_UnitItems o = (Cls_UnitItems) sp_unite.getItemAtPosition(position);







                UnitNo = o.getUnitno().toString();
                UnitName = o.getUnitDesc().toString();
                Operand = o.getOperand().toString();
                min = Float.valueOf(o.getMin());
                //get_min_price();
                checkStoreQty();
            }

            @Override

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        listDoctor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View argu, int position, long arg3) {
               try{
                   argu.setBackgroundColor(Color.GRAY);

                   Doctor o = (Doctor) listDoctor.getItemAtPosition(position);
                   DoctorNm = (String) o.getName();
                   tv_DoctorNm.setText(DoctorNm);
                   DoctorNo = o.getNo().toString();

                   for (int i = 0; i < listDoctor.getChildCount(); i++) {
                       View listItem = listDoctor.getChildAt(i);
                       if(i%2==0)
                           listItem.setBackgroundColor(Color.WHITE);
                       if(i%2==1)
                           listItem.setBackgroundColor(getActivity().getResources().getColor(R.color.Gray2));
                   }
                   argu.setBackgroundColor(Color.GRAY);


                }catch (Exception ex) {
                    Toast.makeText(getActivity(),ex.getMessage().toString(),Toast.LENGTH_SHORT).show();

                }




            }
        });
        EditText et_qty = (EditText) form.findViewById(R.id.et_qty);
        et_qty.setFocusableInTouchMode(false);
        et_qty.setFocusable(false);
        et_qty.setFocusableInTouchMode(true);
        et_qty.setFocusable(true);

        items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                try{
                Double Qty = 0.0;
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

                et_qty.setText("1");
                et_qty.clearFocus();


                str = (String) o.getItem_Name();
                    Item_Name.setText(str);
                // Toast.makeText(getActivity(),str,Toast.LENGTH_LONG).show();
                getDialog().setTitle(str);

                ItemNo = o.getItem_No().toString();
                 fillUnit(o.getItem_No().toString());


                    }catch (Exception ex) {
                    Toast.makeText(getActivity(),ex.getMessage().toString(),Toast.LENGTH_SHORT).show();

                }


                checkStoreQty();


            }
        });



        this.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        return form;
    }


    private double GetCustLastPrice(String ItemNo, String UnitNo) {
        Double r = 0.0;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String CustNo = sharedPreferences.getString("CustNo", "");

        String q = "select ifnull(Price,0) as price  from CustLastPrice where " +
                " Item_No = '" + ItemNo + "' and Cust_No ='" + CustNo + "' and  Unit_No = '" + UnitNo + "'";
        Cursor c = sqlHandler.selectQuery(q);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            r = SToD(c.getString(c.getColumnIndex("price")));
            c.close();
        }

        return r;
    }

    private void get_min_price() {





    }

    private void checkStoreQty() {
        TextView Store_Qty = (TextView) form.findViewById(R.id.tv_StoreQty);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        int Order_qty = 0;
        Double Res = 0.0;
        String query = "SELECT   ifnull( qty,0)   as  qty   from ManStore where  itemno = '" + ItemNo + "'  ";
        Cursor c1 = sqlHandler.selectQuery(query);
        Double Store_qty = 0.0;
        if (c1 != null && c1.getCount() != 0) {
            try {
                if (c1.getCount() > 0) {

                    c1.moveToFirst();
                    Store_qty = SToD(c1.getString(c1.getColumnIndex("qty")));
                    c1.close();
                }
            } catch (Exception exception) {
                Store_qty = 0.0;
            }

        }


              query = "SELECT       ifnull( sum  ( ifnull( sid.qty,0)  * ifnull( sid.Operand,1)) ,0)    as Sal_Qty   " +
                      "  from  SampleItem_Hdr  sih inner join SampleItem_Det sid on  sid.OrderNo = sih.OrderNo" +
                     "   where   sih.Post = -1  and sid.itemNo ='" + ItemNo + "'  and sih.UserID='" + sharedPreferences.getString("UserID", "-1") + "'";


     /*   if (UpdateItem != null && UpdateItem.size() > 0) {
            query = query + "And sid.OrderNo !='" + getArguments().getString("OrderNo") + "'";
        }*/
        c1 = sqlHandler.selectQuery(query);

        Double Sal_Qty = 0.0;
        if (c1 != null && c1.getCount() != 0) {
            if (c1.getCount() > 0) {
                c1.moveToFirst();
                Sal_Qty = SToD((c1.getString(c1.getColumnIndex("Sal_Qty"))).toString());
            }

            c1.close();
        }

        Order_qty =   ((DoctorVisitNew) getActivity()).checkStoreQty(ItemNo);

        Res = Store_qty - Sal_Qty - Order_qty;
        if (SToD(Operand) == 0) {
            Res = 0.0;
        } else {
            Res = Res / SToD(Operand);
        }

        Store_Qty.setText(SToD(Res.toString()).toString());


        GetQtyPerc();
    }


    private void CalcDiscount() {
        final EditText dis = (EditText) form.findViewById(R.id.et_disc_per);
        final EditText totl = (EditText) form.findViewById(R.id.et_totl);
        final EditText Discount = (EditText) form.findViewById(R.id.et_Discount);
        final EditText dis_Amt = (EditText) form.findViewById(R.id.et_dis_Amt);
        final EditText et_totl = (EditText) form.findViewById(R.id.et_totl);

        final EditText et_qty = (EditText) form.findViewById(R.id.et_qty);


        Double pq = 0.0;


        if (totl.getText().toString().equals("0")) {
            dis.setText("0");
            dis_Amt.setText("0");
        }

        et_totl.setText(String.valueOf(SToD(String.valueOf(pq - SToD(dis_Amt.getText().toString())))));
    }

    private void FillDeptf() {
        final Spinner sp_items_cat = (Spinner) form.findViewById(R.id.sp_item_cat);
        sqlHandler = new SqlHandler(getActivity());

        String query = "Select   distinct Type_No , Type_Name , etname from deptf";
        ArrayList<Cls_Deptf> cls_deptfs = new ArrayList<Cls_Deptf>();
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

    private void FillItems() {
        filter = (EditText) form.findViewById(R.id.et_Search_filter);
        String query = "";
        sqlHandler = new SqlHandler(getActivity());


        Bundle bundle = this.getArguments();
        UpdateItem = (List<Cls_SampleItem>) bundle.getSerializable("List");


        AllowSalInvMinus = -1;

            if (getArguments().getString("Scr") == "Sal_inv") {

                if (AllowSalInvMinus==1) {
                    if (filter.getText().toString().equals("")) {
                        query = "Select distinct invf.Item_No , invf.Item_Name,invf.Price, invf.tax   from invf  " ;

                    } else {
                        query = "Select distinct  invf.Item_No , invf.Item_Name,invf.Price, invf.tax from  invf " +
                                "    where Item_Name  like '%" + filter.getText().toString() + "%'  or  Item_No like '%" + filter.getText().toString() + "%'  ";
                    }
                }else {
                    if (filter.getText().toString().equals("")) {
                        query = "Select distinct invf.Item_No , invf.Item_Name,invf.Price, invf.tax   from invf  " +
                                " inner join ManStore   on ManStore.itemno =   invf.Item_No And   CAST(  ifnull(ManStore.qty,0) as decimal)>0  where 1=1 ";
                    } else {
                        query = "Select distinct  invf.Item_No , invf.Item_Name,invf.Price, invf.tax from  invf " +
                                " inner join ManStore   on ManStore.itemno =   invf.Item_No And   CAST(  ifnull(ManStore.qty,0) as decimal)>0  where Item_Name  like '%" + filter.getText().toString() + "%'  or  Item_No like '%" + filter.getText().toString() + "%'  ";
                    }
                }

                Spinner item_cat = (Spinner) form.findViewById(R.id.sp_item_cat);
                Integer indexValue = item_cat.getSelectedItemPosition();

                if (indexValue > 0) {

                    Cls_Deptf o = (Cls_Deptf) item_cat.getItemAtPosition(indexValue);

                    query = query + "and    Type_No = '" + o.getType_No().toString() + "'";

                }


            }



        Spinner item_cat = (Spinner) form.findViewById(R.id.sp_item_cat);
        Integer indexValue = item_cat.getSelectedItemPosition();

        if (indexValue > 0) {

            Cls_Deptf o = (Cls_Deptf) item_cat.getItemAtPosition(indexValue);

            query = query + "and    Type_No = '" + o.getType_No().toString() + "'";

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
    private void FillDoctors() {
        filter = (EditText) form.findViewById(R.id.et_Search_filterDoctor);
        String query = "";
        sqlHandler = new SqlHandler(getActivity());

        if (filter.getText().toString().equals("")) {
            query = "Select Dr_No ,Dr_AName , s.Aname  from Doctor   Left  Join Specialization s on s.No = Doctor.Specialization_No";//   Customers.Cust_type='"+Cust_type+"'";


        } else {

            query = "Select Dr_No ,Dr_AName , s.Aname  from Doctor Left  Join Specialization s on s.No = Doctor.Specialization_No " +
                    "  where Dr_AName like '%" + filter.getText().toString() + "%' or  Dr_No like '%" + filter.getText().toString() + "%'";//  And Customers.Cust_type='"+Cust_type+"'";


        }



        ArrayList<Doctor> cls_invf_List = new ArrayList<Doctor>();
        cls_invf_List.clear();
        Cursor c1 = sqlHandler.selectQuery(query);

        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Doctor cls_unitItems = new Doctor();

                    cls_unitItems.setNo(c1.getString(c1
                            .getColumnIndex("Dr_No")));
                    cls_unitItems.setName(c1.getString(c1
                            .getColumnIndex("Dr_AName")));
                    cls_unitItems.setSpec(c1.getString(c1
                            .getColumnIndex("Aname")));
                    cls_unitItems.setStatus("1");

                    cls_invf_List.add(cls_unitItems);

                } while (c1.moveToNext());


            }
            c1.close();
        }
        Cls_Doctor_Search_Adapter cls_invf_adapter = new Cls_Doctor_Search_Adapter(
                getActivity(), cls_invf_List);
        listDoctor.setAdapter(cls_invf_adapter);


    }
 public void fillUnit(String item_no) {

try{
        Spinner sp_unite = (Spinner) form.findViewById(R.id.sp_units);

        sqlHandler = new SqlHandler(getActivity());

        String query = "Select  distinct UnitItems.unitno ,UnitItems.Min ,UnitItems.price,Unites.UnitName " +
                " , ifnull(Operand,1) as Operand from UnitItems  left join  Unites on Unites.Unitno =UnitItems.unitno where UnitItems.item_no ='" + item_no + "' order by   UnitItems.unitno desc";
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
                    cls_unitItems.setOperand(c1.getString(c1.getColumnIndex("Operand")));

                    cls_unitItemses.add(cls_unitItems);

                } while (c1.moveToNext());


            }
            c1.close();
        }

        Cls_UnitItems_Adapter cls_unitItems_adapter = new Cls_UnitItems_Adapter(
                getActivity(), cls_unitItemses);

        sp_unite.setAdapter(cls_unitItems_adapter);

        if (cls_unitItemses.size() > 0) {
            Cls_UnitItems o = (Cls_UnitItems) sp_unite.getItemAtPosition(0);
            UnitNo = o.getUnitno().toString();
            UnitName = o.getUnitDesc().toString();
            Operand = o.getOperand().toString();
            min = Float.valueOf(o.getMin());
        }

}catch (Exception ex) {
    Toast.makeText(getActivity(),ex.getMessage().toString(),Toast.LENGTH_SHORT).show();

}
    }

    @Override
    public void onClick(View v) {

        if (v == OpenCal) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setComponent(new ComponentName(
                    CALCULATOR_PACKAGE,
                    CALCULATOR_CLASS));
            //startActivity(intent);

       /*  Intent i = new Intent();
         i.setAction(Intent.ACTION_MAIN);
         i.addCategory(Intent.CATEGORY_APP_CALCULATOR);*/
            try {

                startActivity(intent);
            } catch (Exception noSuchActivity) {
                Toast.makeText(getActivity(), noSuchActivity.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }

        }


          EditText et_Search_filter = (EditText) form.findViewById(R.id.et_Search_filter);
        if (v == cancel) {
            this.dismiss();
        }
        if (v == add) {
            et_Search_filter.requestFocus();

            EditText qty = (EditText) form.findViewById(R.id.et_qty);
            TextView tv_qty_perc = (TextView) form.findViewById(R.id.tv_qty_perc);
            TextView tv_StoreQty = (TextView) form.findViewById(R.id.tv_StoreQty);


            qty.clearFocus();




            AllowSalInvMinus = Integer.parseInt(DB.GetValue(this.getActivity(), "ComanyInfo", "AllowSalInvMinus", "1=1"));
            AllowSalInvMinus =-1;
            if (AllowSalInvMinus!=1) {

                 TextView Store_Qty = (TextView) form.findViewById(R.id.tv_StoreQty);

                if (SToD(Store_Qty.getText().toString())==0 ||SToD(tv_qty_perc.getText().toString())<=0 ) {
                    AlertDialog alertDialog = new AlertDialog.Builder(
                            getActivity()).create();
                    alertDialog.setTitle("طلب عينات");
                    alertDialog.setMessage("الكمية المطلوبة غير متوفرة");            // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.tick);
                    alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    alertDialog.show();
                    return;
                }


                if (SToD(tv_qty_perc.getText().toString()) >70 ) {
                    AlertDialog alertDialog = new AlertDialog.Builder(
                            getActivity()).create();
                    alertDialog.setTitle("طلب عينات");
                    alertDialog.setMessage("لا يمكن صرف اكثر من 70% من الكمية المتاحة");            // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.tick);
                    alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    alertDialog.show();
                    return;
                }
            }


            if (SToD(qty.getText().toString()) >= 9000) {
                qty.setError("الرجاء ادخال القيمة");
                qty.requestFocus();
                return;
            }

            if (SToD(qty.getText().toString()) >2) {
                qty.setError("الرجاء ادخال القيمة");
                Toast.makeText(getActivity(),"لا يمكن اضافة اكثر من عينتين  لنفس الطبيب",Toast.LENGTH_SHORT).show();
                qty.requestFocus();
                return;
            }



            if (qty.getText().toString().length() == 0) {
                qty.setError("الرجاء ادخال القيمة");
                qty.requestFocus();
                return;
            }

            if (DoctorNo=="") {
            Toast.makeText(getActivity(),"الرجاء اختيار الطبيب",Toast.LENGTH_SHORT).show();
                return;
            }


            if (ItemNo.toString().length() == 0) {
                Toast.makeText(getActivity(),"الرجاء اختيار المادة",Toast.LENGTH_SHORT).show();
                return;
            }

            int i = 1 ;


            if (getArguments().getString("Scr") == "po") {
             //   ((OrdersItems) getActivity()).Save_List(ItemNo, Price.getText().toString(), qty.getText().toString(), tax.getText().toString(), UnitNo, disc_per.getText().toString(), bounce.getText().toString(), str, UnitName, disc_Amt.getText().toString());

            } else if (getArguments().getString("Scr") == "Sal_inv") {


                if (UpdateItem != null) {
                    if (UpdateItem.size() > 0) {
                        ((DoctorVisitNew) getActivity()).Update_List(ItemNo, qty.getText().toString().replace(",", ""),UnitNo,  str, UnitName,  Operand, DoctorNo ,  DoctorNm );
                        this.dismiss();
                    }
                } else {
                    ((DoctorVisitNew) getActivity()).Save_List(ItemNo, qty.getText().toString().replace(",", ""),UnitNo,  str, UnitName,  Operand, DoctorNo ,  DoctorNm );


                }



            }

            try {
                min = 0;

                tv_qty_perc.setText("");
                tv_StoreQty.setText("");

                ItemNo = "";
                UnitNo = "";
                Operand = "";
              /*  DoctorNo  = "";
                DoctorNm = "";*/

                for (int v1 = 0; v1< items_Lsit.getChildCount(); v1++) {
                    View listItem = items_Lsit.getChildAt(v1);
                    if(v1%2==0)
                        listItem.setBackgroundColor(Color.WHITE);

                    if(v1%2==1)
                        listItem.setBackgroundColor(getActivity().getResources().getColor(R.color.Gray2));
                }
                Item_Name.setText("");


            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }

    public void onListItemClick(ListView l, View v, int position, long id) {



    }


    public void get_total() {
        Double Perc = 0.0;
        EditText Price = (EditText) form.findViewById(R.id.et_price);
        EditText qty = (EditText) form.findViewById(R.id.et_qty);
        EditText tax = (EditText) form.findViewById(R.id.et_tax);
        EditText dis = (EditText) form.findViewById(R.id.et_disc_per);
        EditText bo = (EditText) form.findViewById(R.id.et_bo);
        EditText net_total = (EditText) form.findViewById(R.id.et_totl);


        String str_p, str_q;


        TextView tv_qty_perc = (TextView) form.findViewById(R.id.tv_qty_perc);
        TextView tv_StoreQty = (TextView) form.findViewById(R.id.tv_StoreQty);


        str_p = Price.getText().toString();
        str_q = qty.getText().toString();

        if (Price.getText().toString().length() == 0) {
            str_p = "0";
        }

        if (qty.getText().toString().length() == 0) {
            str_q = "0";
        }
         /* if( dis.getText().toString().length() == 0 ) {
            dis.setText("0");
        }
        if( bo.getText().toString().length() == 0 ) {
            bo.setText("0");
        }*/
        Double p = SToD(str_p);
        Double q = SToD(str_q);

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat) nf;

        net_total.setText(String.valueOf(df.format(p * q)).replace(",", ""));

    }

    private void GetQtyPerc() {

        Double Perc = 0.0;
        EditText qty = (EditText) form.findViewById(R.id.et_qty);
        EditText bo = (EditText) form.findViewById(R.id.et_bo);
        TextView tv_qty_perc = (TextView) form.findViewById(R.id.tv_qty_perc);
        TextView tv_StoreQty = (TextView) form.findViewById(R.id.tv_StoreQty);

        if (tv_StoreQty.getText().toString() == "") {
            Perc = 0.0;
        } else {
            if (SToD(tv_StoreQty.getText().toString()) == 0) {
                Perc = 0.0;
            } else {
                Perc = (SToD(qty.getText().toString()) ) / SToD(tv_StoreQty.getText().toString());
            }
        }
        Perc = (Perc * 100);
        tv_qty_perc.setText(String.valueOf(SToD(Perc.toString())));

    }



}


