package com.example.roman.appfortest;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.roman.appfortest.utils.Utils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ThreadsRx extends AppCompatActivity {

    private static final String TAG = Threads.class.getSimpleName();

    @Nullable private Disposable disposable;
    @Nullable private ProgressBar progressBar;
    @Nullable private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threads_rx);

        progressBar = this.findViewById(R.id.progress);
        textView = this.findViewById(R.id.text);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (progressBar != null){
            progressBar.setVisibility(View.VISIBLE);
        }

        final long timeToWait = 2000L;
        disposable = io.reactivex.Observable.fromCallable(() -> Utils.imitateLoading(timeToWait))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                // look at this as lambda, but even shorter
                .subscribe(this::updateUI,
                        error -> Log.e(TAG, error.getMessage(), error)
                );
    }

    private void updateUI(Long timeWaited) {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        if (textView != null) {
            textView.setText(this.getString(R.string.string_done, timeWaited));
        }
    }



    @Override
    protected void onStop() {
        super.onStop();
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressBar = null;
        textView = null;
    }
    public static void startActivity (Activity activity) {
        Intent activityIntent = new Intent(activity, ThreadsRx.class);
        activity.startActivity(activityIntent);
    }
}
