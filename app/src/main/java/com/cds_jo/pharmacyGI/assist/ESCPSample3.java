package com.cds_jo.pharmacyGI.assist;

import com.sewoo.jpos.command.ESCPOS;
import com.sewoo.jpos.printer.ESCPOSPrinter;
import com.sewoo.jpos.printer.LKPrint;
import com.sewoo.jpos.request.RequestQueue;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ESCPSample3
{
	protected RequestQueue requestQueue;
	private ESCPOSPrinter posPtr;


	// 0x1B
	private final char ESC = ESCPOS.ESC;
	private final char LF = ESCPOS.LF;
	
	public ESCPSample3()
	{
		posPtr = new ESCPOSPrinter();
	}
	
    
    public int sample1() throws UnsupportedEncodingException
    {
    	posPtr.printerCheck();
    	if(posPtr.status() != 0)
    		return posPtr.status();
    	posPtr.printNormal(ESC + "|cA" + ESC + "|bC" + ESC + "|2C" + "Receipt" + LF + LF);
        posPtr.printNormal(ESC + "|rATEL (123)-456-7890\n\n\n");
	    posPtr.printNormal(ESC + "|cAThank you for coming to our shop!\n");
	    posPtr.printNormal(ESC + "|cADate\n\n");
		posPtr.printNormal("      Galaxy Printer                      \n");
	    posPtr.printNormal("Chicken                             $10.00\n");
	    posPtr.printNormal("Hamburger                           $20.00\n");
	    posPtr.printNormal("Pizza                               $30.00\n");
	    posPtr.printNormal("Lemons                              $40.00\n");
	    posPtr.printNormal("Drink                               $50.00\n");
	    posPtr.printNormal("Excluded tax                       $150.00\n");
	    posPtr.printNormal(ESC + "|uCTax(5%)                              $7.50\n");
	    posPtr.printNormal(ESC + "|bC" + ESC + "|2CTotal         $157.50\n\n");
	    posPtr.printNormal("Payment                            $200.00\n");
	    posPtr.printNormal("Change                              $42.50\n\n");
	    posPtr.printBarCode("{Babc456789012", LKPrint.LK_BCS_Code128, 40, 512, LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_HRI_TEXT_BELOW); // Print Barcode
	    return 0;
    }
    
    public int sample2(String scanContent) throws UnsupportedEncodingException
    {
    	posPtr.printerCheck();

    	if(posPtr.status() != 0)
    		return posPtr.status();
		posPtr.printText("Galaxy International Group\r\n\r\n\r\n", LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
		/*posPtr.printText("Receipt\r\n\r\n\r\n", LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_2WIDTH);
	    posPtr.printText("TEL (123)-456-7890\r\n", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
	    posPtr.printText("Thank you for coming to our shop!\r\n", LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
	    posPtr.printText("Chicken                             $10.00\r\n", LKPrint.LK_ALIGNMENT_LEFT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
	    posPtr.printText("Hamburger                           $20.00\r\n", LKPrint.LK_ALIGNMENT_LEFT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
	    posPtr.printText("Pizza                               $30.00\r\n", LKPrint.LK_ALIGNMENT_LEFT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
	    posPtr.printText("Lemons                              $40.00\r\n", LKPrint.LK_ALIGNMENT_LEFT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
	    posPtr.printText("Drink                               $50.00\r\n\r\n", LKPrint.LK_ALIGNMENT_LEFT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
	    posPtr.printText("Excluded tax                       $150.00\r\n", LKPrint.LK_ALIGNMENT_LEFT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
	    posPtr.printText("Tax(5%)                              $7.50\r\n", LKPrint.LK_ALIGNMENT_LEFT, LKPrint.LK_FNT_UNDERLINE, LKPrint.LK_TXT_1WIDTH);
	    posPtr.printText("Total         $157.50\r\n\r\n", LKPrint.LK_ALIGNMENT_LEFT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_2WIDTH);
	    posPtr.printText("Payment                            $200.00\r\n", LKPrint.LK_ALIGNMENT_LEFT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
		posPtr.printText("Maen Naamneh                      $200.00\r\n", LKPrint.LK_ALIGNMENT_LEFT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
		posPtr.printText("Change                              $42.50\r\n\r\n", LKPrint.LK_ALIGNMENT_LEFT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
       // Reverse print.
	    posPtr.printText("Change                              $42.50\r\n\r\n", LKPrint.LK_ALIGNMENT_LEFT, LKPrint.LK_FNT_DEFAULT | LKPrint.LK_FNT_REVERSE, LKPrint.LK_TXT_1WIDTH);
			*/

		posPtr.printBarCode(scanContent, LKPrint.LK_BCS_EAN13, 40, 512, LKPrint.LK_ALIGNMENT_CENTER, LKPrint.LK_HRI_TEXT_BELOW);
		posPtr.printText("\r\n", LKPrint.LK_ALIGNMENT_LEFT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
		posPtr.printText("\r\n", LKPrint.LK_ALIGNMENT_LEFT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);

		return 0;
    }
    
    public int imageTest() throws IOException
    {
    	posPtr.printerCheck();
    	if(posPtr.status() != 0)
    		return posPtr.status();
    	posPtr.printBitmap("//sdcard//v2i.jpg", LKPrint.LK_ALIGNMENT_CENTER);
//    	posPtr.printBitmap("//sdcard//temp//test//danmark_windmill.jpg", LKPrint.LK_ALIGNMENT_LEFT);
//    	posPtr.printBitmap("//sdcard//temp//test//denmark_flag.jpg", LKPrint.LK_ALIGNMENT_RIGHT);
    	return 0;
    }
    
    public int westernLatinCharTest() throws UnsupportedEncodingException
    {

//		posPtr.setCharSet("Big5");
		posPtr.setCharSet("Windows-1256");
		posPtr.setAbsoluteVertical(190);
		posPtr.setAbsoluteHorizontal(20);


		final char [] diff = {0x23,0x24,0x40,0x5B,0x5C,0x5D,0x5E,0x6C,0x7B,0x7C,0x7D,0x7E,
				0xA4,0xA6,0xA8,0xB4,0xB8,0xBC,0xBD,0xBE };

//    	String ad = new String(diff);
    	posPtr.printerCheck();
		if (posPtr.status() != 0)
			return posPtr.status();
		posPtr.printText(diff[0] + "\n\r\n\r", LKPrint.LK_ALIGNMENT_RIGHT, LKPrint.LK_FNT_DEFAULT, LKPrint.LK_TXT_1WIDTH);
		System.out.println();
		String str = new String("UFDF4".getBytes(), "Windows-1256");

		posPtr.printString(str);



//		posPtr.setCharSet("UTF-8");

    	return 0;
    }

public String setUTF8Mode(String charSet) {
		byte[] b5font = new byte[]{(byte)27, (byte)88, (byte)0};
		if(charSet.equalsIgnoreCase("UTF-8")) {
			b5font[2] = 1;
		} else {
			b5font[2] = 0;
		}

		this.requestQueue.addRequest(b5font);
	return charSet;
}
}
