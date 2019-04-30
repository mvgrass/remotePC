package com.mvgrass.remotepc.UI

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.mvgrass.remotepc.Presenters.MouseFragmentContract
import com.mvgrass.remotepc.Presenters.MouseFragmentPresenter

import com.mvgrass.remotepc.R
import java.lang.Exception
import java.net.Socket

class MouseFragment : Fragment(), CodeFragment.CodeFragmentListener, MouseFragmentContract.View {

    private lateinit var presenter: MouseFragmentContract.Presenter
    private lateinit var senseBar: SeekBar


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mouse, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val leftButton = view.findViewById<Button>(R.id.mouse_left_button)
        val rightButton = view.findViewById<Button>(R.id.mouse_right_button)
        val touchpadView = view.findViewById<View>(R.id.touch_pad_view)
        senseBar = view.findViewById(R.id.senseSeekBar)

        presenter = MouseFragmentPresenter()



        //DELETE AFTER TCP CONNECTOR CREATED
        val connect = view.findViewById<Button>(R.id.ConnectButton)
        connect.setOnClickListener{v->
            val dialog = CodeFragment()
            dialog.setTargetFragment(this, 1)
            dialog.show(fragmentManager, "CodeFragment")

        }
        //DELETE AFTER TCP CONNECTOR CREATED


    }

    override fun setSenseBar(value: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    class TouchPadGestureListener{

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    //Delete after created stable tcp connection
    private lateinit var socket: Socket
    override fun onAccepted(str: String?) {

        Thread(Runnable {

            try {
                socket = Socket("192.168.0.102", 49152)
            }catch (exc: Exception){
                exc.printStackTrace()
            }


            val size = ByteArray(2)
            val arr = str?.toByteArray(Charsets.UTF_8)

            size[0] =  (((arr?.size) ?: 0) shr 8 and 0xFF).toByte()
            size[1] = (((arr?.size) ?: 0) and 0xFF).toByte()

            socket.getOutputStream().write(size)
            socket.getOutputStream().write(arr)
        }).start()

    }

    override fun onDecline() {
        Toast.makeText(activity, "Canceled", Toast.LENGTH_SHORT).show()
    }
}

class CodeFragment:DialogFragment(){


    override fun onAttach(context: Context?) {
        super.onAttach(context)

        mListener = targetFragment as CodeFragmentListener
        mEditText = EditText(context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)

        builder.setTitle("Enter validation code")
            .setPositiveButton("OK") {_, _ ->
                mListener.onAccepted(mEditText.text.toString())}
            .setNegativeButton("Cancel") {_, _ ->
                mListener.onDecline()}
            .setView(mEditText)

        return builder.create()
    }

    interface CodeFragmentListener{
        fun onAccepted(str: String?)

        fun onDecline()
    }

    lateinit var mListener: CodeFragmentListener
    lateinit var mEditText: EditText
}
