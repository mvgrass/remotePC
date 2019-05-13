package com.mvgrass.remotepc.UI

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.*
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
import androidx.fragment.app.DialogFragment
import com.mvgrass.remotepc.Adapters.ConnectionListAdapter

import com.mvgrass.remotepc.R
import com.mvgrass.remotepc.Network.TCPConnection
import com.mvgrass.remotepc.Network.UDPConnection
import java.lang.Exception
import java.lang.ref.WeakReference
import java.net.*

class LanConnectionFragment : Fragment(), CodeFragment.CodeFragmentListener{

    private lateinit var listView: ListView
    private lateinit var listViewAdapter: BaseAdapter
    var connectionList: MutableMap<Inet4Address, String> = HashMap()


    private lateinit var udpListenerThread: UdpListenerThread

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lan_connetion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        listView = view.findViewById(R.id.ConnectionList)

        listViewAdapter = ConnectionListAdapter(this.activity, connectionList)

        listView.adapter = listViewAdapter



        listView.setOnItemClickListener { adapter, _, position, _ ->
            val codeFragment = CodeFragment((adapter.getItemAtPosition(position )as Pair<Inet4Address, String>).first)
            codeFragment.setTargetFragment(this, 0)
            codeFragment.show(fragmentManager, "CodeFragment")
        }

    }

    override fun onResume() {
        super.onResume()

        udpListenerThread = UdpListenerThread(UdpThreadHandler(WeakReference(this)))

        udpListenerThread.start()
    }

    override fun onPause() {
        super.onPause()

        udpListenerThread.close()
        udpListenerThread.interrupt()
    }

    private fun addNewHost(host:Pair<String, Inet4Address>){
        connectionList.put(host.second, host.first)
        listViewAdapter.notifyDataSetChanged()
    }


    class UdpThreadHandler(private val outer: WeakReference<LanConnectionFragment>): Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)

            if (msg?.what == 0) {
                val (name, addr) = msg.obj as Pair<String, Inet4Address>

                outer.get()?.addNewHost(Pair<String, Inet4Address>(name, addr))

            }
        }
    }

    class UdpListenerThread(mainHandler: Handler): Thread(){
        private val mHandler = mainHandler

        private lateinit var connection: UDPConnection

        override fun run() {
            connection = UDPConnection(11498)
            connection.open()
            Log.i("Interruption", this.isInterrupted.toString())
            while(!this.isInterrupted){
                val (name, address) = connection.getHostInfo()
                if(name!=null&& address!=null) {
                    mHandler.obtainMessage(0, Pair<String, Inet4Address>(name, address))
                        .sendToTarget()
                }

            }

            connection.close()
        }

        fun close(){
            connection.close()
        }

    }

    override fun onAccepted() {
        Toast.makeText(activity, "Connected", Toast.LENGTH_SHORT).show()
    }

    override fun onDecline() {
        Toast.makeText(activity, "Canceled", Toast.LENGTH_SHORT).show()
    }
}

class CodeFragment(address: Inet4Address, port: Int = 49152): DialogFragment(){

    private var mAddress = address
    private var mPORT = port

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        mListener = targetFragment as CodeFragmentListener
        mEditText = EditText(context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)

        builder.setTitle("Enter validation code")
            .setPositiveButton("OK") { _, _ ->
                var socket: Socket? = null

                var successed: Boolean = false

                val thread = Thread(Runnable {

                    try {
                        socket = Socket()

                        socket?.connect(InetSocketAddress(mAddress, mPORT), 2000)

                        socket?.soTimeout = 2000

                        val size = ByteArray(2)
                        val arr = mEditText.text.toString().toByteArray(Charsets.UTF_8)

                        size[0] = (((arr.size)) shr 8 and 0xFF).toByte()
                        size[1] = (((arr.size)) and 0xFF).toByte()

                        socket?.getOutputStream()?.write(size)
                        socket?.getOutputStream()?.write(arr)

                        val buffer = ByteArray(1)
                        socket?.getInputStream()?.read(buffer, 0, 1)

                        successed = buffer[0] == 1.toByte()

                    } catch (exc: Exception) {
                        successed = false
                        exc.printStackTrace()
                    }

                })

                thread.start()
                thread.join()

                if (successed) {
                    TCPConnection.setTcp(socket)
                    TCPConnection.setPassword(mEditText.text.toString())
                    mListener.onAccepted()
                }else{
                    socket?.close()
                    mListener.onDecline()
                }
            }
            .setNegativeButton("Cancel") {_, _ ->
                mListener.onDecline()}
            .setView(mEditText)

        return builder.create()
    }

    interface CodeFragmentListener{
        fun onAccepted()

        fun onDecline()
    }

    lateinit var mListener: CodeFragmentListener
    lateinit var mEditText: EditText
}
