package com.mvgrass.remotepc.UI

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.SeekBar
import com.mvgrass.remotepc.Presenters.MouseFragmentContract
import com.mvgrass.remotepc.Presenters.MouseFragmentPresenter

import com.mvgrass.remotepc.R
import kotlin.math.abs
import kotlin.math.floor

class MouseFragment : Fragment(), MouseFragmentContract.View {

    private lateinit var presenter: MouseFragmentContract.Presenter
    private lateinit var senseBar: SeekBar

    private lateinit var leftButton: Button
    private lateinit var rightButton: Button
    private lateinit var touchpadView: View

    private lateinit var mDetector: GestureDetector

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mouse, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        leftButton = view.findViewById<Button>(R.id.mouse_left_button)
        rightButton = view.findViewById<Button>(R.id.mouse_right_button)
        touchpadView = view.findViewById<View>(R.id.touch_pad_view)
        senseBar = view.findViewById(R.id.senseSeekBar)

        presenter = MouseFragmentPresenter()


        initializeGestureListeners()

    }

    override fun setSenseBar(value: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun initializeGestureListeners(){
        leftButton.setOnTouchListener { v, event ->
            val action = event.actionMasked

            when(action) {
                MotionEvent.ACTION_DOWN -> {
                    presenter.onLeftButtonPressed()
                    leftButton.isPressed = true
                    true
                }
                MotionEvent.ACTION_UP->{
                    presenter.onLeftButtonReleased()
                    leftButton.isPressed = false
                    true
                }
                else ->
                    false
            }
        }

        rightButton.setOnTouchListener { v, event ->
            val action = event.actionMasked

            when(action) {
                MotionEvent.ACTION_DOWN -> {
                    presenter.onRightButtonPressed()
                    rightButton.isPressed = true
                    true
                }
                MotionEvent.ACTION_UP->{
                    presenter.onRightButtonReleased()
                    rightButton.isPressed = false
                    true
                }
                else ->
                    false
            }
        }

        val listener = TouchPadGestureListener(presenter)
        mDetector = GestureDetector(activity, listener)
        mDetector.setIsLongpressEnabled(false)

        touchpadView.setOnTouchListener{ _, event->
            if(event.actionMasked == MotionEvent.ACTION_UP)
                listener.onUp()
            mDetector.onTouchEvent(event)
        }
    }

    class TouchPadGestureListener(presenter: MouseFragmentContract.Presenter): GestureDetector.SimpleOnGestureListener(){

        private val mPresenter = presenter

        private var downTimeMark:Long = 0
        private var firstScrollTimeMark:Long = -1

        private val SCROLL_MIN_DISTANCE = 50;

        private var firstMovingDown = false
        private var firstMovingUp = false
        private var secondMovingDown = false
        private var secondMovingUp = false
        private var doubleScrolled = 0f


        override fun onDown(e: MotionEvent?): Boolean {
            downTimeMark = System.currentTimeMillis()
            firstScrollTimeMark = -1
            return true
        }

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            if(e2?.pointerCount == 1) {
                if(firstScrollTimeMark == -1L) {
                    firstScrollTimeMark = System.currentTimeMillis()
                    if(firstScrollTimeMark - downTimeMark>300)
                        mPresenter.onLeftButtonPressed()
                }

                mPresenter.onMouseMoved(distanceX, distanceY)

            }else if(e2?.pointerCount == 2){
                val index = e2.actionIndex

                downTimeMark = System.currentTimeMillis()

                if (index == 0) {
                    if (distanceX == 0.0f) {
                        if (distanceY > 0) {
                            firstMovingUp = true
                            firstMovingDown = false
                        } else {
                            firstMovingUp = false
                            firstMovingDown = true
                        }
                    } else if (Math.tan((distanceY / distanceX).toDouble()) > ((Math.PI / 2) - 0.1)
                        && Math.tan((distanceY / distanceX).toDouble()) < ((Math.PI / 2) + 0.1)
                    ) {

                        firstMovingUp = true
                        firstMovingDown = false
                    } else if (Math.tan((distanceY / distanceX).toDouble()) > ((-Math.PI / 2) - 0.1)
                        && Math.tan((distanceY / distanceX).toDouble()) < ((-Math.PI / 2) + 0.1)
                    ) {

                        firstMovingUp = false
                        firstMovingDown = true
                    } else {
                        firstMovingUp = false
                        firstMovingDown = false
                    }
                }

                if(distanceY*doubleScrolled<0)
                    doubleScrolled = 0f

                doubleScrolled+=distanceY

                if ((firstMovingDown || firstMovingUp)&&abs(doubleScrolled)>=SCROLL_MIN_DISTANCE){
                    mPresenter.onScrolled((floor(abs(doubleScrolled)/SCROLL_MIN_DISTANCE)*abs(doubleScrolled)/doubleScrolled).toInt())
                    doubleScrolled = 0f
                }
            }

            return true
        }

        fun onUp(){
            if(firstScrollTimeMark!=-1L && (firstScrollTimeMark - downTimeMark>300)) {
                mPresenter.onLeftButtonReleased()
            }

            firstMovingUp = false
            firstMovingDown = false
            secondMovingUp = false
            secondMovingDown = false
            doubleScrolled = 0f
        }

        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            mPresenter.onLeftButtonClicked()
            return true
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            mPresenter.onLeftButtonDoubleClicked()
            return true
        }

    }
}
