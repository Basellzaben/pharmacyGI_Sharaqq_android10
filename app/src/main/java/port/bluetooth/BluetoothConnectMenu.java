package port.bluetooth;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cds_jo.pharmacyGI.ComInfo;
import com.cds_jo.pharmacyGI.DB;
import com.cds_jo.pharmacyGI.GalaxyLoginActivity;
import com.cds_jo.pharmacyGI.GalaxyMainActivity;
import com.cds_jo.pharmacyGI.GalaxyMainActivity2;
import com.cds_jo.pharmacyGI.R;
import com.cds_jo.pharmacyGI.SqlHandler;
import com.cds_jo.pharmacyGI.We_Result;
import com.cds_jo.pharmacyGI.assist.CallWebServices;
import com.cds_jo.pharmacyGI.assist.Cls_Cur;
import com.cds_jo.pharmacyGI.assist.Cls_Cur_Adapter;
import com.cds_jo.pharmacyGI.assist.OrdersItems;
import com.sewoo.port.android.BluetoothPort;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import hearder.main.Header_Frag;

/**
 * BluetoothConnectMenu
 * @author Sung-Keun Lee
 * @version 2011. 12. 21.
 */
public class BluetoothConnectMenu extends FragmentActivity
{
	private static final String TAG = "BluetoothConnectMenu";
	// Intent request codes
	// private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 2;

	ArrayAdapter<String> adapter;
	private BluetoothAdapter mBluetoothAdapter;
	private Vector<BluetoothDevice> remoteDevices;
	private BroadcastReceiver searchFinish;
	private BroadcastReceiver searchStart;
	private BroadcastReceiver discoveryResult;
	private Thread hThread;
	private Context context;
	// UI
	private EditText btAddrBox;
	private TextView connectButton;
	private Methdes.MyTextView searchButton;
	private ListView list;
	// BT
	String UserID;
	TextView tv;
	Drawable greenProgressbar;
	RelativeLayout.LayoutParams lp;
	private BluetoothPort bluetoothPort;
	 EditText et_VisitSerial;
	// Set up Bluetooth.

	SqlHandler sqlHandler;
	private void bluetoothSetup()
	{
		// Initialize
		clearBtDevData();
		bluetoothPort = BluetoothPort.getInstance();
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null)
		{
			// Device does not support Bluetooth
			return;
		}
		if (!mBluetoothAdapter.isEnabled())
		{
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
	}

	private static final String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "//temp";
	private static final String fileName = dir + "//BTPrinter";
	private String lastConnAddr;
	private void loadSettingFile()
	{
		int rin = 0;
		char [] buf = new char[128];
		try
		{
			FileReader fReader = new FileReader(fileName);
			rin = fReader.read(buf);
			if(rin > 0)
			{
				lastConnAddr = new String(buf,0,rin);
				btAddrBox.setText(lastConnAddr);
			}
			fReader.close();
		}
		catch (FileNotFoundException e)
		{
			Log.i(TAG, "Connection history not exists.");
		}
		catch (IOException e)
		{
			Log.e(TAG, e.getMessage(), e);
		}
	}

	private void saveSettingFile()
	{
		try
		{
			File tempDir = new File(dir);
			if(!tempDir.exists())
			{
				tempDir.mkdir();
			}
			FileWriter fWriter = new FileWriter(fileName);
			if(lastConnAddr != null)
				fWriter.write(lastConnAddr);
			fWriter.close();
		}
		catch (FileNotFoundException e)
		{
			Log.e(TAG, e.getMessage(), e);
		}
		catch (IOException e)
		{
			Log.e(TAG, e.getMessage(), e);
		}
	}

	// clear device data used list.
	private void clearBtDevData()
	{
		remoteDevices = new Vector<BluetoothDevice>();
	}
	// add paired device to list
	private void addPairedDevices()
	{
		BluetoothDevice pairedDevice;
		Iterator<BluetoothDevice> iter = (mBluetoothAdapter.getBondedDevices()).iterator();
		while(iter.hasNext())
		{
			pairedDevice = iter.next();
			if(bluetoothPort.isValidAddress(pairedDevice.getAddress()))
			{
				remoteDevices.add(pairedDevice);
				adapter.add(pairedDevice.getName() +"\n["+pairedDevice.getAddress()+"] [Paired]");
			}
		}
	}




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

		setContentView(R.layout.n_activity_bluetooth_menu);

