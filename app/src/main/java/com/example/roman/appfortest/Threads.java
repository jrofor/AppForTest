package com.example.roman.appfortest;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.roman.appfortest.utils.Utils;

import java.util.Observable;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Threads extends AppCompatActivity {

     @Nullable private Button btn_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threads);

        btn_1 = this.findViewById(R.id.btn_1);
        btn_1.setOnClickListener(v -> ThreadsRx.startActivity(Threads.this));
    }

    public static void startActivity (Activity activity){
        Intent activityIntent = new Intent(activity, Threads.class);
        activity.startActivity(activityIntent);
    }

/*
//--------------------------------------------------------------------------------------------------------------------------
    final String TAG = "myLogs";
    @NonNull private Thread leftThread;
    @NonNull private Thread rightThread;
    private static final Object lock = new Object();
    private static boolean isLeft = true;
    private boolean isRunning =true;
//--------------------------------------------------------------------------------------------------------------------------

/---------------------------------------------------------------------------------------------------------------------------
/*    @Override
    public void onStart(){
        super.onStart();
        leftThread =  new Thread(new LeftLeg());
        rightThread = new Thread(new RightLeg());

        leftThread.start();
        rightThread.start();
    }

    @Override
    public void onStop(){
        super.onStop();
        isRunning =false;
        //new Thread(new LeftLeg()).stop();
        if (leftThread != null) leftThread.interrupt();
        if (rightThread != null) rightThread.interrupt();
        leftThread = null;
        rightThread = null;
        Log.d(TAG, "---onStop---");
    }


    private class LeftLeg implements Runnable {

        @Override
        public void run(){
            //while (isRunning) Log.d(TAG, "Left step");//System.out.print("Left step");
            //while (isRunning == Thread.LeftLeg()) Log.d(TAG, "Left step");//System.out.print("Left step");
            while (!Thread.interrupted()&& isRunning) {
                synchronized (lock){
                    if (isLeft) {
                        Log.d(TAG, "Left step");
                        isLeft =false;
                    }
                }
            }
        }


    }
    privateprivate class RightLeg implements Runnable {

        @Override
        public void run(){
            //while (isRunning) Log.d(TAG, "Right step");//System.out.print("Right step");
            while (!Thread.interrupted() && isRunning){
                synchronized (lock){
                    if (isLeft){
                        Log.d(TAG, "Right step");
                        isLeft = true;
                    }
                }
            }
        }
    }



 */
}
