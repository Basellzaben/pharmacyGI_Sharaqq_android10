package com.cds_jo.pharmacyGI.assist;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.Toast;

import com.sewoo.jpos.command.ESCPOS;
import com.sewoo.jpos.printer.CPCLPrinter;
import com.sewoo.jpos.printer.ESCPOSPrinter;
import com.sewoo.jpos.printer.LKPrint;
import com.sewoo.port.android.BluetoothPort;
import com.sewoo.request.android.RequestHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PrintReport_Zepra_520 {
	public int CopyCount = 1;
	int CurrCopy = 0;
	Context context;
	ArrayList<Bitmap> InvImgList;
	ArrayList<Bitmap> InvImgList_2;

	String BPrinter_MAC_ID;
	Activity Activity;
	private Thread hThread;

	protected CPCLPrinter cpclPrinter;
	private int paperType;
	View ReportView;
	int PageWidth1;
	private ESCPOSPrinter posPtr;
	float ImageCountFactor;
	// 0x1B
	private final char ESC = ESCPOS.ESC;
	private final char LF = ESCPOS.LF;
	public boolean IsBusy = false;

	PrintReport_Zepra_520(Context _context, Activity _Activity,
						  View _ReportView, int _PageWidth, float _ImageCountFactor) {
		context = _context;
		Activity = _Activity;

		BPrinter_MAC_ID = "00:12:6F:36:7C:B8";
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
		BPrinter_MAC_ID =sharedPreferences.getString("AddressBT", "");
		ReportView = _ReportView;
		//PageWidth = _PageWidth;
		ImageCountFactor = _ImageCountFactor;
	}

	public void PrintReport(View ReportView) throws IOException {

		InvImgList = new ArrayList<Bitmap>();
		InvImgList_2 = new ArrayList<Bitmap>();

		Bitmap bitmap = loadBitmapFromView(ReportView);
		double tmp = (double) (((double) bitmap.getHeight()) / 150);

		double ImagesCount = Math.ceil(tmp);
		ImagesCount = Math.ceil(ImagesCount * ImageCountFactor);

		double ImageH = Math.ceil(bitmap.getHeight() / ImagesCount);

		int y = 0;
		for (int i = 0; i < ImagesCount; i++) {

			if (i == ImagesCount - 1) {
				ImageH = (bitmap.getHeight() - y) - 1;
			}
			InvImgList.add(Bitmap.createBitmap(bitmap, 0, y, bitmap.getWidth(),
					((int) ImageH)));


			y = ((int) ImageH) * (i + 1);
		}
		CurrCopy = 0;


		for (int i = 0; i < CopyCount; i++) {
			int j = 0;
			for (Bitmap Img : InvImgList) {

				String filename = "a1.jpg";
				File sd = Environment.getExternalStorageDirectory();
				File dest = new File(sd, filename);

				try {
					FileOutputStream out = new FileOutputStream(dest);
					Img.compress(Bitmap.CompressFormat.JPEG, 70, out);
					out.flush();
					out.close();
					 bitmap.recycle();
				} catch (Exception e) {
					e.printStackTrace();
				}

				posPtr = new ESCPOSPrinter();

				posPtr.printBitmap("//sdcard//a1.jpg",
						LKPrint.LK_ALIGNMENT_LEFT);




				j += 1;

				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		IsBusy = false;

	}

	public static Bitmap loadBitmapFromView(View v) {

		v.measure(MeasureSpec.makeMeasureSpec(v.getLayoutParams().width,
				MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
				v.getLayoutParams().height, MeasureSpec.UNSPECIFIED));
		v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
		Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
				Bitmap.Config.RGB_565);
		Canvas c = new Canvas(b);
		v.draw(c);



		return b;
	}

	private BluetoothPort bluetoothPort;
	private BluetoothAdapter mBluetoothAdapter;

	public void ConnectToPrinter() {
		// Initialize
		// clearBtDevData();
		bluetoothPort = BluetoothPort.getInstance();
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			// Device does not support Bluetooth
			IsBusy = false;
			Log.d("PPPPPPPP", "NULL");
			return;

		}

		if (!mBluetoothAdapter.isEnabled()) {
			// Intent enableBtIntent = new
			// Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			//
			// startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			IsBusy = false;
			Log.d("PPPPPPPP", "NOTisEnabled");
		}

		try {
			Log.d("PPPPPPPP", "connTask");
			new connTask().execute(mBluetoothAdapter
					.getRemoteDevice(BPrinter_MAC_ID));
			Log.d("PPPPPPPP", BPrinter_MAC_ID);
		} catch (IllegalArgumentException e) {
			// Bluetooth Address Format [OO:OO:OO:OO:OO:OO]
			IsBusy = false;
			Log.e("Sewooo", e.getMessage(), e);
			// AlertView.showAlert(e.getMessage(), context);
			return;

		}

	}

	// Bluetooth Connection Task.
	class connTask extends AsyncTask<BluetoothDevice, Void, Integer> {
		private final ProgressDialog dialog = new ProgressDialog(Activity);

		@Override
		protected void onPreExecute() {
			dialog.setTitle("Connecting ...");
			dialog.setMessage("Connecting ...");
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(BluetoothDevice... params) {
			Integer retVal = null;
			try {
				// bluetoothPort.connect(params[0]);

				bluetoothPort.connectSecure(params[0]);

				// lastConnAddr = params[0].getAddress();
				retVal = Integer.valueOf(0);
			} catch (IOException e) {
				IsBusy = false;
				Log.e("SEwoo", e.getMessage());
				retVal = Integer.valueOf(-1);
			}
			return retVal;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (result.intValue() == 0) // Connection success.
			{
				RequestHandler rh = new RequestHandler();
				hThread = new Thread(rh);
				hThread.start();
				// UI

				if (dialog.isShowing())
					dialog.dismiss();
				Toast toast = Toast.makeText(context, "Success",
						Toast.LENGTH_SHORT);
				toast.show();
				Log.d("PPPPPPPP", "Success");
				try {
					PrintReport(ReportView);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					IsBusy = false;
					e.printStackTrace();
				}
			} else // Connection failed.
			{
				if (dialog.isShowing())
					dialog.dismiss();
				Toast toast = Toast.makeText(context,
						"Printer Not Found", Toast.LENGTH_SHORT);
				toast.show();
				Log.d("PPPPPPPP", "Faild");
			}
			super.onPostExecute(result);
		}
	}

	public void DisconnPrinter() {
		try {
			IsBusy = false;
			bluetoothPort.disconnect();
		} catch (Exception e) {
			Log.e("Sewoo", e.getMessage(), e);
		}
		if ((hThread != null) && (hThread.isAlive()))
			hThread.interrupt();

	}

}
