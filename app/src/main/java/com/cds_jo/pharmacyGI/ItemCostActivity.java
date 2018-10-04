package com.cds_jo.pharmacyGI;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ListView;

import com.cds_jo.pharmacyGI.assist.CallWebServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import Methdes.MyTextView;
import hearder.main.Header_Frag;

public class ItemCostActivity extends FragmentActivity {
    ListView items_Lsit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.n_view_item_cost);

        Fragment frag=new Header_Frag();
        android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1,frag).commit();
    }



    public void onProgressUpdate(String... text ){

        items_Lsit=(ListView)findViewById(R.id.lst_acc);
        items_Lsit.setAdapter(null);
        float Qty ,Cost;
        Qty= Cost=  0 ;
        try {
            JSONObject js = new JSONObject(text[0]);
            JSONArray js_bill_no= js.getJSONArray("bill_no");
            JSONArray js_bill_date= js.getJSONArray("bill_date");
            JSONArray js_venname= js.getJSONArray("venname");
            JSONArray js_qty= js.getJSONArray("qty");
            JSONArray js_cost= js.getJSONArray("cost");
            JSONArray js_UnitName= js.getJSONArray("UnitName");
            JSONArray js_unitcost= js.getJSONArray("unitcost");



            ArrayList<Cls_ItemCost> cls_itemCosts = new ArrayList<Cls_ItemCost>();

            Cls_ItemCost cls_itemCost = new Cls_ItemCost();
            cls_itemCost.setBill_no("رقم الفاتورة");
            cls_itemCost.setBill_date("تاريخ الفاتورة");
            cls_itemCost.setVenname("المورد");
            cls_itemCost.setUnitName("الوحدة");
            cls_itemCost.setQty("الكمية");
            cls_itemCost.setUnitcost(" سعر الوحدة");
            cls_itemCost.setCost("الاجمالي");

            cls_itemCosts.add(cls_itemCost);


            for(int i =0 ; i<js_bill_no.length();i++)
            {
                cls_itemCost = new Cls_ItemCost();

                cls_itemCost.setBill_no(js_bill_no.get(i).toString());
                cls_itemCost.setBill_date(js_bill_date.get(i).toString());
                cls_itemCost.setVenname(js_venname.get(i).toString());
                cls_itemCost.setQty(js_qty.get(i).toString());
                cls_itemCost.setUnitName(js_UnitName.get(i).toString());
                cls_itemCost.setQty(js_qty.get(i).toString());
                cls_itemCost.setUnitcost(js_unitcost.get(i).toString());
                cls_itemCost.setCost(js_cost.get(i).toString());
                cls_itemCosts.add(cls_itemCost);

                Qty=Qty+Float.valueOf(cls_itemCost.getQty());
                Cost=Cost+Float.valueOf(cls_itemCost.getCost());


            }


            cls_itemCost = new Cls_ItemCost();
            cls_itemCost.setUnitName("المجموع");
            cls_itemCost.setQty(String.valueOf(Qty));
            cls_itemCost.setCost(String.valueOf(Cost));

            cls_itemCosts.add(cls_itemCost);


            Cls_ItemCost_Adapter cls_itemCost_adapter = new Cls_ItemCost_Adapter(
                    ItemCostActivity.this, cls_itemCosts);

            items_Lsit.setAdapter(cls_itemCost_adapter);


        }
        catch (    Exception ex)
        {

        }
    }


    public void btn_searchCustomer(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "ItemCost");
        FragmentManager Manager =  getFragmentManager();
        Select_Items obj = new Select_Items();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }

    public void Set_Item(final String No, String Nm) {
        final MyTextView CustNm =(MyTextView)findViewById(R.id.tv_cusnm);
        MyTextView acc = (MyTextView)findViewById(R.id.tv_acc);
        acc.setText(No);
        CustNm.setText(Nm);
        CustNm.setError(null);

        Thread rant = new Thread() {
            public void run() {
                CallWebServices ws = new CallWebServices(ItemCostActivity.this);
                ws.ItemCostReport(No, "s");
            }
        };
        rant.start();
        try {
            rant.join();
            onProgressUpdate(We_Result.Msg);

        } catch (Exception ex) {
        }

    }

    public void btn_back(View view) {
        Intent k = new Intent(this, GalaxyMainActivity.class);
        startActivity(k);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), GalaxyMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}





