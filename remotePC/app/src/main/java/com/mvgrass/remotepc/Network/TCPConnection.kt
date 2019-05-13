package com.mvgrass.remotepc.Network

import android.os.Handler
import android.os.HandlerThread
import java.lang.Exception
import java.net.Inet4Address
import java.net.Socket

object TCPConnection{

    private var socket: Socket? = null

    private var address:Inet4Address? = null
    private var port: Int = 0
    private var password:String? = null

    private var writerThread: WriterThread? = null

    private var writerHandler: Handler? = null
    private var readerHandler: Handler? = null

    fun setTcp(mSocket: Socket?) {
        socket = mSocket

        writerThread = WriterThread()
        writerThread?.start()
    }

    fun setPassword(mPassword:String?){
        password = mPassword
    }

    fun send(arr: ByteArray){
        writerHandler?.post{
            try{
                val size = ByteArray(2)

                size[0] =  (((arr.size)) shr 8 and 0xFF).toByte()
                size[1] = (((arr.size)) and 0xFF).toByte()

                socket?.getOutputStream()?.write(size)
                socket?.getOutputStream()?.write(arr)
            }
            catch (exc:Exception){
                exc.printStackTrace()
                this.close()
            }
        }

    }

    fun isConnected():Boolean{
        return socket==null
    }

    fun close(){
        socket?.close()
        socket = null
        writerThread?.looper?.quit()
        writerThread = null
        writerHandler = null
    }

    class WriterThread(): HandlerThread("WriterThread"){
        var mHandler:Handler? = null

        override fun onLooperPrepared() {
            super.onLooperPrepared()
            mHandler = object: Handler(looper){}

            writerHandler = mHandler
        }

    }

}