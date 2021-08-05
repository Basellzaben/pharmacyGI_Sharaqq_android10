package com.cds_jo.pharmacyGI.CustLocations;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.cds_jo.pharmacyGI.GPSService;
import com.cds_jo.pharmacyGI.GalaxyMainActivity;
import com.cds_jo.pharmacyGI.R;
import com.cds_jo.pharmacyGI.Select_Customer;
import com.cds_jo.pharmacyGI.SqlHandler;
import com.cds_jo.pharmacyGI.We_Result;
import com.cds_jo.pharmacyGI.assist.CallWebServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import hearder.main.Header_Frag;

import static com.baoyz.swipemenulistview.SwipeMenuListView.DIRECTION_LEFT;
import static com.baoyz.swipemenulistview.SwipeMenuListView.DIRECTION_RIGHT;


public class CustomerLocation extends FragmentActivity implements OnMapReadyCallback {
    SqlHandler sqlHandler;
    TextView tv;
    ProgressDialog progressDialog;
    Drawable greenProgressbar;
    RelativeLayout.LayoutParams lp;
    String UserID = "";
    TextView ed_Acc;
    EditText  ed_CusName,ed_Notes, ed_Area, ed_Person, ed_Mobile, ed_GpsLocation, ed_Lat, ed_Long,ed_States;
    SwipeMenuListView lv_Items;
    ArrayList<CustLocaltions> Records;
    CustLocaltions obj;
    int REQUEST_CAMERA = 0;
    String StoredPath;
    int GoogleMapType = 1;
    String DIRECTORY, Img_ID;
    String MaxID = "";
    Boolean IsNew;
    CustLocaltions ListObj;
    Cls_Cusf_Locations_Adapter contactListAdapter;
    Double Lat = 0.0;
    Double Lng = 0.0;
    private GoogleMap mMap;
    public ProgressDialog loadingdialog;
    SharedPreferences sharedPreferences;







    SwipeMenuCreator creator;

