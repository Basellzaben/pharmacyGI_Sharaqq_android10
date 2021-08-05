package hearder.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;

import androidx.fragment.app.Fragment;

import com.cds_jo.pharmacyGI.ComInfo;
import com.cds_jo.pharmacyGI.DB;
import com.cds_jo.pharmacyGI.GalaxyLoginActivity;
import com.cds_jo.pharmacyGI.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Hp on 20/11/2016.
 */
public class Header_Frag extends Fragment {

    private ImageView Option, Img_Setting;
    private SimpleSideDrawer mNav;
    Methdes.MyTextView tv_CpmpanyName, tv_UserNm;
    private ArrayList<Main_List_Itme> myList_Setting;
    private ListView listView;

    Locale locale;

 /*   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog2);
    }
*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());

        View view = inflater.inflate(R.layout.header_frag, container, false);
        Initi(view);


        mNav = new SimpleSideDrawer(getActivity());


        mNav.setRightBehindContentView(R.layout.setting_list);


        if (ComInfo.UserType == 1) {
            FillData_Setting();
        } else {
            FillData_Setting2();
        }

        Option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShwoPop(v);
            }
        });
        tv_CpmpanyName = (Methdes.MyTextView) view.findViewById(R.id.tv_CpmpanyName);
        tv_UserNm = (Methdes.MyTextView) view.findViewById(R.id.tv_UserNm);

        tv_UserNm.setText(sharedPreferences.getString("UserName", "") + " / " + sharedPreferences.getString("TypeDesc", ""));

        tv_CpmpanyName.setText(DB.GetValue(this.getContext(), "ComanyInfo", "CompanyNm", "1=1"));

        Img_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mNav.toggleRightDrawer();
                NotificationFun();
            }
        });


        return view;
    }

    private void Initi_ar(View view) {

        Option = (ImageView) view.findViewById(R.id.imageView5);
        Img_Setting = (ImageView) view.findViewById(R.id.imageView6);
        mNav = new SimpleSideDrawer(getActivity());
        mNav.setRightBehindContentView(R.layout.setting_list);


    }

    private void Initi(View view) {

        Option = (ImageView) view.findViewById(R.id.imageView5);
        Img_Setting = (ImageView) view.findViewById(R.id.imageView6);


    }

    private void ShwoPop(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.inflate(R.menu.menu_pop_anim);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.item_0:
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        String Login = sharedPreferences.getString("Login", "No");
//                        if (Login.toString().equals("No")) {
                        Intent intent = new Intent(getActivity().getApplicationContext(), GalaxyLoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        getActivity().startActivity(intent);
//                        }
                }
                return true;
            }
        });
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////

    private void NotificationFun() {
        listView = (ListView) mNav.findViewById(R.id.listView11);
        listView.setAdapter(new Setting_List_Adapter(getActivity(), myList_Setting));

    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    private void FillData_Setting() {
        String[] arr = getResources().getStringArray(R.array.Main_Item);
        myList_Setting = new ArrayList<>();
        Main_List_Itme itme = new Main_List_Itme();
        itme.setTitle(arr[0]);
        itme.setImg(R.mipmap.see1);
        myList_Setting.add(itme);

        itme = new Main_List_Itme();
        itme.setTitle(arr[1]);
        itme.setImg(R.mipmap.see11);
        myList_Setting.add(itme);


        itme = new Main_List_Itme();
        itme.setTitle(arr[2]);
        itme.setImg(R.mipmap.see11);
        myList_Setting.add(itme);

        itme = new Main_List_Itme();
        itme.setTitle(arr[3]);
        itme.setImg(R.mipmap.see2);
        myList_Setting.add(itme);


        itme = new Main_List_Itme();
        itme.setTitle(arr[4]);
        itme.setImg(R.mipmap.see5);
        myList_Setting.add(itme);

        itme = new Main_List_Itme();
        itme.setTitle(arr[5]);
        itme.setImg(R.mipmap.see6);
        myList_Setting.add(itme);

        itme = new Main_List_Itme();
        itme.setTitle(arr[6]);
        itme.setImg(R.mipmap.see7);
        myList_Setting.add(itme);


        itme = new Main_List_Itme();
        itme.setTitle(arr[7]);
        itme.setImg(R.mipmap.see9);
        myList_Setting.add(itme);

        itme = new Main_List_Itme();
        itme.setTitle(arr[8]);
        itme.setImg(R.mipmap.see10);
        myList_Setting.add(itme);

        itme = new Main_List_Itme();
        itme.setTitle(arr[9]);
        itme.setImg(R.mipmap.see11);
        myList_Setting.add(itme);


        itme = new Main_List_Itme();
        itme.setTitle(arr[10]);
        itme.setImg(R.mipmap.see16);
        myList_Setting.add(itme);

        itme = new Main_List_Itme();
        itme.setTitle(arr[11]);
        itme.setImg(R.mipmap.see17);
        myList_Setting.add(itme);


      /*  itme=new Main_List_Itme();
        itme.setTitle(arr[18]);
        itme.setImg(R.mipmap.see0);
        myList_Setting.add(itme);*/

        itme = new Main_List_Itme();
        itme.setTitle(arr[12]);
        itme.setImg(R.mipmap.see9);
        myList_Setting.add(itme);

        itme = new Main_List_Itme();
        itme.setTitle(arr[13]);
        itme.setImg(R.mipmap.see3);
        myList_Setting.add(itme);


        itme = new Main_List_Itme();
        itme.setTitle(arr[14]);
        itme.setImg(R.mipmap.see3);
        myList_Setting.add(itme);


        itme = new Main_List_Itme();
        itme.setTitle(arr[15]);
        itme.setImg(R.mipmap.see18);
        myList_Setting.add(itme);

    }

    private void FillData_Setting2() {
        String[] arr = getResources().getStringArray(R.array.Main_Item2);
        myList_Setting = new ArrayList<>();

        Main_List_Itme itme = new Main_List_Itme();
        itme.setTitle(arr[0]);
        itme.setImg(R.mipmap.see3);
        myList_Setting.add(itme);

        itme = new Main_List_Itme();
        itme.setTitle(arr[1]);
        itme.setImg(R.mipmap.see3);
        myList_Setting.add(itme);


        itme = new Main_List_Itme();
        itme.setTitle(arr[2]);
        itme.setImg(R.mipmap.see17);
        myList_Setting.add(itme);



        itme = new Main_List_Itme();
        itme.setTitle(arr[3]);
        itme.setImg(R.mipmap.see18);
        myList_Setting.add(itme);

    }
}
