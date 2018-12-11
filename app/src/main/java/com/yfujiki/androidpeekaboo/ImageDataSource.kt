package com.yfujiki.androidpeekaboo

import androidx.paging.ItemKeyedDataSource
import android.graphics.drawable.Drawable
import android.media.Image

class ImageDataSource : ItemKeyedDataSource<Int, Drawable>() {
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Drawable>) {
        println("Calling ${object{}.javaClass.enclosingMethod.name}. Key is ${params.key}")
        callback.onResult(listOf(ImageFetcher().fetchRandomImage()))
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Drawable>) {
        println("Calling ${object{}.javaClass.enclosingMethod.name}. Key is ${params.key}")
        callback.onResult(listOf(ImageFetcher().fetchRandomImage()))
    }

    override fun getKey(item: Drawable): Int {
        return -1
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Drawable>) {
        println("Calling ${object{}.javaClass.enclosingMethod.name}. Key is ${params.requestedInitialKey}. Initial size is ${params.requestedLoadSize}")
        callback.onResult(listOf(ImageFetcher().fetchRandomImage()))
    }
}