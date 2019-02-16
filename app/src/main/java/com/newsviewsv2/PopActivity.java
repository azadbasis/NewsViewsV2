package com.newsviewsv2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;

import com.newsviewsv2.adapter.DataItemAdapter;
import com.newsviewsv2.model.Article;
import com.squareup.picasso.Picasso;

public class PopActivity extends AppCompatActivity {

    private ImageView thumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);

        Article item = getIntent().getExtras().getParcelable(DataItemAdapter.ITEM_KEY);
        if (item == null) {
            throw new AssertionError("Null data item received!");
        }

        thumbnail=(ImageView)findViewById(R.id.thumbnail);
        String url = item.getUrlToImage();
        Picasso.get()
                .load(url)
                .into(thumbnail);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .6), (int) (height * .5));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity= Gravity.CENTER;
        params.x = 0;
        params.y =-20;
        getWindow().setAttributes(params);

    }
}
