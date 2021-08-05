package com.cds_jo.pharmacyGI;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cds_jo.pharmacyGI.assist.Cls_Deptf;
import com.cds_jo.pharmacyGI.assist.Cls_Deptf_adapter;
import com.cds_jo.pharmacyGI.assist.Cls_Invf;
import com.cds_jo.pharmacyGI.assist.Cls_UnitItems;
import com.cds_jo.pharmacyGI.assist.Cls_UnitItems_Adapter;
import com.cds_jo.pharmacyGI.assist.OrdersItems;
import com.cds_jo.pharmacyGI.assist.Po_ListItemAdapter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Methdes.MethodToUse;
import Methdes.MyTextView;

public class Pop_Po_Select_Items_New_Activity extends FragmentActivity {

    Button add, cancel, prevBtn, nextBtn;
    CheckBox btn_All_Items ,btn_Sort_by_ItemNo, btn_Sort_by_ItemNm, btn_Sort_by_Order_Qty,btn_Sort_Offeres , btn_OderQty;

    ListView items_Lsit;
    TextView itemnm;
    int RowNumber = 1, PageNumber = 1,ListIndex;

    public int TOTAL_NUM_ITEMS = 60;
    public int ITEMS_PER_PAGE = 9;
    public int ITEMS_REMAINING = TOTAL_NUM_ITEMS % ITEMS_PER_PAGE;
    public int LAST_PAGE = TOTAL_NUM_ITEMS / ITEMS_PER_PAGE;
    int currentPage = 0;
    ArrayList<Cls_Invf> cls_invf_List;
    public String ItemNo = "";
    SqlHandler sqlHandler;
    Po_ListItemAdapter po_listItemAdapter;
    float min = 0;
    Double min_price = 0.0;
    EditText filter;
    ImageButton btn_filter_search;
    String UnitNo = "";
    String Operand = "";
    Cls_Qty_Batch cls_qty_batch;
    ArrayList<Cls_Qty_Batch> cls_qty_batches;
    String UnitName = "";
    String str = "";
    int OrderBy = -1;
    TextView tv_PageNumer;
    String  CustQtyOrder;
    EditText et_totl;
    RecyclerView rv;
    MyTextView tv_ExpDate, tv_Batch;
    List<String> items_Filter;
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
    ListView Lstitems_Filter;

    String CustOrderNo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        setContentView(R.layout.activity_pop__po__select__items__new_);
               items_Filter = new ArrayList<String>();

            sqlHandler = new SqlHandler(this);
            add = (Button) findViewById(R.id.btn_add_item);

            cancel = (Button) findViewById(R.id.btn_cancel_item);
            Lstitems_Filter=(ListView) findViewById(R.id.lstFilter);

            nextBtn = (Button) findViewById(R.id.nextBtn);
            prevBtn = (Button) findViewById(R.id.prevBtn);

            MyTextView  textView254 = (MyTextView) findViewById(R.id.textView254);
            textView254.setText(  getIntent().getExtras().getString("CustNm"));

            btn_Sort_by_ItemNo = (CheckBox) findViewById(R.id.Sort_by_ItemNo);
            btn_Sort_by_ItemNm = (CheckBox) findViewById(R.id.Sort_by_ItemNm);
            btn_Sort_by_Order_Qty = (CheckBox) findViewById(R.id.Sort_by_Pure);
            btn_Sort_Offeres = (CheckBox) findViewById(R.id.Sort_Offeres);
            btn_All_Items = (CheckBox) findViewById(R.id.All_Items);
            btn_OderQty = (CheckBox) findViewById(R.id.OderQty);


