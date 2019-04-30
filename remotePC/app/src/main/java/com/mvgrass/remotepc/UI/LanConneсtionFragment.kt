package com.mvgrass.remotepc.UI

import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.mvgrass.remotepc.R
import java.util.ArrayList
import com.mvgrass.remotepc.Adapters.TCPConnection
import com.mvgrass.remotepc.Adapters.UDPConnection
import java.net.Inet4Address

class LanConnetionFragment : Fragment(){

    private lateinit var list: ListView
    var connectionList: MutableList<Pair<String, Inet4Address>> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lan_connetion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        list = view.findViewById(R.id.ConnectionList)
        UDPSocketAsyncTask().execute()

        list.setOnItemClickListener { _, _, position, _ ->
            Thread(Runnable {
                val tcpConnection = TCPConnection(49152,connectionList[position].second)
                tcpConnection.open()
                tcpConnection.send(showDialog())
            }).start()
        }

    }

    private inner class UDPSocketAsyncTask : AsyncTask<Void, Pair<String, Inet4Address>, Unit>() {

        private var count: Int = 0

        val adapter: ArrayAdapter<Pair<String,Inet4Address>> = ArrayAdapter(activity,android.R.layout.simple_list_item_1, connectionList)

        override fun onPreExecute() {
            list.adapter = adapter
        }

        override fun doInBackground(vararg params: Void) {

            val connection = UDPConnection(11498)
            connection.open()
            while (true) {
                val (name, address) = connection.getPackage()
                if (name == "")
                    break
                publishProgress(Pair(name,address))
            }
        }

        override fun onProgressUpdate(vararg value: Pair<String, Inet4Address>) {
            connectionList.add(count++,value[0])
            adapter.notifyDataSetChanged()
        }

    }

    fun showDialog(): String{

        var password: String = "ya_sobaka"

        return password
    }


}
