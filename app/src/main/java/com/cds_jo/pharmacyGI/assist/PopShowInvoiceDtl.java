package com.cds_jo.pharmacyGI.assist;

import android.annotation.SuppressLint;

import androidx.fragment.app.DialogFragment;

@SuppressLint("ValidFragment")
class Acc_Report_D extends DialogFragment {
   /* cls_ACC_Report cls_acc_report ;
    ArrayList<cls_ACC_Report> list;
    final Handler _handler = new Handler();
    Acc_Report_Adapter adapter;
    ListView listView;
    Button back;

    @Override
    public void onStart()
    {
        super.onStart();
        if (getDialog() == null)
            return;

        int dialogWidth = WindowManager.LayoutParams.MATCH_PARENT;//340; // specify a value here
        int dialogHeight = WindowManager.LayoutParams.MATCH_PARENT;//400; // specify a value here

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(GVal.dis);
        View view = inflater.inflate(R.layout.layout_dialog, container, false);
        int dialogWidth = WindowManager.LayoutParams.MATCH_PARENT;//340; // specify a value here
        int dialogHeight = WindowManager.LayoutParams.MATCH_PARENT;//400; // specify a value here

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
        back=(Button) view.findViewById(R.id.back);
        //cls_acc_report =new cls_ACC_Report();
        list =new ArrayList<>();
        listView=(ListView) view.findViewById(R.id.LstvItems);
        showD();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent myIntent = new Intent(getActivity(), Sale_InvoiceActivity.class);
                //getActivity().startActivity(myIntent);
                dismiss();

            }});
        return view;
    }

    private void showD() {

        Thread thread = new Thread() {
            @Override
            public void run() {


                CallWebServices ws = new CallWebServices(getActivity());
                ws.GET_ACC_Report_D(GVal.bill,GVal.doctype,GVal.myear,GVal.v_type);
                try {
                    Integer i;

                    JSONObject js = new JSONObject(We_Result.Msg);
                    JSONArray Itemname = js.getJSONArray("Itemname");
                    JSONArray js_UserID= js.getJSONArray("item_no");
                    JSONArray TaxValue = js.getJSONArray("TaxValue");
                    JSONArray UnitNo = js.getJSONArray("UnitNo");
                    JSONArray bonus= js.getJSONArray("bonus");
                    JSONArray price= js.getJSONArray("price");
                    JSONArray qty = js.getJSONArray("qty");
                    JSONArray UnitName = js.getJSONArray("UnitName");
                    JSONArray SumWithOutTax = js.getJSONArray("SumWithOutTax");
                    JSONArray linedis = js.getJSONArray("linedis");
                    cls_acc_report = new cls_ACC_Report();
                    for (i = 0; i < Itemname.length(); i++) {
                        cls_acc_report = new cls_ACC_Report();

                        cls_acc_report.setItem_no(js_UserID.get(i).toString());
                        cls_acc_report.setItemname(Itemname.get(i).toString());
                        cls_acc_report.setTaxValue(TaxValue.get(i).toString());
                        cls_acc_report.setUnitName(UnitName.get(i).toString());
                        cls_acc_report.setBonus(bonus.get(i).toString());
                        cls_acc_report.setPrice(price.get(i).toString());
                        cls_acc_report.setQty(qty.get(i).toString());
                        cls_acc_report.setLinedis(linedis.get(i).toString());
                        cls_acc_report.setSumWithOutTax(SumWithOutTax.get(i).toString());


                        list.add(cls_acc_report);
                    }
                    _handler.post(new Runnable() {
                        public void run() {

                            adapter = new Acc_Report_Adapter(getActivity(), list);
                            listView.setAdapter(adapter);
                        }
                    });

                } catch (final Exception e) {

                }

            }
        };
        thread.start();

    }*/

}