            btn_Sort_by_ItemNo.setTypeface(MethodToUse.SetTFace(Pop_Po_Select_Items_New_Activity.this));
            btn_Sort_by_ItemNm.setTypeface(MethodToUse.SetTFace(Pop_Po_Select_Items_New_Activity.this));
            btn_Sort_by_Order_Qty.setTypeface(MethodToUse.SetTFace(Pop_Po_Select_Items_New_Activity.this));
            btn_Sort_Offeres.setTypeface(MethodToUse.SetTFace(Pop_Po_Select_Items_New_Activity.this));
            btn_OderQty.setTypeface(MethodToUse.SetTFace(Pop_Po_Select_Items_New_Activity.this));


            tv_PageNumer = (TextView) findViewById(R.id.tv_PageNumer);

            rv = (RecyclerView) findViewById(R.id.rv);
            rv.setLayoutManager(new LinearLayoutManager(Pop_Po_Select_Items_New_Activity.this));
            et_totl = (EditText) findViewById(R.id.et_totl);
            et_totl.setText("0.000");
            int mIndex = 0;
            int mIndexCount = 0;

            add.setTypeface(MethodToUse.SetTFace(Pop_Po_Select_Items_New_Activity.this));
            cancel.setTypeface(MethodToUse.SetTFace(Pop_Po_Select_Items_New_Activity.this));


            tv_ExpDate = (MyTextView) findViewById(R.id.tv_ExpDate);
            tv_Batch = (MyTextView) findViewById(R.id.tv_Batch);
            cls_qty_batches = new ArrayList<Cls_Qty_Batch>();
            //   FillTempDate();
            FillDeptf();


            items_Lsit = (ListView) findViewById(R.id.listView2);
            final List<String> items_ls = new ArrayList<String>();
            final List<String> promotion_ls = new ArrayList<String>();

            final EditText Price = (EditText) findViewById(R.id.et_price);


            final EditText qty = (EditText) findViewById(R.id.et_qty);
            EditText tax = (EditText) findViewById(R.id.et_tax);


            final NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
            final DecimalFormat df = (DecimalFormat) nf;


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


            filter = (EditText) findViewById(R.id.et_Search_filter);

            filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    filter.setText("");
                    //  ShowFiltersPop();
                }
            });


            filter.requestFocus();


            filter.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    currentPage = 0;
                    FillItems();


                }
            });
            CustQtyOrder="";

            FillItems();
            fillUnit("-1");

            final Spinner item_cat = (Spinner) findViewById(R.id.sp_item_cat);

            item_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    filter.setText("");
                    //ShowFiltersPop();
                    FillItems();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            Lstitems_Filter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    Cls_Filter customers = (Cls_Filter) arg0.getItemAtPosition(position);
                    Set_Filter(customers.getDesc() );

                }


            });
            final Spinner sp_unite = (Spinner) findViewById(R.id.sp_units);

            sp_unite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    Cls_UnitItems o = (Cls_UnitItems) sp_unite.getItemAtPosition(position);
                    EditText Price = (EditText) findViewById(R.id.et_price);
                    NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
                    DecimalFormat df = (DecimalFormat) nf;

                    Double CusPrice = 0.0;

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
                    //    checkStoreQty();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });


            qty.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        qty.setText("", TextView.BufferType.EDITABLE);
                    }
                }

            });

            nextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    currentPage += 1;
                    FillItems();
                    GenrateListPaging();

                }
            });

            prevBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    currentPage -= 1;
                    FillItems();
                    GenrateListPaging();
                }
            });



        }catch (Exception ex ){
            Toast.makeText(this,ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


    }

    private  void  ShowFiltersPop(){


        String q= "";
        Spinner item_cat = (Spinner) findViewById(R.id.sp_item_cat);
        Integer indexValue = item_cat.getSelectedItemPosition();
       if( indexValue>0) {
           Cls_Deptf o = (Cls_Deptf) item_cat.getItemAtPosition(indexValue);
           if (indexValue > 0) {
               q = "SELECT * from deptf_Filter where Type_No ='" + o.getType_No().toString() + "'";
           Cursor c = sqlHandler.selectQuery(q);
           if (c != null && c.getCount() > 0) {
               c.close();
               Bundle bundle = new Bundle();
               bundle.putString("Scr", "po");
               bundle.putString("Type_No", o.getType_No().toString());
               FragmentManager Manager = getFragmentManager();
               Pop_Select_Filters obj = new Pop_Select_Filters();
               obj.setArguments(bundle);
               obj.show(Manager, null);
           }
           }
       }
    }
    public void Set_Filter(String Desc){
        filter.setText(Desc);
    }
    private  void Save_Items(){
    Cls_Invf cls_invf;
    String q ;
    for (int i = 0 ;i <cls_invf_List.size();i++)
        {
              cls_invf = new Cls_Invf();
            cls_invf = cls_invf_List.get(i);
            q=cls_invf.getItem_No();
            if(Integer.parseInt( cls_invf.getQty())> 0 || Integer.parseInt( cls_invf.getBounce())>0){
             q = " Update TempOrderItems set Qty ='" +cls_invf.getQty() + "',Bounce='"+cls_invf.getBounce()+"'  Where ItemNo = '" + cls_invf.getItem_No()+ "'";
             Toast.makeText(this,"تخزين",Toast.LENGTH_SHORT).show();
             sqlHandler.executeQuery(q);

            }
        }



}
    private void FillTempDate() {
        String q = "Delete From TempOrderItems";
        sqlHandler.executeQuery(q);
        q = " Delete From sqlite_sequence where name='TempOrderItems'";
        sqlHandler.executeQuery(q);


        q = "Insert into TempOrderItems (ItemNo,ItemNm,Qty,Bounce, Price ,tax,Type_No) " +
                "SELECT  distinct   i.Item_No,i.Item_Name , ifnull(dt.qty,'0') as qty ,  ifnull(dt.bounce_qty,'0') as bounce" +
                "  , i.Price ,i.tax , i.Type_No From invf i Left join Po_dtl dt " +
                "  on dt.itemno = i.Item_No  and  dt.orderno='" + getIntent().getExtras().getString("OrderNo") + "' Where cast(i.Price as integer ) >0 ";
        sqlHandler.executeQuery(q);
    }

    @SuppressLint("Range")
    private void get_min_price() {
        min_price = 0.0;
        String CatNo = "";
        CatNo = "";
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

    @SuppressLint("Range")
    public Double checkStoreQty(String No, String qq) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Double Order_qty = SToD(qq);
        Double Res = 0.0;

        String query = "SELECT   ifnull( qty,0)   as  qty   " +
                " From ManStore where  itemno = '" + No + "'  ";
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
                "   where   sih.Posted = -1  and ui.item_no ='" + No + "'  ";


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
       /* if (SToD(Operand) == 0) {
            Res = 0.0;
        } else {
            Res = Res / SToD(Operand);
        }*/
        //  GetQtyPerc();
        return Res;
    }

    @SuppressLint("Range")
    public int GetAvQty(String No, String Qty) {


        int Res = 0;

        try {
            if (Qty.toString().equalsIgnoreCase(""))
                Qty = "0";
            int Temp = Integer.parseInt(Qty);
            String query = "SELECT cast(  ifnull( qty,0) as integer)  as  qty   " +
                    " From ManStore where  itemno = '" + No + "'  ";
            Cursor c1 = sqlHandler.selectQuery(query);

            int Store_qty = 0;
            if (c1 != null && c1.getCount() != 0) {
                try {
                    if (c1.getCount() > 0) {

                        c1.moveToFirst();
                        Store_qty = Integer.parseInt(c1.getString(c1.getColumnIndex("qty")));
                        c1.close();
                    }
                } catch (Exception exception) {
                    Store_qty = 0;
                }

            }


            query = "   SELECT  cast ( ifnull(  sum ( ifnull( sid.qty,0)  +   ifnull( sid.bounce_qty,0) ),0)   as integer)   as  Sal_Qty  " +
                    "  from  Po_Hdr  sih inner join Po_dtl sid on  sid.OrderNo = sih.OrderNo" +
                    "   inner join  UnitItems ui on ui.item_no  = sid.itemNo and ui.unitno = sid.unitNo" +
                    "   where   sih.Posted = -1  and ui.item_no ='" + No + "' and  sih.orderno !='" + getIntent().getExtras().getString("OrderNo") + "'";


            c1 = sqlHandler.selectQuery(query);

            int Sal_Qty = 0;
            if (c1 != null && c1.getCount() != 0) {
                if (c1.getCount() > 0) {
                    c1.moveToFirst();
                    Sal_Qty = Integer.parseInt((c1.getString(c1.getColumnIndex("Sal_Qty"))).toString());
                }
                c1.close();
            }
            Res = Store_qty - Sal_Qty - Temp;
        } catch (Exception ex) {
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("طلب البيع");
            alertDialog.setMessage(ex.getMessage().toString());            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.show();
        }


        return Res;
    }

    @SuppressLint("Range")
    private void FillDeptf() {
        try {
            final Spinner sp_items_cat = (Spinner) findViewById(R.id.sp_item_cat);

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
                    this, cls_deptfs);

            sp_items_cat.setAdapter(cls_unitItems_adapter);
        }catch (Exception ex){

        }
    }

    @SuppressLint("Range")
    private void FillItems() {
        try {
            filter = (EditText) findViewById(R.id.et_Search_filter);
            String query = "";

            rv.setAdapter(null);

            if (filter.getText().toString().equals("")) {
                query = "Select distinct hdr.ItemNo , hdr.ItemNm,hdr.Price, hdr.tax,hdr.Qty as qty ,hdr.Bounce  as bounce ,ifnull(hdr.Pack,'0') as CustQty  from TempOrderItems hdr   " +
                        " Where 1=1 ";
            } else {
                query = "Select distinct hdr.ItemNo , hdr.ItemNm,hdr.Price, hdr.tax,hdr.Qty as qty ,hdr.Bounce  as bounce ,ifnull(hdr.Pack,'0') as CustQty from TempOrderItems hdr    " +
                       "  where    ( hdr.ItemNm  like '%" + filter.getText().toString() + "%'  or  hdr.ItemNo like '%" + filter.getText().toString() + "%')   ";
            }

            Spinner item_cat = (Spinner) findViewById(R.id.sp_item_cat);
            Integer indexValue = item_cat.getSelectedItemPosition();



            if (OrderBy == 1) {
                query = query + "and    Type_No = '1'";
            } else if (OrderBy == 3) {
                query = query + "and    Type_No = '3'";
            } else if (OrderBy == 4) {
                query = query + "and    Type_No = '4'";
            } else if (OrderBy == 5) {
                query = query + " and    hdr.ItemNo  in ( SELECT  Search_Key from deptf_Filter WHERE Type_No = '-2')";
            } else if (OrderBy == 6) {
                    query = query + " and     CAST( hdr.Qty AS integer) >0 ";
                }


                query = query + " order by  ItemNm  asc";

            cls_invf_List = new ArrayList<Cls_Invf>();

            cls_invf_List.clear();

            RowNumber = 1;
            ListIndex=0;
            cls_invf_List.clear();
            Cursor c1 = sqlHandler.selectQuery(query);
            if (c1 != null && c1.getCount() != 0) {
                if (c1.moveToFirst()) {
                    do {
                        Cls_Invf cls_invf = new Cls_Invf();
                        cls_invf.setItem_No(c1.getString(c1
                                .getColumnIndex("ItemNo")));
                        cls_invf.setItem_Name(c1.getString(c1
                                .getColumnIndex("ItemNm")));
                        cls_invf.setPrice(c1.getString(c1
                                .getColumnIndex("Price")));
                        cls_invf.setTax(c1.getString(c1
                                .getColumnIndex("tax")));
                        cls_invf.setQty(c1.getString(c1.getColumnIndex("qty")));
                        cls_invf.setBounce(c1.getString(c1.getColumnIndex("bounce")));
                        cls_invf.setCustQty(c1.getString(c1.getColumnIndex("CustQty")));
                        cls_invf.setRowNumber(RowNumber);
                        cls_invf.setListIndex(ListIndex+"");
                        RowNumber = RowNumber + 1;
                        ListIndex=ListIndex+1;
                        cls_invf_List.add(cls_invf);

                    } while (c1.moveToNext());

                }
                c1.close();
            }

            if (cls_invf_List.size() > 0)
                GenrateListPaging();
        }catch (Exception ex){
            Toast.makeText(this,ex.getMessage().toString(),Toast.LENGTH_SHORT).show();

        }

    }

    private void GenrateListPaging() {
        //  Toast.makeText(this,"Next",Toast.LENGTH_SHORT).show();
        int PageListIndex = 0;
        ArrayList<Cls_Invf> ListPage = new ArrayList<Cls_Invf>();
        ListPage.clear();
        Cls_Invf obj;
        TOTAL_NUM_ITEMS = cls_invf_List.size();
        ITEMS_REMAINING = TOTAL_NUM_ITEMS % ITEMS_PER_PAGE;
        LAST_PAGE = TOTAL_NUM_ITEMS / ITEMS_PER_PAGE;
        int startItem = currentPage * ITEMS_PER_PAGE;//+1;
        int numOfData = ITEMS_PER_PAGE;


        int totalPages = TOTAL_NUM_ITEMS / ITEMS_PER_PAGE;
        totalPages = (int) Math.ceil(TOTAL_NUM_ITEMS / (float) ITEMS_PER_PAGE);


        if (totalPages == 0) {
            tv_PageNumer.setText("0/0");
        }
        if (currentPage >= totalPages) {
            currentPage = totalPages - 1;
            nextBtn.setEnabled(false);
            return;
        }
        //   tv_PageNumer.setText ((currentPage +1)+"/"+(totalPages));
        tv_PageNumer.setText((currentPage + 1) + "/" + (totalPages));


        if (currentPage == totalPages) {
            nextBtn.setEnabled(false);
            prevBtn.setEnabled(true);
        } else if (currentPage == 0) {
            prevBtn.setEnabled(false);
            nextBtn.setEnabled(true);
        } else if (currentPage >= 1 && currentPage <= totalPages) {
            nextBtn.setEnabled(true);
            prevBtn.setEnabled(true);
        }


        if (currentPage == LAST_PAGE && ITEMS_REMAINING > 0) {
            for (int i = startItem; i < startItem + ITEMS_REMAINING; i++) {
                obj = new Cls_Invf();
                obj.setRowNumber(PageListIndex);
                obj.setItem_No(cls_invf_List.get(i).getItem_No());
                obj.setItem_Name(cls_invf_List.get(i).getItem_Name());
                obj.setPrice(cls_invf_List.get(i).getPrice());
                obj.setTax(cls_invf_List.get(i).getTax());
                obj.setQty(cls_invf_List.get(i).getQty());
                obj.setQty(cls_invf_List.get(i).getQty());
                obj.setBounce(cls_invf_List.get(i).getBounce());
                obj.setCustQty(cls_invf_List.get(i).getCustQty());
                PageListIndex = PageListIndex + 1;
                ListPage.add(obj);
            }
        } else {
            int lastItem = startItem + numOfData;
            if (lastItem > TOTAL_NUM_ITEMS)
                lastItem = TOTAL_NUM_ITEMS;
            for (int i = startItem; i < lastItem; i++) {
                if (i >= 0) {
                    obj = new Cls_Invf();
                    obj.setRowNumber(PageListIndex);
                    obj.setItem_No(cls_invf_List.get(i).getItem_No());
                    obj.setItem_Name(cls_invf_List.get(i).getItem_Name());
                    obj.setPrice(cls_invf_List.get(i).getPrice());
                    obj.setTax(cls_invf_List.get(i).getTax());
                    obj.setQty(cls_invf_List.get(i).getQty());
                    obj.setBounce(cls_invf_List.get(i).getBounce());
                    obj.setCustQty(cls_invf_List.get(i).getCustQty());
                    PageListIndex = PageListIndex + 1;
                    ListPage.add(obj);
                }
            }
        }
        po_listItemAdapter = new Po_ListItemAdapter(this, ListPage);
        rv.setAdapter(po_listItemAdapter);
    }

    /*private  void GenrateListPages( ){
        int PageListIndex= 0 ;
        ArrayList<Cls_Invf> ListPage = new ArrayList<Cls_Invf>();
        ListPage.clear();
        Cls_Invf obj ;
        TOTAL_NUM_ITEMS = cls_invf_List.size();
        ITEMS_REMAINING=TOTAL_NUM_ITEMS % ITEMS_PER_PAGE;
        LAST_PAGE=TOTAL_NUM_ITEMS/ITEMS_PER_PAGE;
        int startItem=currentPage*ITEMS_PER_PAGE;//+1;
        int numOfData=ITEMS_PER_PAGE;



        int  totalPages =  TOTAL_NUM_ITEMS /  ITEMS_PER_PAGE;
        totalPages= (int) Math.ceil( TOTAL_NUM_ITEMS /  (float)ITEMS_PER_PAGE);
        if (currentPage >= totalPages) {
            nextBtn.setEnabled(false);
            return;
        }

        tv_PageNumer.setText ((currentPage +1)+"/"+(totalPages));

        if (currentPage == totalPages) {
            nextBtn.setEnabled(false);
            prevBtn.setEnabled(true);
        } else if (currentPage == 0) {
            prevBtn.setEnabled(false);
            nextBtn.setEnabled(true);
        } else if (currentPage >= 1 && currentPage <= totalPages) {
            nextBtn.setEnabled(true);
            prevBtn.setEnabled(true);
        }


        if (currentPage==LAST_PAGE && ITEMS_REMAINING>0)
        {
            for (int i=startItem;i<startItem+ITEMS_REMAINING;i++)
            {
                obj=new Cls_Invf();
                obj.setRowNumber(PageListIndex);
                obj.setItem_No(cls_invf_List.get(i).getItem_No());
                obj.setItem_Name(cls_invf_List.get(i).getItem_Name());
                obj.setPrice(cls_invf_List.get(i).getPrice());
                obj.setTax(cls_invf_List.get(i).getTax());
                obj.setQty(cls_invf_List.get(i).getQty());
                obj.setBounce(cls_invf_List.get(i).getBounce());


                PageListIndex=PageListIndex+1;
                ListPage.add(obj);
            }
        }else
        {
            int lastItem = startItem+numOfData;
            if (lastItem >TOTAL_NUM_ITEMS)
                lastItem=TOTAL_NUM_ITEMS;
            for (int i=startItem;i<lastItem;i++)
            {
                obj=new Cls_Invf();
                obj.setRowNumber(PageListIndex);
                obj.setItem_No(cls_invf_List.get(i).getItem_No());
                obj.setItem_Name(cls_invf_List.get(i).getItem_Name());
                obj.setPrice(cls_invf_List.get(i).getPrice());
                obj.setTax(cls_invf_List.get(i).getTax());
                obj.setQty(cls_invf_List.get(i).getQty());
                obj.setBounce(cls_invf_List.get(i).getBounce());
                PageListIndex=PageListIndex+1;
                ListPage.add(obj);
            }
        }



        po_listItemAdapter=new Po_ListItemAdapter(this,ListPage);

        rv.setAdapter(po_listItemAdapter);


    }*/
    @SuppressLint("Range")
    public void fillUnit(String item_no) {


        Spinner sp_unite = (Spinner) findViewById(R.id.sp_units);

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
                this, cls_unitItemses);

        sp_unite.setAdapter(cls_unitItems_adapter);

        if (cls_unitItemses.size() > 0) {
            Cls_UnitItems o = (Cls_UnitItems) sp_unite.getItemAtPosition(0);
            UnitNo = o.getUnitno().toString();
            UnitName = o.getUnitDesc().toString();
            Operand = o.getOperand().toString();
            min = Float.valueOf(o.getMin());
        }
    }

    public void onListItemClick(ListView l, View v, int position, long id) {


        // Set the item as checked to be highlighted
        items_Lsit.setItemChecked(position, true);
        v.setBackgroundColor(Color.BLUE);

        //conversationAdapter.notifyDataSetChanged();

    }

    @SuppressLint("Range")
    public double Calc_Total(String ItemNo, String Qty) {
        //   Toast.makeText(this,ItemNo,Toast.LENGTH_SHORT).show();
       /* Double R =  checkStoreQty(ItemNo,Qty);
        if (R < 0)
            return R;*/

        String q = "select   sum( cast( Qty as float)    *  cast(  Price as float ) )as t from TempOrderItems ";
        Cursor c = sqlHandler.selectQuery(q);

        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            et_totl.setText(c.getString(c.getColumnIndex("t")));
            c.close();
        }
        et_totl.setText(SToD(et_totl.getText().toString()) + "");


        return 1;// R;
    }

    private void GetQtyPerc() {

        Double Perc = 0.0;
        EditText qty = (EditText) findViewById(R.id.et_qty);
        EditText bo = (EditText) findViewById(R.id.et_bo);
        TextView tv_qty_perc = (TextView) findViewById(R.id.tv_qty_perc);
        TextView tv_StoreQty = (TextView) findViewById(R.id.tv_StoreQty);

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

    public void cc(View view) {
        Toast.makeText(this, "dd", Toast.LENGTH_SHORT).show();
    }

    public void btn_Add_All_Items(View view) {
        Save_All_Items();
    }
    private  void Save_All_Items(){
    try {
        Intent intent = new Intent(this, OrdersItems.class);
        intent.putExtra("FillItem", "1");
        intent.putExtra("OrderNo", getIntent().getExtras().getString("OrderNo"));
       // intent.putExtra("OrderNo", getIntent().getExtras().getString("OrderNo"));
        intent.putExtra("CustNm", getIntent().getExtras().getString("CustNm"));
        intent.putExtra("accno", getIntent().getExtras().getString("accno"));
        intent.putExtra("Tax_Include", getIntent().getExtras().getString("Tax_Include"));
        intent.putExtra("MobileOrder", getIntent().getExtras().getString("MobileOrder"));
        intent.putExtra("Pay_Method", getIntent().getExtras().getString("Pay_Method"));


        startActivity(intent);
        finish();//finishing activity


    }catch (Exception ex){}
}
    public void btn_Back(View view) {
        try {
            Intent intent = new Intent(this, OrdersItems.class);
            intent.putExtra("FillItem", "2");
            intent.putExtra("OrderNo", getIntent().getExtras().getString("OrderNo"));
           // intent.putExtra("OrderNo", getIntent().getExtras().getString("OrderNo"));
            intent.putExtra("CustNm", getIntent().getExtras().getString("CustNm"));
            intent.putExtra("accno", getIntent().getExtras().getString("accno"));
            intent.putExtra("Tax_Include", getIntent().getExtras().getString("Tax_Include"));
            intent.putExtra("MobileOrder", getIntent().getExtras().getString("MobileOrder"));
            intent.putExtra("Pay_Method", getIntent().getExtras().getString("Pay_Method"));


           // startActivity(intent);
            finish();//finishing activity

        }catch (Exception ex){}
    }
    public void btn_Sort_Seba_Med(View view) {
        OrderBy = 1;
        Set_Filter("" );
       // FillItems();
        onProgressUpdate();
        //btn_Sort_by_ItemNo.setChecked(false);
        btn_Sort_by_ItemNm.setChecked(false);
        btn_Sort_by_Order_Qty.setChecked(false);
        btn_All_Items.setChecked(false);
        btn_Sort_Offeres.setChecked(false);
        btn_OderQty.setChecked(false);


    }
    public void  Sort_by_Aloe_Fresh(View view) {
        OrderBy = 3;
        Set_Filter("" );
       // FillItems();
        onProgressUpdate();
        btn_Sort_by_ItemNo.setChecked(false);
        //btn_Sort_by_ItemNm.setChecked(false);
        btn_Sort_by_Order_Qty.setChecked(false);
        btn_All_Items.setChecked(false);
        btn_Sort_Offeres.setChecked(false);
        btn_OderQty.setChecked(false);
    }
    public void Sort_by_Pure(View view) {
        OrderBy = 4;
        //FillItems();
        Set_Filter("" );
        onProgressUpdate();
        btn_Sort_by_ItemNo.setChecked(false);
        btn_Sort_by_ItemNm.setChecked(false);
      //  btn_Sort_by_Order_Qty.setChecked(false);
        btn_All_Items.setChecked(false);
        btn_Sort_Offeres.setChecked(false);
        btn_OderQty.setChecked(false);
    }
    public void Sort_by_Offers(View view) {
        OrderBy = 5;
        //FillItems();
        Set_Filter("" );
        onProgressUpdate();
        btn_Sort_by_ItemNo.setChecked(false);
        btn_Sort_by_ItemNm.setChecked(false);
        btn_All_Items.setChecked(false);
        btn_Sort_by_Order_Qty.setChecked(false);
        btn_OderQty.setChecked(false);
    }
    public void Sort_by_Qty(View view) {
        OrderBy = 6;
        //FillItems();
        Set_Filter("" );
        onProgressUpdate();
        btn_Sort_by_ItemNo.setChecked(false);
        btn_Sort_by_ItemNm.setChecked(false);
        btn_All_Items.setChecked(false);
        btn_Sort_by_Order_Qty.setChecked(false);
        btn_Sort_Offeres.setChecked(false);
    }
    public void Btn_Show_Filters(View view) {
        ShowFiltersPop();
    }
    public void All_Items(View view) {
        OrderBy = -1;
        Set_Filter("" );
       // FillItems();
        onProgressUpdate();
        btn_Sort_by_ItemNo.setChecked(false);
        btn_Sort_by_ItemNm.setChecked(false);
        btn_Sort_by_Order_Qty.setChecked(false);
        btn_Sort_Offeres.setChecked(false);
        btn_OderQty.setChecked(false);

       // btn_All_Items.setChecked(false);

    }
    @SuppressLint("Range")
    public void onProgressUpdate( ){
        Lstitems_Filter.setAdapter(null);
        String query ;
        query = "Select distinct Search_Key from deptf_Filter where Type_No ='" +OrderBy+"'";
        Cursor c = sqlHandler.selectQuery(query);
        ArrayList<Cls_Filter> customersesList = new ArrayList<Cls_Filter>();
        customersesList.clear();
        if (c!=null && c.getCount()!=0 ){
            if(c.moveToFirst()){
                do{
                    Cls_Filter     obj = new Cls_Filter();
                    obj.setDesc(c.getString(c.getColumnIndex("Search_Key")));
                    customersesList.add(obj);
                }while (c.moveToNext());
            }
            c.close();
        }




        Filter_Adapter Customer_List_adapter = new Filter_Adapter(
                this, customersesList);
        Lstitems_Filter.setAdapter(Customer_List_adapter);
    }
}


