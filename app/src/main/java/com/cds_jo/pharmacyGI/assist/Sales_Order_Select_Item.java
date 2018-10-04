package com.cds_jo.pharmacyGI.assist;

import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.cds_jo.pharmacyGI.Cls_Sal_InvItems;
import com.cds_jo.pharmacyGI.R;
import com.cds_jo.pharmacyGI.SqlHandler;

import java.util.ArrayList;
import java.util.List;

public class Sales_Order_Select_Item extends FragmentActivity {
    SqlHandler sqlHandler;
    ListView items_Lsit;
    EditText filter;
    String UnitNo = "";
    String Operand = "";
    String UnitName = "";
    float min = 0;
    List<Cls_Sal_InvItems> UpdateItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_po_select_items_new);
        sqlHandler = new SqlHandler(this);
        items_Lsit = (ListView)  findViewById(R.id.listView2);
        filter = (EditText)  findViewById(R.id.et_Search_filter);
       FillDeptf();
         FillItems();
        /*fillUnit("-1");
*/
    }
    private void FillDeptf() {
        final Spinner sp_items_cat = (Spinner) findViewById(R.id.sp_item_cat);
        sqlHandler = new SqlHandler(this);

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
    }
    private void FillItems() {
        filter = (EditText) findViewById(R.id.et_Search_filter);
        String query = "";



        Bundle bundle = getIntent().getExtras();
        UpdateItem = (List<Cls_Sal_InvItems>) bundle.getSerializable("List");


            if (filter.getText().toString().equals("")) {
                query = "Select distinct invf.Item_No , invf.Item_Name,invf.Price, invf.tax   from invf  " +
                        "   where 1=1 ";
            } else {
                query = "Select distinct  invf.Item_No , invf.Item_Name,invf.Price, invf.tax from  invf " +
                        "    where Item_Name  like '%" + filter.getText().toString() + "%'  or  Item_No like '%" + filter.getText().toString() + "%'  ";
            }

            Spinner item_cat = (Spinner)  findViewById(R.id.sp_item_cat);
            Integer indexValue = item_cat.getSelectedItemPosition();

            if (indexValue > 0) {

                Cls_Deptf o = (Cls_Deptf) item_cat.getItemAtPosition(indexValue);

                query = query + "and    Type_No = '" + o.getType_No().toString() + "'";

            }


          item_cat = (Spinner)  findViewById(R.id.sp_item_cat);
          indexValue = item_cat.getSelectedItemPosition();

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
                this, cls_invf_List);
        items_Lsit.setAdapter(cls_invf_adapter);


    }
    public void fillUnit(String item_no) {


        Spinner sp_unite = (Spinner)   findViewById(R.id.sp_units);


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
}
