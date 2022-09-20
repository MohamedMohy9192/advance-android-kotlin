package com.example.android.minipaint

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.View
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
}