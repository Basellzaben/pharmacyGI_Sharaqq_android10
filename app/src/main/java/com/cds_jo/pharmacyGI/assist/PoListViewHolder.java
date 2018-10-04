package com.cds_jo.pharmacyGI.assist;

import android.content.Context;

import android.support.v7.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.pharmacyGI.OnChildClickListener;
import com.cds_jo.pharmacyGI.Pop_Po_Select_Items_New;
import com.cds_jo.pharmacyGI.Pop_Po_Select_Items_New_Activity;
import com.cds_jo.pharmacyGI.R;
import com.cds_jo.pharmacyGI.SqlHandler;

import Methdes.MyTextView;

public class PoListViewHolder extends RecyclerView.ViewHolder {
    SqlHandler sqlHandler;
    TextView ItemNo, ItemNm;
    EditText tv_Qty, tv_BoQty;
    String q;
    Context context;

    ImageButton btn_QtyDec, btn_QtyInc, btn_BoDec, btn_BoInc;


    public PoListViewHolder(View itemView, Context c) {
        super(itemView);
        ItemNo = (MyTextView) itemView.findViewById(R.id.tv_ItemNo);
        ItemNm = (MyTextView) itemView.findViewById(R.id.tv_ItemNm);
        tv_Qty = (EditText) itemView.findViewById(R.id.tv_Qty);
        btn_QtyDec = (ImageButton) itemView.findViewById(R.id.btn_QtyDec);
        btn_QtyInc = (ImageButton) itemView.findViewById(R.id.btn_QtyInc);
        context = c;
        tv_BoQty = (EditText) itemView.findViewById(R.id.tv_BoQty);
        btn_BoDec = (ImageButton) itemView.findViewById(R.id.btn_BoDec);
        btn_BoInc = (ImageButton) itemView.findViewById(R.id.btn_BoInc);

        sqlHandler = new SqlHandler(c);


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
                    if (Integer.parseInt(tv_Qty.getText().toString()) < 1) {
                        tv_Qty.setText("1");
                        btn_QtyDec.setVisibility(View.INVISIBLE);
                    }
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
                    if (Integer.parseInt(tv_Qty.getText().toString()) > 1) {
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
                    if (Integer.parseInt(tv_BoQty.getText().toString()) < 1) {
                        tv_BoQty.setText("1");
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
                    if (Integer.parseInt(tv_BoQty.getText().toString()) > 1) {
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
                q = " Update TempOrderItems set Qty ='" + tv_Qty.getText().toString() + "'  Where ItemNo = '" + ItemNo.getText().toString() + "'";
                sqlHandler.executeQuery(q);
                ((Pop_Po_Select_Items_New_Activity)context).methodOnBtnClick(1);
            }
        });


        tv_BoQty.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                q = " Update TempOrderItems set Bounce ='" + tv_BoQty.getText().toString() + "'  Where ItemNo = '" + ItemNo.getText().toString() + "'";
                sqlHandler.executeQuery(q);

            }
        });
    }

}