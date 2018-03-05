package me.limbre.glideprogress;

import java.io.IOException;

import me.limbre.glideprogress.listener.ProgressListener;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by Limbre on 2018/3/1.
 * 计算下载进度,ResponseBody子类
 */

public class ProgressResponseBody extends ResponseBody {

    private static final String TAG = "ProgressResponseBody";
    private ResponseBody mResponseBody;
    private ProgressListener mListener;
    private BufferedSource mBufferSource;

    public ProgressResponseBody(String url, ResponseBody responseBody) {
        mResponseBody = responseBody;
        mListener = ProgressInterceptor.LISTENER_MAP.get(url);
    }

    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (mBufferSource == null) {
            mBufferSource = Okio.buffer(new ProgressSource(mResponseBody.source()));
        }
        return mBufferSource;
    }

    private class ProgressSource extends ForwardingSource {

        private long mTotalBytesRead;
        private double mCurrentProgress;

        public ProgressSource(Source delegate) {
            super(delegate);
        }

        @Override
        public long read(Buffer sink, long byteCount) throws IOException {
            long bytesRead = super.read(sink, byteCount);
            long fullLength = mResponseBody.contentLength();
            if (byteCount == -1) {
                mTotalBytesRead = fullLength;
            } else {
                mTotalBytesRead += bytesRead;
            }

            int progress = (int) (100f * mTotalBytesRead / fullLength);

//            Log.d(TAG, "download progress is : " + progress);

            if (mListener != null && progress != mCurrentProgress) {
                mListener.onProgress(progress);
            }

            if (mListener != null && mTotalBytesRead == fullLength) {
                mListener = null;
            }
            mCurrentProgress = progress;
            return 0;
        }
    }
}
