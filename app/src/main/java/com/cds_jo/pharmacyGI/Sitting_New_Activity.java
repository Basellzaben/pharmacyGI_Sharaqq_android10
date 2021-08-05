package com.cds_jo.pharmacyGI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.cds_jo.pharmacyGI.assist.Cls_Cur;
import com.cds_jo.pharmacyGI.assist.Cls_Cur_Adapter;

import java.util.ArrayList;

import hearder.main.Header_Frag;

/**
 * BluetoothConnectMenu
 * @author Sung-Keun Lee
 * @version 2011. 12. 21.
 */
public class Sitting_New_Activity extends FragmentActivity
{
	private static final String TAG = "BluetoothConnectMenu";
	// Intent request codes
	// private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 2;



	// UI
	private EditText btAddrBox;


	private ListView list;
	// BT







	private Context context;

	private void FillPrinterType() {

		Spinner printerType = (Spinner) findViewById(R.id.sp_Print_Type);


		ArrayList<Cls_Cur> VouchTypeList = new ArrayList<Cls_Cur>();
		VouchTypeList.clear();

		Cls_Cur cls_cur = new Cls_Cur();
		cls_cur.setName("SEWOO");
		cls_cur.setNo("1");
		VouchTypeList.add(cls_cur);


		cls_cur = new Cls_Cur();
		cls_cur.setName("ZEPRA ZQ520");
		cls_cur.setNo("2");
		VouchTypeList.add(cls_cur);


		cls_cur = new Cls_Cur();
		cls_cur.setName("ZEPRA IME230");
		cls_cur.setNo("3");
		VouchTypeList.add(cls_cur);


		Cls_Cur_Adapter cls_cur_adapter = new Cls_Cur_Adapter(
				this, VouchTypeList);
		try {
			printerType.setAdapter(cls_cur_adapter);
		}
		catch (Exception ex ){
			//Toast.makeText(this,ex.getMessage().toString(),Toast.LENGTH_SHORT).show();

		}
	}
	private void FillMobileType() {

		Spinner  MobileType = (Spinner) findViewById(R.id.sp_MobileType);


		ArrayList<Cls_Cur> VouchTypeList = new ArrayList<Cls_Cur>();
		VouchTypeList.clear();

		Cls_Cur cls_cur = new Cls_Cur();
		cls_cur.setName("Dell");
		cls_cur.setNo("1");
		VouchTypeList.add(cls_cur);


		cls_cur = new Cls_Cur();
		cls_cur.setName("Lenova");
		cls_cur.setNo("2");
		VouchTypeList.add(cls_cur);


		cls_cur = new Cls_Cur();
		cls_cur.setName("Samsung");
		cls_cur.setNo("3");
		VouchTypeList.add(cls_cur);


		Cls_Cur_Adapter cls_cur_adapter = new Cls_Cur_Adapter(
				this, VouchTypeList);
		MobileType.setAdapter(cls_cur_adapter);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_sitting_new);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		Fragment frag = new Header_Frag();
	FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.Frag1, frag).commit();


		// Setting
		btAddrBox = (EditText) findViewById(R.id.EditTextAddressBT);


		list = (ListView) findViewById(R.id.ListView01);
		context = this;

		// Setting
		Spinner printerType = (Spinner) findViewById(R.id.sp_Print_Type);
		Spinner MobileType = (Spinner) findViewById(R.id.sp_MobileType);
		//loadSettingFile();
		//bluetoothSetup();

		FillPrinterType();
		FillMobileType();


		EditText et_Sal_inv = (EditText) findViewById(R.id.et_m4);
		EditText et_Payments = (EditText) findViewById(R.id.et_Payments);
		EditText et_PrepQty = (EditText) findViewById(R.id.et_m4);

		EditText et_m4 = (EditText) findViewById(R.id.et_m4);
		EditText et_m5 = (EditText) findViewById(R.id.et_m4);
		EditText et_m6 = (EditText) findViewById(R.id.et_m6);


		EditText AddressBT = (EditText) findViewById(R.id.EditTextAddressBT);


		EditText IP = (EditText) findViewById(R.id.et_IP);
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		AddressBT.setText(sharedPreferences.getString("AddressBT", "0"));
		IP.setText(sharedPreferences.getString("ServerIP", "0"));


		et_Sal_inv.setText(sharedPreferences.getString("m1", "0"));
		et_Payments.setText(sharedPreferences.getString("m2", "0"));
		et_PrepQty.setText(sharedPreferences.getString("m3", "0"));
		et_m4.setText(sharedPreferences.getString("m4", "0"));
		et_m5.setText(sharedPreferences.getString("m5", "0"));
		et_m6.setText(sharedPreferences.getString("m6", "0"));


		Cls_Cur_Adapter printerType_adapter = (Cls_Cur_Adapter) printerType.getAdapter();
		Cls_Cur obj = new Cls_Cur();


		if (printerType_adapter != null && printerType_adapter.getCount() > 0) {
			for (int i = 0; i < printerType_adapter.getCount(); i++) {
				obj = (Cls_Cur) printerType_adapter.getItem(i);
				if (obj.getNo().equals(sharedPreferences.getString("PrinterType", "-1"))) {
					printerType.setSelection(i);
					break;
				}
			}
		}


		Cls_Cur_Adapter MobileType_adapter = (Cls_Cur_Adapter) MobileType.getAdapter();
		obj = new Cls_Cur();


		if (MobileType_adapter != null && MobileType_adapter.getCount() > 0) {
			for (int i = 0; i < MobileType_adapter.getCount(); i++) {
				obj = (Cls_Cur) MobileType_adapter.getItem(i);
				if (obj.getNo().equals(sharedPreferences.getString("MobileType", "-1"))) {
					MobileType.setSelection(i);
					break;
				}
			}
		}





	}

	@Override
	protected void onDestroy()
	{

		super.onDestroy();
	}


	/*public void btn_Save_Setting(View view) {

    	EditText     AddressBT = (EditText)findViewById(R.id.EditTextAddressBT);
		EditText     IP = (EditText)findViewById(R.id.et_IP);
		EditText     et_Sal_inv = (EditText)findViewById(R.id.et_Sal_inv);
		EditText     et_Payments = (EditText)findViewById(R.id.et_Payments);
		EditText     et_PrepQty = (EditText)findViewById(R.id.et_PrepQty);
		EditText     et_m4 = (EditText)findViewById(R.id.et_m4);
		EditText     et_m5 = (EditText)findViewById(R.id.et_m5);
		EditText     et_m6 = (EditText)findViewById(R.id.et_m6);
		Spinner printerType = (Spinner) findViewById(R.id.sp_Print_Type);
		Spinner MobileType = (Spinner) findViewById(R.id.sp_MobileType);

		Integer index = printerType.getSelectedItemPosition();
		Integer index1 = MobileType.getSelectedItemPosition();
		Cls_Cur p = (Cls_Cur) printerType.getItemAtPosition(index) ;
		Cls_Cur m = (Cls_Cur) MobileType.getItemAtPosition(index1) ;

		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor    = sharedPreferences.edit();
		editor.putString("ServerIP", IP.getText().toString().trim());
		editor.putString("AddressBT", AddressBT.getText().toString().trim());
		editor.putString("m1", et_Sal_inv.getText().toString().trim());
		editor.putString("m2", et_Payments.getText().toString().trim());
		editor.putString("m3", et_PrepQty.getText().toString().trim());
		editor.putString("m4", et_m4.getText().toString().trim());
		editor.putString("m5", et_m5.getText().toString().trim());
		editor.putString("m6", et_m6.getText().toString().trim());
		editor.putString("PrinterType", p.getNo().toString());
		editor.putString("MobileType", m.getNo().toString());
	 	 editor.commit();

		AlertDialog alertDialog = new AlertDialog.Builder(
				BluetoothConnectMenu.this).create();
		alertDialog.setTitle("إعدادت عامة");

		alertDialog.setMessage("تمت عملية الحفظ بنجاح");
		alertDialog.setIcon(R.drawable.tick);

		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		alertDialog.show();

}*/

	public void DeleteAll(View view) {
/*
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("إعدادات عامة");
		alertDialog.setMessage("هل انت متاكد من عملية الحذف");
		alertDialog.setIcon(R.drawable.delete);
		alertDialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {*/
				String q;
				SqlHandler sqlHandler = new SqlHandler(Sitting_New_Activity.this);



				sqlHandler.executeQuery("Delete from TransferQtyhdr");
				sqlHandler.executeQuery("Delete from TransferQtydtl");
				sqlHandler.executeQuery("Delete from invf");
				sqlHandler.executeQuery("Delete from UnitItems");
				sqlHandler.executeQuery("Delete from Unites");
				sqlHandler.executeQuery("Delete from curf");
				sqlHandler.executeQuery("Delete from deptf");
				//	sqlHandler.executeQuery("Delete from manf");
				sqlHandler.executeQuery("Delete from SaleManRounds");
				sqlHandler.executeQuery("Delete from SaleManPath");
				sqlHandler.executeQuery("Delete from Customers");
				sqlHandler.executeQuery("Delete from stores");
				sqlHandler.executeQuery("Delete from storesSetting");
				sqlHandler.executeQuery("Delete from Offers_Dtl_Cond");
				sqlHandler.executeQuery("Delete from Sal_invoice_Hdr");
				sqlHandler.executeQuery("Delete from Sal_invoice_Det");
				sqlHandler.executeQuery("Delete from Po_dtl");
				sqlHandler.executeQuery("Delete from Po_Hdr");

				sqlHandler.executeQuery("Delete from CustStoreQtydetl");
				sqlHandler.executeQuery("Delete from ReturnQtydetl");
				sqlHandler.executeQuery("Delete from ReturnQtyhdr");
				sqlHandler.executeQuery("Delete from ManStore");
				sqlHandler.executeQuery("Delete from Offers_Hdr");

				sqlHandler.executeQuery("Delete from CustStoreQtyhdr");
				sqlHandler.executeQuery("Delete from ComanyInfo");

				sqlHandler.executeQuery("Delete from CustLastPrice");
				sqlHandler.executeQuery("Delete from BalanceQty");
				sqlHandler.executeQuery("Delete from ManPermession");
				sqlHandler.executeQuery("Delete from CustLocation");
				sqlHandler.executeQuery("Delete from Tab_Password");



				EditText     et_Sal_inv = (EditText)findViewById(R.id.et_Sal_inv);
				EditText     et_Payments = (EditText)findViewById(R.id.et_Payments);
				EditText     et_PrepQty = (EditText)findViewById(R.id.et_PrepQty);

				EditText     et_m4 = (EditText)findViewById(R.id.et_m4);
				EditText     et_m5 = (EditText)findViewById(R.id.et_m5);
				EditText     et_m6 = (EditText)findViewById(R.id.et_m6);


			//	et_Sal_inv.setText("0");
				//et_Payments.setText("0");
				//et_PrepQty.setText("0");
				et_m4.setText("0");
				//et_m5.setText("0");
				et_m6.setText("0");




				SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Sitting_New_Activity.this);
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putString("CompanyID", "0");
				editor.putString("CompanyNm", "مجموعة المجرة الدولية");
				editor.putString("TaxAcc1", "0");
				editor.putString("Address", "عمان");
				editor.putString("Notes","");
				editor.putString("CustNo", "");
				editor.putString("CustNm", "");
				editor.putString("CustAdd", "");
				//editor.putString("m1", "0");
			//	editor.putString("m2", "0");
				//editor.putString("m3", "0");
				editor.putString("m4", "0");
			//	editor.putString("m5", "0");
				editor.putString("m6", "0");

				editor.commit();


				AlertDialog alertDialog = new AlertDialog.Builder(
						Sitting_New_Activity.this).create();
				alertDialog.setTitle("الإعدادات العامة");
				alertDialog.setMessage("تمت عملية الحذف بنجاح");
				alertDialog.setIcon(R.drawable.tick);
				alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});


				alertDialog.show();


		/*	}
		});
*/
		// Setting Negative "NO" Button
		/*alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to invoke NO event
				//Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
				dialog.cancel();
			}
		});

		// Showing Alert Message
		alertDialog.show();*/
	}

	public void btn_Save(View view) {

		EditText     AddressBT = (EditText)findViewById(R.id.EditTextAddressBT);
		EditText     IP = (EditText)findViewById(R.id.et_IP);
		EditText     et_Sal_inv = (EditText)findViewById(R.id.et_Sal_inv);
		EditText     et_Payments = (EditText)findViewById(R.id.et_Payments);
		EditText     et_PrepQty = (EditText)findViewById(R.id.et_PrepQty);
		EditText     et_m4 = (EditText)findViewById(R.id.et_m4);
		EditText     et_m5 = (EditText)findViewById(R.id.et_m5);
		EditText     et_m6 = (EditText)findViewById(R.id.et_m6);
		Spinner printerType = (Spinner) findViewById(R.id.sp_Print_Type);
		Spinner MobileType = (Spinner) findViewById(R.id.sp_MobileType);

		Integer index = printerType.getSelectedItemPosition();
		Integer index1 = MobileType.getSelectedItemPosition();
		Cls_Cur p = (Cls_Cur) printerType.getItemAtPosition(index) ;
		Cls_Cur m = (Cls_Cur) MobileType.getItemAtPosition(index1) ;

		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor    = sharedPreferences.edit();
		editor.putString("ServerIP", IP.getText().toString().trim());
		editor.putString("AddressBT", AddressBT.getText().toString().trim());
		//editor.putString("m1", et_Sal_inv.getText().toString().trim());
		//editor.putString("m2", et_Payments.getText().toString().trim());
		//editor.putString("m3", et_PrepQty.getText().toString().trim());
		editor.putString("m4", et_m4.getText().toString().trim());
		//editor.putString("m5", et_m5.getText().toString().trim());
		editor.putString("m6", et_m6.getText().toString().trim());
		editor.putString("PrinterType", p.getNo().toString());
		editor.putString("MobileType", m.getNo().toString());

		editor.commit();

		AlertDialog alertDialog = new AlertDialog.Builder(
				Sitting_New_Activity.this).create();
		alertDialog.setTitle(getResources().getText(R.string.setting));

		alertDialog.setMessage(getResources().getText(R.string.AddCompleteSucc));
		alertDialog.setIcon(R.drawable.tick);

		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		alertDialog.show();
	}



	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent ;
		if ( ComInfo.UserType ==1) {
			  intent = new Intent(getApplicationContext(), GalaxyMainActivity.class);
		}else
		{
			intent = new Intent(getApplicationContext(), GalaxyMainActivity2.class);
		}

		 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}
}