package com.mvgrass.remotepc.Adapters

import android.location.Address
import android.util.Log
import java.lang.Exception
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.Inet4Address
import java.util.*


class UDPConnection(_port: Int) {

    private lateinit var UDPSocket: DatagramSocket

    private var port = _port


    fun open(): Unit{
        UDPSocket = DatagramSocket(port)
    }

    fun getPackage(): Pair<String, Inet4Address>{



        var buffer = ByteArray(32)
        var packet = DatagramPacket(buffer,32)
        buffer = Arrays.copyOfRange(buffer,4,32)

        UDPSocket.soTimeout = 6000
        try {
            UDPSocket.receive(packet)
        }
        catch (exc: Exception){
            return Pair("",Inet4Address.getLocalHost() as Inet4Address)
        }
        return Pair(buffer.toString(Charsets.UTF_8),packet.address as Inet4Address)
    }

    fun close(): Unit{
        if (!UDPSocket.isClosed)
            UDPSocket.close()
    }



}