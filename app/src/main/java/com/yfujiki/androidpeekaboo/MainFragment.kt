package com.yfujiki.androidpeekaboo

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.os.Bundle
import android.view.*
import androidx.core.view.GestureDetectorCompat
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

                adapter.rewindData()
            } else if (position == adapter.getRealCount() + 1) {
                //prepare to jump to the first page
                jumpPosition = 1;

                adapter.forwardData()
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

    private val panGestureDetector = GestureDetectorCompat(activity, object: GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(
            e1: MotionEvent, e2: MotionEvent,
            distanceX: Float, distanceY: Float
        ): Boolean {
            isScrolling = true

            if (view?.viewPager?.isFakeDragging() == false) {
                view?.viewPager?.beginFakeDrag()
            }
            view?.viewPager?.fakeDragBy(-distanceX)

            return true
        }

        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }
    })

    private val touchListner = object: View.OnTouchListener {
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            if (panGestureDetector.onTouchEvent(event)) {
                return true
            }

            if (event?.getAction() == MotionEvent.ACTION_UP) {
                if (isScrolling) {
                    if (view?.viewPager?.isFakeDragging() == true) {
                        view?.viewPager?.endFakeDrag()
                    }
                    isScrolling = false
                }
            }

            return true
        }
    }

    private var isScrolling: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter.initializeData()

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

        view.setOnTouchListener(touchListner)

        return view
    }
}
