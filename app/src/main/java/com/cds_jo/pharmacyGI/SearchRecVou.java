package com.cds_jo.pharmacyGI;

import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchRecVou   extends DialogFragment implements View.OnClickListener   {
    View form ;
    Button add,cancel;
    ListView items_Lsit;
    TextView itemnm;

  public View onCreate(final LayoutInflater inflater   , ViewGroup container  ,Bundle savestate){

        form =inflater.inflate(R.layout.activity_search_rec_vou,container,false);
        //getDialog().setTitle("Galaxy International Group");
       /* add =(Button)form.findViewById(R.id.btn_add);
        cancel=(Button)form.findViewById(R.id.btn_cancel);
        add.setOnClickListener(this);
        cancel.setOnClickListener(this);
*/     /*  itemnm = (TextView)form.findViewById(R.id.ed_item_nm);*/
        /*items_Lsit=(ListView) form.findViewById(R.id.listView2);
        ArrayList<Cls_SearchRecVou> cls_search_pos_list  = new ArrayList<Cls_SearchRecVou>();
        cls_search_pos_list.clear();
        String q = "Select * from RecVoucher";
        SqlHandler sqlHandler = new SqlHandler(getActivity());
        Cursor c1 = sqlHandler.selectQuery(q);

        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                     Cls_SearchRecVou cls_searchRecVou= new Cls_SearchRecVou();

                    cls_searchRecVou.setDocNo(c1.getString(c1.getColumnIndex("DocNo")));
                    cls_searchRecVou.setAmt(c1.getString(c1.getColumnIndex("Amnt")));
*//*
                    cls_searchpos.setDate(c1.getString(c1.getColumnIndex("date")));
                    cls_searchpos.setAcc(c1.getString(c1.getColumnIndex("acc")));
                    cls_searchpos.setNotes("Notes");
*//*


                    cls_search_pos_list.add(cls_searchRecVou);



                }while (c1.moveToNext());
            }
        }
        c1.close();


        Cls_SearchRecVou_Adapter cls_searchRecVou_adapter = new Cls_SearchRecVou_Adapter(
                this.getActivity(),cls_search_pos_list);


        items_Lsit.setAdapter(cls_searchRecVou_adapter);






        items_Lsit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                *//*arg1.setBackgroundColor(Color.GREEN);
                Object o = items_Lsit.getItemAtPosition(position);
                String str=(String)o;//As you are using Default String Adapter*//*
                cls_Search_po po_obj = (cls_Search_po) arg0.getItemAtPosition(position);
                String nm = po_obj.getCustNo();

                Toast.makeText(getActivity(), nm, Toast.LENGTH_LONG).show();

                ((OrdersItems)getActivity()).Set_Order(po_obj.getCustNo(), po_obj.getCustNm(),po_obj.getAcc());
                Exist_Pop();
                //getDialog().setTitle(str);
                ////itemnm.setText(str);
            }


        });
*/
        return  form;
    }

    public void Exist_Pop ()
    {
        this.dismiss();
    }
    @Override
    public void onClick(View v) {
        Button bu = (Button) v ;
        if (bu.getText().toString().equals("Cancel")){
            this.dismiss();
        }
        else  if (bu.getText().toString().equals("Add")){
            Toast.makeText(getActivity(),
                    "Your Message", Toast.LENGTH_SHORT).show();
            //   ((OrdersItems)getActivity()).Save_Method("maen");


        }


    }


    public void onListItemClick(ListView l, View v, int position, long id) {


        // Set the item as checked to be highlighted
        items_Lsit.setItemChecked(position, true);
        v.setBackgroundColor(Color.BLUE);

        //conversationAdapter.notifyDataSetChanged();

    }
/*@Override
    public  void OnClick(View view  ){

}*/
}
