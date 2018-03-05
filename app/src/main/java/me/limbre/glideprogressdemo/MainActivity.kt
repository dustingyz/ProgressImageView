package me.limbre.glideprogressdemo

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.activity_main.*
import me.limbre.glideprogress.ProgressInterceptor
import me.limbre.utils.ProgressImageDisplayer

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url = "https://www.nintendo.co.jp/top/img/switch_zelda_171208_s.jpg"
        val url2 = "https://www.nintendo.co.jp/top/img/switch_kirbystarallies_180214_s.jpg"

        ProgressInterceptor.addListener(url, {progress -> piv_01.showProgress(progress)})

        piv_01.setLoadingDrawable(R.mipmap.ic_launcher_round)

        show_btn.setOnClickListener({
            val requestOptions = RequestOptions()
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
            Glide.with(this)
                    .load(url)
                    .apply(requestOptions)
                    .into(object : DrawableImageViewTarget(piv_01){
                        override fun onLoadStarted(placeholder: Drawable?) {
                            super.onLoadStarted(placeholder)
                            piv_01.showProgress(0)
                        }

                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            super.onResourceReady(resource, transition)
                            piv_01.hideProgress()
                            ProgressInterceptor.removeListener(url)
                        }

                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            super.onLoadFailed(errorDrawable)
                            piv_01.hideProgress()
                            ProgressInterceptor.removeListener(url)
                        }
                    })

        })

        ProgressImageDisplayer.display(url2, piv_02)

    }
}