		sqlHandler = new SqlHandler(this);
		Fragment frag = new Header_Frag();
		android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.Frag1, frag).commit();

		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

		UserID= sharedPreferences.getString("UserID", "");

	if (sharedPreferences.getString("UserID", "").equalsIgnoreCase("admin") && sharedPreferences.getString("password", "").equalsIgnoreCase("12589")){

	}else {
		return;
	}
		// Setting
		btAddrBox = (EditText) findViewById(R.id.EditTextAddressBT);
		connectButton = (TextView) findViewById(R.id.ButtonConnectBT);
		searchButton = (Methdes.MyTextView) findViewById(R.id.ButtonSearchBT);
		list = (ListView) findViewById(R.id.ListView01);
		context = this;

		// Setting
		Spinner printerType = (Spinner) findViewById(R.id.sp_Print_Type);
		Spinner MobileType = (Spinner) findViewById(R.id.sp_MobileType);
		//loadSettingFile();
		//bluetoothSetup();

		/*FillPrinterType();
		FillMobileType();
*/
		et_VisitSerial = (EditText) findViewById(R.id.et_VisitSerial);
		EditText et_Payments = (EditText) findViewById(R.id.et_Payments);
		EditText et_m4 = (EditText) findViewById(R.id.et_m4);
		EditText et_m6 = (EditText) findViewById(R.id.et_m6);
		EditText AddressBT = (EditText) findViewById(R.id.EditTextAddressBT);
		EditText IP = (EditText) findViewById(R.id.et_IP);

		AddressBT.setText(sharedPreferences.getString("AddressBT", "0"));
		IP.setText(sharedPreferences.getString("ServerIP", "0"));


		et_m4.setText(DB.GetValue(BluetoothConnectMenu.this,"OrdersSitting","SalesOrder","1=1"));
		et_VisitSerial.setText(DB.GetValue(BluetoothConnectMenu.this,"OrdersSitting","Visits","1=1"));



       // et_m4.setText(sharedPreferences.getString("m4", "0"));
	    et_m6.setText(sharedPreferences.getString("m6", "0"));
		et_Payments.setText(sharedPreferences.getString("m2", "0"));
	//	et_VisitSerial.setText(sharedPreferences.getString("VisitSerial", "0"));


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

/*
		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//loadSettingFile();
				//	bluetoothSetup();
				if (!mBluetoothAdapter.isDiscovering()) {
					clearBtDevData();
					adapter.clear();
					mBluetoothAdapter.startDiscovery();
				} else {
					mBluetoothAdapter.cancelDiscovery();
				}
			}
		});
		//gff
		// Bluetooth Device List
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		list.setAdapter(adapter);*/


	}

