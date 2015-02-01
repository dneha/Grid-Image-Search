package com.codepath.gridimagesearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Debug;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    public static int BATCH_SIZE = 8;
    public static int MAX_PAGES = 8;
    private EditText etQuery;
    private GridView gvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;
    int startIndex = 0;
    String query;
    String queryPreferences;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
        query = "";
        queryPreferences = "";
        //data source
        imageResults = new ArrayList<ImageResult>();
        //link data source to adapter
        aImageResults = new ImageResultsAdapter(this, imageResults);
        // link adpter to view
        gvResults.setAdapter(aImageResults);

        // Attach the listener to the AdapterView onCreate
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });
    }
    
    @Override
    protected void onResume() {
        super.onResume();

        queryPreferences = "";
        updateQueryPreferences();

    }

    // Append more data into the adapter
    public void customLoadMoreDataFromApi(int offset) {
        // This method probably sends out a network request and appends new data items to your adapter. 
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter
        if (startIndex< MAX_PAGES*BATCH_SIZE) {
            getSearchResults();
        }
    }
    
    public void setupViews() {
        etQuery = (EditText)findViewById(R.id.etSearch);
        gvResults = (GridView)findViewById(R.id.gvResults);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Create Intent
                Intent i = new Intent(MainActivity.this, ImageDetailActivity.class);
                //Get Result to display
                ImageResult result = imageResults.get(position);
                //Pass result to intent
                i.putExtra("result", result);
                //launch new Activity
                startActivity(i);
            }
        });
    }
        
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
    public void onImageSearchClicked(View v) {
        
        //clear for new search
        imageResults.clear();
        startIndex = 0;
        query = etQuery.getText().toString();
        query = query+queryPreferences;
        //Toast.makeText(this, queryPreferences, Toast.LENGTH_SHORT).show();
        getSearchResults();
    }
    
    private void updateQueryPreferences() {
        //Read preferences and append to query
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.setting_prefsFile),Context.MODE_PRIVATE);
        //Site
        String siteValue = sharedPref.getString(getString(R.string.setting_site), "");
        if ((siteValue != null) && (siteValue.length() > 0)) {
            queryPreferences = "&as_sitesearch="+ siteValue;
        }
        //Type
        int typeIndex = sharedPref.getInt(getString(R.string.setting_type),0);
        if (typeIndex>0){
            String[] types = getResources().getStringArray(R.array.type_array);
            String typeStr = types[typeIndex];
            queryPreferences = queryPreferences+"&imgtype="+typeStr;
        }
        //Size
        int sizeIndex = sharedPref.getInt(getString(R.string.setting_size),0);
        if (sizeIndex>0) {
            String[] sizes = getResources().getStringArray(R.array.size_array);
            String sizeStr = sizes[sizeIndex];
            queryPreferences = queryPreferences+"&imgsz="+sizeStr;
        }
        //Color
        int colorIndex = sharedPref.getInt(getString(R.string.setting_color),0);
        if (colorIndex>0){
            String[] colors = getResources().getStringArray(R.array.color_array);
            String color = colors[colorIndex];
            queryPreferences = queryPreferences+"&imgcolor="+color;
        }

    }

    private void getSearchResults() {
        
        String queryWithIndex = query + "&start=" + String.valueOf(startIndex);
        ImageSearchClient client = new ImageSearchClient();
        client.searchImages(query, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int code, JSONObject response) {
                JSONArray items = null;
                try {
                    // Get the json array
                    items = response.getJSONObject("responseData").getJSONArray("results");
                    
                    //imageResults.addAll(ImageResult.fromJSONArray(items));
                    //aImageResults.notifyDataSetChanged();
                    aImageResults.addAll(ImageResult.fromJSONArray(items));
                    // adding to adapter adds to underlying data source and triggers the notify
                    //Debug.d("info",imageResults);
                    startIndex = startIndex+BATCH_SIZE;
                } catch (JSONException e) {
                     e.printStackTrace();
                }

            }
        });
    }
}
