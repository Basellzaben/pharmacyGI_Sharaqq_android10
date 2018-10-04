package com.cds_jo.pharmacyGI;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class GetManMessage extends Service {
    int mStartMode=0;
    boolean mAllowRebind;
    public GetManMessage() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet i`mplemented");
    }

    /** Called when the service is being created. */
    @Override
    public void onCreate() {
        Toast.makeText(this, "Services Is created ", Toast.LENGTH_SHORT).show();
    }
    /** The service is starting, due to a call to startService() */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Services Is Started ", Toast.LENGTH_SHORT).show();





      //  final Handler _handler = new Handler();
       // for (int i =  0; i<4 ; i++){
        //    Toast.makeText(this, "counter " + i, Toast.LENGTH_SHORT).show();


         /*   new Thread(new Runnable() {
                @Override
                public void run() {
                    CallWebServices ws = new CallWebServices(getBaseContext());
                    ws.GetcompanyInfo();
                    try {

                        JSONObject js = new JSONObject(We_Result.Msg);
                        JSONArray js_ID = js.getJSONArray("ID");
                        _handler.post(new Runnable() {
                            public void run() {
                                System.out.println("Yes Internet");

                            }
                        });

                    } catch (final Exception e) {
                        _handler.post(new Runnable() {
                            public void run() {
                                System.out.println("No Internet");
                                AlertDialog alertDialog = new AlertDialog.Builder(getBaseContext()).create();
                                alertDialog = new AlertDialog.Builder(
                                        getBaseContext()).create();
                                alertDialog.setTitle("تحديث البيانات");
                                alertDialog.setMessage("حدث خطأ اثناء عملية الاتصال بالسيرفر الرئيسي ، الرجاء التاكد من اتصال الجهاز بالانترنت");
                                alertDialog.setIcon(R.drawable.delete);
                                alertDialog.setButton("نعم", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        return;
                                    }
                                });
                                alertDialog.show();

                            }
                        });
                    }
                }
            }).start();
*/




           /* try{
                Thread.sleep(1000);
            }catch (Exception ex){

            }*/
       // }
        /*MsgNotification noti = new MsgNotification();
        noti.notify(this, "Str", " الرجاء تبليغ العميل بالملاحظات التالية",  "CusNm",1);*/
        return START_STICKY;
    }


    /** Called when a client is binding to the service with bindService()*/
    @Override
    public void onRebind(Intent intent) {
        Toast.makeText(this, "Services Is onRebind ", Toast.LENGTH_SHORT).show();

    }

    /** Called when The service is no longer used and is being destroyed */
    @Override
    public void onDestroy() {
        Toast.makeText(this, "Services Is Destroy ", Toast.LENGTH_SHORT).show();
    }
    public int Random(){
            return  5;
    }
    @Override
    public boolean onUnbind(Intent intent) {
        Toast.makeText(this, "Services Is onUnbind ", Toast.LENGTH_SHORT).show();
        return mAllowRebind;
    }

}
