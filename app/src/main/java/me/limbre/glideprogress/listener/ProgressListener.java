package me.limbre.glideprogress.listener;

/**
 * Created by Limbre on 2018/3/1.
 * 进度回调接口
 */

@FunctionalInterface
public interface ProgressListener {

    void onProgress(int progress);

}
