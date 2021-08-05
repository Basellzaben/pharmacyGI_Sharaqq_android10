package com.cds_jo.pharmacyGI.assist;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.cds_jo.pharmacyGI.Pop_Po_Select_Items_New_Activity;
import com.cds_jo.pharmacyGI.R;
import com.cds_jo.pharmacyGI.SqlHandler;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import Methdes.MyTextView;

public class PoListViewHolder extends RecyclerView.ViewHolder {
    SqlHandler sqlHandler;
    MyTextView ItemNo, ItemNm,ItemPrice;
    EditText CustQty;
    EditText tv_Qty, tv_BoQty;
    String ListIndex;
    String q;
    Context context;
    ImageButton btn_QtyDec, btn_QtyInc, btn_BoDec, btn_BoInc,btn_CustQtyDec, btn_CustQtyInc;
    int StoreQtyAv;
    public PoListViewHolder(View itemView, Context c) {
        super(itemView);
        CustQty = (EditText) itemView.findViewById(R.id.tv_CustQty);
        ItemNo = (MyTextView) itemView.findViewById(R.id.tv_ItemNo);
        ItemNm = (MyTextView) itemView.findViewById(R.id.tv_ItemNm);
        ItemPrice = (MyTextView) itemView.findViewById(R.id.tv_ItemPrice);
        tv_Qty = (EditText) itemView.findViewById(R.id.tv_Qty);
        btn_QtyDec = (ImageButton) itemView.findViewById(R.id.btn_QtyDec);
        btn_QtyInc = (ImageButton) itemView.findViewById(R.id.btn_QtyInc);
        context = c;
        tv_BoQty = (EditText) itemView.findViewById(R.id.tv_BoQty);
        btn_BoDec = (ImageButton) itemView.findViewById(R.id.btn_BoDec);
        btn_BoInc = (ImageButton) itemView.findViewById(R.id.btn_BoInc);
        btn_CustQtyDec = (ImageButton) itemView.findViewById(R.id.btn_CustQtyDec);
        btn_CustQtyInc = (ImageButton) itemView.findViewById(R.id.btn_CustQtyInc);


        sqlHandler = new SqlHandler(c);


        btn_CustQtyInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                try {

                    if (CustQty.getText().toString().trim().equalsIgnoreCase("")) {
                        CustQty.setText("0");
                    }
                    //  Toast.makeText(context, "3", Toast.LENGTH_SHORT).show();
                    CustQty.setText((Integer.parseInt(CustQty.getText().toString()) + 1) + "");
                    if (Integer.parseInt(CustQty.getText().toString()) >= 1) {
                        btn_CustQtyDec.setVisibility(View.VISIBLE);
                    }

                    sqlHandler.executeQuery("update invf set Pack='"+CustQty.getText().toString()+"' where Item_No='"  +ItemNo.getText().toString()+"'");
                    sqlHandler.executeQuery("update TempOrderItems set Pack='"+CustQty.getText().toString()+"' where ItemNo='"  +ItemNo.getText().toString()+"'");

                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

            }
        });

        btn_CustQtyDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                try {

                    if (CustQty.getText().toString().trim().equalsIgnoreCase("")) {
                        CustQty.setText("0");
                    }
                    CustQty.setText((Integer.parseInt(CustQty.getText().toString()) - 1) + "");
                    if (Integer.parseInt(CustQty.getText().toString()) <=0) {
                        CustQty.setText("0");
                        btn_CustQtyDec.setVisibility(View.INVISIBLE);                    }
                    sqlHandler.executeQuery("update TempOrderItems set Pack='"+CustQty.getText().toString()+"' where ItemNo='"  +ItemNo.getText().toString()+"'");
                    sqlHandler.executeQuery("update invf set Pack='"+CustQty.getText().toString()+"' where Item_No='"  +ItemNo.getText().toString()+"'");

                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        });







        tv_Qty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    tv_Qty.setText("");
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        });

        tv_BoQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    tv_BoQty.setText("");
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        });
        btn_QtyDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                try {

                    if (tv_Qty.getText().toString().trim().equalsIgnoreCase("")) {
                        tv_Qty.setText("0");
                    }
                    tv_Qty.setText((Integer.parseInt(tv_Qty.getText().toString()) - 1) + "");
                    if (Integer.parseInt(tv_Qty.getText().toString()) <=0) {
                        tv_Qty.setText("0");
                        btn_QtyDec.setVisibility(View.INVISIBLE);                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        });






        btn_QtyInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                try {

                    if (tv_Qty.getText().toString().trim().equalsIgnoreCase("")) {
                        tv_Qty.setText("0");
                    }
                  //  Toast.makeText(context, "3", Toast.LENGTH_SHORT).show();
                    tv_Qty.setText((Integer.parseInt(tv_Qty.getText().toString()) + 1) + "");
                    if (Integer.parseInt(tv_Qty.getText().toString()) >= 1) {
                        btn_QtyDec.setVisibility(View.VISIBLE);
                    }


                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        });


        btn_BoDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                try {

                    if (tv_BoQty.getText().toString().trim().equalsIgnoreCase("")) {
                        tv_BoQty.setText("0");
                    }

                    tv_BoQty.setText((Integer.parseInt(tv_BoQty.getText().toString()) - 1) + "");
                    if (Integer.parseInt(tv_BoQty.getText().toString()) <= 0) {
                        tv_BoQty.setText("0");
                        btn_BoDec.setVisibility(View.INVISIBLE);
                    }

                } catch(Exception ex){
                    Toast.makeText(context,ex.getMessage().toString(),Toast.LENGTH_SHORT);
               }

            }
        });

        btn_BoInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                try {
                    if (tv_BoQty.getText().toString().trim().equalsIgnoreCase("")) {
                        tv_BoQty.setText("0");
                    }
                    tv_BoQty.setText((Integer.parseInt(tv_BoQty.getText().toString()) + 1) + "");
                    if (Integer.parseInt(tv_BoQty.getText().toString()) >= 1) {
                        btn_BoDec.setVisibility(View.VISIBLE);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        });
        tv_Qty.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {



            }
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                StoreQtyAv = 0;
                if (!(tv_Qty.getText().toString().equalsIgnoreCase("") || tv_Qty.getText().toString().equalsIgnoreCase("0"))) {
                    StoreQtyAv = ((Pop_Po_Select_Items_New_Activity) context).GetAvQty(ItemNo.getText().toString(), tv_BoQty.getText().toString());


                if (StoreQtyAv < SToD(tv_Qty.getText().toString())) {
                    android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(
                            context).create();
                    alertDialog.setTitle("طلب البيع");
                    alertDialog.setMessage("الكمية المطلوبة غير متوفرة،  الكمية المتاحة :" + StoreQtyAv);            // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.tick);
                    alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.show();
                    tv_Qty.setText(StoreQtyAv + "");
                    try {
                        if (StoreQtyAv < 0) {
                            tv_Qty.setText("0");                        }
                    }catch (Exception ex){}
                } else {
                    q = " Update TempOrderItems set Qty ='" + tv_Qty.getText().toString() + "'  Where ItemNo = '" + ItemNo.getText().toString() + "'";
                    sqlHandler.executeQuery(q);

                    ((Pop_Po_Select_Items_New_Activity) context).Calc_Total(ItemNo.getText().toString(), tv_Qty.getText().toString());

                }
                if (tv_Qty.getText().toString().equalsIgnoreCase("0")) {
                    tv_BoQty.setText("0");
                }
                    btn_QtyDec.setVisibility(View.VISIBLE);
            }else if (tv_Qty.getText().toString().equalsIgnoreCase("0")){
                    q = " Update TempOrderItems set Qty ='" + tv_Qty.getText().toString() + "'  Where ItemNo = '" + ItemNo.getText().toString() + "'";
                    sqlHandler.executeQuery(q);
                    btn_QtyDec.setVisibility(View.INVISIBLE);
                    ((Pop_Po_Select_Items_New_Activity) context).Calc_Total(ItemNo.getText().toString(), tv_Qty.getText().toString());

                }
            }
        });
        tv_BoQty.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                StoreQtyAv=0;
                if(!(tv_BoQty.getText().toString().equalsIgnoreCase("")|| tv_BoQty.getText().toString().equalsIgnoreCase("0"))) {
                    StoreQtyAv = ((Pop_Po_Select_Items_New_Activity) context).GetAvQty(ItemNo.getText().toString(), tv_Qty.getText().toString());

                    if (StoreQtyAv < SToD(tv_BoQty.getText().toString())) {
                        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(
                                context).create();
                        alertDialog.setTitle("طلب البيع");
                        alertDialog.setMessage("الكمية المطلوبة كبونص غير متاحة،الكمية المتاحة :" + StoreQtyAv);            // Setting Icon to Dialog
                        alertDialog.setIcon(R.drawable.tick);
                        alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alertDialog.show();
                        tv_BoQty.setText(StoreQtyAv + "");
                    } else {
                        q = " Update TempOrderItems set Bounce ='" + tv_BoQty.getText().toString() + "'  Where ItemNo = '" + ItemNo.getText().toString() + "'";
                        sqlHandler.executeQuery(q);

                    }
                    btn_BoDec.setVisibility(View.VISIBLE);
                }else if( tv_BoQty.getText().toString().equalsIgnoreCase("0")){
                    q = " Update TempOrderItems set Bounce ='0'  Where ItemNo = '" + ItemNo.getText().toString() + "'";
                    sqlHandler.executeQuery(q);
                    btn_BoDec.setVisibility(View.INVISIBLE);
                }
            }
        });




        CustQty.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                sqlHandler.executeQuery("update TempCustQty set Qty='"+CustQty.getText().toString()+"' where ItemNo='"  +ItemNo.getText().toString()+"'");




            }
        });


    }
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
}