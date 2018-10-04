package com.cds_jo.pharmacyGI;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqlDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_TABLE = "ITEMS_PO";

    public static final String COLUMN1 = "no";
    public static final String COLUMN2 = "name";
    public static final String COLUMN3 = "price";
    public static final String COLUMN4 = "qty";
    public static final String COLUMN5 = "tax";
    public static final String COLUMN6 = "unitNo";
    private static final String SCRIPT_CREATE_DATABASE = "CREATE TABLE IF NOT EXISTS "
            + DATABASE_TABLE + " (" + COLUMN1
            + " integer primary key autoincrement, "
            + COLUMN2 + " text  null, "
            + COLUMN3 + " text  null,"
            + COLUMN4 + " text  null,"
            + COLUMN5 + " text  null,"
            + COLUMN6 + " text  null)";



    public SqlDbHelper(Context context, String name, CursorFactory factory,
                       int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

try {

    db.execSQL(SCRIPT_CREATE_DATABASE);
    db.execSQL("CREATE TABLE IF NOT EXISTS banks ( ID INTEGER PRIMARY KEY AUTOINCREMENT,Bank TEXT Null,BEName TEXT Null,bank_num TEXT Null,Accno TEXT Null,CCntrNo TEXT Null )");
    db.execSQL("CREATE TABLE IF NOT EXISTS RecCheck (   ID INTEGER PRIMARY KEY AUTOINCREMENT,DocNo INTEGER null,CheckNo TEXT null ,CheckDate TEXT null ,BankNo  TEXT null ,Amnt REAL null ,UserID INTEGER null,Post INTEGER null )");
    db.execSQL("CREATE TABLE IF NOT EXISTS invf ( no integer primary key autoincrement , Item_No text null, Item_Name text null,Ename text null  , Unit text null ,Price text null ,OL text null  ,OQ1 text null ,Type_No text null ,Pack text null,Place text null,dno text null ,tax text null ) ");
    db.execSQL("CREATE TABLE IF NOT EXISTS  UnitItems ( no integer primary key autoincrement, item_no  text null, barcode text null,unitno text null  , Operand text null ,price text null ,Max text null  ,Min text null ,posprice text  null ) ");
    db.execSQL("CREATE TABLE IF NOT EXISTS  Unites ( no integer primary key autoincrement, Unitno  text null, UnitName text null,UnitEname text null )");
    db.execSQL("CREATE TABLE IF NOT EXISTS  curf    ( no integer primary key autoincrement, cur_no  text null, cur text null,ename  text null  , dec text null , lrate text null)");
    db.execSQL("CREATE TABLE IF NOT EXISTS  deptf ( no integer primary key autoincrement, Type_No text null, Type_Name text null,etname text null  , route text null) ");
    db.execSQL("CREATE TABLE IF NOT EXISTS  manf ( no integer primary key autoincrement, man integer null, name text null,MEName text null  , username text null,password text null , StoreNo text null ,Stoped integer null , SupNo integer null " +
            ",Mobile2  text null ,Mobile1  text null) ");
    db.execSQL("CREATE TABLE IF NOT EXISTS  SaleManRounds ( no integer primary key autoincrement, ManNo text null, CusNo text null  , DayNum integer null , Tr_Data  text null ,Start_Time text null ,End_Time text null, Duration Text null,Closed Text Null  , Posted integer null, OrderNo  text null ) ");
    db.execSQL("CREATE TABLE IF NOT EXISTS  SaleManPath ( no integer primary key autoincrement, ManNo text null, CusNo text null,sunday text null  , monday text null,tuesday text null , wednesday text null ,thursday text null , friday text null ,saturday text null,Flg  integer null  , DayNum integer null ) ");


    db.execSQL("CREATE TABLE IF NOT EXISTS   Offers_Groups ( ID   text null,grv_no  text null, gro_name   text null,gro_ename  text null, gro_type  text null, item_no   text null,unit_no    text null, unit_rate   text null, qty   text null   ,SerNo text null )");
    db.execSQL("CREATE TABLE IF NOT EXISTS  Offers_Dtl_Gifts (ID  text null,Trans_ID   text null,Item_No  text null,Unit_No   text null,Unit_Rate  text null,QTY   text null )");
    db.execSQL("CREATE TABLE IF NOT EXISTS Offers_Dtl_Cond (ID    text null,Trans_ID text null,Gro_Num  text null,Allaw_Repet   text null )");



    db.execSQL("CREATE TABLE IF NOT EXISTS  Po_Hdr ( no integer primary key autoincrement,orderno text null, Nm text null , acc text null ,date text null ,Delv_day_count text null ,posted text null ,userid text null,hdr_dis_per text null ,hdr_dis_value text null" +
            " , Total Text null , Net_Total text Null ,Tax_Total  text null , bounce_Total text null, include_Tax text null  , disc_Total text null    ,pro_disc_total  text null , pro_bounc_total text null ,V_OrderNo  text null) ");


  db.execSQL(" CREATE TABLE IF NOT EXISTS Po_dtl ( no integer primary key autoincrement,orderno text null, itemno text null" +
            ",   price text null ,qty text null ,tax text null,unitNo text null ,dis_Amt text null ,dis_per text null," +
            "   OrgPrice text null , bounce_qty text null,bounce_unitno text null  , tax_Amt text null ,total text null , net_total text null  ,ProID text null , Pro_bounce  Text Null , Pro_dis_Per Text Null , Pro_amt Text null , pro_Total text null    ) ");


    db.execSQL("CREATE TABLE IF NOT EXISTS  CustStoreQtydetl ( no integer primary key autoincrement,orderno text null, itemno text null" +
            ",   price text null ,qty text null ,tax text null,unitNo text null ,dis_Amt text null ,dis_per text null," +
            "    bounce_qty text null,bounce_unitno text null  , tax_Amt text null ,total text null , net_total text null  ,ProID text null , Pro_bounce  Text Null , Pro_dis_Per Text Null , Pro_amt Text null , pro_Total text null    ) ");

    db.execSQL("CREATE TABLE IF NOT EXISTS  CustStoreQtyhdr    ( no integer primary key autoincrement,orderno text null, Nm text null , acc text null ,date text null ,Delv_day_count text null ,posted text null ,userid text null,hdr_dis_per text null ,hdr_dis_value text null" +
            " , Total Text null , Net_Total text Null ,Tax_Total  text null , bounce_Total text null, include_Tax text null  , disc_Total text null    ,pro_disc_total  text null , pro_bounc_total text null ) ");

       db.execSQL(" CREATE TABLE IF NOT EXISTS  ManStore ( no integer primary key autoincrement,SManNo integer null,date text null ,fromstore text null ,tostore text null , des text null , docno text null , itemno text null  ,qty text null  , UnitNo text null " +
            " , StoreName text null , UnitRate text null  , myear text null , RetailPrice text null ,ser integer null ) ");

  db.execSQL(" CREATE TABLE IF NOT EXISTS Sal_invoice_Hdr ( no integer primary key autoincrement,OrderNo text null , Nm text null , acc text null ,date text null,UserID INTEGER null,Post INTEGER null ,   hdr_dis_per text null ,hdr_dis_value text null" +
            " , QtyStoreSer  integer null , Total Text null , Net_Total text Null ,Tax_Total  text null , bounce_Total text null, include_Tax text null  , disc_Total text null ,inovice_type text null , V_OrderNo  text null ) ");


  db.execSQL("CREATE TABLE IF NOT EXISTS Sal_invoice_Det ( no integer primary key autoincrement, OrderNo text null, itemNo text null,unitNo text null  , price text null ,qty text null ,tax text null  ,UserID INTEGER null ,Post INTEGER null   ,dis_Amt text null ,dis_per text null," +
            " OrgPrice text null , bounce_qty text null,bounce_unitno text null  , tax_Amt text null ,total text null , net_total text null ,ProID text null , Pro_bounce  Text Null , Pro_dis_Per Text Null , Pro_amt Text null , pro_Total text null  ,  Operand  text null  ) ");


    db.execSQL("CREATE TABLE IF NOT EXISTS Offers_Hdr (ID text  null,Offer_No text null,Offer_Name text  null,  Offer_Date text null,Offer_Type text null" +
            ",Offer_Status text NULL,Offer_Begin_Date text null,Offer_Exp_Date text null, Offer_Result_Type text null,Offer_Dis text null,Offer_Amt text null , TotalValue text null)");

    db.execSQL("CREATE TABLE IF NOT EXISTS PrepManQtydetl ( no integer primary key autoincrement,orderno text null, itemno text null" +
            ",   price text null ,qty text null ,tax text null,unitNo text null ,total text null     ) ");

    db.execSQL("CREATE TABLE IF NOT EXISTS PrepManQtyhdr    ( no integer primary key autoincrement,orderno text null,  manno text null ,date text null  ,posted text null ,userid text null ) ");

    db.execSQL("CREATE TABLE IF NOT EXISTS RecVoucher ( ID INTEGER PRIMARY KEY AUTOINCREMENT,DocNo NUMERIC,CustAcc TEXT,Amnt REAL,TrDate TEXT,Desc TEXT,UserID NUMERIC null,Post INTEGER null ,VouchType INTEGER null , curno text null , Cash text null ,CheckTotal text null , V_OrderNo  text null  ) ");


    db.execSQL("CREATE TABLE IF NOT EXISTS ReturnQtyhdr ( no integer primary key autoincrement,OrderNo text null ,invoice_no text null   ,Nm text null , acc text null ,date text null,UserID INTEGER null,Post INTEGER null ,   hdr_dis_per text null ,hdr_dis_value text null" +
            ",V_OrderNo  text null , QtyStoreSer  integer null , Total Text null , Net_Total text Null ,Tax_Total  text null , bounce_Total text null, include_Tax text null  , disc_Total text null ,inovice_type text null ) ");

    db.execSQL("CREATE TABLE IF NOT EXISTS ReturnQtydetl ( no integer primary key autoincrement, OrderNo text null, itemNo text null,unitNo text null  , price text null ,qty text null ,tax text null  ,UserID INTEGER null ,Post INTEGER null   ,dis_Amt text null ,dis_per text null," +
            " OrgPrice text null, bounce_qty text null,bounce_unitno text null  , tax_Amt text null ,total text null , net_total text null ,ProID text null , Pro_bounce  Text Null , Pro_dis_Per Text Null , Pro_amt Text null , pro_Total text null    ) ");


    db.execSQL("CREATE TABLE IF NOT EXISTS Items_Categ ( ID INTEGER PRIMARY KEY AUTOINCREMENT,ItemCode TEXT Null,CategNo TEXT Null,Price TEXT Null ,MinPrice text null ,dis  text null  )");

    db.execSQL("CREATE TABLE IF NOT EXISTS t_RecCheck (   ID INTEGER PRIMARY KEY AUTOINCREMENT,DocNo INTEGER null,CheckNo TEXT null,CheckDate TEXT null ,BankNo  TEXT null ,Amnt REAL null )");
    db.execSQL("CREATE TABLE IF NOT EXISTS  DB_VERVSION ( No  INTEGER   DEFAULT  0  )");
    db.execSQL("CREATE TABLE IF NOT EXISTS  CustLastPrice  ( no integer primary key autoincrement, Item_No  text null, Cust_No text null,Unit_No  text null  , Price text null )");
    db.execSQL("CREATE TABLE IF NOT EXISTS  BalanceQty   ( no integer primary key autoincrement,OrderNo text null, Item_No  text null  ,Unit_No  text null  , Qty text null,ActQty text null,Diff text null  ,UserID text null  , posted text null , date text  null)");


    db.execSQL("CREATE TABLE IF NOT EXISTS  ManPermession ( no integer primary key autoincrement,User_ID text null, APP_Code  text null  ,SCR_Code   text null  , Branch_Code text null,SCR_Action text null,Permession text null  )");


    db.execSQL("CREATE TABLE IF NOT EXISTS ComanyInfo ( ID integer primary key autoincrement, CompanyID integer null, CompanyNm text null,UserNm text null  , TaxAcc1 text null ,TaxAcc2 text null ,Notes text null  ,Address text null  " +
            "  ,Permession  text null ,CompanyMobile  text null  ,CompanyMobile2  text null ,SuperVisorMobile text null ,SalInvoiceUnit text null " +
            " ,PoUnit text null , AllowSalInvMinus  text null     ,GPSAccurent text null , NumOfInvPerVisit text null " +
            ",NumOfPayPerVisit    text null ,  AllowDeleteInvoice  text null , EnbleHdrDiscount  text null)");


    db.execSQL("CREATE TABLE IF NOT EXISTS  CustLocation  ( no integer primary key autoincrement,CusNo text null, Lat  text null  ,Long  text null ,Address  text null   ,UserID text null , posted text null , date text  null)");

    db.execSQL("CREATE TABLE IF NOT EXISTS  Tab_Password ( ID integer primary key autoincrement,PassNo text null, PassDesc  text null  ,Password  text null )");



    db.execSQL("CREATE TABLE IF NOT EXISTS Customers ( ID INTEGER PRIMARY KEY AUTOINCREMENT,no TEXT Null,name TEXT Null,Ename TEXT Null ,catno text null ,barCode  text null , Address text null , State text null , SMan text null , Latitude text null , Longitude text null, Note2 text null " +
            " , sat  INTEGER null ,  sun INTEGER  null ,   mon INTEGER  null ,  tues  INTEGER null ,  wens INTEGER  null ,  thurs INTEGER null ," +
            "   sat1 INTEGER null ,  sun1  INTEGER null,  mon1 INTEGER  null ,    tues1  INTEGER null ,   wens1  INTEGER null,   thurs1  INTEGER null , Celing text null     " +
            ", CustType  text null, PAMENT_PERIOD_NO  text null,CUST_PRV_MONTH  text null , CUST_NET_BAL  text null,Pay_How  text null)");

     db.execSQL("CREATE TABLE IF NOT EXISTS  RndNum  ( No integer primary key autoincrement,ID text null, Value  text null  , Flg    text null )");

    db.execSQL("CREATE TABLE IF NOT EXISTS  UsedCode   ( No integer primary key autoincrement,Status text null,Code text null," +
                "  OrderNo  text null    , CustomerNo  text null , ItemNo text null ,Tr_Date  text null " +
                ", Tr_Time  text null ,UserNo text null , Posted integer null)");

     db.execSQL("CREATE TABLE IF NOT EXISTS  DoctorReport    " +
                "( ID integer primary key autoincrement,VType integer null,No integer null,CustNo text null,LocatNo text null ," +
                "  Sp1  text null    , SampleType  text null , VNotes text null,SNotes text null ,Tr_Date  text null " +
                ", Tr_Time  text null ,UserNo text null , Posted integer null)");

    db.execSQL("CREATE TABLE IF NOT EXISTS  Area    " +
                "( ID integer primary key autoincrement,No integer null,Name text null,Ename text null ," +
                "  City  text null    , Country  text null )");

    db.execSQL("CREATE TABLE IF NOT EXISTS  Doctor    " +
                "( ID integer primary key autoincrement,Dr_No integer null,Dr_AName text null,Dr_EName text null ," +
                "  Dr_Tel  text null    , Specialization_No  integer null , Area integer null  )");

    db.execSQL("CREATE TABLE IF NOT EXISTS  Specialization    " +
                "( ID integer primary key autoincrement,No integer null,Aname text null,Ename text null   )");

    db.execSQL("CREATE TABLE IF NOT EXISTS CASHCUST ( ID integer primary key autoincrement, No text null, Name text null,veName text null ,UserID text null,  V_OrderNo  text null " +
            " , Acc  text null   , Person text null,Celing text null ,State text null ,  Posted text null, Type  text null )" );

        db.execSQL("CREATE TABLE IF NOT EXISTS  ACC_REPORT    " +
                "( ID integer primary key autoincrement,Cust_No  text null,Cust_Nm  text null ,FDate text null,TDate text null , TrDate text null ," +
                "  Tot  text null    , Rate  text null , Cred text null , Dept text null , Bb text null , Des text null , Date text null" +
                " , Cur_no text null , Doctype text null , Doc_num text null , CheqBal text null  , Ball text null, CusTop text null, NetBall text null" +
                " , Notes text null )");





} catch (SQLException e){


}
catch ( Exception ex)
{
    ex.printStackTrace();
}

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

/*
                db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
                db.execSQL("DROP TABLE IF EXISTS  RecCheck");
                db.execSQL("DROP TABLE IF EXISTS  banks");
                db.execSQL("DROP TABLE IF EXISTS  invf");
                db.execSQL("DROP TABLE IF EXISTS  UnitItems");
                db.execSQL("DROP TABLE IF EXISTS  Unites");
                db.execSQL("DROP TABLE IF EXISTS  curf");
                db.execSQL("DROP TABLE IF EXISTS  deptf");
                db.execSQL("DROP TABLE IF EXISTS  manf");
                db.execSQL("DROP TABLE IF EXISTS  SaleManRounds") ;
                db.execSQL("DROP TABLE IF EXISTS  SaleManPath");

                db.execSQL("DROP TABLE IF EXISTS  Offers_Groups");
                db.execSQL("DROP TABLE IF EXISTS  Offers_Dtl_Gifts");
                db.execSQL("DROP TABLE IF EXISTS  Offers_Dtl_Cond");
                db.execSQL("DROP TABLE IF EXISTS  Sal_invoice_Hdr");


                db.execSQL("DROP TABLE IF EXISTS  Po_Hdr");
                db.execSQL("DROP TABLE IF EXISTS  CustStoreQtydetl");
                db.execSQL("DROP TABLE IF EXISTS  CustStoreQtyhdr");
                db.execSQL("DROP TABLE IF EXISTS  ManStore");
                db.execSQL("DROP TABLE IF EXISTS  ComanyInfo");
                db.execSQL("DROP TABLE IF EXISTS  Offers_Hdr");
                db.execSQL("DROP TABLE IF EXISTS  PrepManQtydetl");
                db.execSQL("DROP TABLE IF EXISTS  PrepManQtyhdr");
                db.execSQL("DROP TABLE IF EXISTS  RecVoucher");

                db.execSQL("DROP TABLE IF EXISTS  ReturnQtyhdr");

                db.execSQL("DROP TABLE IF EXISTS  CASHCUST");


                db.execSQL("DROP TABLE IF EXISTS  Sal_invoice_Det");
                db.execSQL("DROP TABLE IF EXISTS  Po_dtl");
                db.execSQL("DROP TABLE IF EXISTS  ReturnQtydetl");


                db.execSQL("DROP TABLE IF EXISTS  Customers");
*/

          onCreate(db);
        try {
            db.execSQL("INSERT INTO DB_VERVSION(No) values (0)");

        } catch (SQLException e) {
        Log.i("ADD COLUMN Operand", "Week already Operand");
      }






    }

}