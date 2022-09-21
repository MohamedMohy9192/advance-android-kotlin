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

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView


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

        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                rotateButton.isEnabled = false
            }
            override fun onAnimationEnd(animation: Animator) {
                rotateButton.isEnabled = true
            }
        })

        // star spin around its center. But it does so really quickly.
        // In fact, it does it in 300 milliseconds, which is the default duration of all animations on the platform.
        animator.start()
    }

    private fun translater() {
    }

    private fun scaler() {
    }

    private fun fader() {
    }

    private fun colorizer() {
    }

    private fun shower() {
    }

}
