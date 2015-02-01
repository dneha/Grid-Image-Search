package com.codepath.gridimagesearch;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class SettingsActivity extends ActionBarActivity {

    private Spinner typeSpinner, sizeSpinner, colorSpinner;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sizeSpinner = (Spinner) findViewById(R.id.spinnerSize);
        ArrayAdapter<CharSequence> adapterSizeSpinner = ArrayAdapter.createFromResource(this,
                R.array.size_array, android.R.layout.simple_spinner_item);
        adapterSizeSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(adapterSizeSpinner);
        
        colorSpinner = (Spinner) findViewById(R.id.spinnerColor);
        ArrayAdapter<CharSequence> adapterColorSpinner = ArrayAdapter.createFromResource(this, 
                R.array.color_array, android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(adapterColorSpinner);
        
        typeSpinner = (Spinner) findViewById(R.id.spinnerImageType);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.type_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
  
        populateSettings();
    }

    public void populateSettings() {

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.setting_prefsFile),Context.MODE_PRIVATE);
        //Size
        int sizeIndex = sharedPref.getInt(getString(R.string.setting_size),0);
        sizeSpinner.setSelection(sizeIndex);
        //Color
        int colorIndex = sharedPref.getInt(getString(R.string.setting_color),0);
        colorSpinner.setSelection(colorIndex);
        //Type
        int typeIndex = sharedPref.getInt(getString(R.string.setting_type),0);
        typeSpinner.setSelection(typeIndex);
        //Site
        String siteValue = sharedPref.getString(getString(R.string.setting_site), "");
        EditText etSite = (EditText) findViewById(R.id.etSite);
        if (siteValue!=null && (siteValue.length()>0)) {
            etSite.setText(siteValue);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
    public void OnSaveClicked(View v) {
        //Save preferences here
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.setting_prefsFile),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        
        //Size
        int sizeIndex = sizeSpinner.getSelectedItemPosition();
        editor.putInt(getString(R.string.setting_size),sizeIndex);
        
        //Color
        int colorIndex = colorSpinner.getSelectedItemPosition();
        editor.putInt(getString(R.string.setting_color), colorIndex);
        
        //Type
        int typeIndex = typeSpinner.getSelectedItemPosition();
        editor.putInt(getString(R.string.setting_type), typeIndex);
        
        //Site
        EditText etSite = (EditText) findViewById(R.id.etSite);
        String siteString = etSite.getText().toString();
        if (siteString != null)
            editor.putString(getString(R.string.setting_site), siteString);
        editor.commit();
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        
        finish();
    }
}
