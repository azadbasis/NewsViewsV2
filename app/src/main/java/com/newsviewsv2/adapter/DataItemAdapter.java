package com.newsviewsv2.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.newsviewsv2.DetailActivity;
import com.newsviewsv2.PopActivity;
import com.newsviewsv2.R;
import com.newsviewsv2.model.Article;
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

        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        try {
            holder.tvName.setText(item.getTitle());
            String url = item.getUrlToImage();
            Picasso.get()
                    .load(url)
                    .resize(width / 2, height / 2)
                    .into(holder.imageView);

        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(mContext, "item selected " + item.getItemName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, DetailActivity.class);
                //intent.putExtra(ITEM_ID_KEY,item.getItemId());
                intent.putExtra(ITEM_KEY, item);
                mContext.startActivity(intent);

                Toast.makeText(mContext, "touch", Toast.LENGTH_SHORT).show();
            }
        });
        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(mContext, PopActivity.class);
                //intent.putExtra(ITEM_ID_KEY,item.getItemId());
                intent.putExtra(ITEM_KEY, item);
                mContext.startActivity(intent);

                Toast.makeText(mContext, "item onlong click " + item.getAuthor(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public ImageView imageView;
        public View mView;

        public ViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.itemNameText);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);

            mView = itemView;
        }
    }
}