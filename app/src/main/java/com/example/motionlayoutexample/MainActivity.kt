package com.example.motionlayoutexample

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageBorder.setOnClickListener {
            if (motionContainer.progress > 0.75)
                motionContainer.transitionToStart()
            else
                motionContainer.transitionToEnd()
        }

        profileInitialText.text = getString(R.string.initials)

        Glide.with(this)
                .load(getString(R.string.profile_url))
                .listener(glideRequestListener)
                .apply(RequestOptions.circleCropTransform())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(profileImage)
    }

    private val glideRequestListener = object : RequestListener<Drawable> {
        override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
        ): Boolean {
            fadeInView(profileImage)
            return false
        }

        override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
        ): Boolean {
            Log.d(TAG, "User avatar failed to load", e)
            return false
        }
    }

    private fun fadeInView(fadeInView: View) =
            fadeInView.apply {
                alpha = 0f
                visibility = View.VISIBLE
                animate()
                        .alpha(1f)
                        .setListener(null)
                        .start()
            }

    private fun fadeOutView(fadeOutView: View, onFinish: (() -> Unit)? = null) =
            fadeOutView
                    .animate()
                    .alpha(0f)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            fadeOutView.visibility = View.GONE
                            onFinish?.invoke()
                        }
                    })
                    .start()

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

}