package com.cds_jo.pharmacyGI.assist;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

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

public class PrintReport_SEWOO_ESCPOS {
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

	PrintReport_SEWOO_ESCPOS(Context _context, Activity _Activity,
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
			// InvImgList_2.add(Bitmap.createBitmap(bitmap, bitmap.getWidth() /
			// 2,
			// y, bitmap.getWidth() / 2, ((int) ImageH)));

			y = ((int) ImageH) * (i + 1);
		}
		CurrCopy = 0;

		// printPhotoFromExternal();
		// String filename1 = "Inv.jpg";
		// File sd1 = Environment.getExternalStorageDirectory();
		// File dest1 = new File(sd1, filename1);
		//
		// try {
		// FileOutputStream out1 = new FileOutputStream(dest1);
		// bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out1);
		// out1.flush();
		// out1.close();
		// bitmap.recycle();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// filename = "a2.jpg";
		// sd = Environment.getExternalStorageDirectory();
		// dest = new File(sd, filename);
		//
		// try {
		// FileOutputStream out = new FileOutputStream(dest);
		// InvImgList_2.get(0).compress(Bitmap.CompressFormat.JPEG, 50, out);
		// out.flush();
		// out.close();
		// bitmap.recycle();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

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
				// filename = "a2.jpg";
				// sd = Environment.getExternalStorageDirectory();
				// dest = new File(sd, filename);
				//
				// try {
				// FileOutputStream out = new FileOutputStream(dest);
				// InvImgList_2.get(j).compress(Bitmap.CompressFormat.JPEG,
				// 70, out);
				// out.flush();
				// out.close();
				// bitmap.recycle();
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				posPtr = new ESCPOSPrinter();
//				posPtr.printerCheck();
//				if (posPtr.status() != 0) {
//					return;
//				}

				posPtr.printBitmap("//sdcard//a1.jpg",
						LKPrint.LK_ALIGNMENT_LEFT);





//				posPtr.printBitmap("//sdcard//v2ir.jpg",
//						LKPrint.LK_ALIGNMENT_LEFT);


				// paperType = CPCLConst.LK_CPCL_CONTINUOUS;
				//
				// cpclPrinter.setForm(0, 0, 0, Img.getHeight(), 1);
				// cpclPrinter.setMedia(paperType);
				// // cpclPrinter.setPageWidth(810);
				// cpclPrinter.setPageWidth(PageWidth);
				//
				// cpclPrinter.printBitmap("//sdcard//a1.jpg", 1, 1);
				// cpclPrinter.printBitmap("//sdcard//a2.jpg",
				// (PageWidth / 2) - 5, 1);
				// cpclPrinter.printForm();
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
		// // DisconnPrinter();
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


// String filename1 = "Inv.jpg";
		// File sd1 = Environment.getExternalStorageDirectory();
		// File dest1 = new File(sd1, filename1);
		//
		// try {
		// FileOutputStream out1 = new FileOutputStream(dest1);
		// b.compress(Bitmap.CompressFormat.JPEG, 100, out1);
		// out1.flush();
		// out1.close();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

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
