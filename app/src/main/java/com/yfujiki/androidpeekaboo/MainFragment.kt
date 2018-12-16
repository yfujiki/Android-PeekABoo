package com.yfujiki.androidpeekaboo

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.Executors
import java.util.concurrent.Executor
import androidx.recyclerview.widget.LinearSnapHelper
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

class MainFragment : Fragment() {

    private var currentPosition: Int = 2

    private val snapHelper = object : LinearSnapHelper() {
        override fun findTargetSnapPosition(
            layoutManager: RecyclerView.LayoutManager?,
            velocityX: Int,
            velocityY: Int
        ): Int {
            val centerView = findSnapView(layoutManager!!) ?: return RecyclerView.NO_POSITION
            var targetPosition = layoutManager.getPosition(centerView)

            if (currentPosition != targetPosition) {
                // Next page is defined solely by the velocity
                if (velocityX > 0) {
                    targetPosition = max(currentPosition, targetPosition)
                } else if (velocityX < 0){
                    targetPosition = min(currentPosition, targetPosition)
                }
            } else {
                // It depends whether where the edge of the current page is
                if (velocityX > 0) {
                    val scrollOffset = view!!.recyclerView.computeHorizontalScrollOffset()
                    val rightEdgeOffset = scrollOffset + centerView.measuredWidth
                    targetPosition = ceil((rightEdgeOffset / centerView.measuredWidth).toDouble()).toInt()
                } else if (velocityX < 0) {
                    val rightEdgeOffset = view!!.recyclerView.computeHorizontalScrollOffset()
                    targetPosition = ceil((rightEdgeOffset / centerView.measuredWidth).toDouble()).toInt()
                }
            }

            currentPosition = targetPosition
            return targetPosition
        }
    }

    private val panGestureDetector = GestureDetectorCompat(activity, object : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            isScrolling = true

            scrolledDistance = (e1!!.getX() - e2!!.getX()).toInt()

            return true
        }

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }
    })

    private val gestureListener = object : View.OnTouchListener {
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            if (panGestureDetector.onTouchEvent(event)) {
                return true
            }

            if (event?.getAction() == MotionEvent.ACTION_UP) {
                if (isScrolling) {
                    view?.recyclerView?.smoothScrollBy(scrolledDistance, 0)
                    isScrolling = false
                }
            }

            return false
        }
    }

    private val adapter: ImagePagedListAdapter = {
        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(3)
            .setPageSize(1)
            .setPrefetchDistance(2)
            .setEnablePlaceholders(false)
            .build()
        var builder = PagedList.Builder<Int, Drawable>(ImageDataSource(), config)
        builder.setNotifyExecutor(UiThreadExecutor())
        builder.setFetchExecutor(BackgroundThreadExecutor())
        val list = builder.build()

        val adapter = ImagePagedListAdapter()
        adapter.submitList(list)
        adapter
    }()

    private var isScrolling = false

    private var scrolledDistance: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.fragment_main, container, false)
        view.recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

        snapHelper.attachToRecyclerView(view.recyclerView)
        view.recyclerView.adapter = adapter
        view.layout.setOnTouchListener(gestureListener)

        return view
    }
}

internal class UiThreadExecutor : Executor {
    private val handler = Handler(Looper.getMainLooper())

    override fun execute(command: Runnable) {
        handler.post(command)
    }
}

// Background thread executor to execute runnable on background thread
internal class BackgroundThreadExecutor : Executor {
    private val executorService = Executors.newFixedThreadPool(2)

    override fun execute(command: Runnable) {
        executorService.execute(command)
    }
}
