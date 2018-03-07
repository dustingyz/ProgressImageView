package me.limbre.glide4progressimageview;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

import okhttp3.OkHttpClient;

/**
 * Created by Limbre on 2018/3/1.
 * Glide 4.x GlideModule
 */

public class GlideModuleImp extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new ProgressInterceptor());
        OkHttpClient okHttpClient = builder.build();
//        glide.getRegistry().register(GlideUrl.class, InputStream.class, new GlideProgressLoader.Factory(okHttpClient));
        registry.replace(GlideUrl.class, InputStream.class, new GlideProgressLoader.Factory(okHttpClient));
    }
}
