package com.jiangli.memorytravel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiangli.memorytravel.core.MemoryResult;
import com.jiangli.memorytravel.core.MemoryTest;
import com.jiangli.memorytravel.util.Utility;

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
    public void onBackPressed() {
        final Activity activity = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("确定返回到主界面吗?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“确认”后的操作
                        activity.finish();
                    }
                })
                .setNegativeButton("返回", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作
                    }
                });

        builder.show();
    }

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
                        String text = cost/1000 +"."+cost%1000+ "秒";

                        //������ɺ��handler������Ϣ
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
                vibrater(80);

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

        vibrater(800);

        Intent intent = new Intent(context, ResultActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Bundle bundle = new Bundle();
        bundle.putSerializable("results", (Serializable)resultList);
        intent.putExtras(bundle);
        intent.putExtra("totalCounts",generate.length );
        startActivity(intent);

        finish();
    }

    private void vibrater(int i) {
        Utility.vibrate(this, i);
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

    class MyDialog  extends Dialog {

        public MyDialog(Context context) {
            super(context, R.style.ImageScale);
        }

        public MyDialog(Context context, int theme) {
            super(context, theme);
        }

        protected MyDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.hint_dialog);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            dismiss();
            return true;
        }

    }
    public void hintFired(View view) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View imgEntryView = inflater.inflate(R.layout.hint_dialog, null); // 加载自定义的布局文件
        ImageView img1 = (ImageView)imgEntryView.findViewById(R.id.hint_dialog_imageView);


//        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        final MyDialog dialog = new MyDialog(this);
//        img1.setImageBitmap(bm);
//        dialog.setView(imgEntryView); // 自定义dialog
        dialog.show();



        // 点击布局文件（也可以理解为点击大图）后关闭dialog，这里的dialog不需要按钮
        imgEntryView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                dialog.cancel();
            }
        });
    }
}
