package com.newsviewsv2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.newsviewsv2.adapter.DataItemAdapter;
import com.newsviewsv2.model.Article;
import com.newsviewsv2.services.MyService;
import com.newsviewsv2.utils.NetworkHelper;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private List<Article> mItemList;
    private RecyclerView mRecyclerView;
    private DataItemAdapter mItemAdapter;
    private boolean networkOk;
    private SwipeRefreshLayout swipeContainer;



    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            mItemList =intent.getParcelableArrayListExtra(MyService.MY_SERVICE_PAYLOAD);
            Toast.makeText(MainActivity.this,
                    "Received " + mItemList.size() + " items from service",
                    Toast.LENGTH_SHORT).show();
            displayData();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        networkOk = NetworkHelper.hasNetworkAccess(this);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        boolean grid = settings.getBoolean(getString(R.string.pref_display_grid), false);

        mRecyclerView = (RecyclerView) findViewById(R.id.rvItems);
        if (grid) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }


        if (networkOk) {
            Intent intent = new Intent(this, MyService.class);
            startService(intent);
        } else {
            Toast.makeText(this, "Network not available", Toast.LENGTH_SHORT).show();
        }

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(MyService.MY_SERVICE_MESSAGE));

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code here
                Toast.makeText(getApplicationContext(), "Works!", Toast.LENGTH_LONG).show();
                if (networkOk) {
                    Intent intent = new Intent(MainActivity.this, MyService.class);
                    startService(intent);
                    mItemAdapter.clear();
                    mItemAdapter.addAll(mItemList);
                    Toast.makeText(MainActivity.this, "Network  available", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, "Network not available", Toast.LENGTH_SHORT).show();
                }
                // To keep animation for 4 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        // Stop animation (This will be after 3 seconds)

                        swipeContainer.setRefreshing(false);
                        Toast.makeText(MainActivity.this, "STOP", Toast.LENGTH_SHORT).show();
                    }
                }, 4000); // Delay in millis

            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private void displayData() {
        if (mItemList != null) {
            mItemAdapter = new DataItemAdapter(this, mItemList);
            mRecyclerView.setAdapter(mItemAdapter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            // Show the settings screen
            Intent settingsIntent = new Intent(this, PrefsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
