package me.limbre.glide4progressimageview;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;

import java.io.InputStream;

import okhttp3.OkHttpClient;

/**
 * Created by Limbre on 2018/3/1.
 */

public class GlideProgressLoader implements ModelLoader<GlideUrl,  InputStream> {

    private OkHttpClient mOkHttpClient;

    private GlideProgressLoader(OkHttpClient client) {
        mOkHttpClient = client;
    }

//    @Override
//    public DataFetcher<InputStream> getResourceFetcher(GlideUrl model, int width, int height) {
//        return new ProgressDataFetcher(model, mOkHttpClient);
//    }

    @Nullable
    @Override
    public LoadData<InputStream> buildLoadData(@NonNull GlideUrl glideUrl, int width, int height, @NonNull Options options) {
        return new LoadData<>(glideUrl, new ProgressDataFetcher(glideUrl, mOkHttpClient));
    }

    @Override
    public boolean handles(@NonNull GlideUrl glideUrl) {
        return true;
    }

    public static class Factory implements ModelLoaderFactory<GlideUrl, InputStream> {
        private OkHttpClient mClient;

        public Factory() {
        }

        public Factory(OkHttpClient client) {
            mClient = client;
        }

        private synchronized OkHttpClient getOkHttpClient() {
            if (mClient == null) {
                mClient = new OkHttpClient();
            }
            return mClient;
        }

        @NonNull
        @Override
        public ModelLoader<GlideUrl, InputStream> build(@NonNull MultiModelLoaderFactory multiFactory) {
            return new GlideProgressLoader(getOkHttpClient());
        }

        @Override
        public void teardown() {
        }
    }

}