/*	@Override
	protected void onDestroy()
	{
		try
		{
			saveSettingFile();
			bluetoothPort.disconnect();
		}
		catch (IOException e)
		{
			Log.e(TAG, e.getMessage(), e);
		}
		catch (InterruptedException e)
		{
			Log.e(TAG, e.getMessage(), e);
		}
		if((hThread != null) && (hThread.isAlive()))
		{
			hThread.interrupt();
			hThread = null;
		}
		unregisterReceiver(searchFinish);
		unregisterReceiver(searchStart);
		unregisterReceiver(discoveryResult);
		super.onDestroy();
	}*/


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
private  void ShowRecord(){
	EditText et_m4 = (EditText) findViewById(R.id.et_m4);
	EditText et_VisitSerial = (EditText) findViewById(R.id.et_VisitSerial);

	et_m4.setText(DB.GetValue(BluetoothConnectMenu.this,"OrdersSitting","SalesOrder","1=1"));
	et_VisitSerial.setText(DB.GetValue(BluetoothConnectMenu.this,"OrdersSitting","Visits","1=1"));


}
	public void DeleteAll(View view) {
/*
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("إعدادات عامة");
		alertDialog.setMessage("هل انت متاكد من عملية الحذف");
		alertDialog.setIcon(R.drawable.delete);
		alertDialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {*/
				String q;
				SqlHandler sqlHandler = new SqlHandler(BluetoothConnectMenu.this);



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




				SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BluetoothConnectMenu.this);
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
	        	editor.putString("VisitSerial", "0");
				editor.commit();


				AlertDialog alertDialog = new AlertDialog.Builder(
						BluetoothConnectMenu.this).create();
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
	public void btn_UpdateSerial(View view) {

			tv = new TextView(getApplicationContext());
			lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			tv.setLayoutParams(lp);
			tv.setLayoutParams(lp);
			tv.setPadding(10, 15, 10, 15);
			tv.setGravity(Gravity.CENTER);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
			tv.setTextColor(Color.WHITE);
			tv.setBackgroundColor(Color.BLUE);
			tv.setTypeface(tv.getTypeface(), Typeface.BOLD);



			final Handler _handler = new Handler();

			final ProgressDialog custDialog = new ProgressDialog(BluetoothConnectMenu.this);
			custDialog.setProgressStyle(custDialog.STYLE_HORIZONTAL);
			custDialog.setCanceledOnTouchOutside(false);
			//custDialog.setProgress(0);
			//custDialog.setMax(100);
			custDialog.setMessage("  الرجاء الانتظار ..." + "  العمل جاري على نسخ البيانات  ");
			tv.setText("التسلسلات");
			custDialog.setCustomTitle(tv);
			custDialog.setProgressDrawable(greenProgressbar);
			custDialog.show();
			new Thread(new Runnable() {
				@Override
				public void run() {
					CallWebServices ws = new CallWebServices(BluetoothConnectMenu.this);
					ws.GetOrdersSerials(UserID);
					try {
						Integer i;
						String q = "";
						JSONObject js = new JSONObject(We_Result.Msg);
						JSONArray js_Sales = js.getJSONArray("Sales");
						JSONArray js_Payment = js.getJSONArray("Payment");
						JSONArray js_SalesOrder = js.getJSONArray("SalesOrder");
						JSONArray js_PrepareQty = js.getJSONArray("PrepareQty");
						JSONArray js_RetSales = js.getJSONArray("RetSales");
						JSONArray js_PostDely = js.getJSONArray("PostDely");
						JSONArray js_Visits = js.getJSONArray("Visits");
						JSONArray js_SampleItems = js.getJSONArray("SampleItems");

						q = "Delete from OrdersSitting";
						sqlHandler.executeQuery(q);

						q = " delete from sqlite_sequence where name='OrdersSitting'";
						sqlHandler.executeQuery(q);


						q = "INSERT INTO OrdersSitting(Sales, Payment , SalesOrder , PrepareQty , RetSales, PostDely , Visits,SampleItems  ) values ('"
								+ js_Sales.get(0).toString()
								+ "','" + js_Payment.get(0).toString()
								+ "','" + js_SalesOrder.get(0).toString()
								+ "','" + js_PrepareQty.get(0).toString()
								+ "','" + js_RetSales.get(0).toString()
								+ "','" + js_PostDely.get(0).toString()
								+ "','" + js_Visits.get(0).toString()
								+ "','" + js_SampleItems.get(0).toString()
								+ "')";
						sqlHandler.executeQuery(q);


						_handler.post(new Runnable() {

							public void run() {

								custDialog.dismiss();
								ShowRecord();

							}
						});

					} catch (final Exception e) {
						custDialog.dismiss();
						_handler.post(new Runnable() {

							public void run() {

								custDialog.dismiss();

							}
						});
					}
				}
			}).start();



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
	/*	Spinner printerType = (Spinner) findViewById(R.id.sp_Print_Type);
		Spinner MobileType = (Spinner) findViewById(R.id.sp_MobileType);*/
/*
		Integer index = printerType.getSelectedItemPosition();
		Integer index1 = MobileType.getSelectedItemPosition();
		Cls_Cur p = (Cls_Cur) printerType.getItemAtPosition(index) ;
		Cls_Cur m = (Cls_Cur) MobileType.getItemAtPosition(index1) ;*/

		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor    = sharedPreferences.edit();
		editor.putString("ServerIP", IP.getText().toString().trim());
		editor.putString("AddressBT", AddressBT.getText().toString().trim());
		//editor.putString("m1", et_Sal_inv.getText().toString().trim());
		//editor.putString("m2", et_Payments.getText().toString().trim());
		//editor.putString("m3", et_PrepQty.getText().toString().trim());
		editor.putString("m4", et_m4.getText().toString().trim());
		 editor.putString("m2", et_Payments.getText().toString().trim());
		editor.putString("m6", et_m6.getText().toString().trim());
		editor.putString("VisitSerial", et_VisitSerial.getText().toString().trim());






	    String	q = "Delete from OrdersSitting";
		sqlHandler.executeQuery(q);

		q = " delete from sqlite_sequence where name='OrdersSitting'";
		sqlHandler.executeQuery(q);


		q = "INSERT INTO OrdersSitting(  SalesOrder ,  Visits   ) values ('"
				 + "','" +  et_m4.getText().toString().trim()
				+ "','" + et_VisitSerial.getText().toString().trim()
				+ "')";
		sqlHandler.executeQuery(q);



		editor.commit();

		AlertDialog alertDialog = new AlertDialog.Builder(
				BluetoothConnectMenu.this).create();
		alertDialog.setTitle(getResources().getText(R.string.setting));

		alertDialog.setMessage(getResources().getText(R.string.AddCompleteSucc));
		alertDialog.setIcon(R.drawable.tick);

		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		alertDialog.show();
	}
	public void DeletePostedPo(View view) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("طلبات البيع الغير مرحلة");
		alertDialog.setMessage("هل انت متاكد من عملية الحذف");
		alertDialog.setIcon(R.drawable.delete);
		alertDialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Delete_Posted_PO();

			}
		});

		// Setting Negative "NO" Button
		alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to invoke NO event
				//Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
				dialog.cancel();
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}

	public void Delete_Posted_PO(){

	String	query ="Delete from  Po_dtl  where orderno in   ( select orderno from  Po_Hdr   where ifnull(posted,'-1') !=-'1'    )  " ;
		sqlHandler.executeQuery(query);
		query ="Delete from  Po_Hdr where ifnull(posted,'-1')!='-1' " ;
		sqlHandler.executeQuery(query);

		AlertDialog alertDialog = new AlertDialog.Builder(
				this).create();
		alertDialog.setTitle("طلبات البيع الغير مرحلة");
		alertDialog.setMessage("تمت عملية الحذف بنجاح");
		alertDialog.setIcon(R.drawable.tick);
		alertDialog.setButton(getResources().getText(R.string.Ok), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		alertDialog.show();


	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent ;

			  intent = new Intent(getApplicationContext(), GalaxyLoginActivity.class);


		 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}
}