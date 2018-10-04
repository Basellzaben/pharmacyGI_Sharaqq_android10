package hearder.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cds_jo.pharmacyGI.ComInfo;
import com.cds_jo.pharmacyGI.CountrySales;
import com.cds_jo.pharmacyGI.CustomerQty;
import com.cds_jo.pharmacyGI.DoctorReportActivity;
import com.cds_jo.pharmacyGI.EditeTransActivity;
import com.cds_jo.pharmacyGI.GalaxyLoginActivity;
import com.cds_jo.pharmacyGI.GetPermession;
import com.cds_jo.pharmacyGI.ItemGalleryActivity;
import com.cds_jo.pharmacyGI.MPChartActivity;
import com.cds_jo.pharmacyGI.MainActivity;
import com.cds_jo.pharmacyGI.ManBalanceQtyActivity;
import com.cds_jo.pharmacyGI.ManSummeryActivity;
import com.cds_jo.pharmacyGI.ManVisitReport;
import com.cds_jo.pharmacyGI.NotificationActivity;
import com.cds_jo.pharmacyGI.PreapareManQty;
import com.cds_jo.pharmacyGI.QuestneerActivity;
import com.cds_jo.pharmacyGI.R;
import com.cds_jo.pharmacyGI.ScheduleManActivity;
import com.cds_jo.pharmacyGI.Sitting_New_Activity;
import com.cds_jo.pharmacyGI.Sitting_New_Activity2;
import com.cds_jo.pharmacyGI.TransQtyReportActivity;
import com.cds_jo.pharmacyGI.UpdateDataToMobileActivity;
import com.cds_jo.pharmacyGI.UpdateDataToMobileActivity2;
import com.cds_jo.pharmacyGI.assist.Acc_ReportActivity;
import com.cds_jo.pharmacyGI.assist.DoctorVisitNew;
import com.cds_jo.pharmacyGI.assist.MonthlySalesManSchedule;
import com.cds_jo.pharmacyGI.assist.OrdersItems;
import com.cds_jo.pharmacyGI.assist.Sale_InvoiceActivity;

import java.util.ArrayList;

import Methdes.MethodToUse;
import port.bluetooth.BluetoothConnectMenu;


public class Setting_List_Adapter extends BaseAdapter {
    private Context context;
    private ArrayList<Main_List_Itme> myList;
    private LayoutInflater layoutInflater;
    private Typeface typeface;

    public Setting_List_Adapter(Context context, ArrayList<Main_List_Itme> myList) {
        this.context = context;
        this.myList = myList;
        layoutInflater= LayoutInflater.from(context);
        typeface= MethodToUse.SetTFace(context);

    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.setting_item1, parent, false);
        TextView Title = (TextView) convertView.findViewById(R.id.textView69);
        ImageView img = (ImageView) convertView.findViewById(R.id.imageView6);
        LinearLayout RR=(LinearLayout)convertView.findViewById(R.id.RR_Set);
        Title.setText(myList.get(position).getTitle());
        img.setImageResource(myList.get(position).getImg());
        Title.setTypeface(typeface);

        if ( ComInfo.UserType ==1) {

            RR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Toast.makeText(context,position+"",Toast.LENGTH_LONG).show();
                    GetPermession obj = new GetPermession();


                    if (position == 0) {
                        Intent intent = new Intent(context.getApplicationContext(), Acc_ReportActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        ((Activity) context).finish();


                    } else if (position == 1) {


                        Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        ((Activity) context).finish();


                    } else if (position == 2) {


                        Intent intent = new Intent(context.getApplicationContext(), ItemGalleryActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        ((Activity) context).finish();


                    }
                    /*else if (position == 3) {

                        Intent intent = new Intent(context.getApplicationContext(), Sale_InvoiceActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        ((Activity) context).finish();


                    }*/
                    else if (position ==3) {


                        Intent intent = new Intent(context.getApplicationContext(), OrdersItems.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        ((Activity) context).finish();


                    } else if (position == 4) {


                        Intent intent = new Intent(context.getApplicationContext(), TransQtyReportActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        ((Activity) context).finish();


                    } else if (position == 5) {
                        Intent intent = new Intent(context.getApplicationContext(), UpdateDataToMobileActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        ((Activity) context).finish();

                    } else if (position ==6) {

                        Intent intent = new Intent(context.getApplicationContext(), ScheduleManActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        ((Activity) context).finish();


                    } else if (position == 7) {


                        Intent intent = new Intent(context.getApplicationContext(), CustomerQty.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        ((Activity) context).finish();


                    } else if (position == 100) {

                        Intent intent = new Intent(context.getApplicationContext(), NotificationActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        ((Activity) context).finish();


                    } else if (position == 8) {

                        Intent intent = new Intent(context.getApplicationContext(), QuestneerActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        ((Activity) context).finish();


                /*    } else if (position == 10) {


                        Intent intent = new Intent(context.getApplicationContext(), BluetoothConnectMenu.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        ((Activity) context).finish();*/


                    }
                   /* else if (position == 9) {

                        Intent intent = new Intent(context.getApplicationContext(), EditeTransActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        ((Activity) context).finish();


                    } */

                    else if (position == 9) {

                        Intent intent = new Intent(context.getApplicationContext(), CountrySales.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        ((Activity) context).finish();


                    } else if (position == 10) {


                        Intent intent = new Intent(context.getApplicationContext(), ManSummeryActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        ((Activity) context).finish();


                    }
                   /* else if (position == 11) {

                        Intent intent = new Intent(context.getApplicationContext(), CountrySales.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        ((Activity) context).finish();


                    } */
                    else if (position == 11) {
                        Intent intent = new Intent(context.getApplicationContext(), ManVisitReport.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        ((Activity) context).finish();

                    }    else if (position == 12) {
                            Intent intent = new Intent(context.getApplicationContext(), MonthlySalesManSchedule.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                            ((Activity) context).finish();
                    /*} else if (position == 16) {
                        Intent intent = new Intent(context.getApplicationContext(), DoctorReportActivity.class);
                        //Intent intent = new Intent(context.getApplicationContext(), MPChartActivity.class);

                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        ((Activity) context).finish();*/


                    } else if (position == 13) {

                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("Login", "No");
                        editor.commit();

                        Intent intent = new Intent(context.getApplicationContext(), GalaxyLoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        ((Activity) context).finish();


                    }


                }
            });

        }

        else   {

            RR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Toast.makeText(context,position+"",Toast.LENGTH_LONG).show();
                    GetPermession obj = new GetPermession();

                    if (position == 0) {
                        // Intent intent = new Intent(context.getApplicationContext(), DoctorReportActivity.class);
                        Intent intent = new Intent(context.getApplicationContext(), DoctorVisitNew.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        ((Activity) context).finish();

                    }

                  /*  if (position == 1) {
                        Intent intent = new Intent(context.getApplicationContext(), DoctorReportActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        ((Activity) context).finish();

                    }*/

                     else  if (position ==1) {
                        Intent intent = new Intent(context.getApplicationContext(), UpdateDataToMobileActivity2.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        ((Activity) context).finish();

                    } else if (position ==2) {

                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("Login", "No");
                        editor.commit();

                        Intent intent = new Intent(context.getApplicationContext(), GalaxyLoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        ((Activity) context).finish();

                    }


                }
            });

        }

        return convertView;
    }
}
