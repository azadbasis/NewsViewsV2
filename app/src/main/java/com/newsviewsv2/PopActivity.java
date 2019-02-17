package com.newsviewsv2;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.newsviewsv2.adapter.DataItemAdapter;
import com.newsviewsv2.model.Article;
import com.newsviewsv2.utils.NetworkHelper;
import com.newsviewsv2.utils.Operation;
import com.squareup.picasso.Picasso;

import static com.newsviewsv2.adapter.DataItemAdapter.ITEM_KEY;

public class PopActivity extends AppCompatActivity {

    private ImageView thumbnail;
    private TextView title;
    private String url;
    private boolean networkok;
    Article item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);

        networkok= NetworkHelper.hasNetworkAccess(this);
        item = getIntent().getExtras().getParcelable(ITEM_KEY);
        if (item == null) {
            throw new AssertionError("Null data item received!");
        }

        url = item.getUrl();

        thumbnail=(ImageView)findViewById(R.id.thumbnail);
        String urlToImage = item.getUrlToImage();
        Picasso.get()
                .load(urlToImage)
                .into(thumbnail);
        title=(TextView)findViewById(R.id.title);
        title.setText(item.getTitle());

        popUpWindow();

    }

    private void popUpWindow() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .5));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity= Gravity.CENTER;
        params.x = 0;
        params.y =-20;
        getWindow().setAttributes(params);
    }

    public void goBrowser(View view) {
        if(networkok){
            if (!url.startsWith("http://") && !url.startsWith("https://"))
                url = "http://" + url;
            try {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(myIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No application can handle this request."
                        + " Please install a webbrowser",  Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }else{
            Toast.makeText(this, "No internet connection!", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    public void goWeb(View view) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(ITEM_KEY, item);
        startActivity(intent);

        finish();
    }


}
