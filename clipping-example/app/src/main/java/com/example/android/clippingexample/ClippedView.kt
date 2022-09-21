package com.example.android.clippingexample

import android.content.Context
import android.graphics.*
import android.os.Build
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

    private val rejectRow = rowFour + rectInset + 2 * clipRectBottom

    // RectF is a class that holds rectangle coordinates in floating point.
    private var rectF = RectF(
        rectInset,
        rectInset,
        clipRectRight - rectInset,
        clipRectBottom - rectInset
    )

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
        drawQuickRejectExample(canvas)
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
        canvas.translate(columnOne, rowOne)
        //Apply clipping shapes and paths.
        //Draw the rectangle or text.
        drawClippedRectangle(canvas)
        //Restore the state of the Canvas.
        canvas.restore()
    }

    /**
     * draw the second rectangle, which uses the difference between two clipping rectangles
     * to create a picture frame effect.
     */
    private fun drawDifferenceClippingExample(canvas: Canvas) {
        // Save the canvas.
        canvas.save()
        // Move the origin to the right for the next rectangle.
        // Translate the origin of the canvas into open space to the first row, second column, to the right of the first rectangle.
        canvas.translate(columnTwo, rowOne)
        // Apply two clipping rectangles.
        // Use the subtraction of two clipping rectangles to create a frame.
        canvas.clipRect(
            2 * rectInset, 2 * rectInset,
            clipRectRight - 2 * rectInset,
            clipRectBottom - 2 * rectInset
        )
        // The method clipRect(float, float, float, float, Region.Op
        // .DIFFERENCE) was deprecated in API level 26. The recommended
        // alternative method is clipOutRect(float, float, float, float),
        // which is currently available in API level 26 and higher.
        // The DIFFERENCE operator subtracts the second rectangle from the first one.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {

            @Suppress("DEPRECATION")
            canvas.clipRect(
                4 * rectInset, 4 * rectInset,
                clipRectRight - 4 * rectInset,
                clipRectBottom - 4 * rectInset,
                Region.Op.DIFFERENCE
            )
        } else {
            canvas.clipOutRect(
                4 * rectInset, 4 * rectInset,
                clipRectRight - 4 * rectInset,
                clipRectBottom - 4 * rectInset
            )
        }
        // Draw the modified canvas.
        drawClippedRectangle(canvas)
        //Restore the state of the Canvas.
        canvas.restore()
    }

    private fun drawCircularClippingExample(canvas: Canvas) {
        canvas.save()
        canvas.translate(columnOne, rowTwo)
        // Clears any lines and curves from the path but unlike reset(),
        // keeps the internal data structure for faster reuse.
        path.rewind()
        path.addCircle(
            circleRadius, clipRectBottom - circleRadius,
            circleRadius, Path.Direction.CCW
        )
        // The method clipPath(path, Region.Op.DIFFERENCE) was deprecated in
        // API level 26. The recommended alternative method is
        // clipOutPath(Path), which is currently available in
        // API level 26 and higher.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            @Suppress("DEPRECATION")
            canvas.clipPath(path, Region.Op.DIFFERENCE)
        } else {
            canvas.clipOutPath(path)
        }
        drawClippedRectangle(canvas)
        canvas.restore()
    }

    private fun drawIntersectionClippingExample(canvas: Canvas) {
        canvas.save()
        canvas.translate(columnTwo, rowTwo)
        canvas.clipRect(
            clipRectLeft, clipRectTop,
            clipRectRight - smallRectOffset,
            clipRectBottom - smallRectOffset
        )
        // The method clipRect(float, float, float, float, Region.Op
        // .INTERSECT) was deprecated in API level 26. The recommended
        // alternative method is clipRect(float, float, float, float), which
        // is currently available in API level 26 and higher.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            @Suppress("DEPRECATION")
            canvas.clipRect(
                clipRectLeft + smallRectOffset,
                clipRectTop + smallRectOffset,
                clipRectRight, clipRectBottom,
                Region.Op.INTERSECT
            )
        } else {
            canvas.clipRect(
                clipRectLeft + smallRectOffset,
                clipRectTop + smallRectOffset,
                clipRectRight, clipRectBottom
            )
        }
        drawClippedRectangle(canvas)
        canvas.restore()
    }

    /**
     *
     *  Combine shapes, a circle and a rectangle, and draw any path to define a clipping region.
     */
    private fun drawCombinedClippingExample(canvas: Canvas) {
        canvas.save()
        canvas.translate(columnOne, rowThree)
        path.rewind()
        path.addCircle(
            clipRectLeft + rectInset + circleRadius,
            clipRectTop + circleRadius + rectInset,
            circleRadius, Path.Direction.CCW
        )
        path.addRect(
            clipRectRight / 2 - circleRadius,
            clipRectTop + circleRadius + rectInset,
            clipRectRight / 2 + circleRadius,
            clipRectBottom - rectInset, Path.Direction.CCW
        )
        canvas.clipPath(path)
        drawClippedRectangle(canvas)
        canvas.restore()
    }

    private fun drawRoundedRectangleClippingExample(canvas: Canvas) {
        canvas.save()
        canvas.translate(columnTwo, rowThree)
        path.rewind()
        // The addRoundRect() function takes a rectangle, values for the x and y values of the corner radius,
        // and the direction to wind the round-rectangle's contour.
        // Path.Direction specifies how closed shapes (e.g. rects, ovals) are oriented when they are added to a path. CCW stands for counter-clockwise.
        path.addRoundRect(
            rectF, clipRectRight / 4,
            clipRectRight / 4, Path.Direction.CCW
        )
        canvas.clipPath(path)
        drawClippedRectangle(canvas)
        canvas.restore()
    }

    // Clip the outside around the rectangle by doubling the insets of the clipping rectangle.
    private fun drawOutsideClippingExample(canvas: Canvas) {
        canvas.save()
        canvas.translate(columnOne, rowFour)
        canvas.clipRect(
            2 * rectInset, 2 * rectInset,
            clipRectRight - 2 * rectInset,
            clipRectBottom - 2 * rectInset
        )
        drawClippedRectangle(canvas)
        canvas.restore()
    }

    private fun drawTranslatedTextExample(canvas: Canvas) {
        canvas.save()
        paint.color = Color.GREEN
        // Align the RIGHT side of the text with the origin.
        paint.textAlign = Paint.Align.LEFT
        // Apply transformation to canvas.
        canvas.translate(columnTwo, textRow)
        // Draw text.
        canvas.drawText(
            context.getString(R.string.translated),
            clipRectLeft, clipRectTop, paint
        )
        canvas.restore()
    }

    private fun drawSkewedTextExample(canvas: Canvas) {
        canvas.save()
        paint.color = Color.YELLOW
        paint.textAlign = Paint.Align.RIGHT
        // Position text.
        canvas.translate(columnTwo, textRow)
        // Apply skew transformation.
        canvas.skew(0.2f, 0.3f)
        canvas.drawText(
            context.getString(R.string.skewed),
            clipRectLeft, clipRectTop, paint
        )
        canvas.restore()
    }

    private fun drawQuickRejectExample(canvas: Canvas) {
        val inClipRectangle = RectF(
            clipRectRight / 2,
            clipRectBottom / 2,
            clipRectRight * 2,
            clipRectBottom * 2
        )

        val notInClipRectangle = RectF(
            RectF(
                clipRectRight + 1,
                clipRectBottom + 1,
                clipRectRight * 2,
                clipRectBottom * 2
            )
        )

        canvas.save()
        canvas.translate(columnOne, rejectRow)
        canvas.clipRect(
            clipRectLeft, clipRectTop,
            clipRectRight, clipRectBottom
        )

        if (canvas.quickReject(
                inClipRectangle, Canvas.EdgeType.AA
            )
        ) {
            canvas.drawColor(Color.WHITE)
        } else {
            canvas.drawColor(Color.BLACK)
            canvas.drawRect(
                inClipRectangle, paint
            )
        }
        canvas.restore()
    }
}