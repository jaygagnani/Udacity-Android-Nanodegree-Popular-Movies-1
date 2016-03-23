package com.example.jay.popularmovies_1_try_2;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviePostersFragment extends Fragment {

    private final String LOG_TAG = getClass().getSimpleName();

    public MoviePostersFragment() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.movie_posters_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.sort_order){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.movie_posters_fragment, container, false);

        Toast.makeText(getContext(),"fragment open",Toast.LENGTH_LONG).show();

        GridView gridView = (GridView) rootView.findViewById(R.id.gridView);

        MoviePosterAdapter adapter = null;

        String sort_order = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_default_value));
        //Toast.makeText(getContext(), sort_order, Toast.LENGTH_SHORT).show();

        AsyncTask<String, Void, HashMap> map = new FetchMoviePosters().execute(sort_order);
        Log.d(LOG_TAG+" , map : ", map.toString());

        try {
            HashMap<Integer,String> hashMap = map.get();
            Log.d(LOG_TAG, hashMap.toString());

            adapter = new MoviePosterAdapter(getContext(), hashMap);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, String.valueOf(view.getTag()));
                Intent intent = new Intent(getContext(),MovieDetailsActivity.class);
                intent.putExtra("id", String.valueOf(view.getTag()));

                startActivity(intent);
            }
        });

        //Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();

        return rootView;
    }

    class FetchMoviePosters extends AsyncTask<String, Void, HashMap> {

        private final String LOG_TAG = FetchMoviePosters.class.getSimpleName();
        private final String API_KEY = "b4f671eb8c6786d9b16338a9de5e6898";

        private Context context;

        @Override
        protected HashMap<Integer,String> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            //String[][] arr = new String[5][5];

            String movieJsonStr = null;

            try{
                final String BASE_URL = "http://api.themoviedb.org/3/movie/"+params[0]+"?api_key="+API_KEY;

//                final String API_KEY_PARAM = "api_key";
//                final String SORT_BY_PARAM = "sort_by";

//                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
//                        .appendQueryParameter(API_KEY_PARAM,API_KEY)
//                        .appendQueryParameter(SORT_BY_PARAM,params[0])
//                        .build();

                URL url = new URL(BASE_URL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                if(inputStream == null){
                    Log.d(LOG_TAG,"input stream empty");
                    return null;
                }

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    stringBuffer.append(line + "\n");
                }

                if(stringBuffer.length() == 0){
                    Log.d(LOG_TAG,"string buffer empty");
                    return null;
                }

                movieJsonStr = stringBuffer.toString();
                Log.d(LOG_TAG,movieJsonStr);

            }
            catch (Exception e){
                Log.d(LOG_TAG,e.getMessage());
                e.printStackTrace();
            }

            try {
                return (new ParseJson(movieJsonStr).getData());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}
