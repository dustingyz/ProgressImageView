package me.limbre.glideprogress;

import android.support.annotation.NonNull;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.HttpException;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.util.ContentLengthInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Limbre on 2018/3/1.
 */

public class ProgressDataFetcher implements DataFetcher<InputStream> {

    private final OkHttpClient mClient;
    private GlideUrl url;
    private ResponseBody mResponseBody;
    private InputStream stream;
    private boolean isCancelled;

    public ProgressDataFetcher(GlideUrl url, OkHttpClient client) {
        this.url = url;
        mClient = client;
    }

//    @Deprecated
//    public InputStream loadData(Priority priority) throws Exception {
//        //ori
//        Request.Builder builder = new Request.Builder().url(this.url.toStringUrl());
//        for (Map.Entry<String, String> headerEntry : url.getHeaders().entrySet()) {
//            String key = headerEntry.getKey();
//            builder.addHeader(key, headerEntry.getValue());
//        }
//        Request request = builder.build();
//        if (isCancelled) {
//            return null;
//        }
//        Response response = mClient.newCall(request).execute();
//        mResponseBody = response.body();
//        if (!response.isSuccessful()) {
//            throw new IOException("Request failed with code: " + response.code());
//        }
//        stream = ContentLengthInputStream.obtain(mResponseBody.byteStream(), mResponseBody.contentLength());
//        return stream;
//    }

    @Override
    public void loadData(@NonNull Priority priority, @NonNull final DataCallback<? super InputStream> callback) {
        Request.Builder builder = new Request.Builder().url(this.url.toStringUrl());
        for (Map.Entry<String, String> headerEntry : url.getHeaders().entrySet()) {
            String key = headerEntry.getKey();
            builder.addHeader(key, headerEntry.getValue());
        }
        Request request = builder.build();
        if (isCancelled) {
            callback.onLoadFailed(new IOException("request cancelled"));
        } else {

//                Response response = mClient.newCall(request).execute();
                mClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        callback.onLoadFailed(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        mResponseBody = response.body();
                        if (!response.isSuccessful()) {
                            callback.onLoadFailed(new HttpException(response.message(), response.code()));
                        } else {
                            stream = ContentLengthInputStream.obtain(mResponseBody.byteStream(), mResponseBody.contentLength());
                            callback.onDataReady(stream);
                        }
                    }
                });

//                mResponseBody = response.body();
//                if (!response.isSuccessful()) {
//                    callback.onLoadFailed(new IOException("Request failed with code: " + response.code()));
//                } else {
//                    stream = ContentLengthInputStream.obtain(mResponseBody.byteStream(), mResponseBody.contentLength());
//                    callback.onDataReady(stream);
//                }

        }

    }

    @Override
    public void cleanup() {
        //ori
        try {
            if (stream != null) {
                stream.close();
            }
            if (mResponseBody != null)
                mResponseBody.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @Deprecated
//    public String getId() {
//        //ori
//        return url.getCacheKey();
//    }

    @Override
    public void cancel() {
        //ori
        isCancelled = true;
    }

    @NonNull
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.REMOTE;
    }


}
