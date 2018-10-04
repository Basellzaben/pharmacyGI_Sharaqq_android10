package com.cds_jo.pharmacyGI;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.meg7.widget.CustomShapeImageView;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BeeHiveActivity extends AppCompatActivity {
    private static final Integer[] INVISIBLE = { 7,8,9,10,80,81,82,83,106,107,108,132,133,134,135};// this array have cell number that hide in view
    private static final Integer[] INEMPTY = {3,6,29,30,31,32};// cell number that visible but it has no image(empty cell)
    private static final Integer[] NORMAL = {1,2,11, 12, 13, 14, 17, 18,19,20, 22, 23, 24, 25, 27,33,34,35,56,57,58,59,60,61,84,85,86,87};// normal cells

    private LinearLayout l_root;
    private List<CustomShapeImageView> photoViewList;// each cell view
    DisplayImageOptions options;

    private final static ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bee_hive);
      initImageLoader(this);

        photoViewList=new ArrayList<>();
        options = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY)
                .showStubImage(R.drawable.avatar)
                .showImageForEmptyUri(R.drawable.avatar)
                .cacheOnDisk(true).build();

       l_root =(LinearLayout)findViewById(R.id.l_root);
        l_root.setOrientation(LinearLayout.VERTICAL);


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = 600;//size.x;  // device screen width

         int height =600;// size.y;
        addView(width);
        addImage();
    }
    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                        //.writeDebugLogs() // Remove for release app
                .build();
        //Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);

    }

    private void addView(int width){

        int margin=(width/8)/4;
        width =(width+(width/16))/4;
         int  iter= 1 ;
        photoViewList.clear();
        for (int j=0;j<=5;j++) { // row
            LinearLayout ll = new LinearLayout(BeeHiveActivity.this);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            //ll.setBackgroundColor(Color.BLUE);
            LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            int marginLeft= (j%2 ==0)?(-(width/2)+margin):margin-20;
            int marginTop=-40;//(j !=0)?(-(width/5)):0;

              if (j==0){
                  llParams.setMargins(0,20,marginLeft-200,marginTop );
              }else{
                  llParams.setMargins(0,20,marginLeft-200,marginTop );
              }


            ll.setLayoutParams(llParams);
int  w = width-30;
            for (int i=0;i<=25;i++) {
                int possition=iter ;

                FrameLayout l_farme = new FrameLayout(BeeHiveActivity.this);
                FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(width-30, width-30);
                FrameLayout.LayoutParams frameParams2 = new FrameLayout.LayoutParams(w-(w/10), w-(w/10));
                frameParams2.gravity = Gravity.CENTER;
                l_farme.setLayoutParams(frameParams);

                ImageView hexagon = new ImageView(BeeHiveActivity.this);
                TextView textview = new TextView(BeeHiveActivity.this);
                hexagon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

                hexagon.setLayoutParams(frameParams);
                textview.setLayoutParams(frameParams);

                CustomShapeImageView photo=new CustomShapeImageView(BeeHiveActivity.this,R.drawable.avatar,
                        CustomShapeImageView.Shape.SVG,R.raw.hex);
                photo.setScaleType(ImageView.ScaleType.FIT_XY);
                photo.setLayoutParams(frameParams2);
                textview.setText(iter + "");
                hexagon.setImageResource(R.drawable.hexagon);
                photo.setVisibility(View.VISIBLE);
                l_farme.setVisibility(View.VISIBLE);
                hexagon.setVisibility(View.VISIBLE);
                if (Arrays.asList(INVISIBLE).contains(possition)){
                    l_farme.setVisibility(View.INVISIBLE);
                }else if (Arrays.asList(INEMPTY).contains(possition)){
                    photo.setVisibility(View.INVISIBLE);
                    l_farme.setVisibility(View.VISIBLE);
                }else if (Arrays.asList(NORMAL).contains(possition)){
                    l_farme.setVisibility(View.VISIBLE);
                    photo.setVisibility(View.INVISIBLE);
                    photo.setTag("-1");
                    photoViewList.add(photo);
                  //  photo.setVisibility(View.VISIBLE);
                }



                l_farme.addView(photo);
                l_farme.addView(hexagon);
                l_farme.addView(textview);
                ll.addView(l_farme);
                iter = iter  + 1 ;
            }
            l_root.addView(ll);
        }
    }

    private void addImage() {

       // for (int i = 0; (i < AppConstans.IMAGEURLS.length && (i < photoViewList.size())); i++) {


        photoViewList.get(0).setVisibility(View.VISIBLE);
        photoViewList.get(0).setTag("sd");
        photoViewList.get(0).setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.gps_map));



        photoViewList.get(1).setVisibility(View.VISIBLE);
        photoViewList.get(1).setTag("sd");
        photoViewList.get(1).setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.rec_mony));


        photoViewList.get(2).setVisibility(View.VISIBLE);
        photoViewList.get(2).setTag("sd");
        photoViewList.get(2).setImageBitmap(BitmapFactory.decodeResource(this.getResources(),R.mipmap.survey_icon));


        photoViewList.get(25).setVisibility(View.VISIBLE);
        photoViewList.get(25).setTag("sd");
        photoViewList.get(25).setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.transferdata));

        photoViewList.get(26).setVisibility(View.VISIBLE);
        photoViewList.get(26).setTag("sd");
        photoViewList.get(26).setImageBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.bee1));

        /*for (int i = 0; i<photoViewList.size(); i++) {


            photoViewList.get(i).setVisibility(View.VISIBLE);
            photoViewList.get(i).setTag("sd");
            photoViewList.get(i).setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.han2));


        } // photoViewList.get(i).setOnClickListener(BeeHiveActivity.this);
*/


          // photoViewList.get(7).setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.invoice));
            /* photoViewList.get(1).setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.trans_from_store));
            photoViewList.get(2).setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.survey_icon));
            photoViewList.get(3).setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.purshase));
            photoViewList.get(4).setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.gallery));
            photoViewList.get(5).setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.acc));
            photoViewList.get(6).setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.gps));
            photoViewList.get(7).setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.checkbank));*/
            /*photoViewList.get(8).setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.setting));
            photoViewList.get(9).setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.updatetrans));
            photoViewList.get(10).setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.manbalance));
            photoViewList.get(11).setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.survey_icon));*/



            final int finalI = 1;

            /*imageLoader.loadImage(image, options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    photoViewList.get(finalI).setImageBitmap(bitmap);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
        }*/

    }


}
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);//Menu Resource, Menu
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/shijilkadambath"));
                startActivity(browserIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
  *//*  @Override
    public void onClick(View v) {
        String url=v.getTag().toString();
        if (!url.equalsIgnoreCase("-1")){
            Toast.makeText(BeeHiveActivity.this,url, Toast.LENGTH_SHORT).show();
        }
    }*//*


    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                        //.writeDebugLogs() // Remove for release app
                .build();
        //Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);

    }


    private void addImage() {

        for (int i = 0; (i < AppConstans.IMAGEURLS.length && (i < photoViewList.size())); i++) {
            String image = AppConstans.IMAGEURLS[i];
            photoViewList.get(i).setVisibility(View.VISIBLE);
            photoViewList.get(i).setTag(image);
            // photoViewList.get(i).setOnClickListener(BeeHiveActivity.this);

            final int finalI = i;
            imageLoader.loadImage(image, options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    photoViewList.get(finalI).setImageBitmap(bitmap);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
        }

    }


    private void addView(int width){

        int margin=-(width/8)/4;
        width =(width+(width/16))/4;

        photoViewList.clear();
        for (int j=0;j<=5;j++) {
            LinearLayout ll = new LinearLayout(BeeHiveActivity.this);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            //ll.setBackgroundColor(Color.BLUE);
            LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    width);

            int marginLeft=(j%2 ==0)?(-(width/2)+margin):margin;
            int marginTop=(j !=0)?(-(width/5)):0;


            llParams.setMargins(marginLeft, marginTop, 0,0 );
            ll.setLayoutParams(llParams);

            for (int i=0;i<=5;i++) {
                int possition=j*5+i +1;

                FrameLayout l_farme = new FrameLayout(BeeHiveActivity.this);
                FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(width, width);
                FrameLayout.LayoutParams frameParams2 = new FrameLayout.LayoutParams(width-(width/10), width-(width/10));
                frameParams2.gravity = Gravity.CENTER;
                l_farme.setLayoutParams(frameParams);

                ImageView hexagon = new ImageView(BeeHiveActivity.this);
                hexagon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

                hexagon.setLayoutParams(frameParams);

                CustomShapeImageView photo=new CustomShapeImageView(BeeHiveActivity.this,R.drawable.avatar,
                        CustomShapeImageView.Shape.SVG,R.raw.hex);
                photo.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                photo.setLayoutParams(frameParams2);

                hexagon.setImageResource(R.drawable.hexagon);
                photo.setVisibility(View.VISIBLE);
                l_farme.setVisibility(View.VISIBLE);
                hexagon.setVisibility(View.VISIBLE);
                if (Arrays.asList(INVISIBLE).contains(possition)){
                    l_farme.setVisibility(View.INVISIBLE);
                }else if (Arrays.asList(INEMPTY).contains(possition)){
                    photo.setVisibility(View.INVISIBLE);
                    l_farme.setVisibility(View.VISIBLE);
                }else if (Arrays.asList(NORMAL).contains(possition)){
                    l_farme.setVisibility(View.VISIBLE);
                    photo.setVisibility(View.INVISIBLE);
                    photo.setTag("-1");
                    photoViewList.add(photo);
                    //photo.setVisibility(View.VISIBLE);
                }

                l_farme.addView(photo);
                l_farme.addView(hexagon);
                ll.addView(l_farme);
            }
            l_root.addView(ll);
        }
    }

}
*/