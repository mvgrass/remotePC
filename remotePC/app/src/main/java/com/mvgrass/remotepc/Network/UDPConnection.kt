package com.mvgrass.remotepc.Network

import java.lang.Exception
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.Inet4Address
import java.util.*


class UDPConnection(_port: Int) {

    private var UDPSocket: DatagramSocket? = null

    private var port = _port


    fun open(){
        while(UDPSocket == null) {
            try {
                UDPSocket = DatagramSocket(port)
            }catch (e: Exception){
                e.printStackTrace()
                Thread.sleep(2000)
            }
        }
    }

    fun getHostInfo(): Pair<String?, Inet4Address?>{



        val buffer = ByteArray(128)
        val packet = DatagramPacket(buffer,128)

        UDPSocket?.soTimeout = 2000
        try {
            UDPSocket?.receive(packet)
        }
        catch (exc: Exception){
            return Pair(null,null)
        }
        return Pair(buffer.toString(Charsets.UTF_8),packet.address as Inet4Address)
    }

    fun close(){
        UDPSocket?.close()
        UDPSocket = null
    }



}