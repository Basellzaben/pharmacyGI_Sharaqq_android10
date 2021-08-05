package com.cds_jo.pharmacyGI;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import hearder.main.Header_Frag;

public class QuestneerActivity extends FragmentActivity {

    String UserID= "";
    TextView accno ;
    TextView CustNm ;
    TextView Maxpo  ;
    EditText Notes ;
    SqlHandler sqlHandler;
    Boolean IsNew = true ;
    ListView lv_Items;
    String query = "";
    ArrayList<Cls_Quest> contactList ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questneer);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sqlHandler = new SqlHandler(this);


        contactList = new ArrayList<Cls_Quest>();
        contactList.clear();
        lv_Items = (ListView) findViewById(R.id.LstvItems);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        UserID= sharedPreferences.getString("UserID", "");
        CustNm =(TextView)findViewById(R.id.tv_cusnm);
        Notes =(EditText)findViewById(R.id.Notes);
        accno = (TextView)findViewById(R.id.tv_acc);
        accno.setText(sharedPreferences.getString("CustNo", ""));
        CustNm.setText(sharedPreferences.getString("CustNm", ""));
        Maxpo = (TextView) findViewById(R.id.et_OrdeNo);

        RadioButton r1 = (RadioButton) findViewById(R.id.r1);
        RadioButton r2 = (RadioButton) findViewById(R.id.r2);
        RadioButton r3 = (RadioButton) findViewById(R.id.r3);
      /*  RadioButton r4 = (RadioButton) findViewById(R.id.r4);
        RadioButton r5 = (RadioButton) findViewById(R.id.r5);
        RadioButton r6 = (RadioButton) findViewById(R.id.r6);
        RadioButton r7 = (RadioButton) findViewById(R.id.r7);
        RadioButton r8 = (RadioButton) findViewById(R.id.r8);
        RadioButton r9 = (RadioButton) findViewById(R.id.r9);
        RadioButton r10 = (RadioButton) findViewById(R.id.r10);
        RadioButton r11 = (RadioButton) findViewById(R.id.r11);
        RadioButton r12 = (RadioButton) findViewById(R.id.r12);
*/
     /*   r1.setTypeface(MethodToUse.SetTFace(QuestneerActivity.this));
        r2.setTypeface(MethodToUse.SetTFace(QuestneerActivity.this));
        r3.setTypeface(MethodToUse.SetTFace(QuestneerActivity.this));
*/
        IsNew = true ;
        Fragment frag=new Header_Frag();
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1,frag).commit();
        GetMaxPONo();
        showList();
    }
    @SuppressLint("Range")
    private void showList(   ) {
        contactList.clear();

        query = "  select Distinct  QuesNo, Questxt      From QuesTbl";

        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                do {
                    Cls_Quest  contactListItems = new Cls_Quest();

                    contactListItems.setQuesNo(c1.getString(c1
                            .getColumnIndex("QuesNo")));
                    contactListItems.setQuestxt(c1.getString(c1
                            .getColumnIndex("Questxt")));
                    contactListItems.setAns1("");
                    contactList.add(contactListItems);

                } while (c1.moveToNext());

            }

            c1.close();
        }
         lv_Items.setAdapter(null);
        QuesrListAdapter contactListAdapter = new QuesrListAdapter(
                QuestneerActivity.this, contactList);
        lv_Items.setAdapter(contactListAdapter);

    }
    public  void GetMaxPONo()    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String u =  sharedPreferences.getString("UserID", "");
        String Login = sharedPreferences.getString("Login", "No");

        String query = "SELECT  COALESCE(MAX(Orderno), 0) +1 AS no FROM QuesHdr where userid ='"+UserID+"'";
        Cursor c1 = sqlHandler.selectQuery(query);
        String max="0";

        if (c1.getCount() > 0) {
            c1.moveToFirst();
            max= String.valueOf(c1.getInt(0));
        }

        if (max.length()==1) {
            Maxpo.setText(intToString(Integer.valueOf(u), 2) + intToString(Integer.valueOf(max), 5));

        }
        else {
            Maxpo.setText(max);

        }



    }

    public static String intToString(int num, int digits) {
        String output = Integer.toString(num);
        while (output.length() < digits) output = "0" + output;
        return output;
    }


    public void btn_save_po(View view) {


        if (accno.getText().toString().length() == 0) {
            accno.setError("required!");
            accno.requestFocus();
            return;
        }

        if (Maxpo.getText().toString().length() == 0) {
            Maxpo.setError("required!");
            Maxpo.requestFocus();
            return;
        }


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle( "الاستبيان");
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

    public void Save_Recod_Po()    {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Long i;
        ContentValues cv =new ContentValues();
        cv.put("Orderno", Maxpo.getText().toString());
        cv.put("acc",accno.getText().toString());
        cv.put("date",currentDateandTime);
        cv.put("userid",UserID);
        cv.put("Notes",Notes.getText().toString().replace(",",""));
        cv.put("Nm",CustNm.getText().toString().replace(",",""));
        cv.put("posted","-1");


        if (IsNew==true) {
            i = sqlHandler.Insert("QuesHdr", null, cv);
        }
        else {
            i = sqlHandler.Update("QuesHdr", cv, " Orderno ='"+Maxpo.getText().toString()+"'");
        }

       if(i>0){
            String q ="Delete from  QuesDtl where Orderno ='"+ Maxpo.getText().toString()+"'";
            sqlHandler.executeQuery(q);

            for (int x = 0; x < contactList.size(); x++) {
                Cls_Quest contactListItems = new Cls_Quest();
                contactListItems = contactList.get(x);


                cv = new ContentValues();
                cv.put("Orderno", Maxpo.getText().toString());
                cv.put("Ans1", contactListItems.getAns1().toString() );
                cv.put("Ans4", contactListItems.getQuesNo().toString().replace(",", ""));
                cv.put("QuesTxt", contactListItems.getQuestxt().toString().replace(",", ""));
                i = sqlHandler.Insert("QuesDtl", null, cv);
            }
        }

        if (i> 0 ) {
             GetMaxPONo();
            showList();
            Notes.setText("");
            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("الاستبيان");
            alertDialog.setMessage(getResources().getText(R.string.AddCompleteSucc));
            IsNew=false;


            alertDialog.setIcon(R.drawable.tick);

            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    View view = null;
                    //btn_print(view);
                }
            });

            // Showing Alert Message
            alertDialog.show();
        }
    }


    public void btn_Search_Orders(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "po");
        android.app.FragmentManager Manager = getFragmentManager();

        SearchQuesActivity obj = new SearchQuesActivity();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }

    @SuppressLint("Range")
    public void Set_Order(String No, String Nm, String acc) { // FillList


        Maxpo.setText(No);
        CustNm.setText(Nm);
        accno.setText(acc);
       // contactList.clear();

      //  showList(0);

       sqlHandler = new SqlHandler(this);


        String query = "  select   Distinct *  from QuesHdr  where Orderno ='" + Maxpo.getText().toString() + "'";
        Cursor c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            if (c1.moveToFirst()) {
                Notes.setText(c1.getString(c1.getColumnIndex("Notes")).toString());
             }

            c1.close();
        }
        query = "  select  QuesTxt ,Ans1 , Ans4 from QuesDtl Where Orderno='" + Maxpo .getText().toString() + "'";
        c1 = sqlHandler.selectQuery(query);
        if (c1 != null && c1.getCount() != 0) {
            contactList.clear();
            if (c1.moveToFirst()) {
                do {
                    Cls_Quest  contactListItems = new Cls_Quest();

                    contactListItems.setQuestxt(c1.getString(c1
                            .getColumnIndex("QuesTxt")));
                    contactListItems.setQuesNo(c1.getString(c1
                            .getColumnIndex("Ans4")));
                    contactListItems.setAns1(c1.getString(c1
                            .getColumnIndex("Ans1")));

                    contactList.add(contactListItems);

                } while (c1.moveToNext());

            }

            c1.close();
        }

        lv_Items.setAdapter(null);
        QuesrListAdapter contactListAdapter = new QuesrListAdapter(
                QuestneerActivity.this, contactList);
        lv_Items.setAdapter(contactListAdapter);
        IsNew=false;

    }



    public void btn_Set3( final View view) {
        int position = lv_Items.getPositionForView(view);
        contactList.get(position).setAns1("3");
    }
    public void btn_Set2( final View view) {
        int position = lv_Items.getPositionForView(view);
        contactList.get(position).setAns1("2");
    }
    public void btn_Set1( final View view) {
        int position = lv_Items.getPositionForView(view);
        contactList.get(position).setAns1("1");
    }

    public void btn_back(View view) {
        Intent k ;
        if(ComInfo.ComNo==1) {
            k = new Intent(this, MainActivity.class);
        }else {
            k = new Intent(this, GalaxyMainActivity.class);
        }

        startActivity(k);
    }
    @Override
    public void onBackPressed() {
        Intent k ;
        if(ComInfo.ComNo==1) {
            k = new Intent(this, MainActivity.class);
        }else {
            k = new Intent(this, GalaxyMainActivity.class);
        }

        startActivity(k);

    }
}
