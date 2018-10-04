
package com.cds_jo.pharmacyGI;

import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


import com.cds_jo.pharmacyGI.assist.Cls_Deptf_adapter;
import com.cds_jo.pharmacyGI.assist.Cls_Invf;
import com.cds_jo.pharmacyGI.assist.Cls_Invf_Adapter;
import com.cds_jo.pharmacyGI.assist.Cls_UnitItems;
import com.cds_jo.pharmacyGI.assist.Cls_UnitItems_Adapter;
import com.cds_jo.pharmacyGI.assist.CustomerReturnQtyActivity;
import com.cds_jo.pharmacyGI.assist.OrdersItems;
import com.cds_jo.pharmacyGI.assist.Sale_InvoiceActivity;
import com.cds_jo.pharmacyGI.assist.Cls_Deptf;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Hp on 07/02/2016.
 */

public class PopMenuItems extends DialogFragment implements View.OnClickListener  {
    public static final String CALCULATOR_PACKAGE ="com.android.calculator2";
    public static final String CALCULATOR_CLASS ="com.android.calculator2.Calculator";
    View form ;
    ImageButton OpenCal;
    Button add,cancel;
    ListView items_Lsit;
    TextView itemnm;
    TextView Store_Qty ;
    public String ItemNo = "";
    SqlHandler sqlHandler;
    float min = 0 ;
    EditText filter   ;
    ImageButton btn_filter_search ;
    String UnitNo ="";
    String Operand ="";

    String UnitName ="";
    String str= "";
    RadioButton rad_Per ;
    RadioButton rad_Amt;
    List<ContactListItems> UpdateItem ;
    private  Double SToD(String str){
        String f = "";
        final NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        final DecimalFormat df = (DecimalFormat)nf;
        str = str.replace(",","");
        Double d = 0.0;
        if (str.length()==0) {
            str = "0";
        }
        if (str.length()>0)
            try {
                d =  Double.parseDouble(str);
                str = df.format(d).replace(",","");

            }
            catch (Exception ex)
            {
                str="0";
            }

        df.setParseBigDecimal(true);

        d = Double.valueOf(str.trim()).doubleValue();

        return d;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

    }

