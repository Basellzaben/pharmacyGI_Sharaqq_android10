
package com.cds_jo.pharmacyGI;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.pharmacyGI.assist.CallWebServices;
import com.cds_jo.pharmacyGI.assist.Cls_Deptf;
import com.cds_jo.pharmacyGI.assist.Cls_Deptf_adapter;
import com.cds_jo.pharmacyGI.assist.Cls_Invf;
import com.cds_jo.pharmacyGI.assist.Cls_Invf_Adapter;
import com.cds_jo.pharmacyGI.assist.Cls_UnitItems;
import com.cds_jo.pharmacyGI.assist.Cls_UnitItems_Adapter;
import com.cds_jo.pharmacyGI.assist.OrdersItems;
import com.cds_jo.pharmacyGI.assist.Po_Fill_Item_Adapter;
import com.cds_jo.pharmacyGI.assist.Pop_Update_Qty;
import com.cds_jo.pharmacyGI.assist.Sale_InvoiceActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Methdes.MethodToUse;
import Methdes.MyTextView;


public class Pop_Po_Select_Items extends DialogFragment implements View.OnClickListener {
    public static final String CALCULATOR_PACKAGE = "com.android.calculator2";
    public static final String CALCULATOR_CLASS = "com.android.calculator2.Calculator";
    View form;
    Button add, cancel,btn_Prv_item,btn_Next_item;
    ImageButton OpenCal;
    ListView items_Lsit;
    TextView itemnm;

