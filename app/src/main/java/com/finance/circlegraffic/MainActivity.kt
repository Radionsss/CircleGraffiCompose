package com.finance.circlegraffic

import android.graphics.Paint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.finance.circlegraffic.ui.theme.CircleGrafficTheme
import kotlin.math.min

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CircleGrafficTheme {
                Column {


                    SuperSimplePieChartAnimate()
                    SuperSimplePieChartWithIndentation()
                    SuperSimplePieChartNormal()
                }
            }
        }
    }
}

data class PieChartItem(val percentage: Float, val color: Color)


@Preview
@Composable
fun SuperSimplePieChartAnimate() {

    var animationPlayed by remember() { // to play animation only once
        mutableStateOf(false)
    }

    val maxAngle by animateFloatAsState( // animate from 0 to 360 degree for 1000ms
        targetValue = if (animationPlayed) 360f else 0f,
        animationSpec = tween(durationMillis = 5000), label = ""
    )

    LaunchedEffect(key1 = true) { // fired on view creation, state change triggers the animation
        animationPlayed = true
    }
    val values = listOf(
        PieChartItem(10f, Color.Red),
        PieChartItem(10f, Color.Green),
        PieChartItem(60f, Color.Yellow),
        PieChartItem(10f, Color.Blue),
        PieChartItem(10f, Color.Black)
    )

    // box with canvas
    Box(
        Modifier
            .size(200.dp) // we give the box some size
            .background(Color.White) // white background
            .padding(10.dp) // padding for nice look
            .drawBehind { // create canvas inside box
                var startAngle: Float = 2f // we use the variable to track start angle of each arc

                values.forEach { // for each value
                    val sweepAngle =
                        it.percentage.mapValueToDifferentRange( // we transform it to degrees from 0 to 360
                            inMin = 0f, // 0%
                            inMax = 100f, // 100%
                            outMin = 0f, // 0 degrees
                            outMax = maxAngle // <--- chagne this to maxAngle
                        )


                    // using extension function we draw the arc
                    drawArc(
                        color = it.color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle
                    )

                    startAngle += sweepAngle // increase sweep angle
                }

                // Draw text in the center of the pie chart
                val centerX = size.width / 2
                val centerY = size.height / 2
                val text = "10000$"
                val textPaint = Paint().apply {
                    color = Color.Black.toArgb()
                    textAlign = Paint.Align.CENTER
                    textSize = 30f // Set the text size here
                }
                drawContext.canvas.nativeCanvas.drawText(text, centerX, centerY, textPaint)

            })


}


@Preview
@Composable
fun SuperSimplePieChartWithIndentation() {
    val values = listOf(
        PieChartItem(10f, Color.Red),
        PieChartItem(10f, Color.Green),
        PieChartItem(60f, Color.Yellow),
        PieChartItem(10f, Color.Blue),
        PieChartItem(10f, Color.Black)
    )
    val gapAngle = 1f // размер отступа между сегментами


    // box with canvas
    Box(
        Modifier
            .size(200.dp) // Set box size to twice the radius
            .background(Color.White) // white background
            .padding(10.dp) // padding for nice look
            .drawBehind { // create canvas inside box
                var startAngle: Float = 2f // we use the variable to track start angle of each arc

                values.forEach { // for each value // for each value
                    val sweepAngle =
                        it.percentage.mapValueToDifferentRange( // we transform it to degrees from 0 to 360
                            inMin = 0f, // 0%
                            inMax = 100f, // 100%
                            outMin = 0f, // 0 degrees
                            outMax = 360f - gapAngle * values.size// Subtract padding angles from total 360 degrees
                        )


                    // using extension function we draw the arc
                    drawArc(
                        color = it.color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle
                    )

                    startAngle += sweepAngle + gapAngle // increase sweep angle
                }

                // Draw text in the center of the pie chart
                val centerX = size.width / 2
                val centerY = size.height / 2
                val text = "10000$"
                val textPaint = Paint().apply {
                    color = Color.Black.toArgb()
                    textAlign = Paint.Align.CENTER
                    textSize = 30f // Set the text size here
                }
                drawContext.canvas.nativeCanvas.drawText(text, centerX, centerY, textPaint)

            })
}

@Preview
@Composable
fun SuperSimplePieChartNormal() {
    val values = listOf(
        PieChartItem(10f, Color.Red),
        PieChartItem(10f, Color.Green),
        PieChartItem(60f, Color.Yellow),
        PieChartItem(10f, Color.Blue),
        PieChartItem(10f, Color.Black)
    )

    Box(
        Modifier
            .size(200.dp) // Set box size to twice the radius
            .background(Color.White) // white background
            .padding(10.dp) // padding for nice look
            .drawBehind { // create canvas inside box
                var startAngle: Float = 2f // we use the variable to track start angle of each arc

                values.forEach { // for each value // for each value
                    val sweepAngle =
                        it.percentage.mapValueToDifferentRange( // we transform it to degrees from 0 to 360
                            inMin = 0f, // 0%
                            inMax = 100f, // 100%
                            outMin = 0f, // 0 degrees
                            outMax = 360f // Subtract padding angles from total 360 degrees
                        )


                    // using extension function we draw the arc
                    drawArc(
                        color = it.color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle
                    )

                    startAngle += sweepAngle  // increase sweep angle
                }

                // Draw text in the center of the pie chart
                val centerX = size.width / 2
                val centerY = size.height / 2
                val text = "10000$"
                val textPaint = Paint().apply {
                    color = Color.Black.toArgb()
                    textAlign = Paint.Align.CENTER
                    textSize = 30f // Set the text size here
                }
                drawContext.canvas.nativeCanvas.drawText(text, centerX, centerY, textPaint)

            })
}



