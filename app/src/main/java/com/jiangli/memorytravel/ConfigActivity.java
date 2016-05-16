package com.jiangli.memorytravel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Arrays;


public class ConfigActivity extends ActionBarActivity {

    public static final String TAG = ConfigActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

       final Context context = ConfigActivity.this;
        loadPageByPref(context);


        ((Button)findViewById(R.id.startMemoryBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "startMemoryBtn...");
                Intent intent = new Intent(context, ProcessActivity.class);
                intent.putExtra("fromNumber", getIntValue(R.id.fromNumber));
                intent.putExtra("toNumber", getIntValue(R.id.toNumber));
                intent.putExtra("arraySize", getIntValue(R.id.arraySize));

                String lable = ((Spinner) findViewById(R.id.modeChoose)).getSelectedItem().toString();
                String[] stringArray = getResources().getStringArray(R.array.mode_options_values);
                String[] optionsArray = getResources().getStringArray(R.array.mode_options);
                int i = Arrays.binarySearch(optionsArray, lable);
                String value = stringArray[i];
                intent.putExtra("modeChoose", value);

                startActivity(intent);
            }

            private int getIntValue(int fromNumber) {
                return Integer.parseInt( ((EditText) findViewById(fromNumber)).getText().toString());
            }
        });
    }

    private void loadPageByPref(Context context) {
        setEditTestByPrefer(context, R.string.pref_fromNumber_key, R.string.pref_fromNumber_default, R.id.fromNumber);
        setEditTestByPrefer(context, R.string.pref_toNumber_key, R.string.pref_toNumber_default, R.id.toNumber);
        setEditTestByPrefer(context, R.string.pref_arraySize_key, R.string.pref_arraySize_default, R.id.arraySize);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String pref_fromNumber_value = prefs.getString(context.getString(R.string.pref_modeChoose_key),
                context.getString(R.string.pref_modeChoose_default));

        String[] stringArray = getResources().getStringArray(R.array.mode_options_values);
        int i = Arrays.binarySearch(stringArray, pref_fromNumber_value);
        ((Spinner)findViewById( R.id.modeChoose)).setSelection(i);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final Context context = ConfigActivity.this;
        loadPageByPref(context);
    }
    private void setEditTestByPrefer(Context context, int pref_key, int pref_default, int edit_id) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String pref_fromNumber_value = prefs.getString(context.getString(pref_key),
                context.getString(pref_default));
        ((EditText)findViewById(edit_id)).setText(pref_fromNumber_value);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.config_menu_config, menu);
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
            Log.d(TAG, "action_settings...");
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
