
package com.cds_jo.pharmacyGI;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.pharmacyGI.assist.CallWebServices;
import com.cds_jo.pharmacyGI.assist.Cls_Deptf;
import com.cds_jo.pharmacyGI.assist.Cls_Deptf_adapter;
import com.cds_jo.pharmacyGI.assist.Cls_Invf;
import com.cds_jo.pharmacyGI.assist.Cls_UnitItems;
import com.cds_jo.pharmacyGI.assist.Cls_UnitItems_Adapter;
import com.cds_jo.pharmacyGI.assist.OrdersItems;
import com.cds_jo.pharmacyGI.assist.Po_ListItemAdapter;
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


public class Pop_Po_CompareSales extends DialogFragment implements View.OnClickListener {

    View form;
    Button add, cancel,prevBtn,nextBtn;
   MyTextView tv_Sales,   tv_Returns,tv_Total;
    public String ItemNo = "";
    SqlHandler sqlHandler;
     String Area ,ManNo, FromDate ,ToDate;
    double Total_Sales,Total_Return,Toral_Diff;
    Button btn_Back;
      ArrayList<Cls_CompareSalesReport> SalesLists ;
      ArrayList<Cls_CompareSalesReport> Returns ;

    ListView Lst_Retrns , Lst_Sales;

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

