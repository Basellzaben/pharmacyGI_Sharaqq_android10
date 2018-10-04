package com.cds_jo.pharmacyGI.assist;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.Toast;

import com.intermec.print.lp.LinePrinter;
import com.intermec.print.lp.LinePrinterException;
import com.intermec.print.lp.PrintProgressEvent;
import com.intermec.print.lp.PrintProgressListener;
import com.sewoo.jpos.command.ESCPOS;
import com.sewoo.jpos.printer.CPCLPrinter;
import com.sewoo.jpos.printer.ESCPOSPrinter;
import com.sewoo.port.android.BluetoothPort;
import com.sewoo.request.android.RequestHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


public class PrintReport_Intermec_PR3 {
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

	PrintReport_Intermec_PR3(Context _context, Activity _Activity,
							 View _ReportView, int _PageWidth, float _ImageCountFactor) {
		context = _context;
		Activity = _Activity;



		BPrinter_MAC_ID = "00:12:6F:36:7C:B8";
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
		BPrinter_MAC_ID =sharedPreferences.getString("AddressBT", "");
		BPrinter_MAC_ID = "00:12:6F:36:7C:B8";
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
				



				//posPtr.printBitmap("//sdcard//a1.jpg",
				//		LKPrint.LK_ALIGNMENT_LEFT);











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
	public class PrintTask extends AsyncTask<String, Integer, String> {
		private static final String PROGRESS_CANCEL_MSG = "Printing cancelled\n";
		private static final String PROGRESS_COMPLETE_MSG = "Printing completed\n";
		private static final String PROGRESS_ENDDOC_MSG = "End of document\n";
		private static final String PROGRESS_FINISHED_MSG = "Printer connection closed\n";
		private static final String PROGRESS_NONE_MSG = "Unknown progress message\n";
		private static final String PROGRESS_STARTDOC_MSG = "Start printing document\n";


		/**
		 * Runs on the UI thread before doInBackground(Params...).
		 */
		@Override
		protected void onPreExecute()
		{
			// Clears the Progress and Status text box.
			//textMsg.setText("");

			// Disables the Print button.
			//buttonPrint.setEnabled(false);
			// Disables the Sign button.
			//buttonSign.setEnabled(false);

			// Shows a progress icon on the title bar to indicate
			// it is working on something.
			//setProgressBarIndeterminateVisibility(true);
		}

		/**
		 * This method runs on a background thread. The specified parameters
		 * are the parameters passed to the execute method by the caller of
		 * this task. This method can call publishProgress to publish updates
		 * on the UI thread.
		 */
		@Override
		protected String doInBackground(String... args)
		{
			LinePrinter lp = null;
			String sResult = null;
			String sPrinterID = args[0];
			String sMacAddr = args[1];
			String sDocNumber = "1234567890";

			if (sMacAddr.contains(":") == false && sMacAddr.length() == 12)
			{
				// If the MAC address only contains hex digits without the
				// ":" delimiter, then add ":" to the MAC address string.
				char[] cAddr = new char[17];

				for (int i=0, j=0; i < 12; i += 2)
				{
					sMacAddr.getChars(i, i+2, cAddr, j);
					j += 2;
					if (j < 17)
					{
						cAddr[j++] = ':';
					}
				}

				sMacAddr = new String(cAddr);
			}

			String sPrinterURI = "bt://" + sMacAddr;
			String sUserText = "maen naamneh";

			LinePrinter.ExtraSettings exSettings = new LinePrinter.ExtraSettings();

			exSettings.setContext(PrintReport_Intermec_PR3.this);

			try
			{
				File profiles = new File (context.getExternalFilesDir(null), "printer_profiles.JSON");

				lp = new LinePrinter(
						profiles.getAbsolutePath(),
						sPrinterID,
						sPrinterURI,
						exSettings);

				// Registers to listen for the print progress events.
				lp.addPrintProgressListener(new PrintProgressListener() {
					public void receivedStatus(PrintProgressEvent aEvent)
					{
						// Publishes updates on the UI thread.
						publishProgress(aEvent.getMessageType());
					}
				});

				//A retry sequence in case the bluetooth socket is temporarily not ready
				int numtries = 0;
				int maxretry = 2;
				while(numtries < maxretry)
				{
					try{
						lp.connect();  // Connects to the printer
						break;
					}
					catch(LinePrinterException ex){
						numtries++;
						Thread.sleep(1000);
					}
				}
				if (numtries == maxretry) lp.connect();//Final retry

				// Prints the Honeywell logo graphic on the receipt if the graphic
				// file exists.
				//  File graphicFile = new File (getExternalFilesDir(null), "b.png");
				//lp.write("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");



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


						File graphicFile = new   File(Environment.getExternalStorageDirectory(), "a1.jpg");
						if (graphicFile.exists()) {

							lp.setDoubleWide(true);
							lp.setDoubleHigh(true);

							lp.writeGraphic(graphicFile.getAbsolutePath(),
									LinePrinter.GraphicRotationDegrees.DEGREE_0,
									0,  // Offset in printhead dots from the left of the page
									576, // Desired graphic width on paper in printhead dots
									600); // Desired graphic height on paper in printhead dots

							lp.write("2");
						}


						j += 1;

						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}


				}




				// lp.newLine(1);

				// Set font style to Bold + Double Wide + Double High.
               /* lp.setBold(true);
                lp.setDoubleWide(true);
                lp.setDoubleHigh(true);
                lp.write("طلب شراء");
                lp.setDoubleWide(false);
                lp.setDoubleHigh(false);
                lp.newLine(2);

                // The following text shall be printed in Bold font style.
                lp.write("CUSTOMER: Casual Step");
                lp.setBold(false);  // Returns to normal font.
                lp.newLine(2);

                // Set font style to Compressed + Double High.
                lp.setDoubleHigh(true);
                lp.setCompress(true);
                lp.write("DOCUMENT#: " + sDocNumber);
                lp.setCompress(false);
                lp.setDoubleHigh(false);
                lp.newLine(2);

                // The following text shall be printed in Normal font style.
                lp.write(" PRD. DESCRIPT.   PRC.  QTY.    NET.");
                lp.newLine(2);

                lp.write(" 1501 Timer-Md1  13.15     1   13.15");
                lp.newLine(1);
                lp.write(" 1502 Timer-Md2  13.15     3   39.45");
                lp.newLine(1);
                lp.write(" 1503 Timer-Md3  13.15     2   26.30");
                lp.newLine(1);
                lp.write(" 1504 Timer-Md4  13.15     4   52.60");
                lp.newLine(1);
                lp.write(" 1505 Timer-Md5  13.15     5   65.75");
                lp.newLine(1);
                lp.write("                        ----  ------");
                lp.newLine(1);
                lp.write("              SUBTOTAL    15  197.25");
                lp.newLine(2);

                lp.write("          5% State Tax          9.86");
                lp.newLine(2);

                lp.write("                              ------");
                lp.newLine(1);
                lp.write("           BALANCE DUE        207.11");
                lp.newLine(1);
                lp.newLine(1);

                lp.write(" PAYMENT TYPE: CASH");
                lp.newLine(2);

                lp.setDoubleHigh(true);
                lp.write("       SIGNATURE / STORE STAMP");
                lp.setDoubleHigh(false);
                lp.newLine(2);

                // Prints the captured signature if it exists.
                if (base64SignaturePng != null)
                {
                    android.util.Log.d("LinePrinterSample", "Base64 graphic:" + base64SignaturePng);
                    lp.writeGraphicBase64(base64SignaturePng,
                            LinePrinter.GraphicRotationDegrees.DEGREE_0,
                            72,   // Offset in printhead dots from the left of the page
                            220,  // Desired graphic width on paper in printhead dots
                            100); // Desired graphic height on paper in printhead dots
                }
                lp.newLine(1);

                lp.setBold(true);
                if (sUserText.length() > 0)
                {
                    // Print the text entered by user in the Optional Text field.
                    lp.write(sUserText);
                    lp.newLine(2);
                }


                lp.write("          ORIGINAL");
                lp.setBold(false);
                lp.newLine(2);

                // Print a Code 39 barcode containing the document number.
                lp.writeBarcode(LinePrinter.BarcodeSymbologies.SYMBOLOGY_CODE39,
                        sDocNumber,   // Document# to encode in barcode
                        90,           // Desired height of the barcode in printhead dots
                        40);          // Offset in printhead dots from the left of the page

                lp.newLine(4);

                sResult = "Number of bytes sent to printer: " + lp.getBytesWritten();*/
			}
			catch (LinePrinterException ex)
			{
				sResult = "LinePrinterException: " + ex.getMessage();
			}
			catch (Exception ex)
			{
				if (ex.getMessage() != null)
					sResult = "Unexpected exception: " + ex.getMessage();
				else
					sResult = "Unexpected exception.";
			}
			finally
			{
				if (lp != null)
				{
					try
					{
						lp.disconnect();  // Disconnects from the printer
						lp.close();  // Releases resources
					}
					catch (Exception ex) {}
				}
			}

			// The result string will be passed to the onPostExecute method
			// for display in the the Progress and Status text box.
			return sResult;
		}

