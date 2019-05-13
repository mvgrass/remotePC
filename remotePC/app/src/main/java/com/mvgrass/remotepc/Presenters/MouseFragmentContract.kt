package com.mvgrass.remotepc.Presenters

interface MouseFragmentContract{
    interface View{
        fun setSenseBar(value:Int)
    }

    interface Presenter{
        fun onLeftButtonPressed()

        fun onLeftButtonReleased()

        fun onLeftButtonClicked()

        fun onLeftButtonDoubleClicked()

        fun onRightButtonPressed()

        fun onRightButtonReleased()

        fun onRightButtonClicked()

        fun onScrolled(value: Int)

        fun onMouseMoved(dx: Float, dy: Float)

        fun onSenseChanged(value: Int)
    }

    interface Repository{
        fun getSense(): Int

        fun setSense(): Int

        fun sendMessage(byteArray: ByteArray)
    }
}