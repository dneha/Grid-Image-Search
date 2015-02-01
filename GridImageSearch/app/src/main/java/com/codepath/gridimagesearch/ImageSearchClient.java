package com.codepath.gridimagesearch;

import android.content.Context;
import android.content.SharedPreferences;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by nehadike on 1/29/15.
 */

public class ImageSearchClient {

    private final String API_BASE_URL = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=";
    private AsyncHttpClient client;
    
    public ImageSearchClient () {
        this.client = new AsyncHttpClient();
    }
    
    public void searchImages(String query, JsonHttpResponseHandler handler) {
        String url = API_BASE_URL + query + "&rsz="+ String.valueOf(MainActivity.BATCH_SIZE);
        client.get(url, handler);
    }
}
