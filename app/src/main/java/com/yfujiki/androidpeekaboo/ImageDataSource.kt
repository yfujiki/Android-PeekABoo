package com.yfujiki.androidpeekaboo

import androidx.paging.ItemKeyedDataSource
import android.graphics.drawable.Drawable
import android.media.Image

class ImageDataSource : ItemKeyedDataSource<Int, Drawable>() {
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Drawable>) {
        callback.onResult(listOf(ImageFetcher().fetchRandomImage()))
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Drawable>) {
        callback.onResult(listOf(ImageFetcher().fetchRandomImage()))
    }

    override fun getKey(item: Drawable): Int {
        return -1
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Drawable>) {
        callback.onResult(listOf(ImageFetcher().fetchRandomImage()))
    }
}