package com.yfujiki.androidpeekaboo

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment : Fragment() {

    val adapter: ImageViewPagerAdapter = ImageViewPagerAdapter(this)

    val listener: ViewPager.OnPageChangeListener = object: ViewPager.OnPageChangeListener {

        private var jumpPosition = -1;


        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            //We do nothing here.
        }

        override fun onPageSelected(position: Int) {
            if (position == 0) {
                //prepare to jump to the last page
                jumpPosition = adapter.getRealCount();

                //TODO: indicator.setActive(adapter.getRealCount() - 1)
            } else if (position == adapter.getRealCount() + 1) {
                //prepare to jump to the first page
                jumpPosition = 1;

                //TODO: indicator.setActive(0)
            } else {
                //TODO: indicator.setActive(position - 1)
            }
        }

        override public fun onPageScrollStateChanged(state: Int) {
            //Let's wait for the animation to be completed then do the jump (if we do this in
            //onPageSelected(int position) scroll animation will be canceled).
            if (state == ViewPager.SCROLL_STATE_IDLE && jumpPosition >= 0) {
                //Jump without animation so the user is not aware what happened.
                view!!.viewPager.setCurrentItem(jumpPosition, false);
                //Reset jump position.
                jumpPosition = -1;
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        var fragmentResourceId = if (resources.configuration.orientation == ORIENTATION_LANDSCAPE)
                        R.layout.fragment_main_landscape else R.layout.fragment_main
        val view = inflater.inflate(fragmentResourceId, container, false)

        view.viewPager.addOnPageChangeListener(listener)
        view.viewPager.adapter = adapter
        view.viewPager.setCurrentItem(1, false)

        return view
    }
}
