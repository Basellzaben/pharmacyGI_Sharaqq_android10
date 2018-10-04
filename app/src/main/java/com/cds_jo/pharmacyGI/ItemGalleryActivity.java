package com.cds_jo.pharmacyGI;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.cds_jo.pharmacyGI.assist.Cls_Cur;
import com.cds_jo.pharmacyGI.assist.Cls_itms_Info;

import java.io.File;
import java.util.ArrayList;

import hearder.main.Header_Frag;

public class ItemGalleryActivity extends FragmentActivity {
    ImageView selectedImage;
    private Integer[] mImageIds = {
            R.drawable.a,
            R.drawable.b,
            R.drawable.c,
            R.drawable.d,
            R.drawable.e,
            R.drawable.f,
            R.drawable.g,

    };
    VideoView video_player_view;
    DisplayMetrics dm;
    SurfaceView sur_View;
    ListView Lst_ItemImages ;
    ImageView ItemImage ;
    MediaController media_Controller;
    File imgFile,VideoPath ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.n_activity_item_gallery);

        Fragment frag=new Header_Frag();
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.Frag1,frag).commit();

        ItemImage =  (ImageView)findViewById(R.id.ItemImage);
        Lst_ItemImages  = (ListView)findViewById(R.id.Lst_ItemImages);
        imgFile = new File("//sdcard/Android/Items_Images/logo.jpg");
        try {
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ItemImage.setImageBitmap(myBitmap);
            }
        }
        catch (Exception ex){}
        FillItemImages();

        Lst_ItemImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Cls_itms_Info obj = (Cls_itms_Info) Lst_ItemImages.getItemAtPosition(position);

                imgFile = obj.getBitmapPath();
                try {

                    getInit(obj.getVideoPath());

                    if (imgFile.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());


                        Bitmap imageRounded = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), myBitmap.getConfig());

                        Canvas canvas = new Canvas(imageRounded);
                        Paint mpaint = new Paint();
                        mpaint.setAntiAlias(true);
                        mpaint.setShader(new BitmapShader(myBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                        canvas.drawRoundRect((new RectF(0, 0, myBitmap.getWidth(), myBitmap.getHeight())), 100, 100, mpaint);// Round Image Corner 100 100 100 100
                        ItemImage.setImageBitmap(imageRounded);

                        //ItemImage.setImageBitmap(myBitmap);
                    }


                }finally {

                }


            }
        });
    }

    public void getInit(File Path) {
        video_player_view = (VideoView)  findViewById(R.id.v1);
        media_Controller = new MediaController(this);



        /*dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        int width = dm.widthPixels;

        video_player_view.setMinimumWidth(width);
        video_player_view.setMinimumHeight(height);*/

        if (Path != null && Path.exists()) {
            video_player_view.setMediaController(media_Controller);
            video_player_view.setVideoPath(Path+"");
            video_player_view.start();
        }else
        {

            video_player_view.pause();
        }

    }
    private void FillItemImages() {
        final ArrayList<Cls_itms_Info> Locations = new ArrayList<Cls_itms_Info>();
        final Handler _handler = new Handler();

        final SqlHandler sqlHandler = new SqlHandler(this);
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(ItemGalleryActivity.this);
        progressDialog.setTitle("الرجاء الانتظار");
        progressDialog.setMessage("");
        progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
       /* progressDialog.show();*/

        new Thread(new Runnable() {
            @Override
            public void run() {


                try {
                    String query = "Select  invf.Type_No, invf.Item_No , invf.Item_Name  from invf   " ;
                    ArrayList<Cls_Cur> cls_curs = new ArrayList<Cls_Cur>();
                    cls_curs.clear();

                    String Path = "";
                    Cursor c1 = sqlHandler.selectQuery(query);
                    if (c1 != null && c1.getCount() != 0) {
                        if (c1.moveToFirst()) {
                            do {
                                Cls_itms_Info cls_Loc = new Cls_itms_Info();

                                cls_Loc.setName(c1.getString(c1
                                        .getColumnIndex("Item_Name")));
                                cls_Loc.setNo(c1.getString(c1
                                        .getColumnIndex("Item_No")));

                               /* Path=(c1.getString(c1.getColumnIndex("Type_No")));
                                Path = Path + "/"+ c1.getString(c1.getColumnIndex("0Item_No"));*/
                                Path =  c1.getString(c1.getColumnIndex("Item_No"));

                                imgFile = new File("//sdcard/Android/Items_Images/"+Path+".jpg");
                                VideoPath= new File("/sdcard/Android/Items_Videos/"+Path+".3gp");
                                try {
                                    if (imgFile.exists()) {
                                        cls_Loc.setBitmapPath(imgFile);

                                    }
                                    else
                                    {  imgFile = new  File("//sdcard/Android/Items_Images/empty.jpg");
                                        cls_Loc.setBitmapPath(imgFile);
                                    }

                                    if (VideoPath.exists()) {

                                        cls_Loc.setVideoPath(VideoPath);
                                    }else
                                    {
                                        cls_Loc.setVideoPath(null);
                                    }
                                }
                                catch (Exception ex){}

                                Locations.add(cls_Loc);

                            } while (c1.moveToNext());


                        }
                        c1.close();
                    }



                    _handler.post(new Runnable() {
                        public void run() {
                            Cls_ItemsImage_Adapter cls_trans_qty_adapter = new Cls_ItemsImage_Adapter(
                                    ItemGalleryActivity.this, Locations);
                            Lst_ItemImages.setAdapter(cls_trans_qty_adapter);
                        }
                    });


                    _handler.post(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    ItemGalleryActivity.this).create();

                            alertDialog.setMessage("تمت عملية تحديث البيانات بنجاح" + "   " + String.valueOf(""));
                            alertDialog.setIcon(R.drawable.tick);
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            // alertDialog.show();

                        }
                    });
                } catch (final Exception e) {
                    progressDialog.dismiss();

                }
            }
        }).start();


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), GalaxyMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}