        int dialogWidth = WindowManager.LayoutParams.MATCH_PARENT;//340; // specify a value here
        int dialogHeight = WindowManager.LayoutParams.MATCH_PARENT;//400; // specify a value here

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savestate) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        form = inflater.inflate(R.layout.pop_po_select_items_new, container, false);

        Lst_Sales = (ListView)  form.findViewById(R.id.Lst_Sales);
        Lst_Retrns = (ListView) form.findViewById(R.id.Lst_Retrns);

        tv_Sales = (MyTextView) form.findViewById(R.id.tv_Sales);
        tv_Returns =(MyTextView)form.findViewById(R.id.tv_Returns);
        tv_Total = (MyTextView) form.findViewById(R.id.tv_Total);

        btn_Back = (Button) form.findViewById(R.id.btn_Back);

        btn_Back.setOnClickListener(this);

        SalesLists = new ArrayList<Cls_CompareSalesReport>();
        Returns = new ArrayList<Cls_CompareSalesReport>();

        Area=getArguments().getString("Area");
        ManNo=getArguments().getString("ManNo");
        FromDate =getArguments().getString("FromDate");
        ToDate=getArguments().getString("ToDate");



        this.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        onProgressUpdate();
        return form;
    }


    @Override
    public void onClick(View v) {

        if (v == btn_Back) {
           this.dismiss();
       }


    }
    public void onProgressUpdate( ) {
        final List<String> items_ls = new ArrayList<String>();


        Lst_Retrns.setAdapter(null);
        Lst_Sales.setAdapter(null);


        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(getResources().getText(R.string.From_Date));
        alertDialog.setMessage(getResources().getText(R.string.PleaseCheckDate));
        alertDialog.setIcon(R.drawable.delete);
        alertDialog.setButton(getResources().getText(R.string.Ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        alertDialog.setTitle(getResources().getText(R.string.To_Date));
        alertDialog.setMessage(getResources().getText(R.string.PleaseCheckDate));
        alertDialog.setIcon(R.drawable.delete);
        alertDialog.setButton(getResources().getText(R.string.Ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


            }
        });


        final Handler _handler = new Handler();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String UserID = sharedPreferences.getString("UserID", "");
        final ProgressDialog custDialog = new ProgressDialog(getActivity());

        custDialog.setTitle(getResources().getText(R.string.PleaseWait));
        custDialog.setMessage(getResources().getText(R.string.Retrive_DataUnderProcess));
        custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
        custDialog.setCanceledOnTouchOutside(false);
        custDialog.setProgress(0);
        custDialog.setMax(100);
        custDialog.show();


        new Thread(new Runnable() {
            @Override
            public void run() {

                CallWebServices ws = new CallWebServices(getActivity());

                ws.CallCountrySalesCompare( Area,ManNo,FromDate,ToDate);


                try {

                    Integer i;
                    String q = "";
                    JSONObject js = new JSONObject(We_Result.Msg);


                    JSONArray js_Total = js.getJSONArray("Total");
                    JSONArray js_bill = js.getJSONArray("bill");
                    JSONArray js_BillDate = js.getJSONArray("BillDate");
                    JSONArray js_cusname = js.getJSONArray("cusname");
                    JSONArray js_DocType = js.getJSONArray("DocType");
                    JSONArray js_DocFlg = js.getJSONArray("DocFlg");



                    Cls_CompareSalesReport Sales_Obj = new Cls_CompareSalesReport();
                    Cls_CompareSalesReport Return_Obj = new Cls_CompareSalesReport();


                    Sales_Obj.setTotal("المجموع");
                    Sales_Obj.setBill("المستند");
                    Sales_Obj.setBillDate("التاريخ");
                    Sales_Obj.setCusname("الصيدلية");
                    Sales_Obj.setDocType("");
                    Sales_Obj.setDocFlg("");

                    Return_Obj.setTotal("المجموع");
                    Return_Obj.setBill("المستند");
                    Return_Obj.setBillDate("التاريخ");
                    Return_Obj.setCusname("الصيدلية");
                    Return_Obj.setDocType("");
                    Return_Obj.setDocFlg("");





                    SalesLists.add(Sales_Obj);
                    Returns .add(Return_Obj);

                    Total_Sales = 0;
                    Total_Return = 0;
                    Toral_Diff = 0;


                    for (i = 0; i < js_bill.length(); i++) {
                        if(js_DocFlg.get(i).toString().equalsIgnoreCase("1")) {
                            Sales_Obj = new Cls_CompareSalesReport();
                            Sales_Obj.setTotal(js_Total.get(i).toString());
                            Sales_Obj.setBill(js_bill.get(i).toString());
                            Sales_Obj.setBillDate(js_BillDate.get(i).toString());
                            Sales_Obj.setCusname(js_cusname.get(i).toString());
                            Sales_Obj.setDocType(js_DocType.get(i).toString());
                            Sales_Obj.setDocFlg(js_DocFlg.get(i).toString());
                            Total_Sales=Total_Sales+SToD(js_Total.get(i).toString());
                            SalesLists.add(Sales_Obj);
                        }else {


                            Sales_Obj = new Cls_CompareSalesReport();
                            Sales_Obj.setTotal(js_Total.get(i).toString());
                            Sales_Obj.setBill(js_bill.get(i).toString());
                            Sales_Obj.setBillDate(js_BillDate.get(i).toString());
                            Sales_Obj.setCusname(js_cusname.get(i).toString());
                            Sales_Obj.setDocType(js_DocType.get(i).toString());
                            Sales_Obj.setDocFlg(js_DocFlg.get(i).toString());
                            Total_Return=Total_Return+SToD(js_Total.get(i).toString());
                            Returns.add(Sales_Obj);
                        }

                            /*Total_Qty = Total_Qty + SToD(js_qty.get(i).toString());
                            Total_Price = Total_Price + SToD(js_price.get(i).toString());
                            Total_Row = Total_Row + (SToD(js_qty.get(i).toString()) * SToD(js_price.get(i).toString()));*/



                        custDialog.setMax(js_bill.length());
                        custDialog.incrementProgressBy(1);

                        if (custDialog.getProgress() == custDialog.getMax()) {
                            custDialog.dismiss();
                        }

                    }

                    _handler.post(new Runnable() {

                        public void run() {


                            Total_Sales = SToD(Total_Sales + "");
                            Total_Return = SToD(Total_Return + "");



                            Toral_Diff = Total_Sales-Total_Return;
                            Toral_Diff = SToD(Toral_Diff + "");
                            tv_Sales.setText(Total_Sales + "");
                            tv_Returns.setText(Total_Return + "");
                            tv_Total.setText(Toral_Diff + "");


                            Cls_Sales_Compare_Adapter Sales_Adapter = new Cls_Sales_Compare_Adapter(
                                    getActivity(), SalesLists);


                               Cls_Sales_Compare_Adapter Return_Adapter = new Cls_Sales_Compare_Adapter(
                                    getActivity(), Returns);




                            Lst_Sales.setAdapter(Sales_Adapter);
                            Lst_Retrns.setAdapter(Return_Adapter);

                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    getActivity()).create();
                            alertDialog.setTitle("تحديث البيانات");

                            alertDialog.setMessage("تمت عملية استرجاع  البيانات بنجاح ");
                            alertDialog.setIcon(R.drawable.tick);
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });


                            custDialog.dismiss();
                        }
                    });

                } catch (final Exception e) {
                    custDialog.dismiss();
                    _handler.post(new Runnable() {

                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    getActivity()).create();

                            alertDialog.setTitle("كشف  المبيعات  ");
                            if (We_Result.ID == -404) {
                                alertDialog.setMessage(We_Result.Msg);
                            } else {
                                alertDialog.setMessage("لا يوجد بيانات");
                            }
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

    }





