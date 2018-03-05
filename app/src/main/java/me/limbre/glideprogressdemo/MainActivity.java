package me.limbre.glideprogressdemo;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;

import me.limbre.glideprogress.ProgressInterceptor;
import me.limbre.glideprogress.listener.ProgressListener;
import me.limbre.utils.ProgressImageDisplayer;
import me.limbre.view.ProgressImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String url = "https://www.nintendo.co.jp/top/img/switch_zelda_171208_s.jpg";
        final String url2 = "https://www.nintendo.co.jp/top/img/switch_kirbystarallies_180214_s.jpg";

        final ProgressImageView imageView = findViewById(R.id.imageView);

        ProgressInterceptor.addListener(url, new ProgressListener() {
            @Override
            public void onProgress(int progress) {
                imageView.showProgress(progress);
            }
        });

        imageView.setLoadingDrawable(R.mipmap.ic_launcher_round);

        findViewById(R.id.show_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
                Glide.with(MainActivity.this)
                        .load(url)
                        .apply(requestOptions)
                        .into(new DrawableImageViewTarget(imageView) {
                            @Override
                            public void onLoadStarted(@Nullable Drawable placeholder) {
                                super.onLoadStarted(placeholder);
//                                progressDialog.show();
                                imageView.showProgress(0);
                            }

                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                super.onResourceReady(resource, transition);
                                imageView.hideProgress();
                                ProgressInterceptor.removeListener(url);
                            }
                        });
            }
        });

        final ProgressImageView viewById = (ProgressImageView) findViewById(R.id.pic_piv);


        ProgressImageDisplayer.display(url2, viewById);


    }
}