    TextView Store_Qty;
    public String ItemNo = "";
    SqlHandler sqlHandler;
    float min = 0;
    Double min_price = 0.0;
    EditText filter;
    ImageButton btn_filter_search;
    String UnitNo = "";
    String Operand = "";
    Cls_Qty_Batch cls_qty_batch ;
    ArrayList<Cls_Qty_Batch> cls_qty_batches ;
    String UnitName = "";
    String str = "";
    RadioButton rad_Per;
    RadioButton rad_Amt;
    List<Cls_Sal_InvItems> UpdateItem;
    int AllowSalInvMinus = 0;
    MyTextView tv_ExpDate,tv_Batch;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
    }
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null)
            return;

        int dialogWidth = WindowManager.LayoutParams.WRAP_CONTENT;//340; // specify a value here
        int dialogHeight = WindowManager.LayoutParams.MATCH_PARENT;//400; // specify a value here

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savestate) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //form = inflater.inflate(R.layout.pop_po_select_items_new, container, false);
        form = inflater.inflate(R.layout.n_layout_pop_po_select_items, container, false);

        getDialog().setTitle("Galaxy");
        add = (Button) form.findViewById(R.id.btn_add_item);
        add.setOnClickListener(this);
        cancel = (Button) form.findViewById(R.id.btn_cancel_item);
        OpenCal = (ImageButton) form.findViewById(R.id.btn_OpenCal);

      /*  btn_Next_item = (Button) form.findViewById(R.id.btn_Next_item);
        btn_Prv_item = (Button) form.findViewById(R.id.btn_Prv_item);
*/


        add.setTypeface(MethodToUse.SetTFace(getActivity()));
        cancel.setTypeface(MethodToUse.SetTFace(getActivity()));
       /* btn_Next_item.setTypeface(MethodToUse.SetTFace(getActivity()));
        btn_Prv_item.setTypeface(MethodToUse.SetTFace(getActivity()));
*/

        tv_ExpDate=(MyTextView) form.findViewById(R.id.tv_ExpDate);
        tv_Batch=(MyTextView) form.findViewById(R.id.tv_Batch);
        cls_qty_batches = new ArrayList<Cls_Qty_Batch>();

        FillDeptf();
        cancel.setOnClickListener(this);
        //OpenCal.setOnClickListener(this);
        AllowSalInvMinus =Integer.parseInt( DB.GetValue(this.getActivity(),"ComanyInfo","AllowSalInvMinus","1=1"));

        TextView Store_Qty = (TextView) form.findViewById(R.id.tv_StoreQty);


        items_Lsit = (ListView) form.findViewById(R.id.listView2);
        final List<String> items_ls = new ArrayList<String>();
        final List<String> promotion_ls = new ArrayList<String>();

        final EditText Price = (EditText) form.findViewById(R.id.et_price);


        final EditText qty = (EditText) form.findViewById(R.id.et_qty);
        EditText tax = (EditText) form.findViewById(R.id.et_tax);
        final EditText dis = (EditText) form.findViewById(R.id.et_disc_per);
        final EditText bo = (EditText) form.findViewById(R.id.et_bo);
        final EditText dis_Amt = (EditText) form.findViewById(R.id.et_dis_Amt);
        final EditText totl = (EditText) form.findViewById(R.id.et_totl);
        final EditText Discount = (EditText) form.findViewById(R.id.et_Discount);
        RadioGroup radGrp = (RadioGroup) form.findViewById(R.id.radDisc);
        int checkedRadioButtonID = radGrp.getCheckedRadioButtonId();
        final Double Total = 0.0;

        final NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        final DecimalFormat df = (DecimalFormat) nf;

        sqlHandler = new SqlHandler(getActivity());


        dis_Amt.setFocusable(false);
        dis_Amt.setEnabled(false);
        dis_Amt.setCursorVisible(false);
        radGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup arg0, int id) {
                CalcDiscount();
            }


        });
        rad_Per = (RadioButton) form.findViewById(R.id.rad_Per);
        rad_Amt = (RadioButton) form.findViewById(R.id.rad_Amt);

        rad_Amt.setTypeface(MethodToUse.SetTFace(getActivity()));
        rad_Per.setTypeface(MethodToUse.SetTFace(getActivity()));

        Discount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                CalcDiscount();

            }


        });
        Discount.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Discount.setText("", TextView.BufferType.EDITABLE);
                }
            }

        });
        Discount.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                Discount.setText("", TextView.BufferType.EDITABLE);
                return false;
            }
        });


        Price.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Price.setText("", TextView.BufferType.EDITABLE);
                }
            }

        });
        qty.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    qty.setText("", TextView.BufferType.EDITABLE);
                }
            }

        });

        bo.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    bo.setText("", TextView.BufferType.EDITABLE);
                }
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
        Price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


              /*  if (s.toString().length()>0 &&  SToD(s.toString())>0 &&    (SToD(s.toString()) < min_price)){

                    AlertDialog alertDialog = new AlertDialog.Builder(
                            getActivity()).create();
                    alertDialog.setTitle("الحد الادني لبيع المادة ");

                    alertDialog.setIcon(R.drawable.delete);
                    alertDialog.setMessage("لقد تجاوزت الحد الادنى للسعر" + "   " + String.valueOf(min_price));
                    // Setting OK Button
                    alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Price.setText(min_price.toString());

                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                }
*/

          /*      EditText Price = (EditText) form.findViewById(R.id.et_price);
                String t = Price.getText().toString();
                Spinner sp_unite = (Spinner) form.findViewById(R.id.sp_units);
                Integer indexValue = sp_unite.getSelectedItemPosition();

                if (indexValue > -1) {
                    Cls_UnitItems o = (Cls_UnitItems) sp_unite.getItemAtPosition(indexValue);
                    if ( min != 0 && (Float.valueOf(o.getMin()) > Float.valueOf(Price.getText().toString()))) {
                        AlertDialog alertDialog = new AlertDialog.Builder(
                                getActivity()).create();
                        alertDialog.setTitle("Galaxy");

                        alertDialog.setIcon(R.drawable.delete);
                        alertDialog.setMessage("لقد تجاوزت الحد الادني للسعر" + "   " + String.valueOf(min));
                        // Setting OK Button
                        alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();
                    }
                }*/
                // Price.setText("15");

                get_total();
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
                get_total();
                GetQtyPerc();
            }
        });


        bo.addTextChangedListener(new TextWatcher() {
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

        ListView lst_Promotion = (ListView) form.findViewById(R.id.lst_Promotion);

        String q = "Select  distinct * from Offers_Hdr ";
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    promotion_ls.add(c1.getString(c1.getColumnIndex("Offer_Name")));

                } while (c1.moveToNext());

            }
            c1.close();
        }

        ArrayAdapter<String> promotion_ad = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, promotion_ls);

        lst_Promotion.setAdapter(promotion_ad);

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
                EditText Price = (EditText) form.findViewById(R.id.et_price);
                NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
                DecimalFormat df = (DecimalFormat) nf;

                Double CusPrice = 0.0;
                CusPrice = GetCustLastPrice(ItemNo, o.getUnitno().toString());
                if (CusPrice > 0) {
                    Price.setText(CusPrice.toString().replace(",", ""));
                } else {
                    Price.setText(String.valueOf(df.format(SToD(o.getPrice().toString()))).replace(",", ""));


                }


                UnitNo = o.getUnitno().toString();
                UnitName = o.getUnitDesc().toString();
                Operand = o.getOperand().toString();
                min = Float.valueOf(o.getMin());
                get_min_price();
                checkStoreQty();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
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


                EditText et_Discount = (EditText) form.findViewById(R.id.et_Discount);
                EditText Price = (EditText) form.findViewById(R.id.et_price);
                EditText et_qty = (EditText) form.findViewById(R.id.et_qty);
                EditText et_totl = (EditText) form.findViewById(R.id.et_totl);
                et_totl.setError(null);
                et_qty.setText("1");
                et_qty.clearFocus();
                et_Discount.setText("");

                rad_Amt.setChecked(true);//Here
                //Price.setText(df.format(Double.valueOf( o.getPrice())).toString());
                EditText tax = (EditText) form.findViewById(R.id.et_tax);
                tax.setText(o.getTax().toString());


                str = (String) o.getItem_Name();

                // Toast.makeText(getActivity(),str,Toast.LENGTH_LONG).show();
                getDialog().setTitle(str);
                fillUnit(o.getItem_No().toString());
                ItemNo = o.getItem_No().toString();


                  get_min_price();
                // itemnm.setText(str);

                Price.setError(null);
                Price.clearFocus();
                et_qty.setError(null);
                tax.setError(null);


               checkStoreQty();




                   //checkStoreQtyBatch();

            }
        });

        qty.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    qty.setText("", TextView.BufferType.EDITABLE);
                }
            }

        });

        this.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        rad_Amt.setChecked(true);//Here
        return form;
    }

    private void checkStoreQtyBatch() {


        final Handler _handler = new Handler();

        tv_ExpDate.setText("");
        tv_Batch.setText("");

        cls_qty_batches.clear();

        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(getActivity());
                ws.TrnsferQtyFromMobileBatch( ItemNo,"1");
                try {
                    Integer i;

                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_Item_No = js.getJSONArray("Item_No");
                    JSONArray js_expdate = js.getJSONArray("expdate");
                    JSONArray js_batchno = js.getJSONArray("batchno");
                    JSONArray js_net     = js.getJSONArray("net");




                    for (i = 0; i < js_Item_No.length(); i++)  {
                        cls_qty_batch = new Cls_Qty_Batch();

                        cls_qty_batch.setItem_No(js_Item_No.get(i).toString());
                        cls_qty_batch.setExpdate(js_expdate.get(i).toString());
                        cls_qty_batch.setBatchno(js_batchno.get(i).toString());
                        cls_qty_batch.setNet(js_net.get(i).toString());

                        cls_qty_batches.add(cls_qty_batch) ;

                    }

                    _handler.post(new Runnable() {
                        public void run() {
                            cls_qty_batch = new Cls_Qty_Batch();
                            if(cls_qty_batches.size()>0) {
                                cls_qty_batch = cls_qty_batches.get(0);
                                // Toast.makeText(getActivity(), cls_qty_batch.getBatchno(), Toast.LENGTH_SHORT).show();
                                CalcBatchQty();
                            }
                         /*   else
                            {
                               Toast.makeText(getActivity(), " حدث خطأ اثناء استرجاع تاريخ الصلاحية", Toast.LENGTH_SHORT).show();
                            }*/
                        }
                    });


                } catch (final Exception e) {
                    _handler.post(new Runnable() {
                        public void run() {
                          //  Toast.makeText(getActivity()," لا يمكن استرجاع تاريخ الصلاحية  ",Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        }).start();


        GetQtyPerc();
    }
    private  void CalcBatchQty (){
        Double OrderQty = 0.0;


        Double StoreQty = 0.0 ;
        tv_ExpDate.setText("");
        tv_Batch.setText("");
        TextView tv_StoreQty = (TextView) form.findViewById(R.id.tv_StoreQty);
        Double Qty = 0.0 ;
        for (int i = 0 ; i < cls_qty_batches.size(); i++)
        {
            cls_qty_batch = new Cls_Qty_Batch();
            cls_qty_batch = cls_qty_batches.get(i);
            Qty =SToD(cls_qty_batch.getNet()) / SToD(Operand);
            OrderQty =   ((OrdersItems) getActivity()).GetOrderQty(ItemNo,cls_qty_batch.getBatchno(),cls_qty_batch.getExpdate());
            StoreQty = Qty - OrderQty ;
            StoreQty =SToD( StoreQty + "");
            if (StoreQty>0){
                tv_ExpDate.setText(cls_qty_batch.getExpdate());
                tv_Batch.setText(cls_qty_batch.getBatchno());
                tv_StoreQty.setText(StoreQty+ "");
                //Toast.makeText(getActivity(),(Qty - OrderQty) + "" , Toast.LENGTH_SHORT).show();
                break;
            }



        }


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


        min_price = 0.0;
        String CatNo = "";
        CatNo = getArguments().getString("CatNo");
        if (CatNo != "0") {
            String q = "Select  ifnull( MinPrice,0) as min_price from Items_Categ where ItemCode = '" + ItemNo + "'   " +
                    "  And CategNo = '" + CatNo + "'";
            Cursor c1 = sqlHandler.selectQuery(q);
            if (c1 != null && c1.getCount() != 0) {
                if (c1.getCount() > 0) {
                    c1.moveToFirst();
                    min_price = SToD(Operand) * SToD(c1.getString(c1.getColumnIndex("min_price")));
                    min_price = SToD(min_price.toString());
                    // Toast.makeText(getActivity(), "الحد الادنى للسعر" +":" +String.valueOf(min_price),Toast.LENGTH_SHORT).show();
                }
                c1.close();
            }
        }

    }

    private void checkStoreQty() {

        TextView Store_Qty = (TextView) form.findViewById(R.id.tv_StoreQty);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());


        Double Order_qty = 0.0;
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


        query = "   SELECT       (ifnull( sum  ( ifnull( sid.qty,0)  * (ifnull( 1,1))) ,0)  +   ifnull( sum  ( ifnull( sid.bounce_qty,0)  * (ifnull( 1,1))) ,0) +  ifnull( sum  ( ifnull( sid.Pro_bounce,0)  * (ifnull(1,1))) ,0))  as Sal_Qty  from  Po_Hdr  sih inner join Po_dtl sid on  sid.OrderNo = sih.OrderNo" +
                "   inner join  UnitItems ui on ui.item_no  = sid.itemNo and ui.unitno = sid.unitNo" +
                "   where   sih.Posted = -1  and ui.item_no ='" + ItemNo + "'  and sih.UserID='" + sharedPreferences.getString("UserID", "-1") + "'";
        //   "    where  QtyStoreSer = "+MaxStoreQtySer+" and ui.item_no ='"+ItemNo+"'";


        if (UpdateItem != null && UpdateItem.size() > 0) {
            query = query + "And sid.OrderNo !='" + getArguments().getString("OrderNo") + "'";
        }
        c1 = sqlHandler.selectQuery(query);

        Double Sal_Qty = 0.0;
        if (c1 != null && c1.getCount() != 0) {
            if (c1.getCount() > 0) {
                c1.moveToFirst();
                Sal_Qty = SToD((c1.getString(c1.getColumnIndex("Sal_Qty"))).toString());
            }

            c1.close();

        }
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
        final EditText et_price = (EditText) form.findViewById(R.id.et_price);

        Double pq = 0.0;
        pq = SToD(et_price.getText().toString()) * SToD(et_qty.getText().toString());
        if (rad_Per.isChecked()) {


            Double Total = ((SToD(Discount.getText().toString().replace(",", "")) / 100)) * pq;

            Total = SToD(Total.toString());

            dis_Amt.setText(String.valueOf((Total)));
            dis.setText(String.valueOf(Discount.getText()));

        } else {
            Double Total = 0.0;
            if (pq == 0) {
                Total = 0.0;
            } else {
                Total = (SToD(Discount.getText().toString()) / pq) * 100;
            }

            Total = SToD(Total.toString());


            dis.setText(Total.toString());
            dis_Amt.setText(String.valueOf(SToD(Discount.getText().toString())));

        }
        if (totl.getText().toString().equals("0")) {
            dis.setText("0");
            dis_Amt.setText("0");
        }

        et_totl.setText(String.valueOf(SToD(String.valueOf(pq - SToD(dis_Amt.getText().toString())))).replace(",", ""));
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
        UpdateItem = (List<Cls_Sal_InvItems>) bundle.getSerializable("List");



            if (getArguments().getString("Scr") == "po") {
                if (filter.getText().toString().equals("")) {
                    query = "Select distinct invf.Item_No , invf.Item_Name,invf.Price, invf.tax   from invf  " +
                            "   where 1=1 ";
                } else {
                    query = "Select distinct  invf.Item_No , invf.Item_Name,invf.Price, invf.tax from  invf " +
                            "    where Item_Name  like '%" + filter.getText().toString() + "%'  or  Item_No like '%" + filter.getText().toString() + "%'  ";
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
                    cls_invf.setQty("0");
                    cls_invf.setBounce("0");
                    cls_invf_List.add(cls_invf);

                } while (c1.moveToNext());

            }
            c1.close();
        }

        Po_Fill_Item_Adapter cls_invf_adapter = new Po_Fill_Item_Adapter(
                getActivity(), cls_invf_List);
        items_Lsit.setAdapter(cls_invf_adapter);


    }
/*public  void Pop_Update_Qty(){
    Bundle bundle = new Bundle();
    bundle.putString("Scr", "SalesOrder");
    bundle.putString("Qty", "8");
    bundle.putString("Nm", "");
    bundle.putString("OrderNo","");

    FragmentManager Manager = getFragmentManager();
    Pop_Update_Qty obj = new Pop_Update_Qty();
    obj.setArguments(bundle);
    obj.show(Manager, null);
}*/
    public void fillUnit(String item_no) {


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


        final EditText dis = (EditText) form.findViewById(R.id.et_disc_per);
        if (v == cancel) {
            this.dismiss();
        }
        if (v == add) {
            EditText Price = (EditText) form.findViewById(R.id.et_price);
            final EditText final_P = (EditText) form.findViewById(R.id.et_price);
            EditText qty = (EditText) form.findViewById(R.id.et_qty);
            EditText tax = (EditText) form.findViewById(R.id.et_tax);

            EditText bo = (EditText) form.findViewById(R.id.et_bo);
            // Spinner sp_unite = (Spinner)form.findViewById(R.id.sp_units);
            EditText bounce = (EditText) form.findViewById(R.id.et_bo);
            EditText disc_per = (EditText) form.findViewById(R.id.et_disc_per);
            EditText disc_Amt = (EditText) form.findViewById(R.id.et_dis_Amt);

            EditText et_Discount = (EditText) form.findViewById(R.id.et_Discount);
            TextView tv_qty_perc = (TextView) form.findViewById(R.id.tv_qty_perc);
            TextView tv_StoreQty = (TextView) form.findViewById(R.id.tv_StoreQty);


            EditText net_total = (EditText) form.findViewById(R.id.et_totl);

            Double PriceAftertAx = 0.0;
            net_total.setError(null);
           // net_total.clearFocus();
            Price.setError(null);
          //  net_total.setError(null);
            qty.setError(null);
            tax.setError(null);

            Price.setError(null);
            net_total.setError(null);
            qty.setError(null);
            tax.setError(null);

/*
            if (Price.getText().toString().length() > 0 && SToD(Price.getText().toString()) > 0 && (SToD(Price.getText().toString()) < min_price)) {

                AlertDialog alertDialog = new AlertDialog.Builder(
                        getActivity()).create();
                alertDialog.setTitle("الحد الادني لبيع المادة ");

                alertDialog.setIcon(R.drawable.delete);
                alertDialog.setMessage("لقد تجاوزت الحد الادنى للسعر");//+ "   " + String.valueOf(min_price));
                // Setting OK Button
                alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //   final_P.setText(min_price.toString());


                    }
                });

                // Showing Alert Message
                alertDialog.show();
                return;
            }*/




             if (SToD(tv_qty_perc.getText().toString()) > 100 || SToD(tv_StoreQty.getText().toString() )<=0 ) {
                 android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(
                         getActivity()).create();
                 alertDialog.setTitle("طلب البيع");
                 alertDialog.setMessage("الكمية المطلوبة غير متوفرة");            // Setting Icon to Dialog
                 alertDialog.setIcon(R.drawable.tick);
                 alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int which) {

                     }
                 });
                 alertDialog.show();
                return;
             }



            if (SToD(net_total.getText().toString()) <= 0) {
                net_total.setError("الرجاء ادخال القيمة");
                net_total.requestFocus();
                return;
            }


            if (SToD(qty.getText().toString()) >= 9000) {
                qty.setError(getResources().getText(R.string.EnterValue));
                qty.requestFocus();
                return;
            }

            if ((SToD(Price.getText().toString()) == 100000) || Price.getText().toString().length() > 10 || (SToD(Price.getText().toString())).toString().contains("E")) {
                qty.setError(getResources().getText(R.string.EnterValue));
                Price.requestFocus();
                return;
            }
            if (Price.getText().toString().length() == 0) {
                qty.setError(getResources().getText(R.string.EnterValue));
                Price.requestFocus();
                return;
            }

            if (qty.getText().toString().length() == 0) {
                qty.setError(getResources().getText(R.string.EnterValue));
                qty.requestFocus();
                return;
            }
            if (tax.getText().toString().length() == 0) {
                qty.setError(getResources().getText(R.string.EnterValue));
                tax.requestFocus();
                return;
            }
        /* if( dis.getText().toString().length() == 0 ) {
             dis.setError("required!");
             dis.requestFocus();
             return;
         }*/

            if (ItemNo.toString().length() == 0) {

                return;
            }


            // PriceAftertAx=     (SToD(Price.getText().toString()) / ((SToD(tax.getText().toString()) / 100) + 1));
            // Price.setText    (  String.valueOf(  SToD(  PriceAftertAx.toString())));


            if (getArguments().getString("Scr") == "po") {
                ((OrdersItems) getActivity()).Save_List(ItemNo, Price.getText().toString().replace(",", ""), qty.getText().toString().replace(",", ""), tax.getText().toString().replace(",", ""), UnitNo, disc_per.getText().toString().replace(",", ""), bounce.getText().toString().replace(",", ""), str, UnitName, disc_Amt.getText().toString().replace(",", ""));

            } else if (getArguments().getString("Scr") == "Sal_inv") {


                if (UpdateItem != null) {
                    if (UpdateItem.size() > 0) {
                        ((Sale_InvoiceActivity) getActivity()).Update_List(ItemNo, Price.getText().toString(), qty.getText().toString(), tax.getText().toString(), UnitNo, disc_per.getText().toString(), bounce.getText().toString(), str, UnitName, disc_Amt.getText().toString(), Operand);
                    }
                } else {
                    ((Sale_InvoiceActivity) getActivity()).Save_List(ItemNo, Price.getText().toString(), qty.getText().toString(), tax.getText().toString(), UnitNo, disc_per.getText().toString(), bounce.getText().toString(), str, UnitName, disc_Amt.getText().toString(), Operand);

                }


            }

            try {
                min = 0;
                Price.requestFocus();
                Price.setText("0");
                qty.setText("");
                tax.setText("");
                bo.setText("");
                dis.setText("0");
                disc_per.setText("0");
                disc_Amt.setText("0");
                net_total.setText("0");
                ItemNo = "";
                UnitNo = "";
                Operand = "";
                tv_qty_perc.setText("");
                tv_StoreQty.setText("");
                et_Discount.setText("");
                rad_Amt.setChecked(true);

            } catch (Exception e) {
                e.printStackTrace();
            }

           EditText et_Search_filter = (EditText) form.findViewById(R.id.et_Search_filter);
            if(!et_Search_filter.getText().toString().equalsIgnoreCase(""))
            et_Search_filter.setText("");
        }

    }

    public void onListItemClick(ListView l, View v, int position, long id) {


        // Set the item as checked to be highlighted
        items_Lsit.setItemChecked(position, true);
        v.setBackgroundColor(Color.BLUE);

        //conversationAdapter.notifyDataSetChanged();

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
                Perc = (SToD(qty.getText().toString()) + SToD(bo.getText().toString())) / SToD(tv_StoreQty.getText().toString());
            }
        }
        Perc = (Perc * 100);
        tv_qty_perc.setText(String.valueOf(SToD(Perc.toString())));

    }


}


