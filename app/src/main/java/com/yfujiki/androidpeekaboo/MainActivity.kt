package com.yfujiki.androidpeekaboo

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executors
import androidx.annotation.NonNull
import java.util.concurrent.Executor


class MainActivity : AppCompatActivity() {

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