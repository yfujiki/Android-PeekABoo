package com.yfujiki.androidpeekaboo

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ViewTreeObserver
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executors
import java.util.concurrent.Executor
import androidx.recyclerview.widget.LinearSnapHelper
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

class MainActivity : AppCompatActivity() {

    private var currentPosition: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val adapter = ImagePagedListAdapter()
        adapter.submitList(list)
        recyclerView.adapter = adapter

        val snapHelper = object : LinearSnapHelper() {
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
                        val scrollOffset = recyclerView.computeHorizontalScrollOffset()
                        val rightEdgeOffset = scrollOffset + centerView.measuredWidth
                        targetPosition = ceil((rightEdgeOffset / centerView.measuredWidth).toDouble()).toInt()
                    } else if (velocityX < 0) {
                        val rightEdgeOffset = recyclerView.computeHorizontalScrollOffset()
                        targetPosition = ceil((rightEdgeOffset / centerView.measuredWidth).toDouble()).toInt()
                    }
                }

                currentPosition = targetPosition
                return targetPosition
            }
        }
        snapHelper.attachToRecyclerView(recyclerView)
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