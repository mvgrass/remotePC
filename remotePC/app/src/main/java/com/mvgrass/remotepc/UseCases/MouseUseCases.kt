package com.mvgrass.remotepc.UseCases

import android.util.Log
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.round

fun getLeftButtonPressedCode(): ByteArray {
    val command: Int = 26
    val arr = ByteArray(2)
    arr[0] = (command shr 8).toByte()
    arr[1] = (command).toByte()

    return arr
}

fun getLeftButtonReleasedCode(): ByteArray {
    val command: Int = 27
    val arr = ByteArray(2)
    arr[0] = (command shr 8).toByte()
    arr[1] = (command).toByte()

    return arr
}

fun getLeftButtonClickedCode(): ByteArray {
    val command: Int = 22
    val arr = ByteArray(2)
    arr[0] = (command shr 8).toByte()
    arr[1] = (command).toByte()

    return arr
}

fun getLeftButtonDoubleClickedCode(): ByteArray {
    val command: Int = 28
    val arr = ByteArray(2)
    arr[0] = (command shr 8).toByte()
    arr[1] = (command).toByte()

    return arr
}


fun getRightButtonPressedCode(): ByteArray {
    val command: Int = 24
    val arr = ByteArray(2)
    arr[0] = (command shr 8).toByte()
    arr[1] = (command).toByte()

    return arr
}

fun getRightButtonReleasedCode(): ByteArray {
    val command: Int = 25
    val arr = ByteArray(2)
    arr[0] = (command shr 8).toByte()
    arr[1] = (command).toByte()

    return arr
}

fun getRightButtonClickedCode(): ByteArray {
    val command: Int = 23
    val arr = ByteArray(2)
    arr[0] = (command shr 8).toByte()
    arr[1] = (command).toByte()

    return arr
}

fun getScrolledCode(value: Int): ByteArray {
    val command: Int = 29
    val arr = ByteArray(6)
    arr[0] = (command shr 8).toByte()
    arr[1] = (command).toByte()


    arr[2] = (value shr 24 and 0xFF).toByte()
    arr[3] = (value shr 16 and 0xFF).toByte()
    arr[4] = (value shr 8 and 0xFF).toByte()
    arr[5] = (value and 0xFF).toByte()

    return arr
}

fun getMouseMovedCode(dx: Float, dy: Float): ByteArray {
    val command: Int = 21
    val arr = ByteArray(10)
    arr[0] = (command shr 8).toByte()
    arr[1] = (command).toByte()

    val dxBits = round(dx).toInt()
    val dyBits = round(dy).toInt()

    arr[2] = (dxBits shr 24 and 0xFF).toByte()
    arr[3] = (dxBits shr 16 and 0xFF).toByte()
    arr[4] = (dxBits shr 8 and 0xFF).toByte()
    arr[5] = (dxBits and 0xFF).toByte()

    arr[6] = (dyBits shr 24 and 0xFF).toByte()
    arr[7] = (dyBits shr 16 and 0xFF).toByte()
    arr[8] = (dyBits shr 8 and 0xFF).toByte()
    arr[9] = (dyBits and 0xFF).toByte()

    return arr
}