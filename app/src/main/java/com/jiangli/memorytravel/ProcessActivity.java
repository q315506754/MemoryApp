package com.jiangli.memorytravel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jiangli.memorytravel.core.MemoryResult;
import com.jiangli.memorytravel.core.MemoryTest;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


public class ProcessActivity extends Activity {
    public static final String TAG = ProcessActivity.class.getSimpleName();


    private int curIdx = 0;
    private long prefTs = System.currentTimeMillis();
    private int[] generate;
    private List<MemoryResult> resultList = new LinkedList<>();

    private TextView idxStatus_tv;
    private TextView contentDisplayed_tv;
    private TextView costTimeMilis_tv;
    private Handler costTimeMilisHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            costTimeMilis_tv.setText(msg.getData().getString("msg"));
        }
    };
    private Thread thread;

    @Override
    protected void onStart() {
        super.onStart();

        idxStatus_tv = (TextView) findViewById(R.id.idxStatus);
        Log.d(TAG, idxStatus_tv + "");

        contentDisplayed_tv = (TextView) findViewById(R.id.contentDisplayed);
        Log.d(TAG, contentDisplayed_tv + "");

        costTimeMilis_tv = (TextView) findViewById(R.id.costTimeMilis);

        refreshPage();

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "thread.run..");

                try {
                    while (true) {
//                        Log.d(TAG, "thread.while..");
                        long cost = System.currentTimeMillis() - prefTs;
                        String text = cost + "ms";

                        //处理完成后给handler发送消息
                        Message msg = new Message();
                        Bundle data = new Bundle();
                        data.putString("msg", text);
                        msg.setData(data);
                        costTimeMilisHandler.sendMessage(msg);


                        Thread.sleep(100);

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            thread.interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            thread.interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);

        final Context context = ProcessActivity.this;


        Intent intent = getIntent();
        String modeChoose = intent.getStringExtra("modeChoose");
        int fromNumber = intent.getIntExtra("fromNumber", 1);
        int toNumber = intent.getIntExtra("toNumber", 100);
        int arraySize = intent.getIntExtra("arraySize", 100);

        Log.d(TAG, modeChoose);
        Log.d(TAG, fromNumber + "");
        Log.d(TAG, toNumber + "");
        Log.d(TAG, arraySize + "");

        generate = MemoryTest.generate(fromNumber, toNumber, arraySize, modeChoose);



        ((Button) findViewById(R.id.finishBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToResultPage(context);
            }
        });

        ((Button) findViewById(R.id.knowBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int data = generate[curIdx];
                resultList.add(new MemoryResult(data, prefTs, System.currentTimeMillis()));

                prefTs = System.currentTimeMillis();
                curIdx++;

                if (curIdx >= generate.length) {
                    goToResultPage(context);
                } else {
                    refreshPage();
                }
            }

        });
    }

    private void goToResultPage(Context context) {
        Intent intent = new Intent(context, ResultActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Bundle bundle = new Bundle();
        bundle.putSerializable("results", (Serializable)resultList);
        intent.putExtras(bundle);

        startActivity(intent);

        finish();
    }

    private void refreshPage() {
        try {
            idxStatus_tv.setText((curIdx + 1) + "/" + generate.length);
            int i = generate[curIdx];
            Log.d(TAG, "cur num:" + i);
            contentDisplayed_tv.setText("" + i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_process, menu);
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
}
