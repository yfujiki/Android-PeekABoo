package com.yfujiki.androidpeekaboo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import kotlinx.android.synthetic.main.view_holder_image.view.*

public class ImageViewPagerAdapter(val fragment: Fragment): PagerAdapter() {

    val imageFetcher = ImageFetcher()

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return 5
    }

    fun getRealCount(): Int {
        return 3
    }

    @Override
    override fun instantiateItem(container: ViewGroup, position: Int): View {
        val modelPosition = mapPagerPositionToModelPosition(position);
        val view = LayoutInflater.from(fragment.context).inflate(R.layout.view_holder_image, container, false)
        view.imageView.setImageDrawable(imageFetcher.fetchRandomImage())

        return view
    }

    private fun mapPagerPositionToModelPosition(pagerPosition: Int): Int {
        // Put last page model to the first position.
        if (pagerPosition == 0) {
            return getRealCount() - 1;
        }
        // Put first page model to the last position.
        if (pagerPosition == getRealCount() + 1) {
            return 0;
        }
        return pagerPosition - 1;
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)

        container.removeView(`object` as View)
    }
}