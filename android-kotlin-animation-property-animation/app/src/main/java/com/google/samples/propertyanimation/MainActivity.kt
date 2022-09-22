/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.propertyanimation

import android.animation.*
import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView


class MainActivity : AppCompatActivity() {

    lateinit var star: ImageView
    lateinit var rotateButton: Button
    lateinit var translateButton: Button
    lateinit var scaleButton: Button
    lateinit var fadeButton: Button
    lateinit var colorizeButton: Button
    lateinit var showerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        star = findViewById(R.id.star)
        rotateButton = findViewById<Button>(R.id.rotateButton)
        translateButton = findViewById<Button>(R.id.translateButton)
        scaleButton = findViewById<Button>(R.id.scaleButton)
        fadeButton = findViewById<Button>(R.id.fadeButton)
        colorizeButton = findViewById<Button>(R.id.colorizeButton)
        showerButton = findViewById<Button>(R.id.showerButton)

        rotateButton.setOnClickListener {
            rotater()
        }

        translateButton.setOnClickListener {
            translater()
        }

        scaleButton.setOnClickListener {
            scaler()
        }

        fadeButton.setOnClickListener {
            fader()
        }

        colorizeButton.setOnClickListener {
            colorizer()
        }

        showerButton.setOnClickListener {
            shower()
        }
    }

    private fun ObjectAnimator.disableViewDuringAnimation(view: View) {
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                view.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator) {
                view.isEnabled = true
            }
        })
    }

    /**
     * create an animation that rotates the ImageView containing the star from a value of -360 to 0.
     * This means that the view, and thus the star inside it, will rotate in a full circle (360 degrees) around its center.
     */
    private fun rotater() {
        // The reason that the animation starts at -360 is that that allows the star to complete a full circle (360 degrees)
        // and end at 0, which is the default rotation value for a non-rotated view,
        // so it's a good value to have at the end of the animation (in case any other action occurs on that view later, expecting the default value).
        // Note that the start starts at 0 degrees, before the animation begins, and then jumps immediately to -360 degrees.
        // But since -360 is visually the same as 0 degrees, there is no noticeable change when the animation begins.
        val animator = ObjectAnimator.ofFloat(star, View.ROTATION, -360f, 0f)

        // Change the duration property of the animator to 1000 milliseconds
        animator.duration = 1000

        // you ran the animation again before it came to a stop.
        // Did you notice a jump when you clicked on the button?
        // This is because you always reset to -360 degrees at the start of the animation,
        // regardless of whether the star is currently in the middle of animating or not.

        //  you're going to just prevent the user from clicking the button while the animation is running,
        //  to allow them to fully enjoy the in-process animation first.

        // Animators have a concept of listeners, which call back into user code to notify the application of changes in the state of the animation.
        // you'd like to disable the ROTATE button as soon as the animation starts, and then re-enable it when the animation ends.
        animator.disableViewDuringAnimation(rotateButton)

        // star spin around its center. But it does so really quickly.
        // In fact, it does it in 300 milliseconds, which is the default duration of all animations on the platform.
        animator.start()
    }

    private fun translater() {
        val animator = ObjectAnimator.ofFloat(star, View.TRANSLATION_X, 200f)
        // Repetition is a way of telling animations to do the same task again and again.
        // You can specify how many times to repeat
        // Controls how many times it repeats after the first run
        animator.repeatCount = 1
        // You can also specify the repetition behavior,
        // either REVERSE (for reversing the direction every time it repeats) or
        // RESTART (for animating from the original start value to the original end value,
        // thus repeating in the same direction every time).
        // The type of repetition
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(translateButton)
        animator.start()
    }

    private fun scaler() {
        //  PropertyValuesHolder, which is an object that holds information about both
        //  a property and the values that that property should animate between.
        // Scaling to a value of 4f means the star will scale to 4 times its default size.
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 4f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 4f)

        // An ObjectAnimator can hold multiple PropertyValuesHolder objects,
        // which will all animate together, in parallel, when the ObjectAnimator starts.
        // The ideal use case for ObjectAnimators which use PropertyValuesHolder parameters
        // is when you need to animate several properties on the same object in parallel.

        val animator = ObjectAnimator.ofPropertyValuesHolder(
            star, scaleX, scaleY
        )

        // leave the star's SCALE_X and SCALE_Y properties at their default values (1.0)
        // when the animation is done.
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(scaleButton)
        animator.start()
    }

    private fun fader() {
        val animator = ObjectAnimator.ofFloat(star, View.ALPHA, 0f)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(fadeButton)
        animator.start()
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun colorizer() {
        // This time, that property isn't an android.util.Property object,
        // but is instead a property exposed via a setter, View.setBackgroundColor(int).
        // Since you cannot refer to a android.util.Property object directly,
        // like you did before with ALPHA, etc.,
        // you will use the approach of passing in the name of the property as a String.
        // The name is then mapped internally to the appropriate setter/getter information on the target object.
        val animator = ObjectAnimator.ofArgb(
            star.parent,
            "backgroundColor", Color.BLACK, Color.RED
        )
        animator.duration = 500
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(colorizeButton)
        animator.start()
    }

    private fun shower() {
        // a reference to the star field ViewGroup (which is just the parent of the current star view).
        val container = star.parent as ViewGroup
        // the width and height of that container (which you will use to calculate the end translation values for our falling stars).
        val containerW = container.width
        val containerH = container.height
        // the default width and height of your star (which you will later alter with a scale factor to get different-sized stars).
        var starW: Float = star.width.toFloat()
        var starH: Float = star.height.toFloat()
        // Create a new View to hold the star graphic. Because the star is a VectorDrawable asset,
        // use an AppCompatImageView, which has the ability to host that kind of resource.
        val newStar = AppCompatImageView(this)
        newStar.setImageResource(R.drawable.ic_star)
        newStar.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        container.addView(newStar)
        // Modify the star to have a random size, from .1x to 1.6x of its default size.
        newStar.scaleX = Math.random().toFloat() * 1.5f + .1f
        newStar.scaleY = newStar.scaleX
        // Use this scale factor to change the cached width/height values
        starW *= newStar.scaleX
        starH *= newStar.scaleY
        //  position the new star. Horizontally, it should appear randomly somewhere from the left edge to the right edge.
        // This code uses the width of the star to position it from half-way off the screen on the left (-starW / 2)
        // to half-way off the screen on the right (with the star positioned at (containerW - starW / 2).
        newStar.translationX = Math.random().toFloat() *
                containerW - starW / 2
        // the rotation will use a smooth linear motion (moving at a constant rate over the entire rotation animation),
        // while the falling animation will use an accelerating motion (simulating gravity pulling the star downward at a constantly faster rate).
        // So you'll create two animators and add an interpolator to each.


        // The mover animation is responsible for making the star "fall."
        // It animates the TRANSLATION_Y property, similar to what you did with TRANSLATION_X in
        // the earlier translation task, but causing vertical instead of horizontal motion.
        // The code animates from -starH to (containerH + starH),
        // which effectively places it just off the container at the top and moves it until it's
        // just outside the container at the bottom
        // The AccelerateInterpolator "interpolator" that you are setting on the star causes a gentle acceleration motion.
        val mover = ObjectAnimator.ofFloat(
            newStar, View.TRANSLATION_Y,
            -starH, containerH + starH
        )
        mover.interpolator = AccelerateInterpolator(1f)
        // For the rotation animation, the star will rotate a random amount between 0 and 1080 degrees (three times around). For the motion,
        // use a LinearInterpolator, so the rotation will proceed at a constant rate as the star falls.
        val rotator = ObjectAnimator.ofFloat(
            newStar, View.ROTATION,
            (Math.random() * 1080).toFloat()
        )
        rotator.interpolator = LinearInterpolator()
        // AnimatorSet is basically a group of animations, along with instructions on when to run those animations.
        // It can play animations in parallel, or sequentially

        // Create the AnimatorSet and add the child animators to it (along with information to play them in parallel).
        val set = AnimatorSet()
        set.playTogether(mover, rotator)
        // The default animation time of 300 milliseconds is too quick to enjoy the falling stars,
        // so set the duration to a random number between 500 and 2000 milliseconds, so stars fall at different speeds.
        set.duration = (Math.random() * 1500 + 500).toLong()
        // Once newStar has fallen off the bottom of the screen, it should be removed from the container.
        // Set a simple listener to wait for the end of the animation and remove it.
        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                container.removeView(newStar)
            }
        })
        set.start()
    }

}
