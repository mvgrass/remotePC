package com.mvgrass.remotepc.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.net.Inet4Address
import java.util.*

class ConnectionListAdapter(context: Context?, objects: MutableMap<Inet4Address, String>) :BaseAdapter(){

    private val connections = objects

    private val lInflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

        val view = p1?: lInflater.inflate(android.R.layout.simple_list_item_2, p2, false)

        val name = view.findViewById<TextView>(android.R.id.text1)
        val ip = view.findViewById<TextView>(android.R.id.text2)

        val (addr, hostName) = this.getItem(p0) as Pair<Inet4Address, String>

        name.text =hostName
        ip.text = addr.toString()

        return view
    }

    override fun getItem(p0: Int): Any {
        val element = connections.iterator()
        for(i in 0 .. (p0-1))
            element.next()

        val item = element.next()

        return Pair<Inet4Address, String>(item.key, item.value)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return connections.size
    }

}