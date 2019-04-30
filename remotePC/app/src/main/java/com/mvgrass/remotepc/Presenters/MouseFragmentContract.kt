package com.mvgrass.remotepc.Presenters

interface MouseFragmentContract{
    interface View{
        fun setSenseBar(value:Int)
    }

    interface Presenter{
        fun onLeftButtonPressed()

        fun onLeftButtonReleased()

        fun onRightButtonPressed()

        fun onRightButtonReleased()

        fun onScrolled(value: Int)

        fun onMouseMoved(dx: Int, dy: Int)

        fun onSenseChanged(value: Int)
    }

    interface Repository{
        fun getSense(): Int

        fun setSense(): Int

        fun sendMessage(byteArray: ByteArray)
    }
}