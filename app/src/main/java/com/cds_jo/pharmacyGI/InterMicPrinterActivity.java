package com.cds_jo.pharmacyGI;

        import com.intermec.print.lp.LinePrinter;
        import com.intermec.print.lp.LinePrinterException;
        import com.intermec.print.lp.PrintProgressEvent;
        import com.intermec.print.lp.PrintProgressListener;

        import android.graphics.Color;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.content.Intent;
        import android.content.res.AssetManager;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.os.Environment;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Base64;
        import android.view.Menu;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.RadioButton;
        import android.widget.TextView;
        import android.widget.ImageView;

        import com.zebra.sdk.comm.BluetoothConnection;
        import com.zebra.sdk.comm.Connection;
        import com.zebra.sdk.comm.ConnectionException;
        import com.zebra.sdk.printer.ZebraPrinter;


        import java.io.*;


/**
 * This sample demonstrates printing on Android using the Intermec LinePrinter
 * class. You may enter or scan an Intermec mobile printer's MAC address and
 * click the Print button to print. The MAC Address text should have the format
 * of "nn:nn:nn:nn:nn:nn" or "nnnnnnnnnnnn" where each n is a hex digit.
 * <p>
 * You may also capture a signature to print by clicking the Sign button. It
 * will display another screen for you to sign and save the signature. After
 * you save the signature, you will see a preview of the signature graphic
 * next to the Sign button.
 * <p>
 * The printing progress will be displayed in the Progress and Status text box.
 */
