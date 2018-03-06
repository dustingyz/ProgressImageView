package me.limbre.glideprogressdemo

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_list.*
import me.limbre.utils.ProgressImageDisplayer
import me.limbre.view.ProgressImageView

/**
 * Created by Limbre on 2018/3/6.
 */
class ListActivity : AppCompatActivity() {

    private val picList = listOf(
            "https://www.nintendo.co.jp/top/img/switch_labo_180118_B_l.jpg",
            "https://www.nintendo.co.jp/top/img/switch_bayonetta_180202_s.jpg",
            "https://www.nintendo.co.jp/top/img/switch_bayonetta_180202_l.jpg",
            "https://www.nintendo.co.jp/top/img/switch_mk8dx_170324_m.jpg",
            "https://www.nintendo.co.jp/top/img/switch_zelda_171208_l.jpg",
            "https://www.nintendo.co.jp/top/img/switch_arms_171220_l.jpg",
            "https://www.nintendo.co.jp/bayonetta/assets/images/mv_bg.jpg",
            "https://www.nintendo.co.jp/bayonetta/assets/images/bayonetta3_bg.jpg"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        content_rv.layoutManager = LinearLayoutManager(this)
        showBtn.setOnClickListener({_->
            val picAdapter = PicAdapter(this, picList)
            content_rv.adapter = picAdapter
            picAdapter.notifyDataSetChanged()
        })
    }

    inner class PicAdapter(val context: Context, var list : List<String>?) : Adapter<Holder>() {

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder{
            val itemView = ProgressImageView(context)
            itemView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500)
            itemView.scaleType = ImageView.ScaleType.CENTER_CROP
            return Holder(itemView)
        }

        override fun getItemCount(): Int {
            if (list == null) {
                return 0
            }
            return list!!.size
        }
        override fun onBindViewHolder(holder: Holder?, position: Int) {
            val iv = holder?.itemView as ProgressImageView
            ProgressImageDisplayer.display(list!![position], iv)
        }
    }


    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)
}