    SimpleDateFormat sdf;
    WebView MapWeb ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cust_locations);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        UserID = sharedPreferences.getString("UserID", "");
        sqlHandler = new SqlHandler(this);
       //     sqlHandler.executeQuery("delete from CustLocaltions");
        ed_Acc = (TextView) findViewById(R.id.ed_Acc);
        Records = new ArrayList<CustLocaltions>();
        Records.clear();

        ed_CusName = (EditText) findViewById(R.id.ed_CusName);
        IsNew = true;

        ed_Area = (EditText) findViewById(R.id.ed_Area);
        ed_Person = (EditText) findViewById(R.id.ed_Person);
        ed_Mobile = (EditText) findViewById(R.id.ed_Mobile);
        ed_GpsLocation = (EditText) findViewById(R.id.ed_GpsLocation);
        ed_Lat = (EditText) findViewById(R.id.ed_Lat);
        ed_Long = (EditText) findViewById(R.id.ed_Long);
        ed_States = (EditText) findViewById(R.id.ed_States);
        ed_Notes = (EditText) findViewById(R.id.ed_Notes);
        MapWeb = (WebView) findViewById(R.id.MapWeb);

        ed_Lat.setEnabled(false);
        ed_Long.setEnabled(false);


        ed_CusName.setEnabled(false);
        ed_CusName.setEnabled(false);
        Getlocation();
        lv_Items = (SwipeMenuListView) findViewById(R.id.lv_Items);

        ShowList();


        creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem Unfav = new SwipeMenuItem(getApplicationContext());
                Unfav.setBackground(R.color.Blue);
                Unfav.setWidth(lv_Items.getWidth() / 4);
                Unfav.setTitle("اعتماد");
                Unfav.setTitleSize(25);
                Unfav.setTitleColor(Color.WHITE);
                menu.addMenuItem(Unfav);

                SwipeMenuItem fav = new SwipeMenuItem(getApplicationContext());
                fav.setBackground(R.color.Red);
                fav.setWidth(lv_Items.getWidth() / 4);
                fav.setTitle("حذف");
                fav.setTitleSize(25);
                fav.setTitleColor(Color.WHITE);
                menu.addMenuItem(fav);
                // break;

                //}
            }
        };

        lv_Items.setMenuCreator(creator);

        lv_Items.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {

                if (Records.get(position).getCustNo().equalsIgnoreCase("1")) {

                    lv_Items.smoothOpenMenu(position);

                }


            }

            @Override
            public void onSwipeEnd(int position) {

            }
        });
        lv_Items.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        ShareLocationNew();
                        break;
                    case 1:
                        DeleteRow(position, menu, index);
                        // delete
                        break;
                }

                return false;
            }
        });

        lv_Items.setSwipeDirection(DIRECTION_RIGHT);


        lv_Items.setSwipeDirection(DIRECTION_LEFT);


        lv_Items.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });


        lv_Items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                ListObj = (CustLocaltions) lv_Items.getItemAtPosition(position);
               // ShowDetails(position);

            }
        });

        Fragment frag = new Header_Frag();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1, frag).commit();


        try {
            Lat = 0.0;
            Lng = 0.0;

            if (!ed_Lat.getText().toString().equalsIgnoreCase(""))
                Lat = Double.parseDouble(ed_Lat.getText().toString());

            if (!ed_Long.getText().toString().equalsIgnoreCase(""))
                Lng = Double.parseDouble(ed_Long.getText().toString());

           /* if (Lat > 0 && Long > 0) {
                ShowMap(Lat, Long);
            }*/
        } catch (Exception ex) {
        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng sydney = new LatLng(Lat, Lng);
                googleMap.addMarker(new MarkerOptions().title("Paris").position(sydney));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                mMap.animateCamera(zoom);
            }
        });





        MapWeb.setWebViewClient(new WebViewClient());
        MapWeb.loadUrl("https://maps.google.com/maps?q=32.019926,35.859867&hl=en&z=14&amp;output=embed");

        WebSettings webSettings = MapWeb.getSettings();
        webSettings.setJavaScriptEnabled(true);

    }

    private void DeleteRow(final int position, SwipeMenu menu, int index) {


            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("حفظ موقع الصيدلية");
            alertDialog.setMessage("هل انت متاكد من عملية حذف  ");
            alertDialog.setIcon(R.drawable.error_new);
            alertDialog.setPositiveButton("" +

                    "نعم", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Do_DeleteRow(position);

                }
            });

            alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();

    }

    public void btn_save_po(View view) {


        if (ed_Acc.getText().toString().length() == 0) {
            ed_Acc.setError("يجب إدخال رقم الصيدلية!");
            ed_Acc.requestFocus();
            return;
        }
        if (ed_CusName.getText().toString().length() == 0) {
            ed_CusName.setError("يجب إدخال اسم الصيدلية!");
            ed_CusName.requestFocus();
            return;
        }

        if (ed_Lat.getText().toString().length() == 0) {
            ed_Lat.setError("الموقع غير متوفر!");
            ed_Lat.requestFocus();
            return;
        }

        AlertDialog Dialog = new AlertDialog.Builder(this).create();


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("موقع الصيدلية");
        alertDialog.setMessage("هل تريد الاستمرار بعملية الحفظ");
        alertDialog.setIcon(R.drawable.save);
        alertDialog.setPositiveButton("موافق", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


            }
        });


        alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
       // alertDialog.show();

        Save_Recod_Po();
    }

    public void Save_Recod_Po() {

        sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        long i;
        ContentValues cv = new ContentValues();
        cv.put("CustNo", ed_Acc.getText().toString().trim());
        cv.put("ManNo",UserID);
        cv.put("Lat_X", ed_Lat.getText().toString());
        cv.put("Lat_Y", ed_Long.getText().toString());
        cv.put("Locat", ed_GpsLocation.getText().toString());
        cv.put("Post", "-1");
        cv.put("Note", ed_Notes.getText().toString());
        cv.put("PersonNm", ed_Person.getText().toString());
        cv.put("MobileNo", ed_Mobile.getText().toString());
        cv.put("Stutes", ed_States.getText().toString());
        cv.put("Tr_Date", currentDateandTime);






        //if (IsNew == true) {
            i = sqlHandler.Insert("CustLocaltions", null, cv);
       // } else {
      //      i = sqlHandler.Update("CustLocaltions", cv, "OrderNo ='" + OrderNo.getText().toString() + "'");
      //  }


        if (i > 0) {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("موقع الصيدلية");
            alertDialog.setMessage("تمت عملية الحفظ بنجاح ");

            IsNew = false;
            alertDialog.setIcon(R.drawable.tick);
            alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ShowList();
                    Do_New();
                    ShareLocationNew();
                    View view = null;
                }
            });
            alertDialog.show();
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("موقع الصيدلية");
            alertDialog.setMessage("عملية الحفظ لم تتم بنجاح");

            IsNew = false;
            alertDialog.setIcon(R.drawable.error_new);
            alertDialog.setButton("رجوع", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    View view = null;
                }
            });
            alertDialog.show();
        }
    }

    private void Do_DeleteRow(int index) {
       ListObj = Records.get(index);
        String q = "Delete from CustLocaltions  where  no ='" + ListObj.getNo() + "'";
        long i = 0;
        sqlHandler.executeQuery(q);
        ShowList();

    }

   public void Set_Stutes(  String Desc) {
       ed_States.setText(Desc);
    }
    @SuppressLint("Range")
    public void ShowList() {

        Records.clear();
        String q = "Select distinct  Tr_Date, CustLocaltions.no as no,  ifnull(name,'') as  name ,Locat, CustNo,Lat_X,Lat_Y  ,  ifnull(Post,'-1') as Post,Note,Tr_Date,PersonNm,MobileNo,Stutes  ,Locat from CustLocaltions  " +
                "  Left join Customers on Customers.no= CustNo ";
        Cursor c1 = sqlHandler.selectQuery(q);
        if (c1 != null && c1.getCount() != 0) {
            c1.moveToFirst();
            do {
                obj = new CustLocaltions();
                obj.setCustNo(c1.getString(c1.getColumnIndex("CustNo")));
                obj.setLat_X(c1.getString(c1.getColumnIndex("Lat_X")));
                obj.setLat_Y(c1.getString(c1.getColumnIndex("Lat_Y")));
                obj.setPost(c1.getString(c1.getColumnIndex("Post")));
                obj.setCustNm(c1.getString(c1.getColumnIndex("name")));
                obj.setLocat(c1.getString(c1.getColumnIndex("Locat")));
                obj.setNo(c1.getString(c1.getColumnIndex("no")));
                obj.setTr_Date(c1.getString(c1.getColumnIndex("Tr_Date")));

                obj.setPersonNm(c1.getString(c1.getColumnIndex("PersonNm")));
                obj.setNote(c1.getString(c1.getColumnIndex("Note")));
                Records.add(obj);

            } while (c1.moveToNext());
            c1.close();
        }

        contactListAdapter = new Cls_Cusf_Locations_Adapter(
                CustomerLocation.this, Records);
        lv_Items.setAdapter(contactListAdapter);

        try {
            loadingdialog.dismiss();
        } catch (Exception ex) {
        }
    }

    public void Set_Order(String No, String Nm, String acc) {


       // ShowCustInfo(No);
        ShowList();
    }
    public void btn_Customer(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("Scr", "CusfCard");
        android.app.FragmentManager Manager = getFragmentManager();
        Select_Customer obj = new Select_Customer();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }
    public void Set_Cust(String No, String Nm) {

        ed_Acc.setText(No);
        ed_CusName.setText(Nm);
        ed_CusName.setError(null);
    }

    public void btn_share(View view) {
        ShareLocationNew();
    }
    public void btn_new(View view) {
        Do_New();


        ShowList();
    }

    public void btn_delete(View view) {



            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("موقع الصدلية");
            alertDialog.setMessage("هل انت متاكد من عملية الحذف");
            alertDialog.setIcon(R.drawable.delete);
            alertDialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Delete_Record_PO();

                }
            });

            // Setting Negative "NO" Button
            alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    dialog.cancel();
                }
            });


            alertDialog.show();
        }


    public void Delete_Record_PO() {
        String query = "Delete from  CustLocaltions ";
        sqlHandler.executeQuery(query);


        Records.clear();

        ShowList();

        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();
        alertDialog.setTitle("موقع العميل ");
        alertDialog.setMessage("تمت عملية الحذف بنجاح");
        alertDialog.setIcon(R.drawable.tick);
        alertDialog.setButton("موافق", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Do_New();
            }
        });
        alertDialog.show();


    }

    private void Do_New() {
        ed_CusName.setText("");
        ed_Area.setText("");
        ed_Person.setText("");
        ed_Mobile.setText("");
        ed_Acc.setText("");
        ed_Lat.setText("");
        ed_Long.setText("");
        ed_GpsLocation.setText("");
        ed_Notes.setText("");
        ed_States.setText("");

        Getlocation();

    }

    public void btn_back(View view) {
        Intent k = new Intent(this, GalaxyMainActivity.class);
        startActivity(k);
    }

    @Override
    public void onBackPressed() {
        Intent k = new Intent(this, GalaxyMainActivity.class);
        startActivity(k);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            Lat = 0.0;
            Lng = 0.0;

            if (!ed_Lat.getText().toString().equalsIgnoreCase(""))
                Lat = Double.parseDouble(ed_Lat.getText().toString());

            if (!ed_Long.getText().toString().equalsIgnoreCase(""))
                Lng = Double.parseDouble(ed_Long.getText().toString());

            if (Lat > 0 && Lng > 0) {
                ShowMap(Lat, Lng);
            }
        } catch (Exception ex) {
        }

    }

    public void btn_Satellite(View view) {
        GoogleMapType = 2;
        mMap.setMapType(GoogleMapType);
    }

    public void btn_Normal(View view) {
        GoogleMapType = 1;
        mMap.setMapType(GoogleMapType);
    }

    public void btn_UpdateLocations(View view) {
        Getlocation();
        ShowMap(0.0, 0.0);
    }

    public void GetStreetName() {
        try {

            Geocoder myLocation = new Geocoder(this, Locale.getDefault());
            List<Address> myList = null;
            try {
                myList = myLocation.getFromLocation(Double.parseDouble(ed_Lat.getText().toString()), Double.parseDouble(ed_Long.getText().toString()), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address1 = myList.get(0);
            String addressStr = "";
            addressStr += address1.getAddressLine(0) + ", ";
            addressStr += address1.getAddressLine(1) + ", ";
            addressStr += address1.getAddressLine(2);
             //Toast.makeText(this , addressStr+"",Toast.LENGTH_SHORT).show();
            ed_GpsLocation.setText(addressStr.replace("null", "").replace(",", ""));
        } catch (Exception ex) {
        }
    }
    LocationManager lm;
    LocationListener ll;
    Location l  ;
    private  void  GetLocation2(){


        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        ll = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                l = (Location) location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };




        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lm.requestLocationUpdates(lm.NETWORK_PROVIDER, 0, 0, ll);
        }

        ed_Lat.setText(l.getLatitude()+"");
        ed_Long.setText(l.getLongitude()+"");




    }
    public void Getlocation() {

        try {
            String address = "";
            GPSService mGPSService = new GPSService(this);
            mGPSService.getLocation();

            if (mGPSService.isLocationAvailable == false) {

                return;

            } else {


                double latitude = mGPSService.getLatitude();
                double longitude = mGPSService.getLongitude();

                address = mGPSService.getLocationAddress();


                int precision = (int) Math.pow(10, 6);
                //tv_x.setText(String.format("%.4f", latitude, Locale.US));

                try {


                } catch (Exception ex) {
                    ed_Lat.setText("0.0");
                    ed_Long.setText("0.0");
                }
                ed_Lat.setText(String.valueOf(latitude));
                ed_Long.setText(String.valueOf(longitude));
               // ed_GpsLocation.setText("address");

            }
            GetStreetName();

        } catch (Exception ex) {
            Toast.makeText(this,ex.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }


    }

    private void ShowMap(Double Lat, Double Long) {

        if (mMap == null) {

            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;
                }
            });
        }
        Lat = 0.0;
        Long = 0.0;

        if (!ed_Lat.getText().toString().equalsIgnoreCase(""))
            Lat = Double.parseDouble(ed_Lat.getText().toString());

        if (!ed_Long.getText().toString().equalsIgnoreCase(""))
            Long = Double.parseDouble(ed_Long.getText().toString());


        if (Lat > 0 && Long > 0) {
            LatLng sydney = new LatLng(Lat, Long);
            // LatLng sydney = new LatLng(Lat,Long);
            try {
                mMap.clear();

            } catch (Exception ex) {

            }
            try {
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);

                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.getUiSettings().setCompassEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.addMarker(new MarkerOptions().position(sydney).title(ed_CusName.getText().toString()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                mMap.animateCamera(zoom);

            } catch (Exception ex) {
                // Toast.makeText(this,ex.getMessage().toString(),Toast.LENGTH_SHORT).show();

            }
        }
    }



    @SuppressLint("Range")
    private  void ShareLocationNew(){

        String query = "  select no, CustNo ,ManNo  , Lat_X ,Lat_Y, Locat , Note ,Tr_Date, PersonNm ,MobileNo,Stutes " +
                       "   from CustLocaltions  where  Post = -1   order by no desc  Limit 1 ";

        String    COMPUTERNAME= Settings.Secure.getString(getApplicationContext().getContentResolver(), "bluetooth_name"  );
        COMPUTERNAME=COMPUTERNAME+" (" + Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID  )+")";

        Cursor c1 = sqlHandler.selectQuery(query);

        if(c1!=null && c1.getCount()>0)
        {
            CustLocaltions obj = new CustLocaltions();
            c1.moveToFirst();
            obj.setCustNm(c1.getString(c1.getColumnIndex("CustNo")));
            obj.setCustNo(c1.getString(c1.getColumnIndex("CustNo")));
            obj.setManNo(c1.getString(c1.getColumnIndex("ManNo")));
            obj.setLat_X(c1.getString(c1.getColumnIndex("Lat_X")));
            obj.setLat_Y(c1.getString(c1.getColumnIndex("Lat_Y")));
            obj.setLocat(c1.getString(c1.getColumnIndex("Locat")));
            obj.setNote(c1.getString(c1.getColumnIndex("Note")));
            obj.setTr_Date(c1.getString(c1.getColumnIndex("Tr_Date")));
            obj.setPersonNm(c1.getString(c1.getColumnIndex("PersonNm")));
            obj.setMobileNo(c1.getString(c1.getColumnIndex("MobileNo")));
            obj.setStutes(c1.getString(c1.getColumnIndex("Stutes")));
            obj.setNo(c1.getString(c1.getColumnIndex("no")));
            c1.close();

            Do_share_Visits(obj);

        }else{

            return;}
    }
    public void Do_share_Visits( final CustLocaltions obj) {
        final String str;

        final Handler _handler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                CallWebServices ws = new CallWebServices(getApplicationContext());
                long i =   ws.SaveManLocationsNew(obj );
                try {
                    if (i> 0) {
                        String query = " Update  CustLocaltions  set Post='"+We_Result.ID+"'  where no ='"+obj.getNo() +"'";
                        sqlHandler.executeQuery(query );

                        _handler.post(new Runnable() {
                            public void run() {
                                ShareLocationNew();
                                ShowList();
                            }
                        });
                    } else {
                        _handler.post(new Runnable() {
                            public void run() {





                            }
                        });
                    }
                } catch (final Exception e) {
                    _handler.post(new Runnable() {
                        public void run() {


                        }
                    });
                }
            }
        }).start();
    }

    public void btn_Show_Stutes(View view) {
        Bundle bundle = new Bundle();
        android.app.FragmentManager Manager = getFragmentManager();
        Pop_Select_Stutes obj = new Pop_Select_Stutes();
        obj.setArguments(bundle);
        obj.show(Manager, null);
    }
}
