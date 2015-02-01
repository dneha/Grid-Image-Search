package com.codepath.gridimagesearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by nehadike on 1/29/15.
 */
public class ImageResult implements Serializable {
    
    public String fullUrl;
    public String thumbUrl;
    public String title;

    public ImageResult (JSONObject jsonObj) {
        try {
           this.fullUrl = jsonObj.getString("url");
            this.thumbUrl = jsonObj.getString("tbUrl");
            this.title = jsonObj.getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
    }
    
    public static ArrayList<ImageResult> fromJSONArray (JSONArray jsonArray) {
        ArrayList<ImageResult> resultsList = new ArrayList<ImageResult>();
        for (int i=0; i<jsonArray.length(); i++) {
            try {
                resultsList.add(new ImageResult(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return resultsList;
    }
}
