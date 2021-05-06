package com.cds_jo.pharmacyGI;
    import android.content.pm.PackageManager;
    import android.support.v4.content.ContextCompat;
    import android.os.Bundle;

    import java.util.Timer;
        import java.util.TimerTask;
        import android.content.Context;
        import android.location.Location;
        import android.location.LocationListener;
        import android.location.LocationManager;
    import android.os.Handler;
    import android.widget.Toast;

public class GetLocation {
    Timer timer1;
    LocationManager lm;
    LocationResult locationResult;
    boolean gps_enabled=false;
    boolean network_enabled=false;
    float RequiredAccurecy =30;

//    public boolean getLocation(Context context,float _RequiredAccurecy,int _MaximumWaitingTime, LocationResult result)
//    {
//        RequiredAccurecy=_RequiredAccurecy;
//        //I use LocationResult callback class to pass location value from GetLocation to user code.
//        locationResult=result;
//        if(lm==null)
//            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//
//        //exceptions will be thrown if provider is not permitted.
//        try{gps_enabled=lm.isProviderEnabled(LocationManager.GPS_PROVIDER);}catch(Exception ex){}
//        try{network_enabled=lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);}catch(Exception ex){}
//
//        //don't start listeners if no provider is enabled
//        //if(!gps_enabled && !network_enabled) {
//        if(!gps_enabled ) {
//            locationResult.gotLocation(null, "PERMISSION");
//            return false;
//        }
//        if(gps_enabled)
//            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
//        if(network_enabled)
//            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
//        timer1=new Timer();
//        timer1.schedule(GetLastLocation, _MaximumWaitingTime);
//        return true;
//    }

    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            if (location.getAccuracy()<= RequiredAccurecy)
            {
                timer1.cancel();
                locationResult.gotLocation(location,"");
                lm.removeUpdates(this);
                lm.removeUpdates(locationListenerNetwork);
            }
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            if (location.getAccuracy()<= RequiredAccurecy) {
                timer1.cancel();
                locationResult.gotLocation(location,"");
                lm.removeUpdates(this);
                lm.removeUpdates(locationListenerGps);
            }
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    final Handler handler = new Handler();

    TimerTask GetLastLocation = new TimerTask() {
        public void run() {
            handler.post(new Runnable() {
                public void run() {
                    lm.removeUpdates(locationListenerGps);
                    lm.removeUpdates(locationListenerNetwork);

                    Location net_loc=null, gps_loc=null;
                    if(gps_enabled)
                        gps_loc=lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                    if(network_enabled)
                        net_loc=lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    //if there are both values use the latest one
                    if(gps_loc!=null && net_loc!=null){
                        if(gps_loc.getTime()>net_loc.getTime()) {
                            if (gps_loc.getAccuracy() <= RequiredAccurecy) {
                                locationResult.gotLocation(gps_loc,"");
                                return;
                            }
                        }
                        else {
                            if (gps_loc.getAccuracy() <= RequiredAccurecy) {
                                locationResult.gotLocation(net_loc,"");
                                return;
                            }
                        }

                    }

                    if(gps_loc!=null){
                        if (gps_loc.getAccuracy() <= RequiredAccurecy) {
                            locationResult.gotLocation(gps_loc,"");
                            return;
                        }
                    }
                    if(net_loc!=null){
                        if (gps_loc.getAccuracy() <= RequiredAccurecy) {
                            locationResult.gotLocation(net_loc,"");
                            return;
                        }
                    }

                    locationResult.gotLocation(null,"ACCURECY");
                    return;

                }

            });


        }
    };

    public static abstract class LocationResult{
        public abstract void gotLocation(Location location,String strError);
    }


    public Location  CurrentLocation(Context context){
        //exceptions will be thrown if provider is not permitted.
        float GPS_Accurecy = 15;
        float NETWORK_Accurecy =15;
        float PASSIVE_Accurecy = 15;
        float pAcceptableAccurecy = 15;
        Location GPS_location = null;
        Location NETWORK_location = null;
        Location PASSIVE_location = null;

        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        if (lm != null) {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListenerNETWORK);
                //lm.removeUpdates(mLocationListenerNETWORK);

                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListenerGPS);
                //lm.removeUpdates(mLocationListenerGPS);

                lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, mLocationListenerPASSIVE);
                //lm.removeUpdates(mLocationListenerPASSIVE);

                boolean NWTWORK_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                if (NWTWORK_enabled)
                {
                    try
                    {
                        NETWORK_location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        lm.removeUpdates(mLocationListenerNETWORK);
                        NETWORK_Accurecy = NETWORK_location.getAccuracy();
                    }
                    catch (Exception  ex)
                    {
                        NETWORK_location =null;
                    }
                }

                boolean GPS_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (GPS_enabled)
                {
                    try
                    {
                        GPS_location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        lm.removeUpdates(mLocationListenerGPS);
                        GPS_Accurecy = GPS_location.getAccuracy();
                    }
                    catch (Exception  ex)
                    {
                        GPS_location = null;
                    }
                }

                boolean PASSIVE_enabled = lm.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);
                if (PASSIVE_enabled)
                {
                    try
                    {
                        PASSIVE_location = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                        lm.removeUpdates(mLocationListenerPASSIVE);
                        PASSIVE_Accurecy = GPS_location.getAccuracy();
                    }
                    catch (Exception  ex)
                    {
                        PASSIVE_location = null;
                    }
                }

                if (GPS_location != null &&  NETWORK_location != null &&  PASSIVE_location != null)
                {
                    if(GPS_Accurecy <= NETWORK_Accurecy && GPS_Accurecy <= PASSIVE_Accurecy && GPS_Accurecy <=pAcceptableAccurecy)
                        return GPS_location;
                    else if(NETWORK_Accurecy <= PASSIVE_Accurecy && NETWORK_Accurecy <= pAcceptableAccurecy)
                        return NETWORK_location;
                    else if(PASSIVE_Accurecy <=pAcceptableAccurecy)
                        return PASSIVE_location;
                }
                else
                {
                    if (GPS_location != null)
                    {
                        if(NETWORK_location != null)
                        {
                            if(GPS_Accurecy <= NETWORK_Accurecy && GPS_Accurecy <=pAcceptableAccurecy)
                                return GPS_location;
                            else if(NETWORK_Accurecy <= pAcceptableAccurecy)
                                return NETWORK_location;
                        }
                        else if(PASSIVE_location != null)
                        {
                            if(GPS_Accurecy <= PASSIVE_Accurecy && GPS_Accurecy <=pAcceptableAccurecy)
                                return GPS_location;
                            else if(PASSIVE_Accurecy <= pAcceptableAccurecy)
                                return PASSIVE_location;
                        }
                        else
                        {
                            return GPS_location;
                        }
                    }
                    else if(NETWORK_location != null)
                    {
                        if(PASSIVE_location != null)
                        {
                            if(NETWORK_Accurecy <= PASSIVE_Accurecy && NETWORK_Accurecy <= pAcceptableAccurecy)
                                return NETWORK_location;
                            else if(PASSIVE_Accurecy <= pAcceptableAccurecy)
                                return PASSIVE_location;
                        }
                        else
                        {
                            return NETWORK_location;
                        }
                    }
                    else if (PASSIVE_location != null)
                    {
                        if(PASSIVE_Accurecy <= pAcceptableAccurecy)
                            return PASSIVE_location;
                    }
                    else
                    {
                        return null;
                    }
                }
            }
        }

        return null;
    }

    public Location  CurrentDeviceLocation(Context context){


        Location GPS_location = null;


        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        if (lm != null) {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {


                boolean GPS_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (GPS_enabled)
                {
                    try
                    {
                        GPS_location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        lm.removeUpdates(mLocationListenerGPS);

                    }
                    catch (Exception  ex)
                    {
                        Toast.makeText(context,"Device:''",Toast.LENGTH_SHORT).show();
                        GPS_location = null;
                    }
                }



                if (GPS_location != null  )
                {
                    Toast.makeText(context,"Device:" + GPS_location.getLongitude(),Toast.LENGTH_SHORT).show();
                   return GPS_location;
                }


            }
        }

        return null;
    }

    public static float  DistanceBetweenTwoPoints(Location CurrenctLocation,Location DistLocation){
        if (CurrenctLocation != null && DistLocation != null)
        {
            return CurrenctLocation.distanceTo(DistLocation);
        }
        else {
            return 0;
        }
    }

    LocationListener mLocationListenerNETWORK = new LocationListener() {
        public void onLocationChanged(Location location) {
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };
    LocationListener mLocationListenerGPS = new LocationListener() {
        public void onLocationChanged(Location location) {
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };
    LocationListener mLocationListenerPASSIVE = new LocationListener() {
        public void onLocationChanged(Location location) {
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

}