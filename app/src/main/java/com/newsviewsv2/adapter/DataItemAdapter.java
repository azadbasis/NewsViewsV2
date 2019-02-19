package com.newsviewsv2.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.newsviewsv2.PopActivity;
import com.newsviewsv2.R;
import com.newsviewsv2.model.Article;
import com.newsviewsv2.utils.Operation;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DataItemAdapter extends RecyclerView.Adapter<DataItemAdapter.ViewHolder> {

    public static final String ITEM_ID_KEY = "item_id_key";
    public static final String ITEM_KEY = "item_key";
    private List<Article> mItems;
    private Context mContext;
    private SharedPreferences.OnSharedPreferenceChangeListener prefsListener;

    public DataItemAdapter(Context context, List<Article> items) {
        this.mContext = context;
        this.mItems = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext);
        prefsListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Log.i("preferences", "onSharedPreferenceChanges " + key);
            }
        };
        settings.registerOnSharedPreferenceChangeListener(prefsListener);
        boolean grid = settings.getBoolean(mContext.getString(R.string.pref_display_grid), false);

        int layoutId = grid ? R.layout.grid_item : R.layout.list_item;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Article item = mItems.get(position);

        String title=item.getTitle();
        holder.titleText.setText(title);


        String publishAt=item.getPublishedAt();
        long milliseconds=Operation.getTimeDifferenceInMillis(publishAt);
        String publishNews=Operation.getDurationBreakdown(milliseconds);
        holder.publishText.setText(publishNews);

        String urlToImage = item.getUrlToImage();
        Picasso.get().setIndicatorsEnabled(true);
        try {
            Picasso.get()
                    .load(urlToImage)
                    .resize(Operation.width / 2, Operation.height / 2)
                    .into(holder.imageView);

        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(mContext, PopActivity.class);
                //intent.putExtra(ITEM_ID_KEY,item.getItemId());
                intent.putExtra(ITEM_KEY, item);
                mContext.startActivity(intent);

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView titleText,publishText;
        public ImageView imageView;
        public View mView;

        public ViewHolder(View itemView) {
            super(itemView);

            titleText = (TextView) itemView.findViewById(R.id.titleText);
            publishText = (TextView) itemView.findViewById(R.id.publishText);
            imageView = (ImageView) itemView.findViewById(R.id.sentimentImage);

            mView = itemView;
        }
    }



}