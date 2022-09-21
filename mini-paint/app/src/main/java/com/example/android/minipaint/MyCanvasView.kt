package com.example.android.minipaint

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat

private const val STROKE_WIDTH = 12f // has to be float

class MyCanvasView(context: Context) : View(context) {
    //  These are your bitmap and canvas for caching what has been drawn before.
    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap

    // For the background color of the canvas
    private val backgroundColor = ResourcesCompat.getColor(resources, R.color.colorBackground, null)

    // For holding the color to draw with
    private val drawColor = ResourcesCompat.getColor(resources, R.color.colorPaint, null)

    // Set up the paint with which to draw.
    private val paint = Paint().apply {
        color = drawColor
        // Smooths out edges of what is drawn without affecting shape.
        isAntiAlias = true
        // Dithering affects how colors with higher-precision than the device are down-sampled.
        isDither = true
        // Sets the type of painting to be done to a stroke, which is essentially a line.
        style = Paint.Style.STROKE // default: FILL
        // Specifies how lines and curve segments join on a stroked path.
        strokeJoin = Paint.Join.ROUND // default: MITER
        // Specifies how the beginning and ending of stroked lines and paths.
        strokeCap = Paint.Cap.ROUND // default: BUTT
        // Specifies the width of the stroke in pixels.
        strokeWidth = STROKE_WIDTH // default: Hairline-width (really thin)
    }

    // The Path is the path of what the user is drawing.
    // The path that is being drawn when following the user's touch on the screen.
    private var path = Path()

    // Caching the x and y coordinates of the current touch event
    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f

    // cache the latest x and y values. After the user stops moving and lifts their touch,
    // these are the starting point for the next path (the next segment of the line to draw).
    private var currentX = 0f
    private var currentY = 0f

    // scaledTouchSlop returns the distance in pixels a touch can wander before the system thinks the user is scrolling.
    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop

    /**
     * The method is called by the Android system whenever a view changes size.
     * Because the view starts out with no size,
     * the view's onSizeChanged() method is also called after the Activity first creates and inflates it.
     * his callback method is called by the Android system with the changed screen dimensions
     */
    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        // Looking at onSizeChanged(), a new bitmap and canvas are created every time the function executes.
        // You need a new bitmap, because the size has changed.
        // However, this is a memory leak, leaving the old bitmaps around.
        // To fix this, recycle extraBitmap before creating the next one
        if (::extraBitmap.isInitialized) extraBitmap.recycle()

        // An instance of Bitmap with the new width and height, which are the screen size
        // The third argument is the bitmap color configuration. ARGB_8888 stores each color in 4 bytes and is recommended.
        extraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(backgroundColor)
    }

    /**
     * The canvas that is passed to onDraw() and used by the system to display the bitmap
     * is different than the one you created in the onSizeChanged() method and used by you to draw on the bitmap.
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Draw the contents of the cached extraBitmap on the canvas associated with the view.
        // The drawBitmap() Canvas method comes in several versions.
        // In this code, you provide the bitmap, the x and y coordinates (in pixels) of the top left corner, and null for the Paint
        canvas.drawBitmap(extraBitmap, 0f, 0f, null)
        // The 2D coordinate system used for drawing on a Canvas is in pixels,
        // and the origin (0,0) is at the top left corner of the Canvas.

    }

    /**
     * This method is called when the user first touches the screen.
     */
    private fun touchStart() {
        // Reset the path, move to the x-y coordinates of the touch event (motionTouchEventX and motionTouchEventY),
        // and assign currentX and currentY to that value.
        path.reset()
        path.moveTo(motionTouchEventX, motionTouchEventY)
        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }

    private fun touchMove() {
        // Using a path, there is no need to draw every pixel and each time request a refresh of the display.
        // Instead, you can (and will) interpolate a path between points for much better performance.
        // Calculate the distance that has been moved (dx, dy).
        val dx = Math.abs(motionTouchEventX - currentX)
        val dy = Math.abs(motionTouchEventY - currentY)
        // If the movement was further than the touch tolerance, add a segment to the path.
        if (dx >= touchTolerance || dy >= touchTolerance) {
            // QuadTo() adds a quadratic bezier from the last point,
            // approaching control point (x1,y1), and ending at (x2,y2).
            path.quadTo(currentX, currentY, (motionTouchEventX + currentX) / 2, (motionTouchEventY + currentY) / 2)
            // Set the starting point for the next segment to the endpoint of this segment.
            currentX = motionTouchEventX
            currentY = motionTouchEventY
            // Draw the path in the extra bitmap to cache it.
            extraCanvas.drawPath(path, paint)
        }
        // Call invalidate() to (eventually call onDraw() and) redraw the view.
        invalidate()
    }

    private fun touchUp() {
        // Reset the path so it doesn't get drawn again.
        path.reset()
    }

    /**
     * The method on a view is called whenever the user touches the display.
     * @param event MotionEvent
     * @return Boolean
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Cache the x and y coordinates of the passed in event.
        motionTouchEventX = event.x
        motionTouchEventY = event.y

        // Handle motion events for touching down on the screen, moving on the screen, and releasing touch on the screen.
        // These are the events of interest for drawing a line on the screen.
        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchUp()
        }
        return true
    }
}