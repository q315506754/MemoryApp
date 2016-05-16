package com.jiangli.memorytravel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jiangli.memorytravel.core.MemoryAnalyseResult;
import com.jiangli.memorytravel.core.MemoryResult;
import com.jiangli.memorytravel.core.MemoryTest;

import java.util.List;


public class ResultActivity extends Activity {
    public static final String TAG = ResultActivity.class.getSimpleName();
    private ListView listView;


    class DataApdapter  extends CursorAdapter {
        public DataApdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            // Choose the layout type
            int layoutId =R.layout.list_item_result;

            View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        final Context context = ResultActivity.this;
        Intent intent = getIntent();

        final List<MemoryResult> results = (List<MemoryResult>) intent.getSerializableExtra("results");
        Log.d(TAG, results.toString());
        Log.d(TAG, results.size() + "");

        final List<MemoryAnalyseResult> analyse = MemoryTest.analyse(results);


        DataApdapter apdapter = new DataApdapter(this, null, 0);
    // Get a reference to the ListView, and attach this adapter to it.
        listView = (ListView) findViewById(R.id.listview_results);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return analyse.size();
            }

            @Override
            public Object getItem(int position) {
                return analyse.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                MemoryAnalyseResult item = (MemoryAnalyseResult)getItem(position);
                Log.d(TAG,item.toString());
//                Log.d(TAG,convertView.toString());
//                Log.d(TAG,parent.toString());
                View view = LayoutInflater.from(context).inflate(R.layout.list_item_result, parent, false);

                setTVVal(view, R.id.list_item_num, item.getData());
                setTVVal(view, R.id.list_item_ts, item.getCostime());
                setTVVal(view, R.id.list_item_ts_unit, "ms");
                return view;
            }

            private void setTVVal(View view, int list_item_num, Object data) {
                TextView viewById = (TextView) view.findViewById(list_item_num);
                viewById.setText(String.valueOf(data));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
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

    public void finishActivity(View view) {
        finish();
    }
}
