package com.example.android.clippingexample

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

/**
 * The @JvmOverloads annotation instructs the Kotlin compiler to generate overloads for this function that substitute default parameter values.
 */
class ClippedView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        // Smooth out edges of what is drawn without affecting shape.
        isAntiAlias = true
        strokeWidth = resources.getDimension(R.dimen.strokeWidth)
        textSize = resources.getDimension(R.dimen.textSize)
    }

    // Store locally the path of what has been drawn.
    private val path = Path()

    // Dimensions for a clipping rectangle around the whole set of shapes.
    private val clipRectRight = resources.getDimension(R.dimen.clipRectRight)
    private val clipRectBottom = resources.getDimension(R.dimen.clipRectBottom)
    private val clipRectTop = resources.getDimension(R.dimen.clipRectTop)
    private val clipRectLeft = resources.getDimension(R.dimen.clipRectLeft)

    // Inset of a rectangle and the offset of a small rectangle.
    private val rectInset = resources.getDimension(R.dimen.rectInset)
    private val smallRectOffset = resources.getDimension(R.dimen.smallRectOffset)

    // This is the radius of the circle drawn inside the rectangle.
    private val circleRadius = resources.getDimension(R.dimen.circleRadius)

    // An offset and a text size for text that is drawn inside the rectangle.
    private val textOffset = resources.getDimension(R.dimen.textOffset)
    private val textSize = resources.getDimension(R.dimen.textSize)

    // The shapes for this app are displayed in two columns and four rows,
    // determined by the values of the dimensions set up above.
    // Set up the coordinates for two columns.
    private val columnOne = rectInset
    private val columnTwo = columnOne + rectInset + clipRectRight

    // the coordinates for each row, including the final row for the transformed text.
    private val rowOne = rectInset
    private val rowTwo = rowOne + rectInset + clipRectBottom
    private val rowThree = rowTwo + rectInset + clipRectBottom
    private val rowFour = rowThree + rectInset + clipRectBottom
    private val textRow = rowFour + (1.5f * clipRectBottom)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // You call a function for each shape you are drawing, which you'll implement later.
        drawBackAndUnclippedRectangle(canvas)
        drawDifferenceClippingExample(canvas)
        drawCircularClippingExample(canvas)
        drawIntersectionClippingExample(canvas)
        drawCombinedClippingExample(canvas)
        drawRoundedRectangleClippingExample(canvas)
        drawOutsideClippingExample(canvas)
        drawSkewedTextExample(canvas)
        drawTranslatedTextExample(canvas)
        // drawQuickRejectExample(canvas)
    }

    private fun drawClippedRectangle(canvas: Canvas) {
        // set the boundaries of the clipping rectangle for the whole shape.
        // Apply a clipping rectangle that constrains to drawing only the square.
        canvas.clipRect(
            clipRectLeft, clipRectTop,
            clipRectRight, clipRectBottom
        )
        // Fill the canvas with white color.
        // Yes! The whole canvas, because you are not drawing rectangles,
        // you are clipping! Because of the clipping rectangle,
        // only the region defined by the clipping rectangle is filled,
        // creating a white rectangle. The rest of the surface remains gray.
        canvas.drawColor(Color.WHITE)

        //Change the color to red and draw a diagonal line inside the clipping rectangle.
        paint.color = Color.RED
        canvas.drawLine(
            clipRectLeft, clipRectTop,
            clipRectRight, clipRectBottom, paint
        )
        // Set the color to green and draw a circle inside the clipping rectangle.
        paint.color = Color.GREEN
        canvas.drawCircle(
            circleRadius, clipRectBottom - circleRadius,
            circleRadius, paint
        )
        // Set the color to blue and draw text aligned with the right edge of the clipping rectangle.
        paint.color = Color.BLUE
        // Align the RIGHT side of the text with the origin.
        paint.textSize = textSize
        paint.textAlign = Paint.Align.RIGHT
        canvas.drawText(
            context.getString(R.string.clipping),
            clipRectRight, textOffset, paint
        )
    }

    private fun drawBackAndUnclippedRectangle(canvas: Canvas) {
        //  Fill the Canvas with the gray background color
        canvas.drawColor(Color.GRAY)
        // Save the current state of the Canvas so you can reset to that initial state.
        canvas.save()
        // Translate the Origin of the canvas to the location where you want to draw.
        // Translate to the first row and column position
        canvas.translate(columnOne,rowOne)
        //Apply clipping shapes and paths.
        //Draw the rectangle or text.
        drawClippedRectangle(canvas)
        //Restore the state of the Canvas.
        canvas.restore()
    }

    private fun drawDifferenceClippingExample(canvas: Canvas) {
    }

    private fun drawCircularClippingExample(canvas: Canvas) {
    }

    private fun drawIntersectionClippingExample(canvas: Canvas) {
    }

    private fun drawCombinedClippingExample(canvas: Canvas) {
    }

    private fun drawRoundedRectangleClippingExample(canvas: Canvas) {
    }

    private fun drawOutsideClippingExample(canvas: Canvas) {
    }

    private fun drawTranslatedTextExample(canvas: Canvas) {
    }

    private fun drawSkewedTextExample(canvas: Canvas) {
    }

    private fun drawQuickRejectExample(canvas: Canvas) {
    }
}