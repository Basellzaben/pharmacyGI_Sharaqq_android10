package com.cds_jo.pharmacyGI.assist;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.cds_jo.pharmacyGI.Cls_SaleManDailyRound;
import com.cds_jo.pharmacyGI.CustLocations.CustLocaltions;
import com.cds_jo.pharmacyGI.We_Result;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.EOFException;

public class CallWebServices  {
    Context context;
    Activity Activity;
    String IPAddress = "";
    //Namespace of the Webservice - can be found in WSDL
    private static String NAMESPACE = "http://tempuri.org/";
    //Webservice URL - WSDL File location
    // private  String URL = "http://192.168.1.60/Webservices/WebService1.asmx";
     private  String URL = "";
     int TIME_OUT_CONN = 50000;

    public  CallWebServices(Context _context ){
          context = _context;
          SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
          IPAddress =sharedPreferences.getString("ServerIP", "");
          URL = "http://"+IPAddress+"/CV.asmx";//
          URL = "http://79.173.249.130:92/CV.asmx";// Sharaq
        //URL  = "http://192.168.8.102:3550/CV.asmx";// Sharaq
        //  URL = "http://10.0.1.104:3550/CV.asmx";// Sharaq
    }
        //SOAP Action URI again Namespace + Web method w333
        public void GET_ACC_Report_D(int bill , int doctype,int my) {

            We_Result.Msg="";
            We_Result.ID =-1;
            SoapObject request = new SoapObject(NAMESPACE, "GET_ACC_Report_D");
            PropertyInfo parm_UserNo = new PropertyInfo();
            parm_UserNo.setName("bill");
            parm_UserNo.setValue(bill);
            parm_UserNo.setType(String.class);
            PropertyInfo doc = new PropertyInfo();
            doc.setName("doctype");
            doc.setValue(doctype);
            doc.setType(String.class);
            request.addProperty(doc);
            request.addProperty(parm_UserNo);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet=true;
            // Set output SOAP object
            envelope.setOutputSoapObject(request);
            // Create HTTP call object
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            Object  response =null;
            try {
                androidHttpTransport.call("http://tempuri.org/GET_ACC_Report_D", envelope);
                SoapObject result  = (SoapObject) envelope.getResponse();
                We_Result.Msg =  result.getProperty("Msg").toString();
                We_Result.ID = Long.parseLong(result.getProperty("ID").toString());

            } catch (Exception e) {
                We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
                We_Result.ID = Long.parseLong("-404");
                e.printStackTrace();

            }


        }
private  static String SOAP_ACTION = "http://tempuri.org/WithJson";
    public String PostCustCardAndGetAcc(String CusName,String OrderNo, String Area , String CustType, String Mobile , String Acc
            , String Lat,String Lng,String GpsLocation  ,String UserID ,String COMPUTERNAME) {

        We_Result.Msg="";
        We_Result.ID =-1;
        SoapObject request = new SoapObject(NAMESPACE, "SaveCustCardAndGetAcc");

        PropertyInfo Para_CusName = new PropertyInfo();
        Para_CusName.setName("CusName");
        Para_CusName.setValue(CusName);
        Para_CusName.setType(String.class);
        request.addProperty(Para_CusName);


        PropertyInfo Param_OrderNo = new PropertyInfo();
        Param_OrderNo.setName("OrderNo");
        Param_OrderNo.setValue(OrderNo);
        Param_OrderNo.setType(String.class);
        request.addProperty(Param_OrderNo);

        PropertyInfo Param_Area = new PropertyInfo();
        Param_Area.setName("Area");
        Param_Area.setValue(Area);
        Param_Area.setType(String.class);
        request.addProperty(Param_Area);


        PropertyInfo Parm_CustType = new PropertyInfo();
        Parm_CustType.setName("CustType");
        Parm_CustType.setValue(CustType);
        Parm_CustType.setType(String.class);
        request.addProperty(Parm_CustType);

        PropertyInfo Parm_Mobile = new PropertyInfo();
        Parm_Mobile.setName("Mobile");
        Parm_Mobile.setValue(Mobile);
        Parm_Mobile.setType(String.class);
        request.addProperty(Parm_Mobile);



        PropertyInfo Parm_Acc = new PropertyInfo();
        Parm_Acc.setName("Acc");
        Parm_Acc.setValue(Acc);
        Parm_Acc.setType(String.class);
        request.addProperty(Parm_Acc);

        PropertyInfo Parm_Lat = new PropertyInfo();
        Parm_Lat.setName("Lat");
        Parm_Lat.setValue(Lat);
        Parm_Lat.setType(String.class);
        request.addProperty(Parm_Lat);


        PropertyInfo Parm_Long = new PropertyInfo();
        Parm_Long.setName("Lng");
        Parm_Long.setValue(Lng);
        Parm_Long.setType(String.class);
        request.addProperty(Parm_Long);


        PropertyInfo Parm_GpsLocation = new PropertyInfo();
        Parm_GpsLocation.setName("GpsLocation");
        Parm_GpsLocation.setValue(GpsLocation);
        Parm_GpsLocation.setType(String.class);
        request.addProperty(Parm_GpsLocation);





        PropertyInfo parm_UserID= new PropertyInfo();
        parm_UserID.setName("UserID");
        parm_UserID.setValue(UserID);
        parm_UserID.setType(String.class);
        request.addProperty(parm_UserID);


        PropertyInfo parm_COMPUTERNAME= new PropertyInfo();
        parm_COMPUTERNAME.setName("COMPUTERNAME");
        parm_COMPUTERNAME.setValue(COMPUTERNAME);
        parm_COMPUTERNAME.setType(String.class);
        request.addProperty(parm_COMPUTERNAME);



        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {

            androidHttpTransport.call("http://tempuri.org/SaveCustCardAndGetAcc", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();

        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }


        return We_Result.ID+"";  }

    public void SaveCusImnages( String Acc,String ImageDesc ,String ImageBase64 ) {

        We_Result.Msg="";
        We_Result.ID =-1;
        String resTxt = null;

        SoapObject request = new SoapObject(NAMESPACE, "Save_Cusf_Attachament");
        PropertyInfo Parm_Acc = new PropertyInfo();
        Parm_Acc.setName("Acc");
        Parm_Acc.setValue(Acc);
        Parm_Acc.setType(String.class);
        request.addProperty(Parm_Acc);


        PropertyInfo sayImageDesc = new PropertyInfo();
        sayImageDesc.setName("ImageDesc");
        sayImageDesc.setValue(ImageDesc);
        sayImageDesc.setType(String.class);
        request.addProperty(sayImageDesc);


        PropertyInfo parm_ImageBase64= new PropertyInfo();
        parm_ImageBase64.setName("ImageBase64");
        parm_ImageBase64.setValue(ImageBase64);
        parm_ImageBase64.setType(String.class);
        request.addProperty(parm_ImageBase64);




        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call( "http://tempuri.org/Save_Cusf_Attachament", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID =Long.parseLong(result.getProperty("ID").toString());



        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"   ;// + String.valueOf(e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }
        //Return resTxt to calling object

    }
    public void Get_ServerDateTime() {
        We_Result.Msg="";
        We_Result.ID =-1;
        SoapObject request = new SoapObject(NAMESPACE, "GetServerDateTime");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {
            androidHttpTransport.call("http://tempuri.org/GetServerDateTime", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());

        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();

        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"   ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }


    }
    public void GET_ORDER_STUTES( String ManNo) {
        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;

        SoapObject request = new SoapObject(NAMESPACE, "Get_SalesOrderStutesFromAB");



        PropertyInfo parm_ManNo= new PropertyInfo();
        parm_ManNo.setName("ManNo");
        parm_ManNo.setValue(ManNo);
        parm_ManNo.setType(String.class);

        request.addProperty(parm_ManNo);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {
            // Invoke web service
            androidHttpTransport.call("http://tempuri.org/Get_SalesOrderStutesFromAB", envelope);
            // Get the response
            SoapObject result  = (SoapObject) envelope.getResponse();


            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());



        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }
        //Return resTxt to calling object

    }
    public void CallReport(String AccNo, String FromDate,String ToDate,String UserNo) {
        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, "GetAccReport");
        // Property which holds input parameters
        PropertyInfo parm_AccNo = new PropertyInfo();

        parm_AccNo.setName("AccNo");
        parm_AccNo.setValue(AccNo);
        parm_AccNo.setType(String.class);

        PropertyInfo parm_FDate = new PropertyInfo();
        parm_FDate.setName("FDate");
        parm_FDate.setValue(FromDate);
        parm_FDate.setType(String.class);


        PropertyInfo parm_TDate = new PropertyInfo();
        parm_TDate.setName("TDate");
        parm_TDate.setValue(ToDate);
        parm_TDate.setType(String.class);



        PropertyInfo parm_UserNo = new PropertyInfo();
        parm_UserNo.setName("UserNo");
        parm_UserNo.setValue(UserNo);
        parm_UserNo.setType(String.class);

        // Add the property to request object
        request.addProperty(parm_AccNo);
        request.addProperty(parm_FDate);
        request.addProperty(parm_TDate);
        request.addProperty(parm_UserNo);
        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {
            // Invoke web service
            androidHttpTransport.call("http://tempuri.org/GetAccReport", envelope);
            // Get the response
            SoapObject result  = (SoapObject) envelope.getResponse();


            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());



        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }
        //Return resTxt to calling object

    }
    public void CallCountryReturnsReport(String Country,String Man, String FromDate,String ToDate) {
        We_Result.Msg =  "" ;
        We_Result.ID = -2;

        String resTxt = null;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, "GatMobileCountryReturns");
        // Property which holds input parameters
        PropertyInfo parm_Country = new PropertyInfo();

