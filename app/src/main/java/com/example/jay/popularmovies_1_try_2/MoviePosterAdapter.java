package com.example.jay.popularmovies_1_try_2;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by jay on 18/03/2016.
 */
public class MoviePosterAdapter extends BaseAdapter {

    private final String LOG_TAG = getClass().getSimpleName();
    Context ctx = null;
    HashMap<Integer,String> hashMap = null;

    public MoviePosterAdapter(Context ctx, HashMap<Integer,String> hashMap){
        this.ctx = ctx;
        this.hashMap = hashMap;
    }

    @Override
    public int getCount() {
        return hashMap.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView poster;

        if(convertView == null){
            //if its not recycled, initialize some attributes
            poster = new ImageView(ctx);
            poster.setLayoutParams(new GridView.LayoutParams(485,885));
            poster.setScaleType(ImageView.ScaleType.CENTER_CROP);
            poster.setPadding(1,1,1,1);
        }else{
            poster = (ImageView) convertView;
        }

        Collection<String> values = hashMap.values();
        Collection<Integer> keys = hashMap.keySet();

        Picasso.with(ctx).load(Uri.parse("http://image.tmdb.org/t/p/w500/" + values.toArray()[position])).into(poster);
        Picasso.with(ctx).setLoggingEnabled(true);

        poster.setTag(keys.toArray()[position]);

        Log.d(LOG_TAG, "Position : " + position);
        Log.d(LOG_TAG, String.valueOf(poster.getTag()));
        Log.d(LOG_TAG, "http://image.tmdb.org/t/p/w500/" + values.toArray()[position]);

        return poster;
    }
}
