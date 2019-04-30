package com.mvgrass.remotepc.Adapters

import java.net.Inet4Address
import java.net.ServerSocket
import java.net.Socket

class TCPConnection(_port: Int, _address: Inet4Address) {

    private lateinit var TCPSocket: Socket

    private val address = _address
    private val port = _port

    fun open(){

        TCPSocket = Socket(address,port)
    }

    fun send(msg: String){
        val size = ByteArray(2)
        val arr = msg.toByteArray(Charsets.UTF_8)

        size[0] =  (((arr?.size) ?: 0) shr 8 and 0xFF).toByte()
        size[1] = (((arr?.size) ?: 0) and 0xFF).toByte()

        TCPSocket.getOutputStream().write(size)
        TCPSocket.getOutputStream().write(arr)

    }

    fun close(){

        TCPSocket.close()
    }

}