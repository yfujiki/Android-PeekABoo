package com.yfujiki.androidpeekaboo

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import kotlinx.android.synthetic.main.view_holder_image.view.*

class ImageViewPagerAdapter(val fragment: Fragment): PagerAdapter() {

    private var imageList = listOf<Drawable>().toMutableList()

    private val imageFetcher = ImageFetcher()

    fun initializeData() {
        imageList.clear()

        for (i in 0 until 5) {
            imageList.add(imageFetcher.fetchRandomImage())
        }
    }

    fun forwardData() {
        for (i in 0 until 3) {
            imageList.removeAt(0)
        }

        for (i in 0 until 3) {
            imageList.add(imageFetcher.fetchRandomImage())
        }
    }

    fun rewindData() {
        for (i in 0 until 3) {
            imageList.removeAt(5 - i - 1)
        }

        for (i in 0 until 3) {
            imageList.add(0, imageFetcher.fetchRandomImage())
        }
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return 5
    }

    override fun instantiateItem(container: ViewGroup, position: Int): View {
        val view = LayoutInflater.from(fragment.context).inflate(R.layout.view_holder_image, container, false)
        view.imageView.setImageDrawable(imageList.get(position))

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}