private fun Float.mapValueToDifferentRange(
    inMin: Float,
    inMax: Float,
    outMin: Float,
    outMax: Float
): Float {
    return outMin + (this - inMin) * (outMax - outMin) / (inMax - inMin)
}

// extension function that facilitates arc drawing
private fun DrawScope.drawArc(
    color: Color,
    startAngle: Float, // angle from which arc will be started
    sweepAngle: Float // angle that arc will cover
) {
    val padding = 48.dp.toPx() // some padding to avoid arc touching the border
    val sizeMin = min(size.width, size.height)
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false, // draw arc without infill
        size = Size(sizeMin - padding, sizeMin - padding), // size of the arc/circle in pixels
        style = Stroke( // width of the ark
            width = sizeMin / 10
        ),
        topLeft = Offset(padding / 2f, padding / 2f) // move the arc to center
    )

}
















/*
@Preview
@Composable
fun SuperSimplePieChart6() {
    var selectedIndex by remember { mutableIntStateOf(-1) } // Состояние для отслеживания выбранного сегмента

    val values = listOf(
        PieChartItem(10f, Color.Red),
        PieChartItem(10f, Color.Green),
        PieChartItem(60f, Color.Magenta),
        PieChartItem(10f, Color.Blue),
        PieChartItem(10f, Color.Black)
    )
    val gapAngle = 1f // размер отступа между сегментами


    // box with canvas
    Box(
        Modifier
            .size(200.dp) // Set box size to twice the radius
            .background(Color.White) // white background
            .padding(10.dp) // padding for nice look
            .pointerInput(Unit) { // Добавляем обработчик нажатий
                forEachGesture {
                    awaitPointerEventScope {
                        awaitFirstDown().let { down ->
                            selectedIndex = calculateSelectedSegment(down, size.toSize(), values, gapAngle)
                        }
                    }
                }
            }
            .drawBehind { // create canvas inside box
                var startAngle: Float = 2f // we use the variable to track start angle of each arc

                values.forEachIndexed { index, it -> // Используем forEachIndexed для доступа к индексу элемента
                    val isSelected = index == selectedIndex // Проверяем, является ли текущий сегмент выбранным
                    val sweepAngle =
                        it.percentage.mapValueToDifferentRange( // we transform it to degrees from 0 to 360
                            inMin = 0f, // 0%
                            inMax = 100f, // 100%
                            outMin = 0f, // 0 degrees
                            outMax = 360f - gapAngle * values.size// Subtract padding angles from total 360 degrees
                        )


                    val offset = if (isSelected) 10f else 0f // Добавляем смещение, если сегмент выбран

                    // using extension function we draw the arc
                    drawArc(
                        color = it.color,
                        startAngle = startAngle+ offset,
                        sweepAngle = sweepAngle
                    )

                    startAngle += sweepAngle+ gapAngle // increase sweep angle
                }

                // Draw text in the center of the pie chart
                val centerX = size.width / 2
                val centerY = size.height / 2
                val text = "10000$"
                val textPaint = Paint().apply {
                    color = Color.Black.toArgb()
                    textAlign = Paint.Align.CENTER
                    textSize = 30f // Set the text size here
                }
                drawContext.canvas.nativeCanvas.drawText(text, centerX, centerY, textPaint)

            })
}
private fun calculateSelectedSegment(
    down: PointerInputChange,
    size: Size,
    values: List<PieChartItem>,
    gapAngle: Float
): Int {
    val angle = calculateAngle(down.position, size) // Вычисляем угол относительно центра круговой диаграммы
    var startAngle: Float = 0f

    values.forEachIndexed { index, item ->
        val sweepAngle = item.percentage.mapValueToDifferentRange(0f, 100f, 0f, 360f - gapAngle * values.size)
        val endAngle = startAngle + sweepAngle

        if (angle >= startAngle && angle <= endAngle) {
            return index // Возвращаем индекс выбранного сегмента
        }

        startAngle += sweepAngle + gapAngle
    }

    return -1
}

private fun calculateAngle(position: Offset, size: Size): Float {
    val center = Offset(size.width / 2, size.height / 2)
    return Math.toDegrees(atan2((position.y - center.y).toDouble(), (position.x - center.x).toDouble())).toFloat()
}
*/