    @Override
    public View onCreateView( LayoutInflater inflater   , ViewGroup container  ,Bundle savestate){

        form =inflater.inflate(R.layout.n_layout,container,false);

        getDialog().setTitle("Galaxy");
        add =(Button) form.findViewById(R.id.btn_add_item);
        add.setOnClickListener(this);
        cancel=(Button) form.findViewById(R.id.btn_cancel_item);
        OpenCal=(ImageButton) form.findViewById(R.id.btn_OpenCal);
        FillDeptf();
        cancel.setOnClickListener(this);
        OpenCal.setOnClickListener(this);


        TextView Store_Qty = (TextView)form.findViewById(R.id.tv_StoreQty);


        items_Lsit=(ListView) form.findViewById(R.id.listView2);
        final List<String> items_ls = new ArrayList<String>();
        final List<String> promotion_ls = new ArrayList<String>();

        final  EditText Price = (EditText)form.findViewById(R.id.et_price);
        final EditText qty = (EditText)form.findViewById(R.id.et_qty);
        EditText tax = (EditText)form.findViewById(R.id.et_tax);
        final EditText dis = (EditText)form.findViewById(R.id.et_disc_per);
        final EditText bo = (EditText)form.findViewById(R.id.et_bo);
        final  EditText dis_Amt = (EditText)form.findViewById(R.id.et_dis_Amt);
        final  EditText totl = (EditText)form.findViewById(R.id.et_totl);
        final  EditText Discount  = (EditText)form.findViewById(R.id.et_Discount);
        RadioGroup radGrp = (RadioGroup) form.findViewById(R.id.radDisc);
        int checkedRadioButtonID = radGrp.getCheckedRadioButtonId();
        final Double Total=0.0;

        final   NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        final DecimalFormat df = (DecimalFormat)nf;

        sqlHandler =  new SqlHandler(getActivity());


        dis_Amt.setFocusable(false);
        dis_Amt.setEnabled(false);
        dis_Amt.setCursorVisible(false);
        radGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup arg0, int id) {
                CalcDiscount();
            }


        });
        rad_Per=   (RadioButton) form.findViewById(R.id.rad_Per);
        rad_Amt = (RadioButton) form.findViewById(R.id.rad_Amt);



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

        Discount.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                Discount.setText("", TextView.BufferType.EDITABLE);
                return false;
            }
        });


      /*  Discount.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Discount.setText("", TextView.BufferType.EDITABLE);
                }
            }

        });*/
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

        bo.setOnFocusChangeListener( new View.OnFocusChangeListener() {

            public void onFocusChange( View v, boolean hasFocus ) {
                if( hasFocus ) {
                    bo.setText( "", TextView.BufferType.EDITABLE );
                }
            }

        } );

        //  btn_filter_search =(ImageButton) form.findViewById(R.id.brn_seachAcc);

        // onProgressUpdate("");
       /* btn_filter_search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

         FillItems();

            }
        });*/
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
        final Double Perc = 0.0 ;
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
            }
        });

        ListView lst_Promotion = (ListView)form.findViewById(R.id.lst_Promotion);

        String q = "Select  distinct * from Offers_Hdr ";
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    promotion_ls.add(c1.getString(c1.getColumnIndex("Offer_Name"))  );

                }while (c1.moveToNext());

            }
            c1.close();
        }
       /* promotion_ls.add("مع كل 50 باكيت بسكوت كناري تحصل على 2 باكيت مجانا");
        promotion_ls.add("مع كل 30 باكيت نسكافية تحصل على خصم 10% على قيمة الفاتورة النهائية");*/
        ArrayAdapter<String> promotion_ad = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_activated_1,promotion_ls);

        lst_Promotion.setAdapter(promotion_ad);

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
                EditText Price = (EditText) form.findViewById(R.id.et_price);
                NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
                DecimalFormat df = (DecimalFormat)nf;



                Price.setText(String.valueOf(df.format(Double.parseDouble(o.getPrice().toString().replace(",","")))));


                UnitNo=o.getUnitno().toString();
                UnitName=o.getUnitDesc().toString();
                Operand = o.getOperand().toString();
                min=Float.valueOf(o.getMin());
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
                arg1.setBackgroundColor(Color.GRAY);
                NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
                DecimalFormat df = (DecimalFormat) nf;

                Cls_Invf o = (Cls_Invf) items_Lsit.getItemAtPosition(position);

                EditText Price = (EditText) form.findViewById(R.id.et_price);
                EditText et_qty = (EditText) form.findViewById(R.id.et_qty);
                et_qty.setText("1");

                //Price.setText(df.format(Double.valueOf( o.getPrice())).toString());
                EditText tax = (EditText) form.findViewById(R.id.et_tax);
                tax.setText(o.getTax().toString());


                str = (String) o.getItem_Name();

                // Toast.makeText(getActivity(),str,Toast.LENGTH_LONG).show();
                getDialog().setTitle(str);
                fillUnit(o.getItem_No().toString());
                ItemNo = o.getItem_No().toString();

                // itemnm.setText(str);

                Price.setError(null);
                et_qty.setError(null);
                tax.setError(null);
                checkStoreQty();



            }
        });


        this.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return  form;
    }

    private void     checkStoreQty(){

        TextView Store_Qty = (TextView)form.findViewById(R.id.tv_StoreQty);

        if (getArguments().getString("Scr") == "Sal_inv") {



            Double Order_qty = 0.0;
            Double Res = 0.0;


            String query = "SELECT   ifnull( qty,0)   as  qty   from ManStore where  itemno = '" + ItemNo + "'  ";
            Cursor c1 = sqlHandler.selectQuery(query);

            Double Store_qty = 0.0;
            if (c1 != null && c1.getCount() != 0) {
                if (c1.getCount() > 0) {
                    c1.moveToFirst();
                    Store_qty = Double.parseDouble(c1.getString(c1.getColumnIndex("qty")));
                }

            c1.close();
        }
            query = "SELECT       (ifnull( sum  ( ifnull( sid.qty,0)  * (ifnull( sid.Operand,1))) ,0)  +   ifnull( sum  ( ifnull( sid.bounce_qty,0)  * (ifnull( sid.Operand,1))) ,0) +  ifnull( sum  ( ifnull( sid.Pro_bounce,0)  * (ifnull( sid.Operand,1))) ,0))  as Sal_Qty  from  Sal_invoice_Hdr  sih inner join Sal_invoice_Det sid on  sid.OrderNo = sih.OrderNo" +
                    " inner join  UnitItems ui on ui.item_no  = sid.itemNo and ui.unitno = sid.unitNo" +
                    "    where  sih.Post = -1 and ui.item_no ='" + ItemNo + "'";
            //   "    where  QtyStoreSer = "+MaxStoreQtySer+" and ui.item_no ='"+ItemNo+"'";


            if (UpdateItem != null && UpdateItem.size() > 0) {

                query = query + "And sid.OrderNo !='"+ getArguments().getString("OrderNo")+"'" ;
            }
            c1 = sqlHandler.selectQuery(query);

            Double Sal_Qty = 0.0;
            if (c1 != null && c1.getCount() != 0) {
                if (c1.getCount() > 0) {
                    c1.moveToFirst();
                    Sal_Qty = Double.parseDouble((c1.getString(c1.getColumnIndex("Sal_Qty"))).toString());
                }

            c1.close();
        }

            Res = Store_qty - Sal_Qty - Order_qty;
            Res = Res / SToD(Operand);
            Store_Qty.setText(Res.toString());
        }
        else {
            Store_Qty.setText("");
        }
    }




    private void CalcDiscount(){
        final EditText dis = (EditText)form.findViewById(R.id.et_disc_per);
        final  EditText totl = (EditText)form.findViewById(R.id.et_totl);
        final  EditText Discount  = (EditText)form.findViewById(R.id.et_Discount);
        final  EditText dis_Amt = (EditText)form.findViewById(R.id.et_dis_Amt);
        final  EditText et_totl = (EditText)form.findViewById(R.id.et_totl);

        final  EditText et_qty = (EditText)form.findViewById(R.id.et_qty);
        final  EditText et_price = (EditText)form.findViewById(R.id.et_price);

        Double pq = 0.0;
        pq=  SToD(et_price.getText().toString()) * SToD(et_qty.getText().toString());
        if (rad_Per.isChecked()) {
            Double Total = ((SToD(Discount.getText().toString().replace(",", "")) / 100)) *  pq ;

            Total = SToD(Total.toString());

            dis_Amt.setText(String.valueOf((Total)));
            dis.setText(String.valueOf(Discount.getText()));

        } else {


            Double Total = (SToD(Discount.getText().toString()) / pq )* 100;


            Total = SToD(Total.toString());


            dis.setText(Total.toString());
            dis_Amt.setText(String.valueOf(SToD(Discount.getText().toString())));
            //  df.format(Double.parseDouble(o.getPrice().toString().replace(",", "")))
        }
        if (totl.getText().toString().equals("0")) {
            dis.setText("0");
            dis_Amt.setText("0");
        }

        et_totl.setText(String.valueOf (SToD(String.valueOf( pq - SToD(dis_Amt.getText().toString())))));
    }

    private void   FillDeptf(){
        final Spinner sp_items_cat = (Spinner) form.findViewById(R.id.sp_item_cat);
        sqlHandler = new SqlHandler(getActivity());

        String  query = "Select   distinct Type_No , Type_Name , etname from deptf";
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
    private  void FillItems(){
        filter = (EditText) form.findViewById(R.id.et_Search_filter);
        String query = "";
        sqlHandler = new SqlHandler(getActivity());


        Bundle bundle = this.getArguments();
        UpdateItem=(List<ContactListItems>)bundle.getSerializable("List");

        if (UpdateItem != null) {

            if(   UpdateItem.size()>0){
                String s = UpdateItem.get(0).getno().toString();
                query = "Select  distinct invf.Item_No , invf.Item_Name,invf.Price, invf.tax   from invf    where   invf.Item_No ='"+UpdateItem.get(0).getno().toString()+"'";
                EditText Price = (EditText) form.findViewById(R.id.et_price);
                EditText qty = (EditText) form.findViewById(R.id.et_qty);
                EditText tax = (EditText) form.findViewById(R.id.et_tax);

                EditText bo = (EditText) form.findViewById(R.id.et_bo);
                // Spinner sp_unite = (Spinner)form.findViewById(R.id.sp_units);
                EditText  bounce = (EditText) form.findViewById(R.id.et_bo);
                EditText disc_per = (EditText) form.findViewById(R.id.et_disc_per);
                EditText disc_Amt = (EditText) form.findViewById(R.id.et_dis_Amt);

                EditText et_Discount = (EditText) form.findViewById(R.id.et_Discount);


                EditText net_total = (EditText) form.findViewById(R.id.et_totl);
                ItemNo= UpdateItem.get(0).getno();
                Price.setText(UpdateItem.get(0).getprice());
                qty.setText(UpdateItem.get(0).getQty());
                tax.setText(UpdateItem.get(0).getTax());
                getDialog().setTitle(UpdateItem.get(0).getName());
                bounce.setText(UpdateItem.get(0).getBounce());
                disc_per.setText(UpdateItem.get(0).getDiscount());
                disc_Amt.setText(UpdateItem.get(0).getDis_Amt());
                et_Discount.setText(UpdateItem.get(0).getDiscount());
                net_total.setText(UpdateItem.get(0).getTotal());
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
        }
        else {



            if (getArguments().getString("Scr") == "Sal_inv") {
                if (filter.getText().toString().equals("")) {
                    query = "Select distinct invf.Item_No , invf.Item_Name,invf.Price, invf.tax   from invf  " +
                            " inner join ManStore   on ManStore.itemno =   invf.Item_No And   CAST(  ifnull(ManStore.qty,0) as decimal)>0  where 1=1 ";
                } else {
                    query = "Select distinct  invf.Item_No , invf.Item_Name,invf.Price, invf.tax from  invf " +
                            " inner join ManStore   on ManStore.itemno =   invf.Item_No And   CAST(  ifnull(ManStore.qty,0) as decimal)>0  where Item_Name  like '%" + filter.getText().toString() + "%'  or  Item_No like '%" + filter.getText().toString() + "%'  ";
                }

                Spinner item_cat = (Spinner) form.findViewById(R.id.sp_item_cat);
                Integer indexValue = item_cat.getSelectedItemPosition();

                if (indexValue > 0) {

                    Cls_Deptf o = (Cls_Deptf) item_cat.getItemAtPosition(indexValue);

                    query = query + "and    Type_No = '" + o.getType_No().toString() + "'";

                }


            } else {
                if (filter.getText().toString().equals("")) {
                    query = "Select distinct  * from invf where 1=1 ";
                } else {
                    query = "Select  distinct * from invf where Item_Name  like '%" + filter.getText().toString() + "%'  or  Item_No like '%" + filter.getText().toString() + "%'  ";
                }



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

    public  void fillUnit(String item_no){


        Spinner sp_unite = (Spinner)form.findViewById(R.id.sp_units);
        sqlHandler = new SqlHandler(getActivity());

        String  query = "Select  distinct UnitItems.unitno ,UnitItems.Min ,UnitItems.price,Unites.UnitName " +
                " , ifnull(Operand,1) as Operand from UnitItems  left join  Unites on Unites.Unitno =UnitItems.unitno where UnitItems.item_no ='"+item_no+"' order by   UnitItems.unitno desc";
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

        if( cls_unitItemses.size()>0){
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
            startActivity(intent);
        }


        final EditText dis = (EditText) form.findViewById(R.id.et_disc_per);
        if (v == cancel) {
            this.dismiss();
        }
        if (v == add) {
            EditText Price = (EditText) form.findViewById(R.id.et_price);
            EditText qty = (EditText) form.findViewById(R.id.et_qty);
            EditText tax = (EditText) form.findViewById(R.id.et_tax);

            EditText bo = (EditText) form.findViewById(R.id.et_bo);
            // Spinner sp_unite = (Spinner)form.findViewById(R.id.sp_units);
            EditText bounce = (EditText) form.findViewById(R.id.et_bo);
            EditText disc_per = (EditText) form.findViewById(R.id.et_disc_per);
            EditText disc_Amt = (EditText) form.findViewById(R.id.et_dis_Amt);

            EditText et_Discount = (EditText) form.findViewById(R.id.et_Discount);
            TextView tv_qty_perc = (TextView)form.findViewById(R.id.tv_qty_perc);
            TextView tv_StoreQty = (TextView)form.findViewById(R.id.tv_StoreQty);



            EditText net_total = (EditText) form.findViewById(R.id.et_totl);

            Double PriceAftertAx = 0.0;
            net_total.setError(null);
            net_total.clearFocus();
            Price.setError(null);
            net_total.setError(null);
            qty.setError(null);
            tax.setError(null);

            Price.clearFocus();
            net_total.clearFocus();
            qty.clearFocus();
            tax.clearFocus();


            if ( SToD(net_total.getText().toString()  )<= 0) {
                net_total.setError("الرجاء ادخال القيمة");
                net_total.requestFocus();
                return;
            }


            if ( SToD(qty.getText().toString())>= 9000) {
                qty.setError("الرجاء ادخال القيمة");
                qty.requestFocus();
                return;
            }

            if ((SToD(Price.getText().toString()) == 100000) ||  Price.getText().toString().length()>10  ||  (SToD(Price.getText().toString())).toString().contains("E") ) {
                Price.setError("الرجاء التأكد من القيمة");
                Price.requestFocus();
                return;
            }
            if (Price.getText().toString().length() == 0) {
                Price.setError("الرجاء ادخال القيمة");
                Price.requestFocus();
                return;
            }

            if (qty.getText().toString().length() == 0) {
                qty.setError("الرجاء ادخال القيمة");
                qty.requestFocus();
                return;
            }
            if (tax.getText().toString().length() == 0) {
                tax.setError("الرجاء ادخال القيمة");
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
                ((OrdersItems) getActivity()).Save_List(ItemNo, Price.getText().toString(), qty.getText().toString(), tax.getText().toString(), UnitNo, disc_per.getText().toString(), bounce.getText().toString(), str, UnitName, disc_Amt.getText().toString());

            } else if (getArguments().getString("Scr") == "Sal_inv") {


                if (UpdateItem != null) {
                    if (UpdateItem.size() > 0) {
                        ((Sale_InvoiceActivity) getActivity()).Update_List(ItemNo, Price.getText().toString(), qty.getText().toString(), tax.getText().toString(), UnitNo, disc_per.getText().toString(), bounce.getText().toString(), str, UnitName, disc_Amt.getText().toString(), Operand);
                    }
                } else {
                    ((Sale_InvoiceActivity) getActivity()).Save_List(ItemNo, Price.getText().toString(), qty.getText().toString(), tax.getText().toString(), UnitNo, disc_per.getText().toString(), bounce.getText().toString(), str, UnitName, disc_Amt.getText().toString(),Operand);

                }




            } else if (getArguments().getString("Scr") == "RetnQty") {

                if (UpdateItem != null) {
                    if (UpdateItem.size() > 0) {
                        ((CustomerReturnQtyActivity) getActivity()).Update_List(ItemNo, Price.getText().toString(), qty.getText().toString(), tax.getText().toString(), UnitNo, disc_per.getText().toString(), bounce.getText().toString(), str, UnitName, disc_Amt.getText().toString());

                    }
                } else {
                    ((CustomerReturnQtyActivity) getActivity()).Save_List(ItemNo, Price.getText().toString(), qty.getText().toString(), tax.getText().toString(), UnitNo, disc_per.getText().toString(), bounce.getText().toString(), str, UnitName, disc_Amt.getText().toString());
                }


            }

            try {
                min = 0;
                Price.requestFocus();
                Price.setText("0");
                qty.setText("");
                tax.setText("");
                bo.setText("");
                dis.setText("");
                disc_per.setText("");
                disc_Amt.setText("");
                net_total.setText("");
                ItemNo = "";
                UnitNo = "";
                Operand = "";
                tv_qty_perc.setText("");
                tv_StoreQty.setText("");
                et_Discount.setText("");


            } catch (Exception e) {
                e.printStackTrace();
            }



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
        Double Perc=0.0;
        EditText Price = (EditText)form.findViewById(R.id.et_price);
        EditText qty = (EditText)form.findViewById(R.id.et_qty);
        EditText tax = (EditText)form.findViewById(R.id.et_tax);
        EditText dis = (EditText)form.findViewById(R.id.et_disc_per);
        EditText bo = (EditText)form.findViewById(R.id.et_bo);
        EditText net_total = (EditText)form.findViewById(R.id.et_totl);


        String str_p,str_q ;


        TextView tv_qty_perc = (TextView)form.findViewById(R.id.tv_qty_perc);
        TextView tv_StoreQty = (TextView)form.findViewById(R.id.tv_StoreQty);
        if (tv_StoreQty.getText().toString()==""){
            Perc=0.0;
        }else {
            Perc=SToD(qty.getText().toString()) / SToD(tv_StoreQty.getText().toString());
        }
        Perc = (Perc*100);
        tv_qty_perc.setText( String.valueOf( SToD(Perc.toString())));

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