		/**
		 * Runs on the UI thread after publishProgress is invoked. The
		 * specified values are the values passed to publishProgress.
		 */
		@Override
		protected void onProgressUpdate(Integer... values)
		{
			// Access the values array.
			int progress = values[0];

			switch (progress)
			{
				case PrintProgressEvent.MessageTypes.CANCEL:
					//textMsg.append(PROGRESS_CANCEL_MSG);
					break;
				case PrintProgressEvent.MessageTypes.COMPLETE:
					//textMsg.append(PROGRESS_COMPLETE_MSG);
					break;
				case PrintProgressEvent.MessageTypes.ENDDOC:
					//textMsg.append(PROGRESS_ENDDOC_MSG);
					break;
				case PrintProgressEvent.MessageTypes.FINISHED:
					//textMsg.append(PROGRESS_FINISHED_MSG);
					break;
				case PrintProgressEvent.MessageTypes.STARTDOC:
				//	textMsg.append(PROGRESS_STARTDOC_MSG);
					break;
				default:
				//	textMsg.append(PROGRESS_NONE_MSG);
					break;
			}
		}

		/**
		 * Runs on the UI thread after doInBackground method. The specified
		 * result parameter is the value returned by doInBackground.
		 */
		@Override
		protected void onPostExecute(String result)
		{
			// Displays the result (number of bytes sent to the printer or
			// exception message) in the Progress and Status text box.
			if (result != null)
			{
				//textMsg.append(result);
			}

			// Dismisses the progress icon on the title bar.
			//setProgressBarIndeterminateVisibility(false);

			// Enables the Print button.
			//buttonPrint.setEnabled(true);
			// Enables the Sign button.
			//buttonSign.setEnabled(true);
		}
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
		copyAssetFiles();
		PrintTask task = new PrintTask();
		task.execute("PR3", "00:1D:DF:59:1C:1C");


	/*
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

		}*/


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
	private void copyAssetFiles(){
		InputStream input = null;
		OutputStream output = null;
		// Copy the asset files we delivered with the application to a location
		// where the LinePrinter can access them.
		try
		{
			AssetManager assetManager =context.getAssets();
			String[] files = { "printer_profiles.JSON", "m.png" };

			for (String filename : files)
			{
				input = assetManager.open(filename);
				File outputFile = new File(context.getExternalFilesDir(null), filename);

				output = new FileOutputStream(outputFile);

				byte[] buf = new byte[1024];
				int len;
				while ((len = input.read(buf)) > 0)
				{
					output.write(buf, 0, len);
				}
				input.close();
				input = null;

				output.flush();
				output.close();
				output = null;
			}
		}
		catch (Exception ex)
		{
			//textMsg.append("Error copying asset files.");
		}
		finally
		{
			try
			{
				if (input != null)
				{
					input.close();
					input = null;
				}

				if (output != null)
				{
					output.close();
					output = null;
				}
			}
			catch (IOException e){}
		}
	}
}
