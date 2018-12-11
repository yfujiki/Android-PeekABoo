package com.yfujiki.androidpeekaboo

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import kotlin.random.Random

class ImageFetcher {

    fun fetchRandomImage(): Drawable {
        val randomIndex = Random.nextInt(16)
        return fetchImageAt(randomIndex);
    }

    private fun fetchImageAt(index: Int): Drawable {
        val context = App.getAppContext()
        val resource = context.resources.getIdentifier("img$index", "drawable", context.packageName)
        return ContextCompat.getDrawable(context, resource)!!
    }
}