package com.mvgrass.remotepc.Presenters

import com.mvgrass.remotepc.Repository.MouseFragmentRepository
import com.mvgrass.remotepc.UseCases.*

class MouseFragmentPresenter: MouseFragmentContract.Presenter{

    private var mRepository:MouseFragmentContract.Repository = MouseFragmentRepository()

    override fun onLeftButtonPressed() {
        val arr = getLeftButtonPressedCode()

        mRepository.sendMessage(arr)
    }

    override fun onLeftButtonReleased() {
       val arr = getLeftButtonReleasedCode()

        mRepository.sendMessage(arr)
    }

    override fun onLeftButtonClicked() {
        val arr = getLeftButtonClickedCode()

        mRepository.sendMessage(arr)
    }

    override fun onLeftButtonDoubleClicked() {
        val arr = getLeftButtonDoubleClickedCode()

        mRepository.sendMessage(arr)
    }


    override fun onRightButtonPressed() {
        val arr = getRightButtonPressedCode()

        mRepository.sendMessage(arr)
    }

    override fun onRightButtonReleased() {
        val arr = getRightButtonReleasedCode()

        mRepository.sendMessage(arr)
    }

    override fun onRightButtonClicked() {
        val arr = getRightButtonClickedCode()

        mRepository.sendMessage(arr)
    }

    override fun onScrolled(value: Int) {
        val arr = getScrolledCode(value)

        mRepository.sendMessage(arr)
    }

    override fun onMouseMoved(dx: Float, dy: Float) {
        val arr = getMouseMovedCode(dx, dy)

        mRepository.sendMessage(arr)
    }

    override fun onSenseChanged(value: Int) {

    }

}