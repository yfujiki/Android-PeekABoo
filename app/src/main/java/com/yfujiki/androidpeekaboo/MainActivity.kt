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

                if (velocityX > 0) {
                    targetPosition += 1
                } else if (velocityX < 0) {
                    targetPosition -= 1
                }

                if (targetPosition > currentPosition) {
                    targetPosition = currentPosition + 1
                } else if (targetPosition < currentPosition) {
                    targetPosition = currentPosition - 1
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