package com.codepath.gridimagesearch;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by nehadike on 1/29/15.
 */
public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {
    
    public ImageResultsAdapter(Context context, List<ImageResult> images) {
        super(context, R.layout.item_image_result, images);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        ImageResult image = getItem(position);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_image_result, parent, false);
        }
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvImage);
        ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);

        tvTitle.setText(Html.fromHtml(image.title));
        ivImage.setImageResource(0);
        Picasso.with(getContext()).load(image.thumbUrl).into(ivImage);
        return convertView;
   
    }
}