public class InterMicPrinterActivity extends AppCompatActivity {
    private Button buttonPrint;
    private Button buttonSign;
    private TextView textMsg;
    private EditText editPrinterID;
    private EditText editMacAddr;
    private EditText editUserText;
    private ImageView imgSignature;
    private String base64SignaturePng = null;
    private Connection printerConnection;
    private RadioButton btRadioButton;
    private ZebraPrinter printer;
    private TextView statusField;
    private EditText macAddress, ipDNSAddress, portNumber;
    private Button testButton;
    public static final int CAPTURE_SIGNATURE_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_inter_mic_printer);

        textMsg = (TextView) findViewById(R.id.textMsg);

        editPrinterID = (EditText) findViewById(R.id.editPrinterID);
        // Set a default Printer ID
        editPrinterID.setText("PR3");

        editMacAddr = (EditText) findViewById(R.id.editMacAddr);
        // Set a default Mac Address
        editMacAddr.setText("00:1D:DF:59:1C:1C");

        editUserText = (EditText) findViewById(R.id.editUserText);
     //   imgSignature = (ImageView) findViewById(R.id.imgSignature);

        copyAssetFiles();

        buttonPrint = (Button) findViewById(R.id.buttonPrint);
        buttonPrint.setOnClickListener(new OnClickListener() {
            public void onClick(View view)
            {
                // Create a PrintTask to do printing on a separate thread.
                PrintTask task = new PrintTask();

                // Executes PrintTask with the specified parameter which is passed
                // to the PrintTask.doInBackground method.
                task.execute(editPrinterID.getText().toString(), editMacAddr.getText().toString());
            }
        });

        buttonSign = (Button) findViewById(R.id.buttonSign);
        buttonSign.setOnClickListener(new OnClickListener() {
            public void onClick(View view)
            {
                Intent intent = new Intent(InterMicPrinterActivity.this, CaptureSignatureActivity.class);
                startActivityForResult(intent, CAPTURE_SIGNATURE_ACTIVITY);
            }
        });
    }
    private void setStatus(final String statusMessage, final int color) {
        runOnUiThread(new Runnable() {
            public void run() {
                statusField.setBackgroundColor(color);
                statusField.setText(statusMessage);
            }
        });
        //DemoSleeper.sleep(1000);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.print, menu);
        return true;
    }

    private void copyAssetFiles(){
        InputStream input = null;
        OutputStream output = null;
        // Copy the asset files we delivered with the application to a location
        // where the LinePrinter can access them.
        try
        {
            AssetManager assetManager =  getAssets();
            String[] files = { "printer_profiles.JSON", "m.png" };

            for (String filename : files)
            {
                input = assetManager.open(filename);
                File outputFile = new File(getExternalFilesDir(null), filename);

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
            textMsg.append("Error copying asset files.");
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

    public void NewPrint(View view) {

        printerConnection = new BluetoothConnection("AC:3F:A4:1B:33:0B");

        SettingsHelper.saveBluetoothAddress(this,"AC:3F:A4:1B:33:0B");




        try {
            printerConnection.open();
            setStatus("Connected", Color.GREEN);
        } catch (ConnectionException e) {
            setStatus("Comm Error! Disconnecting", Color.RED);
            //DemoSleeper.sleep(1000);
           // disconnect();
        }

        PrintTask task = new PrintTask();

        // Executes PrintTask with the specified parameter which is passed
        // to the PrintTask.doInBackground method.
        task.execute(editPrinterID.getText().toString(), editMacAddr.getText().toString());

        LinePrinter lp = null;
        String sResult = null;
        String sPrinterID = "PR3";
        String sMacAddr = "00:1D:DF:59:1C:1C";
        String sDocNumber = "1234567890";


        String sPrinterURI = "bt://" + sMacAddr;
        String sUserText = "maen naamneh";

        LinePrinter.ExtraSettings exSettings = new LinePrinter.ExtraSettings();

        exSettings.setContext(InterMicPrinterActivity.this);


        try {
            File profiles = new File(getExternalFilesDir(null), "printer_profiles.JSON");

            new Thread(new Runnable() {
                @Override
                public void run() {

                }
                }).start();

            lp = new LinePrinter(
                    profiles.getAbsolutePath(),
                    sPrinterID,
                    sPrinterURI,
                    exSettings);

            // Registers to listen for the print progress events.
            lp.addPrintProgressListener(new PrintProgressListener() {
                public void receivedStatus(PrintProgressEvent aEvent) {
                    // Publishes updates on the UI thread.
                    // publishProgress(aEvent.getMessageType());
                }
            });

            //A retry sequence in case the bluetooth socket is temporarily not ready
            int numtries = 0;
            int maxretry = 2;
            while (numtries < maxretry) {
                try {
                    lp.connect();  // Connects to the printer
                    break;
                } catch (LinePrinterException ex) {
                    numtries++;
                    Thread.sleep(1000);
                }
            }
            if (numtries == maxretry) lp.connect();//Final retry


            File graphicFile = new File(Environment.getExternalStorageDirectory(), "v2i.jpg");
            if (graphicFile.exists()) {

                lp.writeGraphic(graphicFile.getAbsolutePath(),
                        LinePrinter.GraphicRotationDegrees.DEGREE_0,
                        0,  // Offset in printhead dots from the left of the page
                        576, // Desired graphic width on paper in printhead dots
                        600); // Desired graphic height on paper in printhead dots
                lp.write("2");



            }

        } catch (LinePrinterException ex) {
            sResult = "LinePrinterException: " + ex.getMessage();
        } catch (Exception ex) {
            if (ex.getMessage() != null)
                sResult = "Unexpected exception: " + ex.getMessage();
            else
                sResult = "Unexpected exception.";
        } finally {
            if (lp != null) {
                try {
                    lp.disconnect();  // Disconnects from the printer
                    lp.close();  // Releases resources
                } catch (Exception ex) {
                }
            }
        }
    }
    /**
     * This class demonstrates printing in a background thread and updates
     * the UI in the UI thread.
     */
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
            textMsg.setText("");

            // Disables the Print button.
            buttonPrint.setEnabled(false);
            // Disables the Sign button.
            buttonSign.setEnabled(false);

            // Shows a progress icon on the title bar to indicate
            // it is working on something.
            setProgressBarIndeterminateVisibility(true);
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

            exSettings.setContext(InterMicPrinterActivity.this);

            try
            {
                File profiles = new File (getExternalFilesDir(null), "printer_profiles.JSON");

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
                File graphicFile = new   File(Environment.getExternalStorageDirectory(), "v2i.jpg");
                if (graphicFile.exists())
                {
                    /* Bitmap myBitmap =null;
                    File imgFile = new  File("/storage/sdcard/DCIM/SharedFolder/Desert.jpg");

                      myBitmap =BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    String encodedImage1;
                    byte[] byteArray;
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteArray = stream.toByteArray();

                    encodedImage1 = Base64.encodeToString(byteArray, Base64.DEFAULT);


                    lp.writeGraphicBase64(encodedImage1,0,

                            72,   // Offset in printhead dots from the left of the page
                            220,  // Desired graphic width on paper in printhead dots
                            100); // Desired graphic height on paper in printhead dots



                  */
                    lp.setDoubleWide(true);
                    lp.setDoubleHigh(true);
                    lp.write("2121212212121212121221");
                    lp.writeGraphic(graphicFile.getAbsolutePath(),
                            LinePrinter.GraphicRotationDegrees.DEGREE_0,
                            0,  // Offset in printhead dots from the left of the page
                            576, // Desired graphic width on paper in printhead dots
                           600); // Desired graphic height on paper in printhead dots

                    lp.write("2");
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
                    textMsg.append(PROGRESS_CANCEL_MSG);
                    break;
                case PrintProgressEvent.MessageTypes.COMPLETE:
                    textMsg.append(PROGRESS_COMPLETE_MSG);
                    break;
                case PrintProgressEvent.MessageTypes.ENDDOC:
                    textMsg.append(PROGRESS_ENDDOC_MSG);
                    break;
                case PrintProgressEvent.MessageTypes.FINISHED:
                    textMsg.append(PROGRESS_FINISHED_MSG);
                    break;
                case PrintProgressEvent.MessageTypes.STARTDOC:
                    textMsg.append(PROGRESS_STARTDOC_MSG);
                    break;
                default:
                    textMsg.append(PROGRESS_NONE_MSG);
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
                textMsg.append(result);
            }

            // Dismisses the progress icon on the title bar.
            setProgressBarIndeterminateVisibility(false);

            // Enables the Print button.
            buttonPrint.setEnabled(true);
            // Enables the Sign button.
            buttonSign.setEnabled(true);
        }
    } //endofclass PrintTask

    /**
     * Called when an activity launched by this activity exits.
     * @param requestCode The integer request code originally supplied to
     * startActivityForResult(), allowing you to identify who this result came from.
     * @param resultCode The integer result code returned by the child activity
     * through its setResult().
     * @param data An Intent, which can return result data to the caller
     * (various data can be attached to Intent "extras").
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Bundle extras;

        switch (requestCode)
        {
            case CAPTURE_SIGNATURE_ACTIVITY:
                if (RESULT_OK == resultCode)
                {
                    // Gets the Base64 encoded PNG signature graphic.
                    extras = data.getExtras();
                    base64SignaturePng = extras.getString(CaptureSignatureActivity.BASE64_SIGNATURE_KEY);
                    displaySignature(base64SignaturePng);
                }
                break;
        }
    }

    /**
     * Displays the specified graphic in the imange view next to the Sign button.
     * @param base64Png A Base64 encoded PNG image.
     */
    private void displaySignature(String base64Png)
    {
        if (base64Png != null)
        {
            byte[] signPngBytes = Base64.decode(base64Png, Base64.DEFAULT);
            Bitmap signBitmap = BitmapFactory.decodeByteArray(signPngBytes, 0, signPngBytes.length);
            if (signBitmap != null)
            {
                imgSignature.setImageBitmap(signBitmap);
            }
            else
            {
                // Clears the image view.
                imgSignature.setImageDrawable(null);
            }
        }
        else
        {
            // Clears the image view.
            imgSignature.setImageDrawable(null);
        }
    }
}
