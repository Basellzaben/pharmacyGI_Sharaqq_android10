package com.cds_jo.pharmacyGI;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cds_jo.pharmacyGI.assist.Cls_itms_Info;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Hp on 18/03/2016.
 */
public class Cls_ItemsImage_Adapter extends BaseAdapter{
    Context context;
    ArrayList<Cls_itms_Info> cls_itms_infos;


    public Cls_ItemsImage_Adapter(Context context, ArrayList<Cls_itms_Info> list) {

        this.context = context;
        cls_itms_infos = list;
    }@Override
     public int getCount() {

        return cls_itms_infos.size();
    }

    @Override
    public Object getItem(int position) {

        return cls_itms_infos.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }
    public long getPostion(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Cls_itms_Info cls_itms_info = cls_itms_infos.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_item_images, null);
        }

        Methdes.MyTextView tv_Name = (Methdes.MyTextView) convertView.findViewById(R.id.tv_Name);
        tv_Name.setText(cls_itms_info.getName());


        Methdes.MyTextView tv_No = (Methdes.MyTextView) convertView.findViewById(R.id.tv_No);
        tv_No.setText(cls_itms_info.getNo());

        ImageView img=(ImageView)convertView.findViewById(R.id.img);
        File imgFile =   cls_itms_info.getBitmapPath();

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                img.setImageBitmap(myBitmap);


        return convertView;
    }

}



