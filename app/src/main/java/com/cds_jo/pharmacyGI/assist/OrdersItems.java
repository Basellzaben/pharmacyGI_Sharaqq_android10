package com.cds_jo.pharmacyGI.assist;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.cds_jo.pharmacyGI.ComInfo;
import com.cds_jo.pharmacyGI.ContactListAdapter;
import com.cds_jo.pharmacyGI.ContactListItems;
import com.cds_jo.pharmacyGI.DB;
import com.cds_jo.pharmacyGI.GalaxyMainActivity;
import com.cds_jo.pharmacyGI.MainActivity;
import com.cds_jo.pharmacyGI.Pop_Po_Select_Items_New_Activity;
import com.cds_jo.pharmacyGI.R;
import com.cds_jo.pharmacyGI.SearchPoActivity;
import com.cds_jo.pharmacyGI.Select_Customer;
import com.cds_jo.pharmacyGI.SqlHandler;
import com.cds_jo.pharmacyGI.We_Result;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import Methdes.MyTextView_Digital;
import cn.pedant.SweetAlert.SweetAlertDialog;
import hearder.main.Header_Frag;
import hearder.main.SimpleSideDrawer;


public class OrdersItems extends FragmentActivity {
    SqlHandler sqlHandler;
    ListView lv_Items;
    ArrayList<ContactListItems> contactList;
    EditText etItemNm, etPrice, etQuantity, etTax;
    CheckBox Radio_thmm, Radio_cash, Radio_Check;
    Button btnsubmit;
    String UserID = "";
    public ProgressDialog loadingdialog;
    public String json;
    Boolean IsNew;
    String CatNo = "-1";
    CheckBox chk_MobileOrder;
    //    Spinner Location;
    TextView tv_LocationNm, tv_Back;
    ImageView imgBack;
    TextView CustNm;
    private ImageView Img_Menu;
    TextView accno;
    LinearLayout LytSerach;
    SharedPreferences sharedPreferences;
    private SimpleSideDrawer mNav;
    TextView OrdeNo;
    TextView tv;
    Drawable greenProgressbar;
    RelativeLayout.LayoutParams lp;
    CheckBox Tax_Include;
    LinearLayout linearLayout;
    Button btn_EnterNotes;
    int FromFillItem = 0;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_item);
        Init();
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getString("FillItem").equalsIgnoreCase("1")) {
                try {
                    FromFillItem = 1;
                    OrdeNo.setText(getIntent().getExtras().getString("OrderNo"));
                    CustNm.setText(getIntent().getExtras().getString("CustNm"));
                    accno.setText(getIntent().getExtras().getString("accno"));

                    if (getIntent().getExtras().getString("Tax_Include").toString().equalsIgnoreCase("1"))
                    Tax_Include.setChecked(true);

                    if (getIntent().getExtras().getString("MobileOrder").toString().equalsIgnoreCase("1") )
                       chk_MobileOrder.setChecked(true);



                    if (getIntent().getExtras().getString("Pay_Method").toString().equalsIgnoreCase("1"))
                       Radio_cash.setChecked(true);


                    if (getIntent().getExtras().getString("Pay_Method").toString().equalsIgnoreCase("2"))
                          Radio_thmm.setChecked(true);


                    if (getIntent().getExtras().getString("Pay_Method").toString().equalsIgnoreCase("3"))
                        Radio_Check.setChecked(true);

                    Save_All_Items_To_List();
                } catch (Exception ex) {

                }
            }

        }
        GetcustQty();
        chk_MobileOrder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (chk_MobileOrder.isChecked()) {
                    LytSerach.setVisibility(View.VISIBLE);
                } else {
                    LytSerach.setVisibility(View.INVISIBLE);
                    accno.setText(sharedPreferences.getString("CustNo", ""));
                    CustNm.setText(sharedPreferences.getString("CustNm", ""));
                }
            }
        });

        Tax_Include.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CalcTotal();


            }
        });


        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k;
                if (ComInfo.ComNo == 1) {
                    k = new Intent(OrdersItems.this, MainActivity.class);
                } else {
                    k = new Intent(OrdersItems.this, GalaxyMainActivity.class);
                }
                startActivity(k);

            }
        });


        tv_Back = (TextView) findViewById(R.id.tv_Back);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        if (ComInfo.ComNo == 1) {
            tv_Back.setText(" فتح الزيارة");
            imgBack.setImageResource(R.mipmap.see1);
        }

        FillLocation();

        Fragment frag = new Header_Frag();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1, frag).commit();


        Img_Menu = (ImageView) findViewById(R.id.Img_Menu);
        Img_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mNav.toggleLeftDrawer();

            }
        });

        btn_EnterNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("Scr", "po");
                bundle.putString("OrdeNo", OrdeNo.getText().toString());
                android.app.FragmentManager Manager = getFragmentManager();

                PopOrderSelesDetails obj = new PopOrderSelesDetails();
                obj.setArguments(bundle);
                obj.show(Manager, null);

            }
        });


        Radio_thmm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Radio_thmm.isChecked() == true) {
                    Radio_cash.setChecked(false);
                    Radio_Check.setChecked(false);
                }
            }
        });
        Radio_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Radio_cash.isChecked() == true) {
                    Radio_thmm.setChecked(false);
                    Radio_Check.setChecked(false);
                }
            }
        });

        Radio_Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Radio_Check.isChecked() == true) {
                    Radio_thmm.setChecked(false);
                    Radio_cash.setChecked(false);
                }
            }
        });
        if (FromFillItem == 0) {
            GetMaxPONo();
            GetSerial();
            FillTempDate();

        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }
    private  void GetcustQty(){
      /*String  CustQtyOrder="";
        sqlHandler.executeQuery("DELETE FROM TempCustQty");


        String q= "Select  max(cast(orderno as int)) as no  from CustStoreQtyhdr Where acc='"+accno.getText().toString()+"'" ;
        Cursor c = sqlHandler.selectQuery(q);
        if(c!=null && c.getCount()>0){
            c.moveToFirst();
            CustQtyOrder= c.getString(c.getColumnIndex("no"));
            c.close();
        }else{
            CustQtyOrder="";
        }
        FillTempCustQty(CustQtyOrder);*/
    }
    private  void FillTempCustQty(String OrderNo){
       /* String q =" INSERT INTO TempCustQty(ItemNo,Qty,OrderNo,CustNo) " +
                  " SELECT                  itemno,sum(ifnull(qty,0)),'"+OrderNo+"','"+accno.getText().toString()+"'   from CustStoreQtydetl  where orderno='"+OrderNo+"' group by itemno ";
        sqlHandler.executeQuery(q);*/
    }
    private void Init() {
        lv_Items = (ListView) findViewById(R.id.LstvItems);
        sqlHandler = new SqlHandler(this);
        IsNew = true;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        UserID = sharedPreferences.getString("UserID", "");
        tv_LocationNm = (TextView) findViewById(R.id.tv_LocationNm);

        chk_MobileOrder = (CheckBox) findViewById(R.id.chk_MobileOrder);
        LytSerach = (LinearLayout) findViewById(R.id.LytSerach);
        LytSerach.setVisibility(View.INVISIBLE);
        CustNm = (TextView) findViewById(R.id.tv_cusnm);
        mNav = new SimpleSideDrawer(OrdersItems.this);
        mNav.setLeftBehindContentView(R.layout.po_nav_menu);
        accno = (TextView) findViewById(R.id.tv_acc);
        accno.setText(sharedPreferences.getString("CustNo", ""));
        CustNm.setText(sharedPreferences.getString("CustNm", ""));

        contactList = new ArrayList<ContactListItems>();
        contactList.clear();

        Get_CatNo(accno.getText().toString());
        showList(0);

        Tax_Include = (CheckBox) findViewById(R.id.chk_Tax_Include);
        linearLayout = (LinearLayout) findViewById(R.id.Home_layout);
        OrdeNo = (TextView) findViewById(R.id.et_OrdeNo);
        btn_EnterNotes = (Button) mNav.findViewById(R.id.btn_EnterNotes);
        Radio_cash = (CheckBox) mNav.findViewById(R.id.Radio_cash);
        Radio_thmm = (CheckBox) mNav.findViewById(R.id.Radio_thmm);
        Radio_Check = (CheckBox) mNav.findViewById(R.id.Radio_Check);

        btn_EnterNotes.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        Radio_cash.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        Radio_thmm.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));
        Radio_Check.setTypeface(Typeface.createFromAsset(this.getAssets(), "Hacen Tunisia Lt.ttf"));

        Radio_thmm.setChecked(true);
    }

    private void FillTempDate() {
        String q = "Delete From TempOrderItems";
        sqlHandler.executeQuery(q);
        q = " Delete From sqlite_sequence where name='TempOrderItems'";
        sqlHandler.executeQuery(q);


        q = "Insert into TempOrderItems (ItemNo,ItemNm,Qty,Bounce, Price ,tax,Type_No,Pack) " +
                "SELECT  distinct   i.Item_No,i.Item_Name , cast( ifnull(dt.qty,'0') as  integer) as  qty ,  cast(  ifnull(dt.bounce_qty,'0') as  integer) as bounce" +
                "  , i.Price ,i.tax , i.Type_No  , Pack  From invf i Left join Po_dtl dt " +
                "  on dt.itemno = i.Item_No  and  dt.orderno='" + OrdeNo.getText().toString() + "' ";//where cast( i.Price as float ) >0   ";
        sqlHandler.executeQuery(q);
    }

    private void FillLocation() {
        ArrayList<Cls_Cur> Locations = new ArrayList<Cls_Cur>();
        Locations.clear();
        SqlHandler sqlHandler = new SqlHandler(this);

        String query = "Select  distinct No , Name from Area";
        ArrayList<Cls_Cur> cls_curs = new ArrayList<Cls_Cur>();
        cls_curs.clear();

        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_Cur cls_Loc = new Cls_Cur();

                    cls_Loc.setName(c1.getString(c1
                            .getColumnIndex("Name")));
                    cls_Loc.setNo(c1.getString(c1
                            .getColumnIndex("No")));

                    Locations.add(cls_Loc);

                } while (c1.moveToNext());
            }
            c1.close();

        }


        Cls_Cur_Adapter cls_cur_adapter = new Cls_Cur_Adapter(
                this, Locations);
