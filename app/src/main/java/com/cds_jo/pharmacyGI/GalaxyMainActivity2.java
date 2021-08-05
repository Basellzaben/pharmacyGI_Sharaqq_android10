package com.cds_jo.pharmacyGI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import hearder.main.Header_Frag;


public class GalaxyMainActivity2 extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galaxy_main2);
try {
    Fragment frag = new Header_Frag();
    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.Frag1, frag).commit();
}
     catch ( Exception ex){
    Toast.makeText(this,ex.getMessage().toString(),Toast.LENGTH_SHORT).show();
}
    }


    @Override
    public void onBackPressed() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String Login = sharedPreferences.getString("Login", "No");
        if (Login.toString().equals("No")) {
            Intent i = new Intent(this, NewLoginActivity.class);
            startActivity(i);
        }
        return;
    }

}
