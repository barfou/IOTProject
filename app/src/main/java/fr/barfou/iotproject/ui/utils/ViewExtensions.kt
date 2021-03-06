package fr.barfou.iotproject.ui.utils

import android.util.TypedValue
import android.view.View
import kotlin.math.roundToInt

/**
 * Extensions methods
 * Top level functions : Allow access from everywhere without class name prefix
 */
fun View.dp(number: Number): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        number.toFloat(),
        this.resources.displayMetrics
    ).roundToInt()
}

fun View.show(){
    visibility = View.VISIBLE
}

fun View.hide(){
    visibility = View.GONE
}

fun View.invisible(){
    visibility = View.INVISIBLE
}