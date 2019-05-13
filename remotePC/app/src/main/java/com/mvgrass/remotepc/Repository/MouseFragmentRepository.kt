package com.mvgrass.remotepc.Repository

import com.mvgrass.remotepc.Network.TCPConnection
import com.mvgrass.remotepc.Presenters.MouseFragmentContract

class MouseFragmentRepository(): MouseFragmentContract.Repository{


    override fun sendMessage(byteArray: ByteArray) {
        TCPConnection.send(byteArray)
    }

    override fun getSense(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setSense(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}