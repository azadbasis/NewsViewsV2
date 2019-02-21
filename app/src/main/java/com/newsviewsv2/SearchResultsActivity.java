package com.newsviewsv2;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.newsviewsv2.services.MyService;
import com.newsviewsv2.services.MyWebService;
import com.newsviewsv2.utils.App;
import com.newsviewsv2.utils.NetworkHelper;
import com.newsviewsv2.utils.Operation;
import com.newsviewsv2.utils.PrefManager;
import com.newsviewsv2.utils.Query;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchResultsActivity extends AppCompatActivity {


    private boolean networkOk;
    private SearchView searchView;
    private String setQueryHint;
    private TextView messageText;
    private ImageView sentimentImage,visibilityImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setStatusBarColor(R.color.colorPrimaryDark);

        messageText=(TextView)findViewById(R.id.messageText);
        sentimentImage=(ImageView)findViewById(R.id.sentimentImage);
        visibilityImage=(ImageView)findViewById(R.id.visibilityImage);


        networkOk = NetworkHelper.hasNetworkAccess(this);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_toolBar);
        final ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.drawer_drop_down, android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinnerConfig(spinner);

        PrefManager prefManager = new PrefManager(getApplicationContext());
        prefManager.setSecondTimeLaunch(false);

        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(this, MainActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (networkOk) {
                requestData(query);

            } else {
                showSnackbar(getString(R.string.no_internet));
                messageText.setText(getString(R.string.no_internet));
                sentimentImage.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                visibilityImage.setImageResource(R.drawable.ic_visibility_off_black_24dp);
            }
        }
    }

    private void requestData(String query) {
        MyWebService webService =
                MyWebService.retrofitForNumber.create(MyWebService.class);

        switch (App.query) {
            case MATH_HINT:
                if (Operation.isNumeric(query)) {
                    Call<String> call = webService.math(query);
                    sendRequest(call);
                    break;
                }
            case TRIVIA_HINT:
                if(Operation.isNumeric(query)) {
                    Call<String> callTrivia = webService.trivia(query);
                    sendRequest(callTrivia);
                    break;
                }
            case DATE_HINT:
                if (Operation.isDate(query)) {
                    String[] parts = query.split("/");
                    String part1 = parts[0]; // 4
                    String part2 = parts[1]; // 6
                    Call<String> callDate = webService.date(part1, part2);
                    sendRequest(callDate);

                    break;
                }
            case RANDOM_MATH_HINT:
                if(Operation.isNumeric(query)) {
                    Call<String> callRndmMath = webService.randomMath(query);
                    sendRequest(callRndmMath);
                    break;
                }
            case RANDOM_TRIVIA_HINT:
                if(Operation.isNumeric(query)) {
                    Call<String> callRndmTrivia = webService.randomTrivia(query);
                    sendRequest(callRndmTrivia);
                    break;
                }

            case RANDOM_DATE_HINT:
                if(Operation.isNumeric(query)) {
                    Call<String> callRndmDate = webService.randomDate(query);
                    sendRequest(callRndmDate);
                    break;
                }
            case RANDOM_YEAR_HINT:
                if(Operation.isNumeric(query)) {
                    Call<String> callRndmYear = webService.randomYear(query);
                    sendRequest(callRndmYear);
                    break;
                }
            default:

                showSnackbar(getString(R.string.right_format));
                messageText.setText(getString(R.string.right_format));
                sentimentImage.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                visibilityImage.setImageResource(R.drawable.ic_visibility_off_black_24dp);

        }


    }

    private void sendRequest(Call<String> call) {
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String message = response.body();
                messageText.setText(message);
               /* Toast.makeText(SearchResultsActivity.this,
                        "Received:  " + "\n" + dataItems,
                        Toast.LENGTH_SHORT).show();*/
                //  mItemList = Arrays.asList(dataItems);
                //displayData();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.search_layout), message, Snackbar.LENGTH_LONG)
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

    @SuppressLint("NewApi")
    public void setStatusBarColor(int color) {
        Window window = SearchResultsActivity.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(SearchResultsActivity.this, color));

    }

}