        parm_Country.setName("Country");
        parm_Country.setValue(Country);
        parm_Country.setType(String.class);


        PropertyInfo parm_Man = new PropertyInfo();

        parm_Man.setName("Man");
        parm_Man.setValue(Man);
        parm_Man.setType(String.class);

        PropertyInfo parm_FDate = new PropertyInfo();
        parm_FDate.setName("FDate");
        parm_FDate.setValue(FromDate);
        parm_FDate.setType(String.class);


        PropertyInfo parm_TDate = new PropertyInfo();
        parm_TDate.setName("TDate");
        parm_TDate.setValue(ToDate);
        parm_TDate.setType(String.class);

        // Add the property to request object
        request.addProperty(parm_Country);
        request.addProperty(parm_Man);
        request.addProperty(parm_FDate);
        request.addProperty(parm_TDate);
        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {
            // Invoke web service
            androidHttpTransport.call("http://tempuri.org/GatMobileCountryReturns", envelope);
            // Get the response
            SoapObject result  = (SoapObject) envelope.getResponse();


            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());



        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }
        //Return resTxt to calling object

    }
    public void CallCountrySalesCompare(String Country,String Man, String FromDate,String ToDate) {
        We_Result.Msg =  "" ;
        We_Result.ID = -2;

        String resTxt = null;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, "GatMobileSalesCompare");
        // Property which holds input parameters
        PropertyInfo parm_Country = new PropertyInfo();

        parm_Country.setName("Country");
        parm_Country.setValue(Country);
        parm_Country.setType(String.class);


        PropertyInfo parm_Man = new PropertyInfo();

        parm_Man.setName("Man");
        parm_Man.setValue(Man);
        parm_Man.setType(String.class);

        PropertyInfo parm_FDate = new PropertyInfo();
        parm_FDate.setName("FDate");
        parm_FDate.setValue(FromDate);
        parm_FDate.setType(String.class);


        PropertyInfo parm_TDate = new PropertyInfo();
        parm_TDate.setName("TDate");
        parm_TDate.setValue(ToDate);
        parm_TDate.setType(String.class);

        // Add the property to request object
        request.addProperty(parm_Country);
        request.addProperty(parm_Man);
        request.addProperty(parm_FDate);
        request.addProperty(parm_TDate);
        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {
            // Invoke web service
            androidHttpTransport.call("http://tempuri.org/GatMobileSalesCompare", envelope);
            // Get the response
            SoapObject result  = (SoapObject) envelope.getResponse();


            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());



        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }
        //Return resTxt to calling object

    }
    public void CallCountrySalesReport(String Country,String Man, String FromDate,String ToDate) {
        We_Result.Msg =  "" ;
        We_Result.ID = -2;

        String resTxt = null;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, "GatMobileCountrySales");
        // Property which holds input parameters
        PropertyInfo parm_Country = new PropertyInfo();

        parm_Country.setName("Country");
        parm_Country.setValue(Country);
        parm_Country.setType(String.class);


        PropertyInfo parm_Man = new PropertyInfo();

        parm_Man.setName("Man");
        parm_Man.setValue(Man);
        parm_Man.setType(String.class);

        PropertyInfo parm_FDate = new PropertyInfo();
        parm_FDate.setName("FDate");
        parm_FDate.setValue(FromDate);
        parm_FDate.setType(String.class);


        PropertyInfo parm_TDate = new PropertyInfo();
        parm_TDate.setName("TDate");
        parm_TDate.setValue(ToDate);
        parm_TDate.setType(String.class);

        // Add the property to request object
        request.addProperty(parm_Country);
        request.addProperty(parm_Man);
        request.addProperty(parm_FDate);
        request.addProperty(parm_TDate);
        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {
            // Invoke web service
            androidHttpTransport.call("http://tempuri.org/GatMobileCountrySales", envelope);
            // Get the response
            SoapObject result  = (SoapObject) envelope.getResponse();


            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());



        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }
        //Return resTxt to calling object

    }
    public void CallManVisitReport(String Country,String Man, String FromDate,String ToDate) {
        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, "GatMobileVisitReport");
        // Property which holds input parameters
        PropertyInfo parm_Country = new PropertyInfo();

        parm_Country.setName("Country");
        parm_Country.setValue(Country);
        parm_Country.setType(String.class);


        PropertyInfo parm_Man = new PropertyInfo();

        parm_Man.setName("Man");
        parm_Man.setValue(Man);
        parm_Man.setType(String.class);

        PropertyInfo parm_FDate = new PropertyInfo();
        parm_FDate.setName("FDate");
        parm_FDate.setValue(FromDate);
        parm_FDate.setType(String.class);


        PropertyInfo parm_TDate = new PropertyInfo();
        parm_TDate.setName("TDate");
        parm_TDate.setValue(ToDate);
        parm_TDate.setType(String.class);

        // Add the property to request object
        request.addProperty(parm_Country);
        request.addProperty(parm_Man);
        request.addProperty(parm_FDate);
        request.addProperty(parm_TDate);
        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {
            // Invoke web service
            androidHttpTransport.call("http://tempuri.org/GatMobileVisitReport", envelope);
            // Get the response
            SoapObject result  = (SoapObject) envelope.getResponse();


            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());



        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }
        //Return resTxt to calling object

    }
    public void GetAccNo(String AccNo, String webMethName) {

        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, "GetAccNo");
        // Property which holds input parameters
        PropertyInfo parm_AccNo = new PropertyInfo();

        parm_AccNo.setName("AccNo");
        parm_AccNo.setValue(AccNo);
        parm_AccNo.setType(String.class);


        // Add the property to request object
        request.addProperty(parm_AccNo);

        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {
            // Invoke web service
            androidHttpTransport.call("http://tempuri.org/GetAccNo", envelope);
            // Get the response
            SoapObject result  = (SoapObject) envelope.getResponse();


            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());



        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }
        //Return resTxt to calling object

    }
    public void GetRndNum() {
        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, "Get_RndNum");




        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call("http://tempuri.org/Get_RndNum", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }


    }
    public void GetAccNoBanks() {
        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

            String resTxt = null;
            SoapObject request = new SoapObject(NAMESPACE, "GetBanks");




            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
            SoapEnvelope.VER11);
            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call("http://tempuri.org/GetBanks", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }


    }
    public void GetQuests() {
        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, "GetQuests");




        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call("http://tempuri.org/GetQuests", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }


    }
    public void GetCustomers(String ManNo) {
        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, "GetCustomers");

        PropertyInfo parm_ManNo = new PropertyInfo();
        parm_ManNo.setName("ManNo");
        parm_ManNo.setValue(ManNo);
        parm_ManNo.setType(String.class);
        request.addProperty(parm_ManNo);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call("http://tempuri.org/GetCustomers", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }


    }
    public void Get_CustLastPrice(String ManNo) {

        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, "LastCustPrice");

        PropertyInfo parm_ManNo = new PropertyInfo();
        parm_ManNo.setName("ManNo");
        parm_ManNo.setValue(ManNo);
        parm_ManNo.setType(String.class);
        request.addProperty(parm_ManNo);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,TIME_OUT_CONN);
        Object  response =null;
        try {

            androidHttpTransport.call("http://tempuri.org/LastCustPrice", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;//+ String.valueOf(e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }


    }
    public void GetTab_Password() {

        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, "Get_Tab_Password");




        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call("http://tempuri.org/Get_Tab_Password", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//;+ String.valueOf(e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }


    }
    public void GetGetManPermession(String ManNo) {

        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, "GetManPermession");

        PropertyInfo parm_ManNo = new PropertyInfo();
        parm_ManNo.setName("ManNo");
        parm_ManNo.setValue(ManNo);
        parm_ManNo.setType(String.class);
        request.addProperty(parm_ManNo);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call("http://tempuri.org/GetManPermession", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;//+ String.valueOf(e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }


    }
    public void GetItems() {
        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, "GetItems");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call("http://tempuri.org/GetItems", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;//+ String.valueOf(e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }

    }
    public void GetUnites() {


        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, "GetUnites");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call("http://tempuri.org/GetUnites", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;//+ String.valueOf(e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }


    }
    public void GetUnitItems() {
        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, "GetUnitItems");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call("http://tempuri.org/GetUnitItems", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;//+ String.valueOf(e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }

    }
    public void Getcurf() {

        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, "Getcurf");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call("http://tempuri.org/Getcurf", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;//+ String.valueOf(e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }
    }
    public void deptf() {

        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, "deptf");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call("http://tempuri.org/deptf", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;//+ String.valueOf(e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }

    }

      public void deptf_Filter() {

        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, "deptf_Filter");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call("http://tempuri.org/deptf_Filter", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;//+ String.valueOf(e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }

    }
    public void Save_CustQty(String Json) {

        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE,"Save_CustQty");
        // Property which holds input parameters
        PropertyInfo sayHelloPI = new PropertyInfo();
        // Set Name
        sayHelloPI.setName("JsonStr");
        // Set Value
        sayHelloPI.setValue(Json);
        // Set dataType
        sayHelloPI.setType(String.class);
        // Add the property to request object
        request.addProperty(sayHelloPI);
        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {
            // Invoke web service
            androidHttpTransport.call( "http://tempuri.org/Save_CustQty", envelope);
            // Get the response
            SoapObject result  = (SoapObject) envelope.getResponse();



            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());



        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"   ;// + String.valueOf(e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }
        //Return resTxt to calling object

    }
    public void Save_po(String Json, String webMethName) {
        We_Result.Msg =  ""  ;
        We_Result.ID = -2;

        String resTxt = null;

        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo sayHelloPI = new PropertyInfo();
        // Set Name
        sayHelloPI.setName("JsonStr");
        // Set Value
        sayHelloPI.setValue(Json);
        // Set dataType
        sayHelloPI.setType(String.class);
        // Add the property to request object
        request.addProperty(sayHelloPI);
        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {
            // Invoke web service
            androidHttpTransport.call( "http://tempuri.org/Insert_PurshOrder", envelope);
            // Get the response
            SoapObject result  = (SoapObject) envelope.getResponse();


            // Assign it to resTxt variable static variable
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());



        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"   ;// + String.valueOf(e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }
        //Return resTxt to calling object

    }
    public void SavePrepareQty(String Json ) {
        We_Result.Msg =  ""     ;
        We_Result.ID = -2;


        String resTxt = null;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, "SavePrepareQty");
        // Property which holds input parameters
        PropertyInfo sayHelloPI = new PropertyInfo();
        // Set Name
        sayHelloPI.setName("JsonStr");
        // Set Value
        sayHelloPI.setValue(Json);
        // Set dataType
        sayHelloPI.setType(String.class);
        // Add the property to request object
        request.addProperty(sayHelloPI);
        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {
            // Invoke web service
            androidHttpTransport.call( "http://tempuri.org/SavePrepareQty", envelope);
            // Get the response
            SoapObject result  = (SoapObject) envelope.getResponse();


            // Assign it to resTxt variable static variable
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());



        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"   ;// + String.valueOf(e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }
        //Return resTxt to calling object

    }
    public void ItemCostReport(String ItemNo, String webMethName) {
        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, "GatMobileItemCost");
        // Property which holds input parameters
        PropertyInfo parm_AccNo = new PropertyInfo();

        parm_AccNo.setName("ItemNo");
        parm_AccNo.setValue(ItemNo);
        parm_AccNo.setType(String.class);

        PropertyInfo parm_FDate = new PropertyInfo();
        parm_FDate.setName("FDate");
        parm_FDate.setValue("FDate");
        parm_FDate.setType(String.class);


        PropertyInfo parm_TDate = new PropertyInfo();
        parm_TDate.setName("TDate");
        parm_TDate.setValue("TDate");
        parm_TDate.setType(String.class);

        // Add the property to request object
        request.addProperty(parm_AccNo);
        request.addProperty(parm_FDate);
        request.addProperty(parm_TDate);
        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {
            // Invoke web service
            androidHttpTransport.call("http://tempuri.org/GatMobileItemCost", envelope);
            // Get the response
            SoapObject result  = (SoapObject) envelope.getResponse();


            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());



        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"   ;// + String.valueOf(e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }
        //Return resTxt to calling object

    }
    public void GetManf() {
        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        try {
        SoapObject request = new SoapObject(NAMESPACE, "GetManf");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;


            androidHttpTransport.call("http://tempuri.org/GetManf", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());



        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;//+ String.valueOf( e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }

//
    }


    public void GetTime() {
        We_Result.Msg ="";
        We_Result.ID = -2;

        String resTxt = null;
        try {
            SoapObject request = new SoapObject("https://api.ipgeolocation.io/", "timezone?apiKey=a8c0020bd9484d4497542a22515ff30f&lat=32.01931692665541&long=35.92699122528424");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            Object  response =null;


            androidHttpTransport.call("https://api.ipgeolocation.io/timezone?apiKey=a8c0020bd9484d4497542a22515ff30f&lat=32.01931692665541&long=35.92699122528424", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());



        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;//+ String.valueOf( e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }

//
    }

    public void GetSaleManPath(String No,String DayNum ,String Tr_Date) {
        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, "GetSaleManPath");

        PropertyInfo parm_No = new PropertyInfo();

        parm_No.setName("No");
        parm_No.setValue(No);
        parm_No.setType(String.class);
        request.addProperty(parm_No);

        PropertyInfo parm_DayNum = new PropertyInfo();
        parm_DayNum.setName("DayNum");
        parm_DayNum.setValue(DayNum);
        parm_DayNum.setType(String.class);
        request.addProperty(parm_DayNum);

        PropertyInfo parm_Tr_Date = new PropertyInfo();
        parm_Tr_Date.setName("Tr_Date");
        parm_Tr_Date.setValue(Tr_Date);
        parm_Tr_Date.setType(String.class);
        request.addProperty(parm_Tr_Date);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call("http://tempuri.org/GetSaleManPath", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }


    }
    public void SavePayment(String Json) {
        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, "SavePayment");
        PropertyInfo sayHelloPI = new PropertyInfo();
        sayHelloPI.setName("JsonStr");

        sayHelloPI.setValue(Json);
        sayHelloPI.setType(String.class);
        request.addProperty(sayHelloPI);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,40000);
        Object  response =null;
        try {
            androidHttpTransport.call( "http://tempuri.org/SavePayment", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            // Assign it to resTxt variable static variable
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }
        //Return resTxt to calling object

    }
    public void Get_Offers_Groups() {
        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, "Get_Offers_Groups");
        // Property which holds input parameters

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {
            androidHttpTransport.call("http://tempuri.org/Get_Offers_Groups", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());

        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"   ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }
        //Return resTxt to calling object

    }
    public void Get_Offers_Dtl_Cond() {
        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        // Create request
        try {
        SoapObject request = new SoapObject(NAMESPACE, "Get_Offers_Dtl_Cond");
        // Property which holds input parameters

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;

            androidHttpTransport.call("http://tempuri.org/Get_Offers_Dtl_Cond", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());

        }


  /* } catch (NullPointerException   en){
        We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
        We_Result.ID = Long.parseLong("-404");
        en.printStackTrace();
        resTxt = "Error occured";

      } catch (EOFException eof ){
        We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
        We_Result.ID = Long.parseLong("-404");
        eof.printStackTrace();
        resTxt = "Error occured";
    }*/
    catch (Exception e) {
        We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"        ;
        We_Result.ID = Long.parseLong("-404");
        e.printStackTrace();
        resTxt = "Error occured";
    }
        //Return resTxt to calling object

    }
    public void GetOffers_Hdr() {
        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, "Offers_Hdr");
        // Property which holds input parameters

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {
            androidHttpTransport.call("http://tempuri.org/Offers_Hdr", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());

        } catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }
        //Return resTxt to calling object

    }
    public void Get_Offers_Dtl_Gifts() {
        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, "Get_Offers_Dtl_Gifts");
        // Property which holds input parameters

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {
            androidHttpTransport.call("http://tempuri.org/Get_Offers_Dtl_Gifts", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());

        } catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }
        //Return resTxt to calling object

    }
    public void TrnsferQtyFromMobile(String ManNo, String Max_Order,String TDate) {
        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, "TrnsferQty");
        // Property which holds input parameters

        PropertyInfo parm_ManNo = new PropertyInfo();
        parm_ManNo.setName("ManNo");
        parm_ManNo.setValue(ManNo);
        parm_ManNo.setType(String.class);
        request.addProperty(parm_ManNo);



        PropertyInfo parm_Max_Order = new PropertyInfo();
        parm_Max_Order.setName("Max_Order");
        parm_Max_Order.setValue(Max_Order);
        parm_Max_Order.setType(String.class);
        request.addProperty(parm_Max_Order);

        PropertyInfo parm_TDate = new PropertyInfo();
        parm_TDate.setName("TDate");
        parm_TDate.setValue(TDate);
        parm_TDate.setType(String.class);
        request.addProperty(parm_TDate);
        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {
            androidHttpTransport.call("http://tempuri.org/TrnsferQty", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());

        } catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }
        //Return resTxt to calling object

    }
    public void Save_Sample_Item(String Json) {
        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, "Insert_Sample_Items");
        PropertyInfo sayHelloPI = new PropertyInfo();
        sayHelloPI.setName("JsonStr");
        sayHelloPI.setValue(Json);
        sayHelloPI.setType(String.class);
        request.addProperty(sayHelloPI);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,40000);
        Object  response =null;
        try {

            androidHttpTransport.call( "http://tempuri.org/Insert_Sample_Items", envelope);

            SoapObject result  = (SoapObject) envelope.getResponse();



            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.Msg =  result.getProperty("Msg").toString().replace("#I","  الكمية المتاحة :  ").replace("#M","الكمية غير متاحة من المادة ");
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());



        } catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

            resTxt = "Error occured";

        }


    }
    public void Save_Sal_Invoice(String Json) {
        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
       SoapObject request = new SoapObject(NAMESPACE, "Insert_Sale_Invoice");
        PropertyInfo sayHelloPI = new PropertyInfo();
        sayHelloPI.setName("JsonStr");
        sayHelloPI.setValue(Json);
        sayHelloPI.setType(String.class);
        request.addProperty(sayHelloPI);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,40000);
        Object  response =null;
        try {

            androidHttpTransport.call( "http://tempuri.org/Insert_Sale_Invoice", envelope);

            SoapObject result  = (SoapObject) envelope.getResponse();



            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());



        } catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

            resTxt = "Error occured";

        }


    }
    public void Save_Ret_Sal_Invoice(String Json) {

        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, "Insert_Ret_Sale_Invoice");
        PropertyInfo sayHelloPI = new PropertyInfo();
        sayHelloPI.setName("JsonStr");
        sayHelloPI.setValue(Json);
        sayHelloPI.setType(String.class);
        request.addProperty(sayHelloPI);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call( "http://tempuri.org/Insert_Ret_Sale_Invoice", envelope);

            SoapObject result  = (SoapObject) envelope.getResponse();



            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());



        } catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

            resTxt = "Error occured";

        }


    }
    public void SaveCustLocation(String Json) {

        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, "SaveCustLocation");
        PropertyInfo sayHelloPI = new PropertyInfo();
        sayHelloPI.setName("JsonStr");
        sayHelloPI.setValue(Json);
        sayHelloPI.setType(String.class);
        request.addProperty(sayHelloPI);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {
            androidHttpTransport.call( "http://tempuri.org/SaveCustLocation", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            // Assign it to resTxt variable static variable
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");
            //Print error
            e.printStackTrace();
            //Assign error message to resTxt
            resTxt = "Error occured";
        }
        //Return resTxt to calling object

    }
    public long SaveManLocationsNew(CustLocaltions obj) {

        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, "SaveCustLocations");


        // CusNo   , DayNum ,End_Time  , ManNo ,Start_Time, Tr_Data  ,  no ,    OrderNo
        //       Duration  ,  VisitType  ,  X , Y  ,Locat ,  VisitType1  ,VisitType2,VisitType3 ,
        //      VisitType4 ,Po_Order , Notes

        PropertyInfo P_CustNo = new PropertyInfo();
        P_CustNo.setName("CustNo");
        P_CustNo.setValue(obj.getCustNo());
        P_CustNo.setType(String.class);
        request.addProperty(P_CustNo);

        PropertyInfo P_DayNum = new PropertyInfo();
        P_DayNum.setName("ManNo");
        P_DayNum.setValue(obj.getManNo());
        P_DayNum.setType(String.class);
        request.addProperty(P_DayNum);

        PropertyInfo P_Lat_X= new PropertyInfo();
        P_Lat_X.setName("Lat_X");
        P_Lat_X.setValue(obj.getLat_X());
        P_Lat_X.setType(String.class);
        request.addProperty(P_Lat_X);

        PropertyInfo P_Lat_Y= new PropertyInfo();
        P_Lat_Y.setName("Lat_Y");
        P_Lat_Y.setValue(obj.getLat_Y());
        P_Lat_Y.setType(String.class);
        request.addProperty(P_Lat_Y);

        PropertyInfo P_Locat= new PropertyInfo();
        P_Locat.setName("Locat");
        P_Locat.setValue(obj.getLocat());
        P_Locat.setType(String.class);
        request.addProperty(P_Locat);

        PropertyInfo P_Note= new PropertyInfo();
        P_Note.setName("Note");
        P_Note.setValue(obj.getNote());
        P_Note.setType(String.class);
        request.addProperty(P_Note);




        PropertyInfo P_Tr_Date= new PropertyInfo();
        P_Tr_Date.setName("Tr_Date");
        P_Tr_Date.setValue(obj.getTr_Date());
        P_Tr_Date.setType(String.class);
        request.addProperty(P_Tr_Date);

        PropertyInfo P_PersonNm= new PropertyInfo();
        P_PersonNm.setName("PersonNm");
        P_PersonNm.setValue(obj.getPersonNm());
        P_PersonNm.setType(String.class);
        request.addProperty(P_PersonNm);

        PropertyInfo P_MobileNo= new PropertyInfo();
        P_MobileNo.setName("MobileNo");
        P_MobileNo.setValue(obj.getMobileNo());
        P_MobileNo.setType(String.class);
        request.addProperty(P_MobileNo);

        PropertyInfo P_Stutes= new PropertyInfo();
        P_Stutes.setName("Stutes");
        P_Stutes.setValue(obj.getStutes());
        P_Stutes.setType(String.class);
        request.addProperty(P_Stutes);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {
            androidHttpTransport.call( "http://tempuri.org/SaveCustLocations", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            // Assign it to resTxt variable static variable
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");
            //Print error
            e.printStackTrace();
            //Assign error message to resTxt
            resTxt = "Error occured";
        }
        return   We_Result.ID;
        //Return resTxt to calling object

    }
    public long SaveManVisitsNew(Cls_SaleManDailyRound obj) {

        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, "SaveALLVisits");


       // CusNo   , DayNum ,End_Time  , ManNo ,Start_Time, Tr_Data  ,  no ,    OrderNo
        //       Duration  ,  VisitType  ,  X , Y  ,Locat ,  VisitType1  ,VisitType2,VisitType3 ,
        //      VisitType4 ,Po_Order , Notes

        PropertyInfo P_CusNo = new PropertyInfo();
        P_CusNo.setName("CusNo");
        P_CusNo.setValue(obj.getCusNo());
        P_CusNo.setType(String.class);
        request.addProperty(P_CusNo);

        PropertyInfo P_DayNum = new PropertyInfo();
        P_DayNum.setName("DayNum");
        P_DayNum.setValue(obj.getDayNum());
        P_DayNum.setType(String.class);
        request.addProperty(P_DayNum);

        PropertyInfo P_End_Time= new PropertyInfo();
        P_End_Time.setName("End_Time");
        P_End_Time.setValue(obj.getEnd_Time());
        P_End_Time.setType(String.class);
        request.addProperty(P_End_Time);

        PropertyInfo P_Start_Time= new PropertyInfo();
        P_Start_Time.setName("Start_Time");
        P_Start_Time.setValue(obj.getStart_Time());
        P_Start_Time.setType(String.class);
        request.addProperty(P_Start_Time);

        PropertyInfo P_Tr_Data= new PropertyInfo();
        P_Tr_Data.setName("Tr_Data");
        P_Tr_Data.setValue(obj.getTr_Data());
        P_Tr_Data.setType(String.class);
        request.addProperty(P_Tr_Data);

        PropertyInfo P_ManNo= new PropertyInfo();
        P_ManNo.setName("ManNo");
        P_ManNo.setValue(obj.getManNo());
        P_ManNo.setType(String.class);
        request.addProperty(P_ManNo);




        PropertyInfo P_no= new PropertyInfo();
        P_no.setName("no");
        P_no.setValue(obj.getNo());
        P_no.setType(String.class);
        request.addProperty(P_no);

        PropertyInfo P_OrderNo= new PropertyInfo();
        P_OrderNo.setName("OrderNo");
        P_OrderNo.setValue(obj.getOrderNo());
        P_OrderNo.setType(String.class);
        request.addProperty(P_OrderNo);

        PropertyInfo P_Duration= new PropertyInfo();
        P_Duration.setName("Duration");
        P_Duration.setValue(obj.getDuration());
        P_Duration.setType(String.class);
        request.addProperty(P_Duration);

        PropertyInfo P_VisitType= new PropertyInfo();
        P_VisitType.setName("VisitType");
        P_VisitType.setValue(obj.getVisitType());
        P_VisitType.setType(String.class);
        request.addProperty(P_VisitType);

        PropertyInfo P_X= new PropertyInfo();
        P_X.setName("X");
        P_X.setValue(obj.getX());
        P_X.setType(String.class);
        request.addProperty(P_X);

        PropertyInfo P_Y= new PropertyInfo();
        P_Y.setName("Y");
        P_Y.setValue(obj.getY());
        P_Y.setType(String.class);
        request.addProperty(P_Y);

        PropertyInfo P_Locat= new PropertyInfo();
        P_Locat.setName("Locat");
        P_Locat.setValue(obj.getLocat());
        P_Locat.setType(String.class);
        request.addProperty(P_Locat);

        PropertyInfo P_VisitType1= new PropertyInfo();
        P_VisitType1.setName("VisitType1");
        P_VisitType1.setValue(obj.getVisitType1());
        P_VisitType1.setType(String.class);
        request.addProperty(P_VisitType1);

        PropertyInfo P_VisitType2= new PropertyInfo();
        P_VisitType2.setName("VisitType2");
        P_VisitType2.setValue(obj.getVisitType2());
        P_VisitType2.setType(String.class);
        request.addProperty(P_VisitType2);

        PropertyInfo P_VisitType3= new PropertyInfo();
        P_VisitType3.setName("VisitType3");
        P_VisitType3.setValue(obj.getVisitType3());
        P_VisitType3.setType(String.class);
        request.addProperty(P_VisitType3);

        PropertyInfo P_VisitType4= new PropertyInfo();
        P_VisitType4.setName("VisitType4");
        P_VisitType4.setValue(obj.getVisitType4());
        P_VisitType4.setType(String.class);
        request.addProperty(P_VisitType4);

        PropertyInfo P_Po_Order= new PropertyInfo();
        P_Po_Order.setName("Po_Order");
        P_Po_Order.setValue(obj.getPo_Order());
        P_Po_Order.setType(String.class);
        request.addProperty(P_Po_Order);

        PropertyInfo P_Notes= new PropertyInfo();
        P_Notes.setName("Notes");
        P_Notes.setValue(obj.getNotes());
        P_Notes.setType(String.class);
        request.addProperty(P_Notes);


        PropertyInfo P_COMPUTERNAME= new PropertyInfo();
        P_COMPUTERNAME.setName("COMPUTERNAME");
        P_COMPUTERNAME.setValue(obj.getCOMPUTERNAME());
        P_COMPUTERNAME.setType(String.class);
        request.addProperty(P_COMPUTERNAME);






        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {
            androidHttpTransport.call( "http://tempuri.org/SaveALLVisits", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            // Assign it to resTxt variable static variable
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");
            //Print error
            e.printStackTrace();
            //Assign error message to resTxt
            resTxt = "Error occured";
        }
        return   We_Result.ID;
        //Return resTxt to calling object

    }
    public void SaveManVisits(String Json) {

        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, "SaveManVisits");
        PropertyInfo sayHelloPI = new PropertyInfo();
        sayHelloPI.setName("JsonStr");
        sayHelloPI.setValue(Json);
        sayHelloPI.setType(String.class);
        request.addProperty(sayHelloPI);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {
            androidHttpTransport.call( "http://tempuri.org/SaveManVisits", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            // Assign it to resTxt variable static variable
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");
            //Print error
            e.printStackTrace();
            //Assign error message to resTxt
            resTxt = "Error occured";
        }
        //Return resTxt to calling object

    }
    public void GetSerial() {
        We_Result.Msg =  ""     ;
        We_Result.ID = -2;
        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, "GetVoucherSerial");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call("http://tempuri.org/GetVoucherSerial", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }


    }
    public void GetcompanyInfo() {

        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, "GetCopmanyInfo");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call("http://tempuri.org/GetCopmanyInfo", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }


    }
    public void GetCASHCUST() {
        We_Result.Msg =  ""     ;
        We_Result.ID = -2;
        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, "GetCASHCUST");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call("http://tempuri.org/GetCASHCUST", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }


    }
    public void Get_Items_Categs() {
        We_Result.Msg =  "";
        We_Result.ID = -2;

       SoapObject request = new SoapObject(NAMESPACE, "GetItems_Categ");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
               envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {
            androidHttpTransport.call("http://tempuri.org/GetItems_Categ", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());

        } catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }
        //Return resTxt to calling object

    }
    public void SaveDoctorReport(String Json) {

        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, "SaveDoctorReport");
        PropertyInfo sayHelloPI = new PropertyInfo();
        sayHelloPI.setName("JsonStr");

        sayHelloPI.setValue(Json);
        sayHelloPI.setType(String.class);
        request.addProperty(sayHelloPI);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,40000);
        Object  response =null;
        try {
            androidHttpTransport.call( "http://tempuri.org/SaveDoctorReport", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            // Assign it to resTxt variable static variable
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }
        //Return resTxt to calling object

    }
    public void GetAreas() {

        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, "GetAreas");




        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call("http://tempuri.org/GetAreas", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }


    }
    public void GetDoctors(String ManNo) {

        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, "GetDoctors");

        PropertyInfo parm_ManNo = new PropertyInfo();
        parm_ManNo.setName("ManNo");
        parm_ManNo.setValue(ManNo);
        parm_ManNo.setType(String.class);
        request.addProperty(parm_ManNo);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call("http://tempuri.org/GetDoctors", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }


    }
    public void GetSpecialization() {

        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, "GetSpecialization");




        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call("http://tempuri.org/GetSpecialization", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }


    }
    public void ShareUsedCode(String Json) {

        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, "SaveUsedCodes");
        PropertyInfo sayHelloPI = new PropertyInfo();
        sayHelloPI.setName("JsonStr");
        sayHelloPI.setValue(Json);
        sayHelloPI.setType(String.class);
        request.addProperty(sayHelloPI);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {
            androidHttpTransport.call( "http://tempuri.org/SaveUsedCodes", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            // Assign it to resTxt variable static variable
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");
            //Print error
            e.printStackTrace();
            //Assign error message to resTxt
            resTxt = "Error occured";
        }
        //Return resTxt to calling object

    }
    public void SaveTrandferStoreQty(String Json) {

        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, "SaveStoreQty");
        PropertyInfo sayHelloPI = new PropertyInfo();
        sayHelloPI.setName("JsonStr");

        sayHelloPI.setValue(Json);
        sayHelloPI.setType(String.class);
        request.addProperty(sayHelloPI);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,40000);
        Object  response =null;
        try {
            androidHttpTransport.call( "http://tempuri.org/SaveStoreQty", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            // Assign it to resTxt variable static variable
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }
        //Return resTxt to calling object

    }
    public void Get_Stores() {

        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        SoapObject request = new SoapObject(NAMESPACE, "GetStores");
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {
            androidHttpTransport.call("http://tempuri.org/GetStores", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());

        } catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }
        //Return resTxt to calling object

    }
    public void GetStoresSetting() {

        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        SoapObject request = new SoapObject(NAMESPACE, "GetStoresSetting");




        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {

            androidHttpTransport.call("http://tempuri.org/GetStoresSetting", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }


    }
    public void GetStoreQty(String StoreNo) {
        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, "GetStoreQty");
        // Property which holds input parameters

        PropertyInfo parm_ManNo = new PropertyInfo();
        parm_ManNo.setName("StoreNo");
        parm_ManNo.setValue(StoreNo);
        parm_ManNo.setType(String.class);
        request.addProperty(parm_ManNo);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {
            androidHttpTransport.call("http://tempuri.org/GetStoreQty", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());

        } catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }
        //Return resTxt to calling object

    }
    public void TrnsferQtyFromMobileBatch(String ItemNo, String StoreNo ) {

        We_Result.Msg =  ""     ;
        We_Result.ID = -2;

        String resTxt = null;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, "TrnsferQtyBatch");
        // Property which holds input parameters

        PropertyInfo parm_ItemNo = new PropertyInfo();
        parm_ItemNo.setName("ItemNo");
        parm_ItemNo.setValue(ItemNo);
        parm_ItemNo.setType(String.class);
        request.addProperty(parm_ItemNo);



        PropertyInfo parm_StoreNo = new PropertyInfo();
        parm_StoreNo.setName("StoreNo");
        parm_StoreNo.setValue(StoreNo);
        parm_StoreNo.setType(String.class);
        request.addProperty(parm_StoreNo);

        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,1000);
        Object  response =null;
        try {
            androidHttpTransport.call("http://tempuri.org/TrnsferQtyBatch", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());

        } catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }
        //Return resTxt to calling object

    }
    public void GetOrdersSerials(String ManNo) {

        We_Result.Msg="";
        We_Result.ID =-1;
        SoapObject request = new SoapObject(NAMESPACE, "GetMaxOrders");

        PropertyInfo parm_ManNo = new PropertyInfo();
        parm_ManNo.setName("ManNo");
        parm_ManNo.setValue(ManNo);
        parm_ManNo.setType(String.class);
        request.addProperty(parm_ManNo);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {

            androidHttpTransport.call("http://tempuri.org/GetMaxOrders", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());
        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();

        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;//+ String.valueOf(e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }


    }
    public long GetMonth_Dates(String ManNo,String Month,String Year  ) {
        We_Result.ID=-1;
        We_Result.Msg="";

        SoapObject request = new SoapObject(NAMESPACE, "Get_Month_Dates");

        PropertyInfo parm_ManNo = new PropertyInfo();
        parm_ManNo.setName("ManNo");
        parm_ManNo.setValue(ManNo);
        parm_ManNo.setType(String.class);
        request.addProperty(parm_ManNo);



        PropertyInfo parm_Month = new PropertyInfo();
        parm_Month.setName("Month");
        parm_Month.setValue(Month);
        parm_Month.setType(String.class);
        request.addProperty(parm_Month);




        PropertyInfo parm_Year= new PropertyInfo();
        parm_Year.setName("Year");
        parm_Year.setValue(Year);
        parm_Year.setType(String.class);
        request.addProperty(parm_Year);



        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,1000);
        Object  response =null;
        try {
            androidHttpTransport.call("http://tempuri.org/Get_Month_Dates", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());

        } catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"  ;//+ e.getMessage().toString();
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }
        return We_Result.ID;

    }
    public long Save_MonthlySedule(String Json ) {

        We_Result.Msg="";
        We_Result.ID =-1;

        SoapObject request = new SoapObject(NAMESPACE, "SaveMonthly_Schedule");
        PropertyInfo sayHelloPI = new PropertyInfo();
        sayHelloPI.setName("JsonStr");
        sayHelloPI.setValue(Json);
        sayHelloPI.setType(String.class);
        request.addProperty(sayHelloPI);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {
            androidHttpTransport.call( "http://tempuri.org/SaveMonthly_Schedule", envelope);
            SoapObject result  = (SoapObject) envelope.getResponse();
            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());

        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();


        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();

        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"   ;// + String.valueOf(e.getMessage().toString());
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();

        }


        return  We_Result.ID;
    }
    public void CusLastInvoice(String Acc ) {
        We_Result.Msg =  " "    ;
        We_Result.ID =-1;
        String resTxt = null;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, "GetGatCustLastInvoice");
        // Property which holds input parameters
        PropertyInfo parm_AccNo = new PropertyInfo();

        parm_AccNo.setName("Acc");
        parm_AccNo.setValue(Acc);
        parm_AccNo.setType(String.class);




        request.addProperty(parm_AccNo);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {
            // Invoke web service
            androidHttpTransport.call("http://tempuri.org/GetGatCustLastInvoice", envelope);
            // Get the response
            SoapObject result  = (SoapObject) envelope.getResponse();


            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());



        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }
        //Return resTxt to calling object

    }
    public void CusInvoiceFromBatch(String BatchNo, String Acc ) {
        We_Result.Msg =  " "    ;
        We_Result.ID =-1;
        String resTxt = null;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, "GetGatInvoiceFromBatch");
        // Property which holds input parameters
        PropertyInfo parm_BatchNo = new PropertyInfo();

        parm_BatchNo.setName("BatchNo");
        parm_BatchNo.setValue(BatchNo);
        parm_BatchNo.setType(String.class);
        request.addProperty(parm_BatchNo);

        PropertyInfo parm_Acc = new PropertyInfo();
        parm_Acc.setName("Acc");
        parm_Acc.setValue(Acc);
        parm_Acc.setType(String.class);
        request.addProperty(parm_Acc);



        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet=true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        Object  response =null;
        try {
            // Invoke web service
            androidHttpTransport.call("http://tempuri.org/GetGatInvoiceFromBatch", envelope);
            // Get the response
            SoapObject result  = (SoapObject) envelope.getResponse();


            We_Result.Msg =  result.getProperty("Msg").toString();
            We_Result.ID = Long.parseLong(result.getProperty("ID").toString());



        } catch (NullPointerException   en){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"       ;
            We_Result.ID = Long.parseLong("-404");
            en.printStackTrace();
            resTxt = "Error occured";

        } catch (EOFException eof ){
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"         ;
            We_Result.ID = Long.parseLong("-404");
            eof.printStackTrace();
            resTxt = "Error occured";
        }
        catch (Exception e) {
            We_Result.Msg =  "عملية الاتصال بالسيرفر لم تتم بنجاح"    ;
            We_Result.ID = Long.parseLong("-404");
            e.printStackTrace();
            resTxt = "Error occured";
        }
        //Return resTxt to calling object

    }
}