//        Location.setAdapter(cls_cur_adapter);
    }

    public void GetMaxPONo() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String u = sharedPreferences.getString("UserID", "");
        String Login = sharedPreferences.getString("Login", "No");
        if (Login.toString().equals("No")) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }

        String query = "SELECT  Distinct  COALESCE(MAX(orderno), 0) +1 AS no FROM Po_Hdr where userid ='" + UserID + "'";
        Cursor c1 = sqlHandler.selectQuery(query);
        String max = "0";
        TextView Maxpo = (TextView) findViewById(R.id.et_OrdeNo);
        if (c1 != null && c1.getCount() > 0) {
            c1.moveToFirst();
            max = c1.getString(c1.getColumnIndex("no"));
            c1.close();
        }

        String max1 = "0";
        max1 = DB.GetValue(OrdersItems.this, "OrdersSitting", "SalesOrder", "1=1");

        if (max1 == "") {
            max1 = "0";
        }
        max1 = String.valueOf(Integer.parseInt(max1) + 1);
        if (SToD(max1) > SToD(max)) {
            max = max1;
        }

        if (max.length() == 1) {
            Maxpo.setText(intToString(Integer.valueOf(u), 2) + intToString(Integer.valueOf(max), 5));

        } else {
            Maxpo.setText(intToString(Integer.valueOf(max), 7));

        }


        Maxpo.setFocusable(false);
        Maxpo.setEnabled(false);
        Maxpo.setCursorVisible(false);


    }

    public static String intToString(int num, int digits) {
        String output = Integer.toString(num);
        while (output.length() < digits) output = "0" + output;
        return output;
    }

    private void GetSerial() {


        tv = new TextView(getApplicationContext());
        lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(lp);
        tv.setLayoutParams(lp);
        tv.setPadding(10, 15, 10, 15);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        tv.setTextColor(Color.WHITE);
        tv.setBackgroundColor(Color.BLUE);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);


        final Handler _handler = new Handler();

        final ProgressDialog custDialog = new ProgressDialog(OrdersItems.this);
        custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
        custDialog.setCanceledOnTouchOutside(false);
        //custDialog.setProgress(0);
        //custDialog.setMax(100);
        custDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
        tv.setText("التسلسلات");
        custDialog.setCustomTitle(tv);
        custDialog.setProgressDrawable(greenProgressbar);
        custDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(OrdersItems.this);
                ws.GetOrdersSerials(UserID);
                try {
                    Integer i;
                    String q = "";
                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray js_Sales = js.getJSONArray("Sales");
                    JSONArray js_Payment = js.getJSONArray("Payment");
                    JSONArray js_SalesOrder = js.getJSONArray("SalesOrder");
                    JSONArray js_PrepareQty = js.getJSONArray("PrepareQty");
                    JSONArray js_RetSales = js.getJSONArray("RetSales");
                    JSONArray js_PostDely = js.getJSONArray("PostDely");
                    JSONArray js_Visits = js.getJSONArray("Visits");

                    q = "Delete from OrdersSitting";
                    sqlHandler.executeQuery(q);

                    q = " delete from sqlite_sequence where name='OrdersSitting'";
                    sqlHandler.executeQuery(q);


                    q = "INSERT INTO OrdersSitting(Sales, Payment , SalesOrder , PrepareQty , RetSales, PostDely , Visits  ) values ('"
                            + js_Sales.get(0).toString()
                            + "','" + js_Payment.get(0).toString()
                            + "','" + js_SalesOrder.get(0).toString()
                            + "','" + js_PrepareQty.get(0).toString()
                            + "','" + js_RetSales.get(0).toString()
                            + "','" + js_PostDely.get(0).toString()

                            + "','" + js_Visits.get(0).toString()
                            + "')";
                    sqlHandler.executeQuery(q);


                    _handler.post(new Runnable() {

                        public void run() {

                            custDialog.dismiss();
                            GetMaxPONo();

                        }
                    });

                } catch (final Exception e) {
                    custDialog.dismiss();
                    _handler.post(new Runnable() {

                        public void run() {

                            custDialog.dismiss();

                        }
                    });
                }
            }
        }).start();

    }

    private void UpDateMaxOrderNo() {

        String query = "SELECT  Distinct COALESCE(MAX(orderno), 0)   AS no FROM Po_Hdr";
        Cursor c1 = sqlHandler.selectQuery(query);
        String max = "0";

        if (c1 != null && c1.getCount() != 0) {
            c1.moveToFirst();
            max = c1.getString(c1.getColumnIndex("no"));
            c1.close();
        }

        max = (intToString(Integer.valueOf(max), 7));
        query = "Update OrdersSitting SET SalesOrder ='" + max + "'";
        sqlHandler.executeQuery(query);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("m4", max);
        editor.commit();
    }

    private void showList(Integer f) {

        lv_Items.setAdapter(null);
        float Total = 0;
        float Total_Tax = 0;
        float TTemp = 0;
        float PTemp = 0;
        float PQty = 0;
        String query = "";
        MyTextView_Digital etTotal, et_Tottal_Tax;
        TextView ed_date;
        etTotal = (MyTextView_Digital) findViewById(R.id.et_Total);
        et_Tottal_Tax = (MyTextView_Digital) findViewById(R.id.et_TotalTax);
        // etTotal.setText(String.valueOf(Total));
        // et_Tottal_Tax.setText(String.valueOf(Total_Tax));
        ed_date = (TextView) findViewById(R.id.ed_date);
        ContactListAdapter contactListAdapter = new ContactListAdapter(
                OrdersItems.this, contactList);
        lv_Items.setAdapter(contactListAdapter);
        //  json = new Gson().toJson(contactList);
    }

    public void showPop() {
        TextView Maxpo = (TextView) findViewById(R.id.et_OrdeNo);
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "po");
        bundle.putString("CatNo", CatNo);
        bundle.putString("OrderNo", Maxpo.getText().toString());
        bundle.putString("CustNm", CustNm.getText().toString());
        bundle.putString("accno", accno.getText().toString());
        if (Tax_Include.isChecked())
            bundle.putString("Tax_Include", "1");
        else
            bundle.putString("Tax_Include", "0");

        if (chk_MobileOrder.isChecked())
            bundle.putString("MobileOrder", "1");
        else
            bundle.putString("MobileOrder", "0");


        bundle.putString("Pay_Method", "0");
        if (Radio_cash.isChecked())
            bundle.putString("Pay_Method", "1");

        if (Radio_thmm.isChecked())
            bundle.putString("Pay_Method", "2");

        if (Radio_Check.isChecked())
            bundle.putString("Pay_Method", "3");





     /*     FragmentManager Manager =  getFragmentManager();
        Pop_Po_Select_Items_New obj = new Pop_Po_Select_Items_New();
        obj.setArguments(bundle);
        obj.show(Manager, null);*/
        Intent i = new Intent(this, Pop_Po_Select_Items_New_Activity.class);
        i.putExtras(bundle);
         startActivity(i);

    }

    public Double GetOrderQty(String ItemNo, String Batchno, String Expdate) {
        Double Qty = 0.0;


        for (int x = 1; x < contactList.size(); x++) {
            ContactListItems contactListItems = new ContactListItems();
            contactListItems = contactList.get(x);
            if (contactListItems.getNo() != null) {
                if (contactListItems.getNo().equalsIgnoreCase(ItemNo) && contactListItems.getBatch().equalsIgnoreCase(Batchno) && contactListItems.getExpDate().equalsIgnoreCase(Expdate)) {
                    Qty = Qty + (SToD(contactListItems.getQty()) * SToD(contactListItems.getOperand()));
                }
            }
        }

        return Qty;
    }

    private void Get_CatNo(String ACC_NO) {
        SqlHandler sqlHandler = new SqlHandler(this);
        String q = "Select  distinct ifnull( CatNo,0) as catno from Customers where no = '" + ACC_NO + "'";
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.getCount() > 0) {
                c1.moveToFirst();
                CatNo = c1.getString(c1.getColumnIndex("catno"));
            }
            c1.close();
        }

    }

    public void btn_Po(View view) {

        Intent k = new Intent(this, Convert_Layout_Img.class);
        // Intent k = new Intent(this,BluetoothConnectMenu.class);


        startActivity(k);
    }

    public void btn_back(View view) {
        Intent k;
        if (ComInfo.ComNo == 1) {
            k = new Intent(this, MainActivity.class);
        } else {
            k = new Intent(this, GalaxyMainActivity.class);
        }

        startActivity(k);
    }

    @Override
    public void onBackPressed() {
        Intent k;
        if (ComInfo.ComNo == 1) {
            k = new Intent(this, MainActivity.class);
        } else {
            k = new Intent(this, GalaxyMainActivity.class);
        }

        startActivity(k);

    }

    public void btn_showPop(View view) {

        showPop();
    }

    public void Save_Method(String ItemNo, String p, String q, String t, String u, String dis, String bounce) {

        TextView OrderNo = (TextView) findViewById(R.id.et_OrdeNo);
        String bounce_unitno, bounce_qty, dis_per, dis_value;
        bounce_unitno = bounce_qty = dis_per = dis_value = "";
        String query = "INSERT INTO Po_dtl(orderno,itemno,price,qty,tax,unitNo,dis_Amy,dis_per,bounce_qty,bounce_unitno) values " +
                "('" + OrderNo.getText().toString() + "','" + ItemNo + "'," + p + "," + q + "," + t + ",'" + u + "','" + dis_value + "','" + dis_value + "','" + bounce_qty + "','" + bounce_unitno + "')";

        sqlHandler.executeQuery(query);
        showList(1);
    }

    //FillList
    public void Save_List(String ItemNo, String p, String q, String t, String u, String dis, String bounce, String ItemNm, String UnitName, String dis_Amt) {

        if (bounce.toString().equals(""))
            bounce = "0";

        if (dis.toString().equals(""))
            dis = "0";

        if (dis_Amt.toString().equals(""))
            dis_Amt = "0";

        Double Item_Total, Price, Tax_Amt, Tax, Total, Net_Total, Tax_Total;

        for (int x = 1; x < contactList.size(); x++) {
            ContactListItems contactListItems = new ContactListItems();
            contactListItems = contactList.get(x);

            if (contactListItems.getNo().equals(ItemNo)) {
                AlertDialog alertDialog = new AlertDialog.Builder(

                        this).create();

                alertDialog.setTitle("Galaxy");
                alertDialog.setMessage(getResources().getText(R.string.ItemExists));            // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.tick);
                alertDialog.setButton(getResources().getText(R.string.Ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialog.show();
                return;
            }

        }
        AddEmptyRow();


        Item_Total = Double.parseDouble(q.replace(",", "")) * Double.parseDouble(p.toString().replace("'", ""));
        Price = Double.parseDouble(p.replace("'", ""));
        Tax = Double.parseDouble(t.replace(",", ""));
        Item_Total = Double.parseDouble(Item_Total.toString().replace("'", ""));
        //  Tax_Amt =Double.valueOf((Tax / 100))  * Item_Total;
        // Tax_Amt =(Double.parseDouble(Tax.toString()) /100)   *  ( Double.parseDouble(Item_Total.toString().replace(",","")) -  Double.parseDouble( dis_Amt.toString().replace(",","") ));
        double TaxFactor = 0.0;
        CheckBox Tax_Include = (CheckBox) findViewById(R.id.chk_Tax_Include);
        Double DisValue = Double.parseDouble(dis_Amt.toString().replace(",", ""));


        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat) nf;

        ContactListItems contactListItems = new ContactListItems();
        contactListItems.setno(ItemNo);
        contactListItems.setName(ItemNm);
        if (Tax_Include.isChecked()) {
            contactListItems.setprice(String.valueOf(Price / ((Tax / 100) + 1)));
        } else {
            contactListItems.setprice(String.valueOf(Price));

        }

        // contactListItems.setprice(String.valueOf(Price));
        contactListItems.setItemOrgPrice(String.valueOf(Price));
        contactListItems.setQty(q);
        contactListItems.setTax(String.valueOf(Tax));
        contactListItems.setUnite(u);
        contactListItems.setBounce(bounce);
        contactListItems.setDiscount(dis);
        contactListItems.setDis_Amt(dis_Amt);
        contactListItems.setUniteNm(UnitName);
        contactListItems.setPro_amt("0");
        contactListItems.setPro_dis_Per("0");
        contactListItems.setPro_bounce("0");
        contactListItems.setPro_Total("0");
        contactListItems.setDisAmtFromHdr("0");
        contactListItems.setDisPerFromHdr("0");
        contactListItems.setProID("");
        contactListItems.setTax_Amt("0");
        contactListItems.setTotal(String.valueOf(df.format(Item_Total)));
        contactListItems.setBatch("");
        contactListItems.setExpDate("");
        contactListItems.setOperand("1");
        contactList.add(contactListItems);
        CalcTotal();

        showList(1);
    }

    public void Save_All_Items_To_List() {
        try {

            NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
            DecimalFormat df = (DecimalFormat) nf;
            int Qty, Bounce;
            Double Item_Total, Price, Tax, Total;
            contactList = new ArrayList<ContactListItems>();
            contactList.clear();
            AddEmptyRow();
            String item_No, UnitNo, UnitNm;
            //  CheckBox Tax_Include = (CheckBox)findViewById(R.id.chk_Tax_Include);


            String q = "Select ItemNo,ItemNm ,cast(Qty as integer) as   Qty  ,cast(Bounce as integer) as   Bounce,Price,tax from TempOrderItems where  cast(Qty as integer)>0";
            Cursor c = sqlHandler.selectQuery(q);
            if (c != null && c.getCount() > 0) {
                if (c.moveToFirst()) {
                    do {
                        ContactListItems contactListItems = new ContactListItems();
                        item_No = c.getString(c.getColumnIndex("ItemNo"));
                        contactListItems.setno(item_No);
                        UnitNo = DB.GetValue(this, "UnitItems", "unitno", "item_no='" + item_No + "'");
                        UnitNm = DB.GetValue(this, "Unites", "UnitName", "Unitno='" + UnitNo + "'");
                        contactListItems.setName(c.getString(c.getColumnIndex("ItemNm")));

                        Price = 0.0;
                        Price = SToD(c.getString(c.getColumnIndex("Price")));

                        Tax = 0.0;
                        Tax = SToD(c.getString(c.getColumnIndex("tax")));

                        Qty = 0;

                        Bounce = Integer.parseInt(c.getString(c.getColumnIndex("Bounce")));
                        Qty = Integer.parseInt(c.getString(c.getColumnIndex("Qty")));
                        Total = Qty * Price;
                        if (Tax_Include.isChecked()) {
                            contactListItems.setprice(String.valueOf(Price / ((Tax / 100) + 1)));
                        } else {
                            contactListItems.setprice(String.valueOf(Price));
                        }


                        contactListItems.setItemOrgPrice(String.valueOf(Price));
                        contactListItems.setQty(Qty + "");
                        contactListItems.setTax(String.valueOf(Tax));
                        contactListItems.setUnite(UnitNo);
                        contactListItems.setBounce(Bounce + "");
                        contactListItems.setDiscount("0");
                        contactListItems.setDis_Amt("0");
                        contactListItems.setUniteNm(UnitNm);
                        contactListItems.setPro_amt("0");
                        contactListItems.setPro_dis_Per("0");
                        contactListItems.setPro_bounce("0");
                        contactListItems.setPro_Total("0");
                        contactListItems.setDisAmtFromHdr("0");
                        contactListItems.setDisPerFromHdr("0");
                        contactListItems.setProID("0");
                        contactListItems.setTax_Amt("0");
                        contactListItems.setTotal(String.valueOf(df.format(Total)));
                        contactListItems.setBatch("0");
                        contactListItems.setExpDate("0");
                        contactListItems.setOperand("1");
                        contactList.add(contactListItems);
                    } while (c.moveToNext());
                }
                c.close();
            }
            CalcTotal();
            showList(1);
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void CalcTax() {

        Double All_Dis = 0.0;
        Double RowTotal = 0.0;
        Double NetRow = 0.0;
        Double TaxAmt = 0.0;
        Double TaxFactor = 0.0;
        Double All_Dis_Per = 0.0;
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat) nf;

        CheckBox Tax_Include = (CheckBox) findViewById(R.id.chk_Tax_Include);


        ContactListItems contactListItems;
        for (int x = 1; x < contactList.size(); x++) {
            contactListItems = new ContactListItems();
            contactListItems = contactList.get(x);
            All_Dis = Double.parseDouble(contactListItems.getDis_Amt().replace(",", "")) + Double.parseDouble(contactListItems.getDisAmtFromHdr().replace(",", "")) + Double.parseDouble(contactListItems.getPro_amt().replace(",", ""));
            All_Dis_Per = SToD(contactListItems.getDiscount()) + SToD(contactListItems.getDisPerFromHdr()) + SToD(contactListItems.getPro_dis_Per());

            if (Tax_Include.isChecked()) {

                contactListItems.setprice(SToD(String.valueOf((SToD(contactListItems.getItemOrgPrice())) / ((SToD(contactListItems.getTax()) / 100) + 1))).toString());

            } else {
                contactListItems.setprice(String.valueOf(SToD(contactListItems.getItemOrgPrice())));


            }
            //  contactListItems.setDis_Amt( (SToD(contactListItems.getprice()) * SToD(contactListItems.getQty()))  * (100)   );
            RowTotal = SToD(contactListItems.getprice()) * SToD(contactListItems.getQty());
            TaxFactor = (Double.parseDouble(contactListItems.getTax()) / 100);
            NetRow = RowTotal - (RowTotal * (All_Dis_Per / 100));
             /*if(Tax_Include.isChecked()) {
                 TaxAmt = NetRow - ( NetRow / (TaxFactor + 1)) ;
                  TaxAmt = NetRow - ( NetRow / (TaxFactor + 1)) ;
             }
             else {
                TaxAmt = NetRow  *  TaxFactor ;
           }*/
            TaxAmt = NetRow * TaxFactor;
            contactListItems.setTax_Amt(df.format(TaxAmt).toString());
        }
        showList(0);
    }

    private void CalcTotal() {

        Double Total, Tax_Total, Dis_Amt, Po_Total;
        ContactListItems contactListItems = new ContactListItems();
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat df = (DecimalFormat) nf;
        Double All_Dis = 0.0;
        Double All_Dis_Per = 0.0;
        Total = 0.0;
        Tax_Total = 0.0;
        Dis_Amt = 0.0;
        Po_Total = 0.0;
        TextView Subtotal = (TextView) findViewById(R.id.et_Total);
        TextView TotalTax = (TextView) findViewById(R.id.et_TotalTax);
        TextView dis = (TextView) findViewById(R.id.et_dis);
        TextView NetTotal = (TextView) findViewById(R.id.tv_NetTotal);
        CheckBox Tax_Include = (CheckBox) findViewById(R.id.chk_Tax_Include);
        Double TaxVal = 0.0;
        double TaxFactor = 0.0;
        CalcTax();
        Double RowTotal = 0.0;
        Double pq = 0.0;
        Double opq = 0.0;
        Double V_NetTotal = 0.0;
        for (int x = 1; x < contactList.size(); x++) {
            contactListItems = new ContactListItems();
            contactListItems = contactList.get(x);
            All_Dis = SToD(contactListItems.getDis_Amt()) + SToD(contactListItems.getDisAmtFromHdr()) + SToD(contactListItems.getPro_amt());
            All_Dis_Per = SToD(contactListItems.getDiscount()) + SToD(contactListItems.getDisPerFromHdr()) + SToD(contactListItems.getPro_dis_Per());
            pq = SToD(contactListItems.getprice()) * SToD(contactListItems.getQty());
            opq = SToD(contactListItems.getItemOrgPrice()) * SToD(contactListItems.getQty());

            Tax_Total = Tax_Total + (SToD(contactListItems.getTax_Amt().toString()));
            Dis_Amt = Dis_Amt + (((pq) * (All_Dis_Per / 100)));

            if (Tax_Include.isChecked()) {
                RowTotal = opq - ((opq) * (All_Dis_Per / 100));//+ SToD(contactListItems.getTax_Amt());
               /* if( All_Dis_Per > 0) {
                    Total = Total + ((opq * (All_Dis_Per / 100)) - SToD(contactListItems.getTax_Amt()) + Dis_Amt);
                }else{
                    Total = Total + ((opq ) - SToD(contactListItems.getTax_Amt()) );

                }*/


            } else {
                RowTotal = pq - ((pq) * (All_Dis_Per / 100)) + SToD(contactListItems.getTax_Amt());
                Total = Total + pq;

            }

            V_NetTotal = V_NetTotal + SToD(RowTotal.toString().replace(",", ""));

            contactListItems.setTotal((SToD(RowTotal.toString().replace(",", ""))).toString());
            All_Dis = 0.0;

        }
        Total = V_NetTotal - Tax_Total + Dis_Amt;
        TotalTax.setText(String.valueOf(df.format(Tax_Total)).replace(",", ""));
        Subtotal.setText(String.valueOf(df.format(Total)).replace(",", ""));
        dis.setText(String.valueOf(df.format(Dis_Amt)).replace(",", ""));


        Po_Total = Po_Total + ((SToD(Subtotal.getText().toString()) - SToD(dis.getText().toString())) + SToD(TotalTax.getText().toString()));

        showList(0);
        NetTotal.setText(String.valueOf(df.format(V_NetTotal)).replace(",", ""));


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

    public void btn_Search_Cust(View view) {

        android.app.FragmentManager Manager = getFragmentManager();

        Select_Customer obj = new Select_Customer();
        obj.show(Manager, null);
    }

    public void Set_Cust(String No, String Nm) {
        TextView CustNm = (TextView) findViewById(R.id.tv_cusnm);
        TextView acc = (TextView) findViewById(R.id.tv_acc);
        acc.setText(No);
        CustNm.setText(Nm);
        CustNm.setError(null);
    }

    private void AddEmptyRow() {

        if (contactList.size() == 0) {
            ContactListItems contactListItems = new ContactListItems();
            contactListItems.setno("");
            contactListItems.setName("");
            contactListItems.setprice("");
            contactListItems.setItemOrgPrice("");
            contactListItems.setQty("");
            contactListItems.setTax("");
            contactListItems.setUniteNm("");
            contactListItems.setBounce("");
            contactListItems.setDiscount("");
            contactListItems.setDis_Amt("");
            contactListItems.setDis_Amt("");
            contactListItems.setUnite("");
            contactListItems.setTax_Amt("");
            contactListItems.setTotal("");
            contactListItems.setProID("");
            contactListItems.setPro_bounce("0");
            contactListItems.setPro_dis_Per("0");
            contactListItems.setBatch("");
            contactListItems.setExpDate("");
            contactListItems.setOperand("1");
            contactListItems.setPro_amt("0");
            contactListItems.setPro_Total("0");
            contactListItems.setDisAmtFromHdr("0");
            contactListItems.setDisPerFromHdr("0");
            contactList.add(contactListItems);
        }
    }

    @SuppressLint("Range")
    public void Set_Order(String No, String Nm, String acc) { // FillList

        // Radio_cash.setChecked(false);
        //Radio_thmm.setChecked(false);
        //Radio_Check.setChecked(false);

        TextView CustNm = (TextView) findViewById(R.id.tv_cusnm);
        TextView Order_no = (TextView) findViewById(R.id.et_OrdeNo);
        TextView accno = (TextView) findViewById(R.id.tv_acc);
        TextView Total = (TextView) findViewById(R.id.et_Total);
        TextView dis = (TextView) findViewById(R.id.et_dis);
        TextView NetTotal = (TextView) findViewById(R.id.tv_NetTotal);
        TextView TotalTax = (TextView) findViewById(R.id.et_TotalTax);
        CheckBox Tax_Include = (CheckBox) findViewById(R.id.chk_Tax_Include);

        Order_no.setText(No);
        CustNm.setText(Nm);
        accno.setText(acc);
        contactList.clear();
        AddEmptyRow();

        showList(0);


        sqlHandler = new SqlHandler(this);

        Tax_Include.setChecked(false);
        String query = "  select   Distinct *  from Po_Hdr  where orderno ='" + Order_no.getText().toString() + "'";
        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                Total.setText(c1.getString(c1.getColumnIndex("Total")).toString());
                dis.setText(c1.getString(c1.getColumnIndex("disc_Total")).toString());
                NetTotal.setText(c1.getString(c1.getColumnIndex("Net_Total")).toString());
                TotalTax.setText(c1.getString(c1.getColumnIndex("Tax_Total")).toString());

                if (c1.getString(c1.getColumnIndex("include_Tax")).equals("0")) {
                    Tax_Include.setChecked(true);
                }
                if (c1.getString(c1.getColumnIndex("MobileOrder")).equals("0")) {
                    chk_MobileOrder.setChecked(true);
                }
            }

            if (c1.getString(c1.getColumnIndex("pay_method")).toString().equalsIgnoreCase("1")) {
                Radio_cash.setChecked(true);
                Radio_thmm.setChecked(false);
                Radio_Check.setChecked(false);
            } else if (c1.getString(c1.getColumnIndex("pay_method")).toString().equalsIgnoreCase("2")) {
                Radio_cash.setChecked(false);
                Radio_thmm.setChecked(true);
                Radio_Check.setChecked(false);
            } else {
                Radio_cash.setChecked(false);
                Radio_thmm.setChecked(false);
                Radio_Check.setChecked(true);
            }
            c1.close();
        }


        query = "  select Distinct Unites.UnitName, pod.OrgPrice ,  invf.Item_Name, pod.itemno,pod.price,pod.qty,pod.tax ,pod.unitNo ,pod.dis_Amt,pod.dis_per,pod.bounce_qty ,  pod.tax_Amt   , pod.total  " +
                " , pod.pro_Total    , pod.ProID , pod.Pro_bounce  ,pod.Pro_dis_Per,  pod.Pro_amt     from Po_dtl pod left join invf on invf.Item_No =  pod.itemno    left join Unites on Unites.Unitno=  pod.unitNo  Where pod.orderno='" + Order_no.getText().toString() + "'";
        c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    ContactListItems contactListItems = new ContactListItems();

                    contactListItems.setno(c1.getString(c1
                            .getColumnIndex("itemno")));
                    contactListItems.setName(c1.getString(c1
                            .getColumnIndex("Item_Name")));
                    contactListItems.setprice(c1.getString(c1
                            .getColumnIndex("price")));
                    contactListItems.setItemOrgPrice(c1.getString(c1
                            .getColumnIndex("OrgPrice")));
                    contactListItems.setQty(c1.getString(c1
                            .getColumnIndex("qty")));
                    contactListItems.setTax(c1.getString(c1
                            .getColumnIndex("tax")));
                    contactListItems.setUniteNm(c1.getString(c1
                            .getColumnIndex("UnitName")));
                    contactListItems.setBounce(c1.getString(c1
                            .getColumnIndex("bounce_qty")));
                    contactListItems.setDiscount(c1.getString(c1
                            .getColumnIndex("dis_per")));
                    contactListItems.setDis_Amt(c1.getString(c1
                            .getColumnIndex("dis_Amt")));

                    contactListItems.setDis_Amt(c1.getString(c1
                            .getColumnIndex("dis_Amt")));

                    contactListItems.setUnite(c1.getString(c1
                            .getColumnIndex("unitNo")));

                    contactListItems.setTax_Amt(c1.getString(c1
                            .getColumnIndex("tax_Amt")));

                    contactListItems.setTotal(c1.getString(c1
                            .getColumnIndex("total")));

                    contactListItems.setProID("");

                    contactListItems.setPro_bounce("0");

                    contactListItems.setPro_dis_Per("0");
                    contactListItems.setBatch("");
                    contactListItems.setExpDate("");
                    contactListItems.setOperand("1");

                    contactListItems.setPro_amt("0");

                    contactListItems.setPro_Total("0");
                    contactListItems.setDisAmtFromHdr("0");

                    contactListItems.setDisPerFromHdr("0");

                    contactList.add(contactListItems);

                } while (c1.moveToNext());

            }

            c1.close();
        }
        CalcTotal();
        showList(0);
        IsNew = false;
        FillTempDate();
    }

    public void btn_searchCustomer(View view) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String u = sharedPreferences.getString("UserID", "");

        if (chk_MobileOrder.isChecked()) {
            Bundle bundle = new Bundle();
            bundle.putString("Scr", "po");
            android.app.FragmentManager Manager = getFragmentManager();

            Select_Customer obj = new Select_Customer();
            obj.setArguments(bundle);
            obj.show(Manager, null);
        }
    }

    public void btn_show_Pop(View view) {
        showPop();
    }

    public void btn_print(View view) {

        Intent k = new Intent(this, Convert_Layout_Img.class);
        //  Intent k = new Intent(this,BluetoothConnectMenu.class);
        k.putExtra("Scr", "po");
        TextView CustNm = (TextView) findViewById(R.id.tv_cusnm);
        TextView OrdeNo = (TextView) findViewById(R.id.et_OrdeNo);
        TextView accno = (TextView) findViewById(R.id.tv_acc);
        k.putExtra("cusnm", CustNm.getText().toString());
        k.putExtra("OrderNo", OrdeNo.getText().toString());
        k.putExtra("accno", accno.getText().toString());

        startActivity(k);
    }

    public void Save_Recod_Po() {

        TextView custNm = (TextView) findViewById(R.id.tv_cusnm);
        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        TextView acc = (TextView) findViewById(R.id.tv_acc);

        TextView Total = (TextView) findViewById(R.id.et_Total);
        TextView dis = (TextView) findViewById(R.id.et_dis);
        TextView NetTotal = (TextView) findViewById(R.id.tv_NetTotal);
        TextView TotalTax = (TextView) findViewById(R.id.et_TotalTax);

        CheckBox Tax_Include = (CheckBox) findViewById(R.id.chk_Tax_Include);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences
                (this);
        Long i;
        ContentValues cv = new ContentValues();
        cv.put("orderno", pono.getText().toString());
        cv.put("acc", acc.getText().toString());
        cv.put("date", currentDateandTime);
        cv.put("userid", UserID);
        cv.put("Total", Total.getText().toString().replace(",", ""));
        cv.put("Net_Total", NetTotal.getText().toString().replace(",", ""));
        cv.put("Tax_Total", TotalTax.getText().toString().replace(",", ""));
        cv.put("bounce_Total", "0");
        cv.put("posted", "-1");
        cv.put("V_OrderNo", sharedPreferences.getString("V_OrderNo", "0"));

        if (Radio_cash.isChecked()) {
            cv.put("pay_method", "1");
        } else if (Radio_thmm.isChecked()) {
            cv.put("pay_method", "2");
        } else {
            cv.put("pay_method", "3");
        }

        //cv.put("V_OrderNo",sharedPreferences.getString("V_OrderNo", "0"));


        if (chk_MobileOrder.isChecked()) {
            cv.put("MobileOrder", "0");
        } else {
            cv.put("MobileOrder", "-1");
        }


        if (Tax_Include.isChecked()) {
            cv.put("include_Tax", "0");
        } else {
            cv.put("include_Tax", "-1");
        }

        cv.put("disc_Total", dis.getText().toString());


        if (IsNew == true) {
            i = sqlHandler.Insert("Po_Hdr", null, cv);
        } else {
            i = sqlHandler.Update("Po_Hdr", cv, "orderno ='" + pono.getText().toString() + "'");
        }

        if (i > 0) {
            String q = "Delete from  Po_dtl where orderno ='" + pono.getText().toString() + "'";
            sqlHandler.executeQuery(q);

            for (int x = 1; x < contactList.size(); x++) {
                ContactListItems contactListItems = new ContactListItems();
                contactListItems = contactList.get(x);


                cv = new ContentValues();
                cv.put("orderno", pono.getText().toString());
                cv.put("itemno", contactListItems.getNo());
                cv.put("price", contactListItems.getPrice().toString().replace(",", ""));
                cv.put("qty", contactListItems.getQty().toString().replace(",", ""));
                cv.put("tax", contactListItems.getTax().toString().replace(",", ""));
                cv.put("unitNo", contactListItems.getUnite().toString().replace(",", ""));
                cv.put("dis_per", contactListItems.getDiscount().toString().replace(",", ""));
                cv.put("dis_Amt", contactListItems.getDis_Amt().toString().replace(",", ""));
                cv.put("bounce_qty", contactListItems.getBounce().toString().replace(",", ""));
                cv.put("tax_Amt", contactListItems.getTax_Amt().toString().replace(",", ""));
                cv.put("total", contactListItems.getTotal().toString().replace(",", ""));
                cv.put("ProID", contactListItems.getProID().toString().replace(",", ""));
                cv.put("Pro_bounce", contactListItems.getPro_bounce().toString().replace(",", ""));
                cv.put("Pro_dis_Per", contactListItems.getPro_dis_Per().toString().replace(",", ""));
                cv.put("Pro_amt", contactListItems.getPro_amt().toString().replace(",", ""));
                cv.put("pro_Total", contactListItems.getPro_Total().toString().replace(",", ""));
                cv.put("OrgPrice", contactListItems.getItemOrgPrice().toString().replace(",", ""));

                i = sqlHandler.Insert("Po_dtl", null, cv);
                if (i<1){
                    new SweetAlertDialog(OrdersItems.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("العملية لم تتم بنجاح")
                            .setContentText("الرجاء محاولة تخزين طلب البيع مرة اخرى")
                            .setCustomImage(R.drawable.error_new)
                            .setConfirmText("رجـوع")
                            .show();

                    break;
                }
            }
        }

        if (i > 0) {

            IsNew = false;
            UpDateMaxOrderNo();


            new SweetAlertDialog(OrdersItems.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("طلب البيع")
                    .setContentText("تمت عملية الحفظ بنجاح")
                    .setCustomImage(R.drawable.tick)
                    .setConfirmText("رجــوع")
                    .show();



        }
    }

    public void btn_save_po(View view) {
        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        String q = "SELECT Distinct *  from  Po_Hdr where   posted >0 AND   orderno ='" + pono.getText().toString() + "'";
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {

            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle(getResources().getText(R.string.Ordersales));
            alertDialog.setMessage(getResources().getText(R.string.UpdateNotAllowed));            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setButton(getResources().getText(R.string.Ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.show();
            c1.close();
            return;
        }


        if (contactList.size() < 2) {

            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle(getResources().getText(R.string.Ordersales));
            alertDialog.setMessage(getResources().getText(R.string.SaveNotAllowedWithoutItem));            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setButton(getResources().getText(R.string.Ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.show();

            return;
        }

        TextView custNm = (TextView) findViewById(R.id.tv_cusnm);

        TextView acc = (TextView) findViewById(R.id.tv_acc);
        if (custNm.getText().toString().length() == 0) {
            custNm.setError("required!");
            custNm.requestFocus();
            return;
        }

        if (pono.getText().toString().length() == 0) {
            pono.setError("required!");
            pono.requestFocus();
            return;
        }


        for (int x = 1; x < contactList.size(); x++) {
            ContactListItems contactListItems = new ContactListItems();
            contactListItems = contactList.get(x);
            if (SToD(contactListItems.getprice()) == 0 && SToD(contactListItems.getQty()) >= 0) {

                AlertDialog alertDialog = new AlertDialog.Builder(
                        this).create();
                alertDialog.setTitle(getResources().getText(R.string.Ordersales));
                alertDialog.setMessage("  الرجاء التاكد من سعر المادة  " + contactListItems.getName().toString());            // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.tick);
                alertDialog.setButton(getResources().getText(R.string.Ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.show();
                return;
            }
        }

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getResources().getText(R.string.Ordersales));
        alertDialog.setMessage(getResources().getText(R.string.DoYouWantToContinSave));
        alertDialog.setIcon(R.drawable.save);
        alertDialog.setPositiveButton(getResources().getText(R.string.Ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Save_Recod_Po();
            }
        });


        alertDialog.setNegativeButton(getResources().getText(R.string.No), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });


        alertDialog.show();


    }

    public void btn_delete(View view) {
        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        String q = "SELECT Distinct *  from  Po_Hdr where   posted >0 AND   orderno ='" + pono.getText().toString() + "'";
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {

            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle(getResources().getText(R.string.Ordersales));
            alertDialog.setMessage(getResources().getText(R.string.UpdateNotAllowed));            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setButton(getResources().getText(R.string.Ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.show();
            c1.close();
            return;
        } else {


            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle(getResources().getText(R.string.Ordersales));
            alertDialog.setMessage(getResources().getText(R.string.DoYouWantContinDelete));
            alertDialog.setIcon(R.drawable.delete);
            alertDialog.setPositiveButton(getResources().getText(R.string.Ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Delete_Record_PO();

                }
            });

            // Setting Negative "NO" Button
            alertDialog.setNegativeButton(getResources().getText(R.string.No), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to invoke NO event
                    //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        }
    }

    public void Delete_Record_PO() {
        TextView custNm = (TextView) findViewById(R.id.tv_cusnm);
        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        TextView acc = (TextView) findViewById(R.id.tv_acc);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String currentDateandTime = sdf.format(new Date());


        String query = "Delete from  Po_Hdr where orderno ='" + pono.getText().toString() + "'";
        sqlHandler.executeQuery(query);

        query = "Delete from  Po_dtl where orderno ='" + pono.getText().toString() + "'";
        sqlHandler.executeQuery(query);
        contactList.clear();
        GetMaxPONo();
        showList(0);
        IsNew = true;
        //custNm.setText("");
        //acc.setText("");

        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();
        alertDialog.setTitle(getResources().getText(R.string.Ordersales));
        alertDialog.setMessage(getResources().getText(R.string.DeleteCompleteSuccsully));
        alertDialog.setIcon(R.drawable.tick);
        alertDialog.setButton(getResources().getText(R.string.Ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }

    public void btn_Search_Orders(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "po");
        android.app.FragmentManager Manager = getFragmentManager();

        SearchPoActivity obj = new SearchPoActivity();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }

    public void btn_share(View view) {


        final SqlHandler sql_Handler = new SqlHandler(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final TextView custNm = (TextView) findViewById(R.id.tv_cusnm);
        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        final TextView acc = (TextView) findViewById(R.id.tv_acc);
        final TextView Total = (TextView) findViewById(R.id.et_Total);
        final TextView dis = (TextView) findViewById(R.id.et_dis);
        final TextView NetTotal = (TextView) findViewById(R.id.tv_NetTotal);
        final TextView TotalTax = (TextView) findViewById(R.id.et_TotalTax);
        final CheckBox Tax_Include = (CheckBox) findViewById(R.id.chk_Tax_Include);

        final String str;


        String query = "SELECT Distinct Notes , acc,  date ,Delv_day_count  ,pay_method  FROM Po_Hdr where orderno  ='" + pono.getText().toString() + "'";
        Cursor c1 = sqlHandler.selectQuery(query);
        String Notes, Date, Cust_No, Delv_day_count, paymethod;
        Date = Cust_No = Delv_day_count = Notes = paymethod = "";

        if (c1.getCount() > 0 && c1 != null) {
            c1.moveToFirst();
            Cust_No = c1.getString(c1.getColumnIndex("acc"));
            Notes = c1.getString(c1.getColumnIndex("Notes"));
            Delv_day_count = c1.getString(c1.getColumnIndex("Delv_day_count"));
            Date = c1.getString(c1.getColumnIndex("date"));
            paymethod = c1.getString(c1.getColumnIndex("pay_method"));

            c1.close();
        } else {
            Toast.makeText(this, "الرجاء تخزين طلب البيع أولا", Toast.LENGTH_SHORT).show();
            //  btn_save_po(view);
            //  btn_share(view);


        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Cust_No", Cust_No.toString());
            jsonObject.put("day_Count", Delv_day_count == null ? "null" : Delv_day_count.toString());
            jsonObject.put("Date", Date.toString());
            jsonObject.put("UserID", sharedPreferences.getString("UserID", ""));
            jsonObject.put("OrderNo", pono.getText().toString());

            jsonObject.put("Total", Total.getText().toString().replace(",", ""));
            jsonObject.put("Net_Total", NetTotal.getText().toString().replace(",", ""));
            jsonObject.put("Tax_Total", TotalTax.getText().toString().replace(",", ""));
            jsonObject.put("bounce_Total", "0");

            jsonObject.put("disc_Total", dis.getText().toString().replace(",", ""));
            jsonObject.put("Notes", Notes);
            jsonObject.put("pay_method", paymethod);


            if (Tax_Include.isChecked()) {
                jsonObject.put("include_Tax", "0");
            } else {
                jsonObject.put("include_Tax", "-1");
            }


            if (chk_MobileOrder.isChecked()) {
                jsonObject.put("MobileOrder", "0");
            } else {
                jsonObject.put("MobileOrder", "-1");
            }

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        String json = new Gson().toJson(contactList);
        str = jsonObject.toString() + json;


        loadingdialog = ProgressDialog.show(OrdersItems.this, getResources().getText(R.string.PleaseWait), getResources().getText(R.string.PostUnderProccess), true);
        loadingdialog.setCancelable(false);
        loadingdialog.setCanceledOnTouchOutside(false);
        loadingdialog.show();
        final Handler _handler = new Handler();


        // Toast.makeText(getApplicationContext(),str, Toast.LENGTH_LONG).show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebServices ws = new CallWebServices(OrdersItems.this);
                ws.Save_po(str, "Insert_PurshOrder");
                try {

                    if (We_Result.ID > 0) {
                        ContentValues cv = new ContentValues();
                        TextView DocNo = (TextView) findViewById(R.id.et_OrdeNo);
                        cv.put("posted", We_Result.ID);
                        long i;
                        i = sql_Handler.Update("Po_Hdr", cv, "orderno='" + DocNo.getText().toString() + "'");

                        _handler.post(new Runnable() {
                            public void run() {

                                new SweetAlertDialog(OrdersItems.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("طلب البيع")
                                        .setContentText("تمت عملية الأعتماد بنجاح")
                                        .setCustomImage(R.drawable.tick)
                                        .setConfirmText("رجــوع")
                                        .show();

                                loadingdialog.dismiss();


                                contactList.clear();
                                showList(0);
                                GetMaxPONo();

                                Total.setText("");
                                dis.setText("");
                                NetTotal.setText("");
                                TotalTax.setText("");
                            }
                        });
                    } else {

                        loadingdialog.dismiss();
                        _handler.post(new Runnable() {
                            public void run() {
                                new SweetAlertDialog(OrdersItems.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("طلب البيع")
                                         .setContentText(getResources().getText(R.string.PostNotCompleteSuccfully) + "   " + We_Result.ID + "")
                                        .setCustomImage(R.drawable.error_new)
                                        .setConfirmText("رجــوع")
                                        .show();




                            }
                        });
                    }

                } catch (final Exception e) {
                    loadingdialog.dismiss();
                    _handler.post(new Runnable() {
                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    OrdersItems.this).create();
                            alertDialog.setTitle(getResources().getText(R.string.ConnectError));
                            alertDialog.setMessage(e.getMessage().toString());
                            alertDialog.setIcon(R.drawable.tick);
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                            alertDialog.show();
                        }
                    });
                }
            }
        }).start();
    }

    public void btn_new(View view) {
        TextView custNm = (TextView) findViewById(R.id.tv_cusnm);
        TextView pono = (TextView) findViewById(R.id.et_OrdeNo);
        TextView acc = (TextView) findViewById(R.id.tv_acc);
        TextView Total = (TextView) findViewById(R.id.et_Total);
        TextView dis = (TextView) findViewById(R.id.et_dis);
        TextView TotalTax = (TextView) findViewById(R.id.et_TotalTax);
        TextView NetTotal = (TextView) findViewById(R.id.tv_NetTotal);

        Radio_cash.setChecked(false);
        Radio_thmm.setChecked(false);
        Radio_Check.setChecked(false);

        //  mNav.toggleLeftDrawer();
        Total.setText("0.0");
        dis.setText("0.0");
        TotalTax.setText("0.0");
        NetTotal.setText("0.0");

        IsNew = true;
        custNm.setText("");
        pono.setText("");
        acc.setText("");
        GetMaxPONo();
        contactList.clear();
        showList(0);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        acc.setText(sharedPreferences.getString("CustNo", ""));
        custNm.setText(sharedPreferences.getString("CustNm", ""));
        FillTempDate();
    }

    public void btn_Delete_Item(final View view) {
        int position = lv_Items.getPositionForView(view);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getResources().getText(R.string.Ordersales));
        alertDialog.setMessage(getResources().getText(R.string.DoYouWantContinDelete));
        alertDialog.setIcon(R.drawable.delete);
        alertDialog.setPositiveButton(getResources().getText(R.string.Ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                int position = lv_Items.getPositionForView(view);
                contactList.remove(position);
                sqlHandler.executeQuery("update   TempOrderItems  set Qty ='0',Bounce ='0'  where ItemNo='" + contactList.get(position - 1).getno() + "'");

                CalcTotal();
                showList(0);
            }
        });

        alertDialog.setNegativeButton(getResources().getText(R.string.No), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}
