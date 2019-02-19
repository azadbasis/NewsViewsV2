package com.newsviewsv2;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.newsviewsv2.adapter.DataItemAdapter;
import com.newsviewsv2.model.Article;
import com.newsviewsv2.services.MyService;
import com.newsviewsv2.utils.App;
import com.newsviewsv2.utils.NetworkHelper;
import com.newsviewsv2.utils.Operation;
import com.newsviewsv2.utils.Query;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static com.newsviewsv2.SigninActivity.USER_ID_KEY;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    private static final int SIGNIN_REQUEST = 1001;
    public static final String MY_GLOBAL_PREFS = "my_global_prefs";


    private List<Article> mItemList;
    private RecyclerView mRecyclerView;
    private DataItemAdapter mItemAdapter;
    private boolean networkOk;
    private SwipeRefreshLayout swipeContainer;
    private String setQueryHint;
    private SearchView searchView;
    private String userId;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            mItemList = intent.getParcelableArrayListExtra(MyService.MY_SERVICE_PAYLOAD);
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
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        networkOk = NetworkHelper.hasNetworkAccess(this);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        // drawer.addDrawerListener(toggle);
        //toggle.syncState();



        SharedPreferences prefs =
                getSharedPreferences(MainActivity.MY_GLOBAL_PREFS, MODE_PRIVATE);
        String userIdSp = prefs.getString(USER_ID_KEY, "");
        if (!TextUtils.isEmpty(userIdSp)) {

            Bitmap bitmap = Operation.getFacebookProfilePicture(userIdSp);
            Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap, 100, 90, false);
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmapResized);
            roundedBitmapDrawable.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()) / 2.0f);
            roundedBitmapDrawable.setCircular(true);
            toggle.setHomeAsUpIndicator(roundedBitmapDrawable);
        }else {
              toggle.setHomeAsUpIndicator(R.drawable.ic_sentiment_satisfied_black_24dp);

        }

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
            showSnackbar();
        }

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(MyService.MY_SERVICE_MESSAGE));

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        Spinner spinner = (Spinner) findViewById(R.id.spinner_toolBar);


        // set Spinner Adapter


        final ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.drawer_drop_down, android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinnerConfig(spinner);

    }

    private void spinnerConfig(Spinner spinner) {

        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                        Query whichView = Query.values()[pos];
                        App.query = whichView;

                        switch (whichView) {
                            case MATH_HINT:
                                setQueryHint = getString(R.string.math_hint);
                                searchView.setQueryHint(setQueryHint);
                                break;

                            case TRIVIA_HINT:
                                setQueryHint = getString(R.string.math_hint);
                                searchView.setQueryHint(setQueryHint);
                                break;

                            case DATE_HINT:
                                setQueryHint = getString(R.string.date_hint);
                                searchView.setQueryHint(setQueryHint);
                                break;

                            case RANDOM_MATH_HINT:
                                setQueryHint = getString(R.string.math_hint);
                                searchView.setQueryHint(setQueryHint);
                                break;
                            case RANDOM_TRIVIA_HINT:
                                setQueryHint = getString(R.string.math_hint);
                                searchView.setQueryHint(setQueryHint);
                                break;

                            case RANDOM_DATE_HINT:
                                setQueryHint = getString(R.string.math_hint);
                                searchView.setQueryHint(setQueryHint);
                                break;
                            case RANDOM_YEAR_HINT:
                                setQueryHint = getString(R.string.math_hint);
                                searchView.setQueryHint(setQueryHint);
                                break;

                            default:
                                setQueryHint = getString(R.string.search_hint);
                                searchView.setQueryHint(setQueryHint);
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
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
            swipeContainer.setRefreshing(false);
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

        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {

            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, PrefsActivity.class);
                startActivity(settingsIntent);
                return true;

            case R.id.action_signin:
                Intent intent = new Intent(this, SigninActivity.class);
                startActivityForResult(intent, SIGNIN_REQUEST);
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


    @Override
    public void onRefresh() {

        if (networkOk) {
            Intent intent = new Intent(this, MyService.class);
            startService(intent);
        } else {
            showSnackbar();
        }
    }

    private void showSnackbar() {
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.drawer_layout), R.string.no_internet, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });

// Changing message text color
        snackbar.setActionTextColor(Color.RED);

// Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == SIGNIN_REQUEST) {
            userId = data.getStringExtra(USER_ID_KEY);
            Toast.makeText(this, "You signed in as " + userId, Toast.LENGTH_SHORT).show();

            SharedPreferences.Editor editor =
                    getSharedPreferences(MY_GLOBAL_PREFS, MODE_PRIVATE).edit();
            editor.putString(USER_ID_KEY, userId);
            editor.apply();

        }

    }
}
