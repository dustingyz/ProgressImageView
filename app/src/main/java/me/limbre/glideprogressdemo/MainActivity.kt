package me.limbre.glideprogressdemo

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.activity_main.*
import me.limbre.glide4progressimageview.ProgressImageDisplayer
import me.limbre.glide4progressimageview.ProgressInterceptor

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        actionBar.

        val url = "https://www.nintendo.co.jp/top/img/switch_zelda_171208_s.jpg"
        val urlGif = "https://wx4.sinaimg.cn/bmiddle/64112046gy1fp13klost7g207s09lx6s.gif"
        val url2 = "https://www.nintendo.co.jp/top/img/switch_kirbystarallies_180214_s.jpg"

        ProgressInterceptor.addListener(urlGif, { progress -> piv_01.showProgress(progress)})

        piv_01.setLoadingDrawable(R.mipmap.ic_launcher_round)
        piv_01.scaleType = ImageView.ScaleType.FIT_CENTER
//        piv_01.setCircleSize(50)
        piv_01.setCircleWidth(12f)


        show_btn.setOnClickListener({
            val requestOptions = RequestOptions()
//            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
            Glide.with(this)
                    .load(urlGif)
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.item_show_list) {
            startActivity(Intent(this, ListActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_activity_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

}
