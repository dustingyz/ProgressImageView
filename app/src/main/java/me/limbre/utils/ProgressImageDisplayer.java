package me.limbre.utils;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;

import me.limbre.glideprogress.ProgressInterceptor;
import me.limbre.glideprogress.listener.ProgressListener;
import me.limbre.view.ProgressImageView;

/**
 * Created by Limbre on 2018/3/5.
 */

public class ProgressImageDisplayer {

    /**
     * 无Options展示图片
     * @param url
     * @param imageView
     */
    public static void display(final String url, final ProgressImageView imageView) {
        ProgressInterceptor.addListener(url, new ProgressListener() {
            @Override
            public void onProgress(int progress) {
                imageView.showProgress(progress);
            }
        });
        Glide.with(imageView.getContext())
                .load(url)
                .into(new DrawableImageViewTarget(imageView){
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        imageView.showProgress(0);
                    }

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        super.onResourceReady(resource, transition);
                        imageView.hideProgress();
                        ProgressInterceptor.removeListener(url);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        imageView.hideProgress();
                        ProgressInterceptor.removeListener(url);
                    }
                });
    }

    /**
     * requestOptions 将被 apply
     * @param url
     * @param imageView
     * @param requestOptions 不能为空
     */
    public static void display(final String url, final ProgressImageView imageView, @NonNull RequestOptions requestOptions) {
        ProgressInterceptor.addListener(url, new ProgressListener() {
            @Override
            public void onProgress(int progress) {
                imageView.showProgress(progress);
            }
        });
        Glide.with(imageView.getContext())
                .load(url)
                .apply(requestOptions)
                .into(new DrawableImageViewTarget(imageView){
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        imageView.showProgress(0);
                    }

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        super.onResourceReady(resource, transition);
                        imageView.hideProgress();
                        ProgressInterceptor.removeListener(url);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        imageView.hideProgress();
                        ProgressInterceptor.removeListener(url);
                    }
                });
    }
}
