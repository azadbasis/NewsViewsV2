package com.newsviewsv2;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.newsviewsv2.model.Article;
import com.newsviewsv2.utils.NetworkHelper;

import static com.newsviewsv2.adapter.DataItemAdapter.ITEM_KEY;

public class WebActivity extends AppCompatActivity {


    WebView webNews;
    private boolean networkok;
    private Article item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
         networkok=NetworkHelper.hasNetworkAccess(this);


        item = getIntent().getExtras().getParcelable(ITEM_KEY);
        if (item == null) {
            throw new AssertionError("Null data item received!");
        }

        if (networkok) {

            webNews = findViewById(R.id.webNews);
            webNews.setWebViewClient(new WebViewClient());

            webNews.getSettings().setJavaScriptEnabled(true);
            webNews.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });
            webNews.loadUrl(item.getUrl());

        } else {
            showSnackbar();
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webNews.canGoBack()) {
                        webNews.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }


    private  void showSnackbar(){
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.webLayout),  R.string.no_internet, Snackbar.LENGTH_LONG)
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
}