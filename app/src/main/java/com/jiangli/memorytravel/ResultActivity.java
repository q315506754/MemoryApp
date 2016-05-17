package com.jiangli.memorytravel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jiangli.memorytravel.core.IdxComparator;
import com.jiangli.memorytravel.core.MaxComparator;
import com.jiangli.memorytravel.core.MemoryAnalyseResult;
import com.jiangli.memorytravel.core.MemoryResult;
import com.jiangli.memorytravel.core.MemoryTest;

import java.util.Collections;
import java.util.List;


public class ResultActivity extends Activity {
    public static final String TAG = ResultActivity.class.getSimpleName();
    private ListView listView;
    private static final int sortMode_by_costtime=1;
    private static final int sortMode_by_idx=2;

    private int sortMode=sortMode_by_costtime;
    private Button orderByTsBtn;
    private Button orderByIdxBtn;
    private List<MemoryAnalyseResult> analyse;
    private BaseAdapter baseAdapter;


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
        int totalCounts = intent.getIntExtra("totalCounts", -1);

        final List<MemoryResult> results = (List<MemoryResult>) intent.getSerializableExtra("results");
        Log.d(TAG, results.toString());
        Log.d(TAG, results.size() + "");

        analyse = MemoryTest.analyse(results);

        String result_summary1 = "";
        String result_summary2 = "";
        long maximumCostTime = 0;
        long avgCostTime = 0;
        if (analyse.size()>0) {
            MemoryAnalyseResult maxTsRs = analyse.get(0);
             maximumCostTime = maxTsRs.getMaximumCostTime();
            MemoryAnalyseResult minTsRs = analyse.get(analyse.size()-1);
            long sumTs = 0l;
            for (MemoryAnalyseResult one : analyse) {
                for (Long lOne : one.getCostime()) {
                    sumTs = sumTs + lOne;
                }
            }
            avgCostTime = sumTs/ analyse.size();

            long t_sumTs = sumTs/1000;
            long seconds = t_sumTs%60;
            t_sumTs/=60;


            result_summary1 = context.getString(
                    R.string.format_result_summary1,
                    analyse.size()+"/"+totalCounts,
                    sumTs, t_sumTs+"分"+seconds+"秒");
             result_summary2 = context.getString(
                    R.string.format_result_summary2,
                     minTsRs.getCostime().get(0),
                     maxTsRs.getCostime().get(0),avgCostTime);

        }


        ((TextView)findViewById(R.id.result_summary1)).setText(result_summary1);
        ((TextView)findViewById(R.id.result_summary2)).setText(result_summary2);

        orderByTsBtn = (Button) findViewById(R.id.orderByTsBtn);
        orderByIdxBtn = (Button) findViewById(R.id.orderByIdxBtn);

//        DataApdapter apdapter = new DataApdapter(this, null, 0);
    // Get a reference to the ListView, and attach this adapter to it.

        listView = (ListView) findViewById(R.id.listview_results);
        //                Log.d(TAG,convertView.toString());
//                Log.d(TAG,parent.toString());
        final long finalMaximumCostTime = maximumCostTime;
        final long finalAvgCostTime = avgCostTime;
        baseAdapter = new BaseAdapter() {
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
                MemoryAnalyseResult item = (MemoryAnalyseResult) getItem(position);
                Log.d(TAG, item.toString());
//                Log.d(TAG,convertView.toString());
//                Log.d(TAG,parent.toString());
                View view = LayoutInflater.from(context).inflate(R.layout.list_item_result, parent, false);

                setTVVal(view, R.id.list_item_num, item.getData());
                setTVVal(view, R.id.list_item_ts, item.getCostime().get(0));
                setTVVal(view, R.id.list_item_ts_unit, "ms");

                ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.list_item_progress);
                progressBar.setMax((int) finalMaximumCostTime);
                progressBar.setProgress((int) item.getMaximumCostTime());
                progressBar.setSecondaryProgress((int) finalAvgCostTime);
                Log.e(TAG, progressBar.getProgress() + "/" + progressBar.getMax());
                return view;
            }

            private void setTVVal(View view, int list_item_num, Object data) {
                TextView viewById = (TextView) view.findViewById(list_item_num);
                viewById.setText(String.valueOf(data));
            }
        };
        listView.setAdapter(baseAdapter);

        paintBtnUI();
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

    public void paintBtnUI() {
        switch (sortMode) {
            case sortMode_by_costtime:{
                orderByTsBtn.setEnabled(false);
                orderByIdxBtn.setEnabled(true);
                break;
            }
            case sortMode_by_idx:{
                orderByTsBtn.setEnabled(true);
                orderByIdxBtn.setEnabled(false);
                break;
            }
        }
    }

    public void orderByIdxButton(View view) {
        sortMode = sortMode_by_idx;
        paintBtnUI();

        Collections.sort(analyse, new IdxComparator());

        baseAdapter.notifyDataSetChanged();
    }

    public void orderByTsButton(View view) {
        sortMode = sortMode_by_costtime;
        paintBtnUI();

        Collections.sort(analyse, new MaxComparator());

        baseAdapter.notifyDataSetChanged();
    }

    public void finishActivity(View view) {


        finish();
    }
}
