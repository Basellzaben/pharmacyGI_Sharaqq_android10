package com.cds_jo.pharmacyGI;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.cds_jo.pharmacyGI.assist.Sale_InvoiceActivity;

public class MsgNotification {
    /**
     * The unique identifier for this type of notification.
     */
    private static final String NOTIFICATION_TAG = "رسالة ";


    public static void notify(final Context context,
                              final String exampleString, final String MsgContent,final String CustomerNm , final int number) {
        final Resources res = context.getResources();
        final Bitmap picture = BitmapFactory.decodeResource(res, R.mipmap.gi);


        final String ticker = exampleString;
        final String title =MsgContent ;// res.getString(
             //   R.string.msg_notification_title_template, exampleString);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        final String text = exampleString ;//res.getString(
               // R.string.msg_notification_placeholder_text_template, exampleString);
     //   new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com")
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
               // .setDefaults(Notification.DEFAULT_ALL)
                .setSound(soundUri)
                .setSmallIcon(R.drawable.newmesage)
                .setContentTitle(" الرجاء تبليغ العميل :  "+ CustomerNm + "   بالملاحظات التالية :- " )
                .setContentText(" الرجاء تبليغ العميل :  "+ CustomerNm + "   بالملاحظات التالية :- ")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setLargeIcon(picture)
                .setTicker("رسائــل خاصة بالعميل" + "  " + CustomerNm)
                //.setNumber(number)


                .setContentIntent(
                        PendingIntent.getActivity(
                                context,
                                0,
                                new Intent(context, Sale_InvoiceActivity.class),
                                PendingIntent.FLAG_UPDATE_CURRENT))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(text)
                        .setBigContentTitle(" الرجاء تبليغ العميل :  "+ CustomerNm + "   بالملاحظات التالية :- ")
                        .setSummaryText(" حالة الرسالة" + ":" + "  عادية  "))
                .addAction(
                        R.drawable.ic_action_stat_share, " ارســال",
                        PendingIntent.getActivity(
                                context,
                                0,
                                Intent.createChooser(new Intent(Intent.ACTION_SEND)

                                        .setType("text/plain")
                                        .putExtra("address", "96278538193")
                                        .putExtra(Intent.EXTRA_TEXT, "  السيد :  " + CustomerNm + " المحترم " + "\r\n" + " نــود اعلامــكم بمــا يلـــي :-" + "\r\n" + text), "ارسال الرسائل الخاصة بالعميل" + " " + CustomerNm),
                                PendingIntent.FLAG_UPDATE_CURRENT))
                .addAction(
                        R.drawable.ic_action_done,
                        "مــوافــق",
                        PendingIntent.getActivity(
                                context,
                                0,
                                new Intent(context, Sale_InvoiceActivity.class),
                                PendingIntent.FLAG_UPDATE_CURRENT))

                .setAutoCancel(true);

        notify(context, builder.build());
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, 0, notification);
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
        }
    }


    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG, 0);
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode());
        }
    }
}
