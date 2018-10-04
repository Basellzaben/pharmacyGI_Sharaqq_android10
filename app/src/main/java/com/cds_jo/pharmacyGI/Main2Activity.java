package com.cds_jo.pharmacyGI;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cds_jo.pharmacyGI.assist.Acc_ReportActivity;
import com.cds_jo.pharmacyGI.assist.CustomerReturnQtyActivity;
import com.cds_jo.pharmacyGI.assist.OrdersItems;
import com.cds_jo.pharmacyGI.assist.Sale_InvoiceActivity;

import port.bluetooth.BluetoothConnectMenu;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Galaxy Sales Force");

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with ymour own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
          //  Intent ji = new Intent(this, LoginActivity.class);

            //startActivity(ji);
           // super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Tracker) {
            Intent k = new Intent(this, MainActivity.class);
            startActivity(k);
            // Handle the camera action
        } else if (id == R.id.UPdate) {
            Intent k = new Intent(this, UpdateDataToMobileActivity.class);
            startActivity(k);

        } else if (id == R.id.Sched) {
            Intent k = new Intent(this, ScheduleManActivity.class);
            startActivity(k);

        } else if (id == R.id.Rec) {
            Intent k = new Intent(this, RecvVoucherActivity.class);
            startActivity(k);

        } else if (id == R.id.Setting) {
            Intent k = new Intent(this, BluetoothConnectMenu.class);
            startActivity(k);

        } else if (id == R.id.nav_Logout) {
            Intent k = new Intent(this, NewLoginActivity.class);
            startActivity(k);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void btn_CashReport(View view) {
        Intent k = new Intent(this, Acc_ReportActivity.class);
        startActivity(k);
    }
    public void btn_Invoice(View view) {
        Intent k = new Intent(this, Sale_InvoiceActivity.class);
        startActivity(k);
    }
    public void btn_po(View view) {
        Intent k = new Intent(this, OrdersItems.class);
        startActivity(k);
    }
     public void btn_RetInv(View view) {
        Intent k = new Intent(this, CustomerReturnQtyActivity.class);
        startActivity(k);
    }
    public void btn_ItemCost(View view) {
        Intent k = new Intent(this, ItemCostActivity.class);
        startActivity(k);
    }
    public void btn_Quat(View view) {
        Intent k = new Intent(this, QuestneerActivity.class);
        startActivity(k);
    }

    public void btn_Summery(View view) {
        Intent k = new Intent(this, ManSummeryActivity.class);
        startActivity(k);
    }
}
