 package com.cds_jo.pharmacyGI;

 import android.app.AlertDialog;
 import android.content.DialogInterface;
 import android.content.Intent;
 import android.content.SharedPreferences;
 import android.graphics.Bitmap;
 import android.graphics.BitmapFactory;
 import android.os.Bundle;
 import android.preference.PreferenceManager;
 import android.support.v7.app.AppCompatActivity;
 import android.text.InputType;
 import android.text.method.PasswordTransformationMethod;
 import android.view.KeyEvent;
 import android.view.View;
 import android.widget.AdapterView;
 import android.widget.EditText;
 import android.widget.GridView;
 import android.widget.LinearLayout;
 import android.widget.TextView;
 import android.widget.Toast;

 import com.cds_jo.pharmacyGI.assist.Acc_ReportActivity;
 import com.cds_jo.pharmacyGI.assist.CustomerReturnQtyActivity;
 import com.cds_jo.pharmacyGI.assist.OrdersItems;
 import com.cds_jo.pharmacyGI.assist.Sale_InvoiceActivity;

 import java.util.ArrayList;

 import port.bluetooth.BluetoothConnectMenu;


 public class PhrMasterActivity extends AppCompatActivity {
     GridView gridView;
     ArrayList<Items> gridArray = new ArrayList<Items>();
     JalImageGridAdapter customGridAdapter;


     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);

         setContentView(R.layout.activity_jal_master);
         TextView Un = (TextView) findViewById(R.id.tv_UserName);

         SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

       /*  SharedPreferences.Editor editor    = sharedPreferences.edit();
         editor.putString("CompanyID","1");
         editor.putString("CompanyNm", " مجموعة المجرة الدولية ");
         editor.putString("TaxAcc1", "123456");
         editor.putString("Address","عمان  - الاردن");
         editor.putString("Notes", "");

         editor.commit();*/



         String Login = sharedPreferences.getString("Login", "No");
         if (Login.toString().equals("No")) {
             Intent i = new Intent(this, NewLoginActivity.class);
             startActivity(i);
         }


         Un.setText(sharedPreferences.getString("UserName", ""));

         this.setTitle(sharedPreferences.getString("CompanyNm", "") + "/" + sharedPreferences.getString("Address", ""));


         Bitmap rec_mony = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jal4);
         Bitmap invoice = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jal11);
         Bitmap Doctor_Visit = BitmapFactory.decodeResource(this.getResources(), R.mipmap.doctor_visit);
         Bitmap po = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jal1);
         Bitmap print = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jal8);
         Bitmap logout = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jal7);
         Bitmap acc = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jal6);
         Bitmap gps = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jal10);
         Bitmap Transfer = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jal3);
         Bitmap ItemCostReport = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jel15);
         Bitmap schedule = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jel16);
         Bitmap inventory = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jel17);
         Bitmap survey_icon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jal9);
         Bitmap setting = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jal2);
         Bitmap stockadd = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jal12);
         Bitmap preapreqty = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jel19);
         Bitmap updatetrans = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jel18);
         Bitmap manbalance = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jal13);
         Bitmap dailysummery = BitmapFactory.decodeResource(this.getResources(), R.mipmap.jel20);



         Bitmap homeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.home);
         Bitmap userIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.home);
         gridArray.add(new Items(acc, "كشف حساب"));
         gridArray.add(new Items(gps, "خط السير"));
         gridArray.add(new Items(print, "استعراض المواد"));
         gridArray.add(new Items(invoice, "فاتورة"));
         gridArray.add(new Items(rec_mony, "سند قبض"));
         gridArray.add(new Items(po, "طلب بيع"));
         // gridArray.add(new Items(trans_from_store, "استعراض كميات المستودع"));
          gridArray.add(new Items(Doctor_Visit, "زيارة الاطباء"));
         gridArray.add(new Items(Transfer, "تحديث البيانات"));
        // gridArray.add(new Items(ItemCostReport, "استعلام كلفة مادة"));
         gridArray.add(new Items(schedule, "جدول زيارات العميل"));
         gridArray.add(new Items(inventory, " جرد كميات العميل"));
         gridArray.add(new Items(survey_icon, "الاستبيان"));
         gridArray.add(new Items(stockadd, " ارجاع المواد"));
         gridArray.add(new Items(setting, "إعدادات عامة"));
         //gridArray.add(new Items(preapreqty, "طلب تجهيز كميات"));
         gridArray.add(new Items(updatetrans, "تعديل الحركات"));
        // gridArray.add(new Items(manbalance, "جرد كميات المندوب"));
         gridArray.add(new Items(dailysummery, " الملخص اليومي"));
         gridArray.add(new Items(logout, "خروج"));

         try {
             gridView = (GridView) findViewById(R.id.gridView1);
             customGridAdapter = new JalImageGridAdapter(this, R.layout.jel_row_grid, gridArray);
             //customGridAdapter = new CustomGridAdapter    (this, R.layout.gridimage, gridArray);

             //Cls_Main_Adapter apter = new Cls_Main_Adapter(this,gridArray);

             gridView.setAdapter(customGridAdapter);
         } catch (Exception ex) {
             System.out.println("bbbbbbbbbbbbbbbbbbbbbb" + ex.getMessage().toString());
         }

         gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {

                 GetPermession obj = new GetPermession();
                 //  Toast.makeText(getApplicationContext(), gridArray.get(position).getTitle(), Toast.LENGTH_SHORT).show();

                 if (position == 0) {

                     if (!obj.CheckAction(v.getContext(), "30001", SCR_ACTIONS.open.getValue())) {
                         AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
                         alertDialog.setTitle("كشف حساب" + "30001");
                         alertDialog.setMessage("نأسف أنت لا تملك صلاحية فتح هذه الشاشة");
                         alertDialog.setIcon(R.drawable.key);
                         alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int which) {
                                 return;
                             }
                         });
                         alertDialog.show();
                     } else {

                         Intent k = new Intent(v.getContext(), Acc_ReportActivity.class);
                         startActivity(k);
                     }

                 } else if (position == 1) {

                     if (!obj.CheckAction(v.getContext(), "30002", SCR_ACTIONS.open.getValue())) {
                         AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
                         alertDialog.setTitle("خط سير المندوب" + "30002");
                         alertDialog.setMessage("نأسف أنت لا تملك صلاحية فتح هذه الشاشة");
                         alertDialog.setIcon(R.drawable.key);
                         alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int which) {
                                 return;
                             }
                         });
                         alertDialog.show();
                     } else {

                         Intent k = new Intent(v.getContext(), MainActivity.class);
                         startActivity(k);
                     }


                 } else if (position == 2) {

                     if (!obj.CheckAction(v.getContext(), "30007", SCR_ACTIONS.open.getValue())) {
                         AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
                         alertDialog.setTitle("استعراض الصور" + "30007");
                         alertDialog.setMessage("نأسف أنت لا تملك صلاحية فتح هذه الشاشة");
                         alertDialog.setIcon(R.drawable.key);
                         alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int which) {
                                 return;
                             }
                         });
                         alertDialog.show();
                     } else {
                         Intent k = new Intent(v.getContext(), ItemGalleryActivity.class);
                        // startActivity(k);
                     }


                 } else if (position == 3) {
                     if (!obj.CheckAction(v.getContext(), "30003", SCR_ACTIONS.open.getValue())) {
                         AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
                         alertDialog.setTitle("فاتورة مبيعات" + "30003");
                         alertDialog.setMessage("نأسف أنت لا تملك صلاحية فتح هذه الشاشة");
                         alertDialog.setIcon(R.drawable.key);
                         alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int which) {
                                 return;
                             }
                         });
                         alertDialog.show();
                     } else {

                         Intent k = new Intent(v.getContext(), Sale_InvoiceActivity.class);
                         startActivity(k);
                     }


                 } else if (position == 4) {

                     if (!obj.CheckAction(v.getContext(), "30004", SCR_ACTIONS.open.getValue())) {
                         AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
                         alertDialog.setTitle("سند القبض" + "   " + "30004");
                         alertDialog.setMessage("نأسف أنت لا تملك صلاحية فتح هذه الشاشة");
                         alertDialog.setIcon(R.drawable.key);
                         alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int which) {
                                 return;
                             }
                         });
                         alertDialog.show();
                     } else {

                         Intent k = new Intent(v.getContext(), RecvVoucherActivity.class);
                         startActivity(k);
                     }

                 } else if (position == 5) {

                     if (!obj.CheckAction(v.getContext(), "30005", SCR_ACTIONS.open.getValue())) {
                         AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
                         alertDialog.setTitle("طلب البيع " + "  " + "30005");
                         alertDialog.setMessage("نأسف أنت لا تملك صلاحية فتح هذه الشاشة");
                         alertDialog.setIcon(R.drawable.key);
                         alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int which) {
                                 return;
                             }
                         });
                         alertDialog.show();
                     } else {

                         Intent k = new Intent(v.getContext(), OrdersItems.class);
                         startActivity(k);
                     }

                 } else if (position == 6) {
                     Intent k = new Intent(PhrMasterActivity.this, DoctorReportActivity.class);
                     startActivity(k);

                 } else if (position == 7) {
                     if (!obj.CheckAction(v.getContext(), "30008", SCR_ACTIONS.open.getValue())) {
                         AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
                         alertDialog.setTitle("تحديث البيانات" + "  " + "30008");
                         alertDialog.setMessage("نأسف أنت لا تملك صلاحية فتح هذه الشاشة");
                         alertDialog.setIcon(R.drawable.key);
                         alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int which) {
                                 return;
                             }
                         });
                         alertDialog.show();
                     } else {

                         Intent k = new Intent(v.getContext(), UpdateDataToMobileActivity.class);
                         startActivity(k);
                     }



                 } else if (position == 8) {
                     if (!obj.CheckAction(v.getContext(), "30010", SCR_ACTIONS.open.getValue())) {
                         AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
                         alertDialog.setTitle("جدول زيارات العميل" + "  " + "30010");
                         alertDialog.setMessage("نأسف أنت لا تملك صلاحية فتح هذه الشاشة");
                         alertDialog.setIcon(R.drawable.key);
                         alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int which) {
                                 return;
                             }
                         });
                         alertDialog.show();
                     } else {

                         Intent k = new Intent(v.getContext(), ScheduleManActivity.class);
                         startActivity(k);
                     }


                 } else if (position == 9) {

                     if (!obj.CheckAction(v.getContext(), "30011", SCR_ACTIONS.open.getValue())) {
                         AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
                         alertDialog.setTitle("جرد كميات العميل" + "  " + "30011");
                         alertDialog.setMessage("نأسف أنت لا تملك صلاحية فتح هذه الشاشة");
                         alertDialog.setIcon(R.drawable.key);
                         alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int which) {
                                 return;
                             }
                         });
                         alertDialog.show();
                     } else {

                         Intent k = new Intent(v.getContext(), CustomerQty.class);
                         startActivity(k);
                     }


                 } else if (position == 10) {
                     Intent k = new Intent(v.getContext(), QuestneerActivity.class);
                     startActivity(k);

                 } else if (position == 11) {
                     if (!obj.CheckAction(v.getContext(), "30013", SCR_ACTIONS.open.getValue())) {
                         AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
                         alertDialog.setTitle("ارجاع المواد" + "  " + "30013");
                         alertDialog.setMessage("نأسف أنت لا تملك صلاحية فتح هذه الشاشة");
                         alertDialog.setIcon(R.drawable.key);
                         alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int which) {
                                 return;
                             }
                         });
                         alertDialog.show();
                     } else {

                        Intent k = new Intent(v.getContext(), CustomerReturnQtyActivity.class);
                         //        Intent k = new Intent(v.getContext(), Main2Activity.class);

                         startActivity(k);
                     }


                 } else if (position == 12) {

                     if (!obj.CheckAction(v.getContext(), "30014", SCR_ACTIONS.open.getValue())) {
                         AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
                         alertDialog.setTitle("اعدادات عامة" + "  " + "30014");
                         alertDialog.setMessage("نأسف أنت لا تملك صلاحية فتح هذه الشاشة");
                         alertDialog.setIcon(R.drawable.key);
                         alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int which) {
                                 return;
                             }
                         });
                         alertDialog.show();
                     } else {

                         Intent k = new Intent(v.getContext(), BluetoothConnectMenu.class);
                         startActivity(k);
                     }




                 } else if (position == 13) {


                     final String pass = DB.GetValue(PhrMasterActivity.this, "Tab_Password", "Password", "PassNo = 2");
                     AlertDialog.Builder alertDialog = new AlertDialog.Builder(PhrMasterActivity.this);
                     alertDialog.setTitle(DB.GetValue(PhrMasterActivity.this, "Tab_Password", "PassDesc", "PassNo = 2"));
                     alertDialog.setMessage("الرجاء ادخال كلمة المرور الخاصة بفتح هذه الشاشة");

                     final EditText input = new EditText(PhrMasterActivity.this);
                     input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                     input.setTransformationMethod(new PasswordTransformationMethod());
                     LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                             LinearLayout.LayoutParams.MATCH_PARENT,
                             LinearLayout.LayoutParams.MATCH_PARENT);
                     input.setLayoutParams(lp);
                     alertDialog.setView(input);
                     alertDialog.setIcon(R.drawable.key);

                     alertDialog.setPositiveButton("موافق",
                             new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int which) {
                                     String password = input.getText().toString();

                                     if (pass.equals(password)) {
                                         Intent k = new Intent(PhrMasterActivity.this, EditeTransActivity.class);
                                         startActivity(k);

                                     } else {
                                         Toast.makeText(getApplicationContext(),
                                                 "كلمة المرور غير صحيحة", Toast.LENGTH_SHORT).show();

                                     }

                                 }
                             });

                     alertDialog.setNegativeButton("لا",
                             new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int which) {
                                     dialog.cancel();
                                 }
                             });

                     alertDialog.show();





                 } else if (position == 14) {

                     Intent k = new Intent(v.getContext(),ManSummeryActivity.class);
                     startActivity(k);



             } else if (position == 15) {
                 SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                 SharedPreferences.Editor editor = sharedPreferences.edit();
                 editor.putString("Login", "No");
                 editor.commit();
                 Intent k = new Intent(v.getContext(), NewLoginActivity.class);
                 startActivity(k);


             }

             }
         });


     }

     @Override
     public boolean onKeyDown(int keyCode, KeyEvent event) {
         if ((keyCode == KeyEvent.KEYCODE_BACK)) {
             SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

             String Login = sharedPreferences.getString("Login", "No");
             if (Login.toString().equals("No")) {
                 Intent i = new Intent(this, NewLoginActivity.class);
                 startActivity(i);
             }
         }
         return super.onKeyDown(keyCode, event);
     }

     @Override
     public void onBackPressed() {
         SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

         String Login = sharedPreferences.getString("Login", "No");
         if (Login.toString().equals("No")) {
             Intent i = new Intent(this, NewLoginActivity.class);
             startActivity(i);
         }
         return;
     }
 }


