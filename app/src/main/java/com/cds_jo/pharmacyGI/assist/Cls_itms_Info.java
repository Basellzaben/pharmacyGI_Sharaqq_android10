package com.cds_jo.pharmacyGI.assist;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by Hp on 07/01/2017.
 */

public class Cls_itms_Info {
    public Bitmap getMyBitmap() {
        return myBitmap;
    }

    public void setMyBitmap(Bitmap myBitmap) {
        this.myBitmap = myBitmap;
    }

    Bitmap myBitmap ;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNo() {
        return No;
    }

    public void setNo(String no) {
        No = no;
    }

    String Name;
    String No ;

    public File getBitmapPath() {
        return BitmapPath;
    }

    public void setBitmapPath(File bitmapPath) {
        BitmapPath = bitmapPath;
    }

    File BitmapPath ;

    public File getVideoPath() {
        return VideoPath;
    }

    public void setVideoPath(File videoPath) {
        VideoPath = videoPath;
    }

    File VideoPath ;
}
